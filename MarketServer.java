import java.util.ArrayList;
import java.util.Arrays;
import java.net.*;
import java.io.*;

/**
 * 
 * A complex server class
 * This keeps all of the information about the marketplace centralized
 * and starts multiple threads to service as many users as come in
 * 
 * @author Alpamys Sultanbek
 * @version 1.0
 */
public class MarketServer {
    public static ArrayList<User> userList = new ArrayList<User>();
    
    public static void main(String[] args) {
        ServerSocket server;
        try {
            server = new ServerSocket(4242);
        } catch (IOException e) {
            System.out.println("Could not create server");
            return;
        }

        while (true) {
            try {
                Socket client = server.accept();
                LoginThread newThread = new LoginThread(client);
                newThread.start();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

   /*
    finds user with same login and password
    */
    public static User find(String login, String password) {
        for (int i = 0; i < userList.size(); i++) {
            User user = userList.get(i);
            if (user.getLogin().equals(login) && user.getPassword().equals(password)) {
                return user;
            }
        }
        return null;
    }

    public static void addUser(User newIdentity) {
        userList.add(newIdentity);
    }

    public static ArrayList<Product> constructListings() {
        ArrayList<Product> out = new ArrayList<Product>();

        for (User user : userList) {
            if (user instanceof Seller) {
                Seller temp = (Seller) user;
                out.addAll(Arrays.asList(temp.getProducts()));
            }
        }

        return out;
    }

    /*
    if user is a seller if a product matches the product to be removed it is taken off
    the productArray
    */
    public static void remove(Product product) {
        boolean done = false;
        for (User user : userList) {
            if (user instanceof Seller) {
                Seller temp = (Seller) user;
                Product[] productArray = temp.getProducts();
                for (Product p : productArray) {
                    if (p == product) {
                        temp.removeProduct(product);
                        done = true;
                        break;
                    }
                }
            }
            if (done) {
                break;
            }
        }
    }
    /*
    prints out a formated list for all users that are customers showing the customer,
    the product they bought and how much they bought running through multiple for loops
    */
    public static String customerString(String login) {
        String out = "|table|Customer,Product,Amount Bought\\n";
        Product[] products = {};
        ArrayList<String> productNames = new ArrayList<String>();

        for (User u : userList) {
            if (u.getLogin().equals(login)) {
                products = ((Seller) u).getProducts();
                break;
            }
        }

        for (Product p : products) {
            productNames.add(p.getName());
        }

        for (User u : userList) {
            if (u instanceof Customer) {
                boolean written = false;
                for (Product p : ((Customer) u).getPurchases()) {
                    if (p.getSeller().equals(login)) {
                        if (productNames.contains(p.getName())) {
                            if (!written) {
                                written = true;
                                out += String.format("%s,%s,%d\\n",
                                                     u.getLogin(),
                                                     p.getName(),
                                                     p.getQuantity());
                            } else {
                                out += String.format("%s,%s,%d\\n",
                                                     "",
                                                     p.getName(),
                                                     p.getQuantity());
                            }
                        }
                    }
                }
            }
        }
        return out;
    }
}
