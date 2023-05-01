import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

/**
 * 
 * A simple customer class that tracks shopping list and purchases
 * 
 * @author Alpamys Sultanbek
 * @version 1.0
 */
public class Customer extends User {
    private ArrayList<Product> shoppingList;
    private ArrayList<Product> purchases;

    public Customer(String login, String password) {
        super(login, password);
        shoppingList = new ArrayList<Product>();
        purchases = new ArrayList<Product>();
    }

    public ArrayList<Product> getShoppingList() {
        return shoppingList;
    }

    public ArrayList<Product> getPurchases() {
        return purchases;
    }

    public void addShoppingCart(Product product) {
        shoppingList.add(product);
    }

    public void addPurchases(Product product) {
        purchases.add(product);
    }

    public void removeShoppingCart(String name) {
        for (Product p : shoppingList) {
            if (p.getName().equals(name)) {
                shoppingList.remove(p);
                break;
            }
        }
    }

    public void removeShoppingCart(Product product) {
        if (shoppingList.contains(product)) {
            shoppingList.remove(product);
        }
    }

    public void removePurchases(Product product) {
        int i = purchases.indexOf(product);
        shoppingList.remove(i);
    }
    /*
    prints the users cart with the name of an item, what store it is from,
    description of item, quantity, and price of item
     */
    public String cart() {
        String out = "";
        out += "|table|Name,Store,Description,Quantity,Price\\n";
        for (int i = 0; i < shoppingList.size(); i++) {
            Product p = shoppingList.get(i);
            out += String.format("%s,%s,%s,%d,%.2f\\n",
                                 p.getName(), 
                                 p.getStore(),
                                 p.getDescription(),
                                 p.getQuantity(),
                                 p.getPrice());
        }
        return out;
    }
    /*
    shows what the user has purchased with the name of an item, what store it is from,
    description of item, quantity, and price of item
     */
    public String purchasesPrint() {
        String out = "";
        out += "|table|Name,Store,Description,Quantity,Price\\n";
        for (int i = 0; i < purchases.size(); i++) {
            Product p = purchases.get(i);
            out += String.format("%s,%s,%s,%d,%.2f\\n",
                                 p.getName(), 
                                 p.getStore(),
                                 p.getDescription(),
                                 p.getQuantity(),
                                 p.getPrice());
        }
        return out;
    }

    public void exportCSV(String fileName) {
        try {
            FileWriter fw = new FileWriter(fileName);
            String out = "";
            Product temp;
            for (int i = 0; i < purchases.size(); i++) {
                temp = purchases.get(i);
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

    public void setShoppingList(ArrayList<Product> shoppingList) {
        this.shoppingList = shoppingList;
    }   
}
