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

    public String cart() {
        String out = "";
        out += "  |name           |store          |description                   | quantity|     price|\\n";
        out += "=======================================================================================\\n";
        for (int i = 0; i < shoppingList.size(); i++) {
            Product p = shoppingList.get(i);
            out += String.format("%-2d|%-15.15s|%-15.15s|%-30.30s|%9d|%10.2f|\\n", 
                                 i, 
                                 p.getName(), 
                                 p.getStore(),
                                 p.getDescription(),
                                 p.getQuantity(),
                                 p.getPrice());
            out += "=======================================================================================\\n";
        }
        return out;
    }
    
    public String purchasesPrint() {
        String out = "";
        out += "  |name           |store          |description                   | quantity|     price|\\n";
        out += "=======================================================================================\\n";
        for (int i = 0; i < purchases.size(); i++) {
            Product p = purchases.get(i);
            out += String.format("%-2d|%-15.15s|%-15.15s|%-30.30s|%9d|%10.2f|\\n", 
                                 i, 
                                 p.getName(), 
                                 p.getStore(),
                                 p.getDescription(),
                                 p.getQuantity(),
                                 p.getPrice());
            out += "=======================================================================================\\n";
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
