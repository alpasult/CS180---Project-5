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
            pw.write("Welcome " + user.getLogin() + "\\n" +
                     "COMMANDS:\\n" +
                     "  data: Data on your current products available for purchase\\n" +
                     "  add: Add a new product to one of your stores\\n" +
                     "  edit: Edit one of your products\\n" +
                     "  remove: Remove one of your products\\n" +
                     "  csv_import: Import a csv file containing your new products\\n" +
                     "  csv_export: Export a csv file containing your current products\\n" +
                     "  exit: Exit the application");
            pw.println();
            pw.flush();
            String input;
            while (true) {
                input = br.readLine();

                // Sellers want to sort their products
                if (input.equals("data")) {
                    // Displays all the ways sellers can sort their products
                    pw.write("COMMANDS:\\n" +
                             "  name: sort by name\\n" +
                             "  store: Sort by store\\n" +
                             "  quantity: Sort by quantity\\n" +
                             "  price: Sort by price\\n" +
                             "  amount_sold: Sort by the amount sold\\n" +
                             "  customers: View which users have bought which of your products\\n" +
                             "  back: Go back to the main terminal");
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
                            pw.write("COMMANDS:\\n" +
                                     "  data: Data on your current products available for purchase\\n" +
                                     "  add: Add a new product to one of your stores\\n" +
                                     "  edit: Edit one of your products\\n" +
                                     "  remove: Remove one of your products\\n" +
                                     "  csv_import: Import a csv file containing your new products\\n" +
                                     "  csv_export: Export a csv file containing your current products\\n" +
                                     "  exit: Exit the application");
                            pw.println();
                            pw.flush();
                            break;

                        // Invalid command message
                        } else {
                            pw.write("Invalid Command");
                            pw.println();
                            pw.flush();
                        }
                    }

                // Seller wants to add a new product
                } else if (input.equals("add")) {
                    // Gets all the product information
                    pw.write("Enter the product name:");
                    pw.println();
                    pw.flush();
                    String name = br.readLine();
                    
                    pw.write("Enter the store name:");
                    pw.println();
                    pw.flush();
                    String store = br.readLine();
                    
                    pw.write("Enter the product description:");
                    pw.println();
                    pw.flush();
                    String description = br.readLine();
                    
                    pw.write("Enter the quantity:");
                    pw.println();
                    pw.flush();
                    int quantity;

                    // Checking to ensure the quantity is a valid number
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

                    // Getting the cost for the product
                    pw.write("Enter the cost:");
                    pw.println();
                    pw.flush();
                    float price;

                    // Checking to ensure the cost is a valid number
                    while (true) {
                        try {
                            price = Float.parseFloat(br.readLine());
                            break;
                        } catch (NumberFormatException e) {
                            pw.write("Invalid number\\n" +
                                     "Enter the cost:");
                            pw.println();
                            pw.flush();
                        }
                    }

                    // Displays that a new product was successfully added
                    pw.write("New Product Added");
                    pw.println();
                    pw.flush();

                    // Creates the new product
                    Product newProduct = new Product(name, store, user.getLogin(), description, quantity, price);
                    user.addProduct(newProduct);

                // Seller wants to edit an existing product
                } else if (input.equals("edit")) {
                    // Getting the name of the product
                    pw.write("Enter the name of the product you wish to edit:");
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
                            pw.write("Product not found" +
                                     "Enter the name of the product you wish to edit:");
                            pw.println();
                            pw.flush();
                        }
                    }

                    // Getting the new information
                    pw.write("Enter the new name:");
                    pw.println();
                    pw.flush();
                    String newName = br.readLine();

                    pw.write("Enter the new store:");
                    pw.println();
                    pw.flush();
                    String store = br.readLine();

                    pw.write("Enter the new description:");
                    pw.println();
                    pw.flush();
                    String description = br.readLine();

                    pw.write("Enter the new quantity:");
                    pw.println();
                    pw.flush();
                    int quantity;

                    // Checking that the quantity is valid
                    while (true) {
                        try {
                            quantity = Integer.parseInt(br.readLine());
                            break;
                        } catch (NumberFormatException e) {
                            pw.write("Invalid number\\n" +
                                     "Enter the new quantity:");
                            pw.println();
                            pw.flush();
                        }
                    }

                    pw.write("Enter the new price:");
                    pw.println();
                    pw.flush();
                    float price;

                    // Checking that the price is valid
                    while (true) {
                        try {
                            price = Float.parseFloat(br.readLine());
                            break;
                        } catch (NumberFormatException e) {
                            pw.write("Invalid number\\n" +
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
                    pw.write("Product changed");
                    pw.println();
                    pw.flush();

                // Seller wants to remove a product
                } else if (input.equals("remove")) {
                    // Getting the product name
                    pw.write("Enter the name of the product you wish to delete:\\n" +
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
                    pw.write("Item successfully deleted");
                    pw.println();
                    pw.flush();

                // Imports a csv file containing a seller's new products
                } else if (input.equals("csv_import")) {
                    // Getting the file name
                    pw.write("Enter the file name:");
                    pw.println();
                    pw.flush();
                    input = br.readLine();
                    boolean success = user.addCSV(input);

                    // Displaying that the file was imported successfully
                    if (success) {
                        pw.write("CSV Imported");
                        pw.println();
                        pw.flush();

                    // File import failed
                    } else {
                        pw.write("CSV Import Failed");
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
                    pw.write("CSV Exported");
                    pw.println();
                    pw.flush();

                // Seller wants to exit the marketplace
                } else if (input.equals("exit")) {
                    pw.write("Thank you for visiting the Market!");
                    pw.println();
                    pw.flush();
                    break;

                // An invalid command was used
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