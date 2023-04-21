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

    public SellerThread(Socket client, Seller user) {
        this.client = client;
        this.user = user;
    }

    public void run() {
        try {
            BufferedReader br = new BufferedReader(new InputStreamReader(client.getInputStream()));
            PrintWriter pw = new PrintWriter(client.getOutputStream()); 
            
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
                if (input.equals("data")) {
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
                        if (input.equals("name") || 
                            input.equals("store") || 
                            input.equals("quantity") ||
                            input.equals("price") ||
                            input.equals("amount_sold")) {
                            String out = user.data(input);
                            pw.write(out);
                            pw.println();
                            pw.flush();
                        } else if (input.equals("customers")) {
                            String out = MarketServer.customerString(user.getLogin());
                            pw.write(out);
                            pw.println();
                            pw.flush();
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
                        } else {
                            pw.write("Invalid Command");
                            pw.println();
                            pw.flush();
                        }
                    }
                } else if (input.equals("add")) {
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
                    
                    pw.write("Enter the cost:");
                    pw.println();
                    pw.flush();
                    float price;
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

                    pw.write("New Product Added");
                    pw.println();
                    pw.flush();
                    Product newProduct = new Product(name, store, user.getLogin(), description, quantity, price);
                    user.addProduct(newProduct);
                } else if (input.equals("edit")) {
                    pw.write("Enter the name of the product you wish to edit:");
                    pw.println();
                    pw.flush();
                    Product[] products = user.getProducts();
                    boolean done = false;
                    Product product = null;
                    String name;
                    while (!done) {
                        name = br.readLine();
                        for (Product p : products) {
                            if (p.getName().equals(name)) {
                                product = p;
                                done = true;
                                break;
                            }
                        }
                        if (!done) {
                            pw.write("Product not found" +
                                     "Enter the name of the product you wish to edit:");
                            pw.println();
                            pw.flush();
                        }
                    }

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

                    
                    product.setName(newName);
                    product.setPrice(price);
                    product.setStore(store);
                    product.setDescription(description);
                    product.setQuantity(quantity);
                    pw.write("Product changed");
                    pw.println();
                    pw.flush();
                } else if (input.equals("remove")) {
                    pw.write("Enter the name of the product you wish to delete:\\n" +
                             "WARNING: DELETING THE PRODUCT WILL DELETE ALL RECORDS OF IT");
                    pw.println();
                    pw.flush();
                    String name = br.readLine();
                    Product[] products = user.getProducts();
                    for (Product p : products) {
                        if (p.getName().equals(name)) {
                            user.removeProduct(p);
                            break;
                        }
                    }
                    pw.write("Item successfully deleted");
                    pw.println();
                    pw.flush();
                } else if (input.equals("csv_import")) {
                    pw.write("Enter the file name:");
                    pw.println();
                    pw.flush();
                    input = br.readLine();
                    boolean success = user.addCSV(input);
                    if (success) {
                        pw.write("CSV Imported");
                        pw.println();
                        pw.flush();
                    } else {
                        pw.write("CSV Import Failed");
                        pw.println();
                        pw.flush();
                    }
                } else if (input.equals("csv_export")) {
                    String fileName = "";
                    fileName += user.getLogin() + "_products.csv";
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