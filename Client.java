import java.net.Socket;
import java.io.IOException;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.util.Arrays;
import java.util.ArrayList;
import java.util.List;

class Client {

  public static void main(String[] args) {
    if (args.length != 1 || inputValid(args[0])) {
      System.out.println("A valid bit string must be supplied");
      System.exit(1);
    }

    ArrayList<Character> bitStream = getHDB3BitStream(args[0]);
    for (Character c : bitStream) {
      System.out.print(c);
    }
    System.out.println();
    Socket myClient;
    DataOutputStream output;
    DataInputStream input;
    try {
      myClient = new Socket("localhost", 8080);
      output = new DataOutputStream(myClient.getOutputStream());
      input = new DataInputStream(myClient.getInputStream());

      output.writeBytes("request-to-send\n");
      String response = "";
      String line;
      while ((line = input.readLine()) != null) {
        System.out.println(line);
        response += line;
      }

      input.close();
      output.close();
      myClient.close();
    } catch (IOException e) {
      System.out.printf("Encountered error: %s. Shutting down\n", e);
      System.exit(1);
    }
  }

  private static boolean inputValid(String arg) {
    for (char c : arg.toCharArray()) {
      if (c != '0' || c != '1') {
        return false;
      }
    }
    return true;
  }

  private static ArrayList<Character> getHDB3BitStream(String input) {
    Character onesPolarity = '+';
    ArrayList<Character> inputList = new ArrayList<>();
    for (char c : input.toCharArray()) {
      inputList.add(c);
    }
    ArrayList<Character> result = new ArrayList<>();
    
    int i = 0;
    int numOnes = 0;
    while (i < input.length()) {
      if (inputList.get(i) == '1') {
        result.add(onesPolarity);
        result.add(input.charAt(i));
        onesPolarity = oppositePolarity(onesPolarity);
        numOnes += 1;
        i += 1;
        continue;
      } else if (inputList.size() - i > 3) {
        List<Character> segment = inputList.subList(i, i+4);
        if (segment.equals(fourZeros())) {
          if (numOnes %2 == 0) {
            result.add(onesPolarity);
            result.add('B');
            result.add('0');
            result.add('0');
            result.add(onesPolarity);
            result.add('V');
            onesPolarity = oppositePolarity(onesPolarity);
          } else {
            result.add('0');
            result.add('0');
            result.add('0');
            result.add(oppositePolarity(onesPolarity));
            result.add('V');
            numOnes = 0;
          }
          i += 4;
        } 
      }
      else {
        result.add('0');
        i += 1;
      }
    }
    return result;
  }

  private static List<Character> fourZeros() {
    List<Character> res = new ArrayList<Character>();
    res.add('0');
    res.add('0');
    res.add('0');
    res.add('0');
    return res;
  }

  private static Character oppositePolarity(Character polarity) {
    polarity = (polarity == '+' ? '-' : '+');
    return polarity;
  }
}