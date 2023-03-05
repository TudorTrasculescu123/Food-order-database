package ro.tucn.assignment3.Model;

public class Product {
    private int ID;
    private String foodName;
    private int currentStock;
    private int price;

    public Product(int ID, String foodName, int currentStock, int price) {
        this.ID = ID;
        this.foodName = foodName;
        this.currentStock = currentStock;
        this.price = price;
    }

    public Product(String foodName, int currentStock, int price){
        this.foodName = foodName;
        this.currentStock = currentStock;
        this.price = price;
    }

    public Product(){

    }

    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    public String getFoodName() {
        return foodName;
    }

    public void setFoodName(String foodName) {
        this.foodName = foodName;
    }

    public int getCurrentStock() {
        return currentStock;
    }

    public void setCurrentStock(int currentStock) {
        this.currentStock = currentStock;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }
}
