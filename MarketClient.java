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

        // Connects the client to the host
        try {
            String hostname;
            int port;

            // Welcomes the user and prompts for hostname and port number
            // Host name is localhost and port number is 4242
            JOptionPane.showMessageDialog(null, "Welcome to the Marketplace Client!", "Marketplace", JOptionPane.INFORMATION_MESSAGE);
            hostname = JOptionPane.showInputDialog(null, "Host Name: ", "Marketplace", JOptionPane.QUESTION_MESSAGE);
            port = Integer.parseInt(JOptionPane.showInputDialog(null, "Port", "Marketplace", JOptionPane.QUESTION_MESSAGE));
            client = new Socket(hostname, port);

        // Error message if connection could not be made
        } catch (IOException e) {
            JOptionPane.showMessageDialog(null, "Could not connect to server", "Marketplace", JOptionPane.ERROR_MESSAGE);
            return;

        // Error message if port number is invalid
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(null, "Invalid Port Number", "Marketplace", JOptionPane.ERROR_MESSAGE);
            return;
        }


        // Creates all the GUI for the inputs for each thread class
        try {
            BufferedReader br = new BufferedReader(new InputStreamReader(client.getInputStream()));
            PrintWriter pw = new PrintWriter(client.getOutputStream()); 

            String input;
            while(true) {
                input = br.readLine();
                System.out.println(input);
                input = input.replace("\\n", "\n");

                // Users are given several options to pick from
                if (input.contains("|options|")) {
                    input = input.substring(9);

                    // Gets the message for what the user needs to pick
                    String text = input.substring(0, input.indexOf("|text|"));

                    // Gets all the options the user has and puts them into an array
                    input = input.substring(input.indexOf("|text|") + 6);
                    String[] options = input.split(",");

                    // Displays a GUI to the user and saves their answer choice
                    int i = JOptionPane.showOptionDialog(null, text, "Marketplace", JOptionPane.DEFAULT_OPTION, JOptionPane.INFORMATION_MESSAGE, null, options, options[0]);
                    System.out.println(options[i]);
                    pw.write(options[i]);
                    pw.println();
                    pw.flush();

                // Displays a GUI when a message needs to be displayed to the user
                } else if (input.contains("|info|")) {
                    // Gets the message
                    input = input.substring(6);

                    // Displays a GUI with the message
                    JOptionPane.showMessageDialog(null, input, "Marketplace", JOptionPane.INFORMATION_MESSAGE);
                    pw.write("");
                    pw.println();
                    pw.flush();

                // Displays a GUI when user input is required
                } else if (input.contains("|input|")) {
                    // Gets what information the user needs to input
                    input = input.substring(7);

                    // Displays a GUI with what the user needs to input and saves it
                    String out = JOptionPane.showInputDialog(null, input, "Marketplace", JOptionPane.QUESTION_MESSAGE);
                    pw.write(out);
                    pw.println();
                    pw.flush();

                // Displays a GUI when an error has occurred
                } else if (input.contains("|error|")) {
                    // Gets the error message
                    input = input.substring(7);

                    // Displays a GUI with the error message
                    JOptionPane.showMessageDialog(null, input, "Marketplace", JOptionPane.ERROR_MESSAGE);
                    in.close();
                    client.close();
                    return;

                // Displays a GUI with an exit message
                } else if (input.contains("|exit|")) {
                    // Gets the exit message
                    input = input.substring(6);

                    // Displays a GUI with the exit message
                    JOptionPane.showMessageDialog(null, input, "Marketplace", JOptionPane.INFORMATION_MESSAGE);
                    in.close();
                    client.close();
                    return;

                // Displays a GUI when an unknown error has occurred and ends the program
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
