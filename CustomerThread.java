import java.io.*;
import java.net.Socket;
import java.util.ArrayList;

/**
 * 
 * A complex thread class. 
 * This interfaces with the Market Server to facilitate the actions of a customer
 * simultaneously with other clients
 * 
 * @author Alpamys Sultanbek
 * @version 1.0
 */
public class CustomerThread extends Thread {
    private Socket client;
    private Customer user;

    public CustomerThread(Socket client, Customer user) {
        this.client = client;
        this.user = user;
    }

    public void run() {
        try {
            BufferedReader br = new BufferedReader(new InputStreamReader(client.getInputStream()));
            PrintWriter pw = new PrintWriter(client.getOutputStream()); 

            pw.write("Welcome " + user.getLogin() + "\\n" +
                     "COMMANDS:\\n" +
                     "  browse: See what is currently available on the market\\n" +
                     "  search: Search for a specific product on the market\\n" +
                     "  cart: See what is currently in your cart\\n" +
                     "  purchases: History of purchases\\n" +
                     "  add: Add a new product to your shopping cart\\n" +
                     "  remove: Remove an item from your cart\\n" +
                     "  checkout: Check out and purchase everything in your cart\\n" +
                     "  csv_export: Export a csv file containing your current products\\n" +
                     "  exit: Exit the application");
            pw.println();
            pw.flush();
            String input;
            while (true) {
                input = br.readLine();
                if (input.equals("browse")) {
                    ArrayList<Product> listings = MarketServer.constructListings();
                    Product[] sortedListings = listings.toArray(new Product[listings.size()]);

                    for (int i = 0; i < sortedListings.length - 1; i++) {
                        for (int j = i + 1; j < sortedListings.length; j++) {
                            Product smaller = sortedListings[i];
                            Product bigger = sortedListings[j];
                            if (smaller.getStore().compareTo(bigger.getStore()) > 0) {
                                bigger = sortedListings[i];
                                smaller = sortedListings[j];
                            }
                            sortedListings[i] = smaller;
                            sortedListings[j] = bigger;
                        }
                    }

                    String out = "";
                    out += "  |name           |store          |description                   | quantity|     price|\\n";
                    out += "==========================================" +
                        "=============================================\\n";
                    for (int i = 0; i < sortedListings.length; i++) {
                        Product p = sortedListings[i];
                        out += String.format("%-2d|%-15.15s|%-15.15s|%-30.30s|%9d|%10.2f|\\n", 
                                            i, 
                                            p.getName(), 
                                            p.getStore(),
                                            p.getDescription(),
                                            p.getQuantity(),
                                            p.getPrice());
                        out += "========================================" +
                            "===============================================\\n";
                    }

                    pw.write(out);
                    pw.println();
                    pw.flush();
                } else if (input.equals("search")) {
                    pw.write("Enter your search term: ");
                    pw.println();
                    pw.flush();
                    String search = br.readLine();

                    ArrayList<Product> listings = MarketServer.constructListings();
                    ArrayList<Product> filteredListings = new ArrayList<Product>();
                    for (Product p : listings) {
                        if(p.getName().contains(search) ||
                           p.getStore().contains(search) ||
                           p.getDescription().contains(search)) {
                            filteredListings.add(p);
                        }
                    }
                    Product[] sortedListings = filteredListings.toArray(new Product[filteredListings.size()]);

                    for (int i = 0; i < sortedListings.length - 1; i++) {
                        for (int j = i + 1; j < sortedListings.length; j++) {
                            Product smaller = sortedListings[i];
                            Product bigger = sortedListings[j];
                            if (smaller.getStore().compareTo(bigger.getStore()) > 0) {
                                bigger = sortedListings[i];
                                smaller = sortedListings[j];
                            }
                            sortedListings[i] = smaller;
                            sortedListings[j] = bigger;
                        }
                    }

                    String out = "";
                    out += "  |name           |store          |description                   | quantity|     price|\\n";
                    out += "==========================================" +
                        "=============================================\\n";
                    for (int i = 0; i < sortedListings.length; i++) {
                        Product p = sortedListings[i];
                        out += String.format("%-2d|%-15.15s|%-15.15s|%-30.30s|%9d|%10.2f|\\n", 
                                            i, 
                                            p.getName(), 
                                            p.getStore(),
                                            p.getDescription(),
                                            p.getQuantity(),
                                            p.getPrice());
                        out += "========================================" +
                            "===============================================\\n";
                    }

                    pw.write(out);
                    pw.println();
                    pw.flush();
                } else if (input.equals("cart")) {
                    String out = user.cart();
                    pw.write(out);
                    pw.println();
                    pw.flush();
                } else if (input.equals("purchases")) {
                    String out = user.purchasesPrint();
                    pw.write(out);
                    pw.println();
                    pw.flush();
                } else if (input.equals("add")) {
                    pw.write("Enter the name of the product: ");
                    pw.println();
                    pw.flush();
                    ArrayList<Product> listings = MarketServer.constructListings();
                    String name = br.readLine();
                    pw.write("Enter the amount you want to buy: ");
                    pw.println();
                    pw.flush();
                    int quantity;
                    while (true) {
                        try {
                            quantity = Integer.parseInt(br.readLine());
                            break;
                        } catch (NumberFormatException e) {
                            pw.write("Invalid number\\n" +
                                     "Enter the quantity:");
                            pw.println();
                            pw.flush();
                        }
                    }

                    boolean found = false;
                    for (Product p : listings) {
                        if (p.getName().equals(name)) {
                            found = true;
                            if (p.getQuantity() < quantity) {
                                quantity = p.getQuantity();
                            }
                            if (quantity != 0) {
                                Product temp = new Product(name, p.getStore(), p.getSeller(), 
                                                           p.getDescription(), quantity, p.getPrice());
                                user.addShoppingCart(temp);
                            }
                            break;
                        }
                    }
                    if (quantity == 0) {
                        pw.write("Item not Available");
                        pw.println();
                        pw.flush();
                    } else if (found) {
                        pw.write("Item Added to Shopping Cart!");
                        pw.println();
                        pw.flush();
                    } else {
                        pw.write("No such item found");
                        pw.println();
                        pw.flush();
                    }
                } else if (input.equals("remove")) {
                    pw.write("Enter the name of the product: ");
                    pw.println();
                    pw.flush();
                    String name = br.readLine();
                    user.removeShoppingCart(name);
                } else if (input.equals("checkout")) {
                    ArrayList<Product> cart = user.getShoppingList();
                    ArrayList<Product> listings = MarketServer.constructListings();
                    String out = "";
                    for (Product p : cart) {
                        for (Product r : listings) {
                            if (p.getName().equals(r.getName()) && 
                                p.getSeller().equals(r.getSeller())) {
                                if (p.getQuantity() > r.getQuantity()) {
                                    p.setQuantity(r.getQuantity());
                                    r.setQuantity(0);
                                    out += String.format("Only able to buy %d of %s\\n", p.getQuantity(), p.getName());
                                } else {
                                    r.setQuantity(r.getQuantity() - p.getQuantity());
                                }
                                r.setAmountSold(r.getAmountSold() + p.getQuantity());

                                if (p.getQuantity() == 0) {
                                    out += "Not able to buy " + p.getName() + "\\n";
                                }

                                user.addPurchases(p);
                                break;
                            }
                        }
                    }
                    user.setShoppingList(new ArrayList<Product>());

                    out += "Checked out";
                    pw.write(out);
                    pw.println();
                    pw.flush();
                } else if (input.equals("csv_export")) {
                    String fileName = "";
                    fileName += user.getLogin() + "_purchases.csv";
                    user.exportCSV(fileName);
                    pw.write("CSV Exported");
                    pw.println();
                    pw.flush();
                } else if (input.equals("exit")) {
                    pw.write("Thank you for visiting the Market!");
                    pw.println();
                    pw.flush();
                    break;
                } else {
                    pw.write("Invalid Command");
                    pw.println();
                    pw.flush();
                }
                if (input == null) {
                    throw new IOException();
                }
            }
            br.close();
            pw.close();
        } catch (IOException e) {
            System.out.println("Connection Error");
            return;
        }
    }
}