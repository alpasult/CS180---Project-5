import java.io.*;
import java.net.*;
import java.util.Scanner;
import javax.swing.JOptionPane;

/**
 * 
 * A simple client class
 * It connect to the Marketplace server
 * Then accepts data, prints it and sends data to the server
 * 
 * @author Alpamys Sultanbek
 * @version 1.0
 */
public class MarketClient {
    public static void main(String[] args) {
        Socket client;
        Scanner in = new Scanner(System.in);

        try {
            String hostname;
            int port;
            JOptionPane.showMessageDialog(null, "Welcome to the Marketplace Client!", "Marketplace", JOptionPane.INFORMATION_MESSAGE);
            hostname = JOptionPane.showInputDialog(null, "Host Name: ", "Marketplace", JOptionPane.QUESTION_MESSAGE);
            port = Integer.parseInt(JOptionPane.showInputDialog(null, "Port", "Marketplace", JOptionPane.QUESTION_MESSAGE));

            client = new Socket(hostname, port);
        } catch (IOException e) {
            JOptionPane.showMessageDialog(null, "Could not connect to server", "Marketplace", JOptionPane.ERROR_MESSAGE);
            return;
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(null, "Invalid Port Number", "Marketplace", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        try {
            BufferedReader br = new BufferedReader(new InputStreamReader(client.getInputStream()));
            PrintWriter pw = new PrintWriter(client.getOutputStream()); 

            String input;
            String output = "";
            while(true) {
                input = br.readLine();

                input = input.replace("\\n", "\n");
                
                if (input.contains("|options|")) {
                    input = input.substring(9);
                    String text = input.substring(0, input.indexOf("|text|"));
                    input = input.substring(input.indexOf("|text|") + 6);
                    String[] options = input.split(",");
                    JOptionPane.showOptionDialog(null, text, "Marketplace", JOptionPane.DEFAULT_OPTION, JOptionPane.INFORMATION_MESSAGE, null, options, options[0]);
                } else if (input.contains("|input|")) {
                    
                } else if (input.contains("|exit|")) {
                    System.out.println("Thank you for using the Market Client!");
                    in.close();
                    client.close();
                    return;
                }

                output = in.nextLine();
                pw.write(output);
                pw.println();
                pw.flush();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
