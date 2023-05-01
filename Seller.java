import java.io.*;
import java.util.ArrayList;

/**
 * 
 * A simple Seller class that tracks products up for sale
 * 
 * @author Alpamys Sultanbek
 * @version 1.0
 */
public class Seller extends User {
    private ArrayList<Product> products;

    public Seller(String login, String password) {
        super(login, password);
        products = new ArrayList<Product>();
    }

    public Product[] getProducts() {
        return products.toArray(new Product[products.size()]);
    }

    public void addProduct(Product product) {
        products.add(product);
    }

    public void editProduct(Product product, Product newProduct) {
        int index = products.indexOf(product);
        products.set(index, newProduct);
    }

    public void removeProduct(Product product) {
        products.remove(product);
    }

    /*
    function allows seller to import csv to add new products
     */
    public boolean addCSV(String fileName) {
        ArrayList<String> newProducts = new ArrayList<String>();
        BufferedReader br;
        try {
            br = new BufferedReader(new FileReader(fileName));
            String line = br.readLine();
            while (line != null) {
                newProducts.add(line);
                line = br.readLine();
            }
            br.close();
        } catch (IOException e) {
            return false;
        }

        String[] product;
        for (int i = 0; i < newProducts.size(); i++) {
            try {
                product = newProducts.get(i).split(",");
                this.addProduct(new Product(product[0], 
                                            product[1], 
                                            this.getLogin(), 
                                            product[2], 
                                            Integer.parseInt(product[3]), 
                                            Float.parseFloat(product[4])));
            } catch (Exception e) {
                return false;
            }
        }
        return true;
    }

    public void exportCSV(String fileName) {
        try {
            FileWriter fw = new FileWriter(fileName);
            String out = "";
            Product temp;
            for (int i = 0; i < products.size(); i++) {
                temp = products.get(i);
                out += temp.getName() + ",";
                out += temp.getStore() + ",";
                out += temp.getDescription() + ",";
                out += temp.getQuantity() + ",";
                out += temp.getPrice();
                out += "\n";
            }
            fw.write(out);
            fw.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }   

    public String data(String format) {
        String out = "";
        Product[] sortedProducts = products.toArray(new Product[products.size()]); 
        for (int i = 0; i < sortedProducts.length - 1; i++) {
            for (int j = i + 1; j < sortedProducts.length; j++) {
                Product smaller = sortedProducts[i];
                Product bigger = sortedProducts[j];

                if (format.equals("name")) {
                    if (smaller.getName().toUpperCase().compareTo(bigger.getName().toUpperCase()) > 0) {
                        bigger = sortedProducts[i];
                        smaller = sortedProducts[j];
                    }
                } else if (format.equals("store")) {
                    if (smaller.getStore().toUpperCase().compareTo(bigger.getStore().toUpperCase()) > 0) {
                        bigger = sortedProducts[i];
                        smaller = sortedProducts[j];
                    }
                } else if (format.equals("quantity")) {
                    if (smaller.getQuantity() > bigger.getQuantity()) {
                        bigger = sortedProducts[i];
                        smaller = sortedProducts[j];
                    }
                } else if (format.equals("price")) {
                    if (smaller.getPrice() > bigger.getPrice()) {
                        bigger = sortedProducts[i];
                        smaller = sortedProducts[j];
                    }
                } else if (format.equals("amount_sold")) {
                    if (smaller.getAmountSold() > bigger.getAmountSold()) {
                        bigger = sortedProducts[i];
                        smaller = sortedProducts[j];
                    }
                }

                sortedProducts[i] = smaller;
                sortedProducts[j] = bigger;
            }
        }
        out += "|table|Name,Store,Description,Quantity,Price,Amount Sold,Profit\\n";
        for (int i = 0; i < sortedProducts.length; i++) {
            Product p = sortedProducts[i];
            out += String.format("%.15s,%.15s,%.30s,%d,%.2f,%d,%.2f\\n", 
                                 p.getName(), 
                                 p.getStore(),
                                 p.getDescription(),
                                 p.getQuantity(),
                                 p.getPrice(),
                                 p.getAmountSold(),
                                 p.getAmountSold() * p.getPrice());
        }
        return out;
    }
}
