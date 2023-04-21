/**
 * 
 * A simple Product class, has a name price and quanitity
 * Can be bought and sold by the user class
 * 
 * @author Alpamys Sultanbek
 * @version 1.0
 */
public class Product {
    private String name;
    private String store;
    private String seller;
    private String description;
    private int quantity;
    private int amountSold;
    private float price;
    

    public Product(String name, String store, String seller, String description, int quantity, float price) {
        this.name = name;
        this.store = store;
        this.seller = seller;
        this.description = description;
        this.quantity = quantity;
        this.price = price;
        amountSold = 0;
    }

    public String getName() {
        return name;
    }

    public String getStore() {
        return store;
    }

    public String getSeller() {
        return seller;
    }

    public String getDescription() {
        return description;
    }

    public int getQuantity() {
        return quantity;
    }

    public float getPrice() {
        return price;
    }

    public int getAmountSold() {
        return amountSold;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setStore(String store) {
        this.store = store;
    }

    public void setDescription(String description) {
        this.description = description;
    }
    
    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public void setPrice(float price) {
        this.price = price;
    }

    public void setAmountSold(int amountSold) {
        this.amountSold = amountSold;
    }
}
