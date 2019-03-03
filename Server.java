import java.net.*;
import java.io.IOException;
import java.io.DataInputStream;
import java.io.PrintStream;
import java.util.ArrayList;

class Server {
    
  public static void main(String[] args) {
    ServerSocket myService;
    Socket serviceSocket;
    DataInputStream input;
    PrintStream output;
    try {
      myService = new ServerSocket(8080);

      while (true) {
        System.out.println("Waiting for input on port 8080");
        serviceSocket = myService.accept();
        input = new DataInputStream(serviceSocket.getInputStream());
        output = new PrintStream(serviceSocket.getOutputStream());
        
        String message = input.readLine();

        if (!message.equals("request-to-send")) {
          output.println("unknown-command-not-ready-for-send");
          serviceSocket.close();
          continue;
        }
        output.println("clear-to-send");

        message = input.readLine();
        output.println("receipt-acknowledged");
        System.out.printf("GOT MESSAGE: %s\n", message);
        System.out.println("Decoding message...");

        input.close();
        output.close();

        // Decode from HDB3
        message = message.replaceAll("\\+000\\+", "10000");
        message = message.replaceAll("\\-000\\-", "10000");
        message = message.replaceAll("\\+00\\+", "0000");
        message = message.replaceAll("\\-00\\-", "0000");
        message = message.replaceAll("\\-", "+");
        message = message.replaceAll("\\+", "1");
        System.out.printf("DECODED MESSAGE: %s\n", message);

      }

    } catch (IOException e) {
      System.out.printf("Encountered error: %s. Shutting down\n", e);
      System.exit(1);
    }
  }
}
