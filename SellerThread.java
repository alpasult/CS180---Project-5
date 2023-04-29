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
                if (input.equals("data")) {
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
                        if (input.equals("name") || 
                            input.equals("store") || 
                            input.equals("quantity") ||
                            input.equals("price") ||
                            input.equals("amount_sold")) {
                            String out = user.data(input);
                            out = "|info|" + out;
                            pw.write(out);
                            pw.println();
                            pw.flush();
                        } else if (input.equals("customers")) {
                            String out = MarketServer.customerString(user.getLogin());
                            out = "|info|" + out;
                            pw.write(out);
                            pw.println();
                            pw.flush();
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
                } else if (input.equals("add")) {
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
                    while (true) {
                        try {
                            quantity = Integer.parseInt(br.readLine());
                            break;
                        } catch (NumberFormatException e) {
                            pw.write("|input|Invalid number\\n" +
                                     "Enter the quantity:");
                            pw.println();
                            pw.flush();
                        }
                    }
                    
                    pw.write("|input|Enter the cost:");
                    pw.println();
                    pw.flush();
                    float price;
                    while (true) {
                        try {
                            price = Float.parseFloat(br.readLine());
                            break;
                        } catch (NumberFormatException e) {
                            pw.write("|input|Invalid number\\n" +
                                     "Enter the cost:");
                            pw.println();
                            pw.flush();
                        }
                    }

                    pw.write("|info|New Product Added");
                    pw.println();
                    pw.flush();
                    Product newProduct = new Product(name, store, user.getLogin(), description, quantity, price);
                    user.addProduct(newProduct);
                } else if (input.equals("edit")) {
                    pw.write("|input|Enter the name of the product you wish to edit:");
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
                            pw.write("|input|Product not found\\n" +
                                     "Enter the name of the product you wish to edit:");
                            pw.println();
                            pw.flush();
                        }
                    }

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
                    while (true) {
                        try {
                            quantity = Integer.parseInt(br.readLine());
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
                    while (true) {
                        try {
                            price = Float.parseFloat(br.readLine());
                            break;
                        } catch (NumberFormatException e) {
                            pw.write("|input|Invalid number\\n" +
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
                    pw.write("|info|Product changed");
                    pw.println();
                    pw.flush();
                } else if (input.equals("remove")) {
                    pw.write("|input|Enter the name of the product you wish to delete:\\n" +
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
                    pw.write("|info|Item successfully deleted");
                    pw.println();
                    pw.flush();
                } else if (input.equals("csv_import")) {
                    pw.write("|input|Enter the file name:");
                    pw.println();
                    pw.flush();
                    input = br.readLine();
                    boolean success = user.addCSV(input);
                    if (success) {
                        pw.write("|info|CSV Imported");
                        pw.println();
                        pw.flush();
                    } else {
                        pw.write("|info|CSV Import Failed");
                        pw.println();
                        pw.flush();
                    }
                } else if (input.equals("csv_export")) {
                    String fileName = "";
                    fileName += user.getLogin() + "_products.csv";
                    user.exportCSV(fileName);
                    pw.write("|info|CSV Exported");
                    pw.println();
                    pw.flush();
                } else if (input.equals("exit")) {
                    pw.write("|info|Thank you for visiting the Market!");
                    pw.println();
                    pw.flush();
                    break;
                } 

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