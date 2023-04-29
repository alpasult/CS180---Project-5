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
            while(true) {
                input = br.readLine();
                System.out.println(input);
                input = input.replace("\\n", "\n");
                
                if (input.contains("|options|")) {
                    input = input.substring(9);
                    String text = input.substring(0, input.indexOf("|text|"));
                    input = input.substring(input.indexOf("|text|") + 6);
                    String[] options = input.split(",");
                    int i = JOptionPane.showOptionDialog(null, text, "Marketplace", JOptionPane.DEFAULT_OPTION, JOptionPane.INFORMATION_MESSAGE, null, options, options[0]);

                    pw.write(options[i]);
                    pw.println();
                    pw.flush();
                } else if (input.contains("|info|")) {
                    input = input.substring(6);
                    JOptionPane.showMessageDialog(null, input, "Marketplace", JOptionPane.INFORMATION_MESSAGE);
                    pw.write("");
                    pw.println();
                    pw.flush();
                } else if (input.contains("|input|")) {
                    input = input.substring(7);
                    String out = JOptionPane.showInputDialog(null, input, "Marketplace", JOptionPane.QUESTION_MESSAGE);
                    pw.write(out);
                    pw.println();
                    pw.flush();
                } else if (input.contains("|error|")) {
                    input = input.substring(7);
                    JOptionPane.showMessageDialog(null, input, "Marketplace", JOptionPane.ERROR_MESSAGE);
                    in.close();
                    client.close();
                    return;
                } else if (input.contains("|exit|")) {
                    input = input.substring(6);
                    JOptionPane.showMessageDialog(null, input, "Marketplace", JOptionPane.INFORMATION_MESSAGE);
                    in.close();
                    client.close();
                    return;
                } else {
                    JOptionPane.showMessageDialog(null, "UNKNOWN ERROR: CLOSING CONNECTION", "Marketplace", JOptionPane.ERROR_MESSAGE);
                    in.close();
                    client.close();
                    return;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
