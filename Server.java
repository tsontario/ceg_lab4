import java.net.*;
import java.io.IOException;
import java.io.DataInputStream;
import java.io.PrintStream;

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
        output.println(message);
        // serviceSocket.close();
      }

    } catch (IOException e) {
      System.out.printf("Encountered error: %s. Shutting down\n", e);
      System.exit(1);
    }
  }
}