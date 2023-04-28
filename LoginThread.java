import java.io.*;
import java.net.Socket;

/**
 * 
 * A complex thread class. 
 * This interfaces with the Market Server to log in or register a user
 * simultaneously with other users
 * 
 * @author Alpamys Sultanbek
 * @version 1.0
 */
public class LoginThread extends Thread {
    Socket client;

    public LoginThread(Socket client) {
        this.client = client;
    }

    public void run() {
        try {
            BufferedReader br = new BufferedReader(new InputStreamReader(client.getInputStream()));
            PrintWriter pw = new PrintWriter(client.getOutputStream()); 

            pw.write("|options|Welcome to the Market!|text|" +
                     "  login: Login to an existing account," +
                     "  register: Create a new account," +
                     "  exit: Exit the program");
            pw.println();
            pw.flush();
            while (true) {
                String input = br.readLine();
                if (input.equals("login")) {
                    pw.write("|input|Enter your username:");
                    pw.println();
                    pw.flush();
                    String login = br.readLine();
                    pw.write("|input|Enter your password:");
                    pw.println();
                    pw.flush();
                    String password = br.readLine();

                    User user = MarketServer.find(login, password);
                    if (user != null) {
                        if (user instanceof Customer) {
                            CustomerThread newThread = new CustomerThread(client, (Customer) user);
                            newThread.start();
                            return;
                        } else {
                            SellerThread newThread = new SellerThread(client, (Seller) user);
                            newThread.start();
                            return;
                        }
                    } else {
                        pw.write("Invalid login");
                        pw.println();
                        pw.flush();
                        client.close();
                        return;
                    }
                } else if (input.equals("register")) {
                    String login;
                    String password;
                    String userType;

                    pw.write("Enter a new username:");
                    pw.println();
                    pw.flush();
                    while (true) {
                        login = br.readLine();
                        boolean found = false;
                        for (User u : MarketServer.userList) {
                            if (u.getLogin().equals(login)) {
                                found = true;
                                pw.write("Username already exists!\\n" +
                                         "Enter a new Username:");
                                pw.println();
                                pw.flush();
                            }
                        }
                        if (!found) {
                            break;
                        }
                    }

                    pw.write("Enter a new password:");
                    pw.println();
                    pw.flush();
                    password = br.readLine();

                    pw.write("Enter a user type:\\n" +
                             "Options:\\n  customer\\n  seller");
                    pw.println();
                    pw.flush();
                    while (true) {
                        userType = br.readLine();
                        if (!(userType.equals("customer") ||
                             userType.equals("seller"))) {
                            pw.write("Invalid Command\\n" +
                                     "Enter a user type!\\n" +
                                     "Options:\\n  customer\\n  seller");
                            pw.println();
                            pw.flush();
                            continue;
                        }
                        break;
                    }

                    User newIdentity;
                    if (userType.equals("customer")) {
                        newIdentity = new Customer(login, password);
                        MarketServer.addUser(newIdentity);
                        CustomerThread newThread = new CustomerThread(client, (Customer) newIdentity);
                        newThread.start();
                        return;
                    } else {
                        newIdentity = new Seller(login, password);
                        MarketServer.addUser(newIdentity);
                        SellerThread newThread = new SellerThread(client, (Seller) newIdentity);
                        newThread.start();
                        return;
                    }
                } else if (input.equals("exit")) {
                    pw.write("Exiting...");
                    pw.println();
                    pw.flush();
                    client.close();
                    return;
                } else {
                    pw.write("Invalid Command");
                    pw.println();
                    pw.flush();
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}