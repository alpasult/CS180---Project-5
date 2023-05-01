import java.io.*;
import java.net.Socket;

/**
 * 
 * A complex thread class. 
 * This interfaces with the Market Server to facilitate the actions of a seller
 * simultaneously with other clients
 * 
 * @author Alpamys Sultanbek
 * @version 1.0
 */
public class SellerThread extends Thread {
    private Socket client;
    private Seller user;

    // Creates a seller's thread
    public SellerThread(Socket client, Seller user) {
        this.client = client;
        this.user = user;
    }

    // Run method for all of seller's options
    public void run() {
        try {
            BufferedReader br = new BufferedReader(new InputStreamReader(client.getInputStream()));
            PrintWriter pw = new PrintWriter(client.getOutputStream()); 

            // Displays all the options seller's have
            pw.write("|options|Welcome " + user.getLogin() + "|text|" +
                     "data," +
                     "add," +
                     "edit," +
                     "remove," +
                     "csv_import," +
                     "csv_export," +
                     "exit");
            pw.println();
            pw.flush();
            String input;
            while (true) {
                input = br.readLine();

                // Sellers want to sort their products
                if (input.equals("data")) {
                    // Displays all the ways sellers can sort their products
                    pw.write("|options|COMMANDS:|text|" +
                             "name," +
                             "store," +
                             "quantity," +
                             "price," +
                             "amount_sold," +
                             "customers," +
                             "back");
                    pw.println();
                    pw.flush();
                    while (true) {
                        input = br.readLine();

                        // Making sure input is valid
                        if (input.equals("name") ||
                            input.equals("store") || 
                            input.equals("quantity") ||
                            input.equals("price") ||
                            input.equals("amount_sold")) {
                            String out = user.data(input);
                            pw.write(out);
                            pw.println();
                            pw.flush();

                        // Displays all the customers who have purchased items from their shop
                        } else if (input.equals("customers")) {
                            String out = MarketServer.customerString(user.getLogin());
                            pw.write(out);
                            pw.println();
                            pw.flush();

                        // Brings sellers back to the previous page
                        } else if (input.equals("back")) {
                            pw.write("|info|Going back to the main menu");
                            pw.println();
                            pw.flush();
                            break;
                        }

                        input = br.readLine();
                        pw.write("|options|COMMANDS:|text|" +
                             "name," +
                             "store," +
                             "quantity," +
                             "price," +
                             "amount_sold," +
                             "customers," +
                             "back");
                        pw.println();
                        pw.flush();
                    }

                // Seller wants to add a new product
                } else if (input.equals("add")) {
                    // Gets all the product information
                    pw.write("|input|Enter the product name:");
                    pw.println();
                    pw.flush();
                    String name = br.readLine();
                    
                    pw.write("|input|Enter the store name:");
                    pw.println();
                    pw.flush();
                    String store = br.readLine();
                    
                    pw.write("|input|Enter the product description:");
                    pw.println();
                    pw.flush();
                    String description = br.readLine();
                    
                    pw.write("|input|Enter the quantity:");
                    pw.println();
                    pw.flush();
                    int quantity;

                    // Checking to ensure the quantity is a valid number
                    while (true) {
                        try {
                            quantity = Integer.parseInt(br.readLine());
                            if (quantity < 1) {
                                throw new NumberFormatException();
                            }
                            break;
                        } catch (NumberFormatException e) {
                            pw.write("|input|Invalid number\\n" +
                                     "Enter the quantity:");
                            pw.println();
                            pw.flush();
                        }
                    }

                    // Getting the cost for the product
                    pw.write("|input|Enter the cost:");
                    pw.println();
                    pw.flush();
                    float price;

                    // Checking to ensure the cost is a valid number
                    while (true) {
                        try {
                            price = Float.parseFloat(br.readLine());
                            if (price < 0) {
                                throw new NumberFormatException();
                            }
                            break;
                        } catch (NumberFormatException e) {
                            pw.write("|input|Invalid number\\n" +
                                     "Enter the cost:");
                            pw.println();
                            pw.flush();
                        }
                    }

                    // Displays that a new product was successfully added
                    pw.write("|info|New Product Added");
                    pw.println();
                    pw.flush();

                    // Creates the new product
                    Product newProduct = new Product(name, store, user.getLogin(), description, quantity, price);
                    user.addProduct(newProduct);

                // Seller wants to edit an existing product
                } else if (input.equals("edit")) {
                    // Getting the name of the product
                    pw.write("|input|Enter the name of the product you wish to edit:");
                    pw.println();
                    pw.flush();
                    Product[] products = user.getProducts();
                    boolean done = false;
                    Product product = null;
                    String name;

                    // Checking for the product
                    while (!done) {
                        name = br.readLine();
                        for (Product p : products) {
                            if (p.getName().equals(name)) {
                                product = p;
                                done = true;
                                break;
                            }
                        }
                        // Product does not exist
                        if (!done) {
                            pw.write("|input|Product not found\\n" +
                                     "Enter the name of the product you wish to edit:");
                            pw.println();
                            pw.flush();
                        }
                    }

                    // Getting the new information
                    pw.write("|input|Enter the new name:");
                    pw.println();
                    pw.flush();
                    String newName = br.readLine();

                    pw.write("|input|Enter the new store:");
                    pw.println();
                    pw.flush();
                    String store = br.readLine();

                    pw.write("|input|Enter the new description:");
                    pw.println();
                    pw.flush();
                    String description = br.readLine();

                    pw.write("|input|Enter the new quantity:");
                    pw.println();
                    pw.flush();
                    int quantity;

                    // Checking that the quantity is valid
                    while (true) {
                        try {
                            quantity = Integer.parseInt(br.readLine());
                            if (quantity < 1) {
                                throw new NumberFormatException();
                            }
                            break;
                        } catch (NumberFormatException e) {
                            pw.write("|input|Invalid number\\n" +
                                     "Enter the new quantity:");
                            pw.println();
                            pw.flush();
                        }
                    }

                    pw.write("|input|Enter the new price:");
                    pw.println();
                    pw.flush();
                    float price;

                    // Checking that the price is valid
                    while (true) {
                        try {
                            price = Float.parseFloat(br.readLine());
                            if (price < 0) {
                                throw new NumberFormatException();
                            }
                            break;
                        } catch (NumberFormatException e) {
                            pw.write("|input|Invalid number\\n" +
                                     "Enter the new price:");
                            pw.println();
                            pw.flush();
                        }
                    }

                    // Setting the new product values
                    product.setName(newName);
                    product.setPrice(price);
                    product.setStore(store);
                    product.setDescription(description);
                    product.setQuantity(quantity);

                    // Showing that the product has been changed
                    pw.write("|info|Product changed");
                    pw.println();
                    pw.flush();

                // Seller wants to remove a product
                } else if (input.equals("remove")) {
                    // Getting the product name
                    pw.write("|input|Enter the name of the product you wish to delete:\\n" +
                             "WARNING: DELETING THE PRODUCT WILL DELETE ALL RECORDS OF IT");
                    pw.println();
                    pw.flush();
                    String name = br.readLine();
                    Product[] products = user.getProducts();

                    // Getting the product and deleting it
                    for (Product p : products) {
                        if (p.getName().equals(name)) {
                            user.removeProduct(p);
                            break;
                        }
                    }

                    // Displaying that the item was deleted
                    pw.write("|info|Item successfully deleted");
                    pw.println();
                    pw.flush();

                // Imports a csv file containing a seller's new products
                } else if (input.equals("csv_import")) {
                    // Getting the file name
                    pw.write("|input|Enter the file name:");
                    pw.println();
                    pw.flush();
                    input = br.readLine();
                    boolean success = user.addCSV(input);

                    // Displaying that the file was imported successfully
                    if (success) {
                        pw.write("|info|CSV Imported");
                        pw.println();
                        pw.flush();

                    // File import failed
                    } else {
                        pw.write("|info|CSV Import Failed");
                        pw.println();
                        pw.flush();
                    }

                // Exports a csv file with a seller's current products
                } else if (input.equals("csv_export")) {
                    // Creating the filename
                    String fileName = "";
                    fileName += user.getLogin() + "_products.csv";
                    user.exportCSV(fileName);
                    
                    // Displaying that the file was exported successfully
                    pw.write("|info|CSV Exported");
                    pw.println();
                    pw.flush();

                // Seller wants to exit the marketplace
                } else if (input.equals("exit")) {
                    pw.write("|info|Thank you for visiting the Market!");
                    pw.println();
                    pw.flush();
                    break;
                } 
                
                // Prompt the user for a command
                input = br.readLine();
                pw.write("|options|Welcome " + user.getLogin() + "|text|" +
                     "data," +
                     "add," +
                     "edit," +
                     "remove," +
                     "csv_import," +
                     "csv_export," +
                     "exit");
                pw.println();
                pw.flush();
            }
            br.close();
            pw.close();
        } catch (IOException e) {
            System.out.println("|error|Connection Error");
            return;
        }
    }
}