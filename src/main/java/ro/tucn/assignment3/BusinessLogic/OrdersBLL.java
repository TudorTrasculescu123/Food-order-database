package ro.tucn.assignment3.BusinessLogic;

import javafx.scene.control.Alert;
import javafx.scene.control.TableColumn;
import ro.tucn.assignment3.DataAccess.AbstractDAO;
import ro.tucn.assignment3.DataAccess.ClientDAO;
import ro.tucn.assignment3.DataAccess.OrdersDAO;
import ro.tucn.assignment3.DataAccess.ProductDAO;
import ro.tucn.assignment3.Model.Client;
import ro.tucn.assignment3.Model.Orders;
import ro.tucn.assignment3.Model.Product;

import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

/**
 * This class can be found inside the Business Logic package. I's purpose is
 * to communicate between the Controller and the Data Access. The data taken
 * from the Controller will be validated, then send to the Data Access where
 * the queries will be generated and the results o the queries will be sent
 * back to the Controller in order to make the changes to the graphical users
 * interface. The class contains one private field which is of OrdersDAO type
 * and is instantiated through the constructor.
 */
public class OrdersBLL {
    private AbstractDAO myOrdersDao;

    public OrdersBLL() {
        myOrdersDao = new OrdersDAO();
    }

    public List<Orders> findAll(){
        List<Orders> lst;
        lst = myOrdersDao.findAll();
        return lst;
    }

    public List <TableColumn<Orders, Integer>> setFields(){
        List <TableColumn<Orders, Integer>> lst = myOrdersDao.setFields();
        return lst;
    }

    /**
     * This method takes as parameter an object of type Orders. In order to perform
     * an order some constraints must be met. Firstly the same person can not order
     * the same product in different orders. He must choose a quantity that he wants.
     * If the quantity is a positive number and if the stock of the product is larger
     * or equal to the quantity that the client is ordering, the order will be listed
     * and the stock of that product will be updated. If the previous conditions are
     * not met, then an Alert will be popped up on the screen with a suggestive message.
     * @param orders Order from the Model package
     */
    public void addOrder(Orders orders){
        AbstractDAO clientDao = new ClientDAO();
        AbstractDAO productDao = new ProductDAO();
        Client client = (Client) clientDao.findById(orders.getClientID());
        Product product = (Product) productDao.findById(orders.getProductID());
        if(product != null && client != null) {
            if (orders.getQuantity() < 0) {
                String s = "Please enter a positive quantity!";
                showErr(s);
            } else if (orders.getQuantity() > product.getCurrentStock()) {
                String s = "There are not enough products in the stock!";
                showErr(s);
            } else {
                product.setCurrentStock(product.getCurrentStock() - orders.getQuantity());
                productDao.update(product);
                myOrdersDao.insert(orders);
                int finalPrice = orders.getQuantity() * product.getPrice();
                generateBill(finalPrice, client.getName(), orders.getQuantity(), product.getFoodName());
            }
        }else{
            String s = "ID out of range!";
            showErr(s);
        }
    }

    /**
     * This method takes as parameter a String which will be used to pop
     * up on the screen an alert with a suggestive text.
     * @param errorType String used to determine the type of alert
     */
    private void showErr(String errorType){
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error!");
        alert.setHeaderText(errorType);
        alert.showAndWait();
    }

    /**
     * This method is used for generating a bill after each order. If the order
     * was made with success, then a text file ("bill.txt") will be generated,
     * where the name of the client, the quantity of the product ordered and the
     * final price can be found.
     * @param finalPrice the final price
     * @param clientName the name of the client
     * @param noProducts the number of products ordered
     * @param producName the name of the ordered product

     */
    public void generateBill(int finalPrice, String clientName, int noProducts, String producName){
        String s = "Client: " + clientName + " ordered " + noProducts +  " " + producName + "\n" + "The final price is: " + finalPrice;
        FileWriter myWriter = null;
        try {
            myWriter = new FileWriter("bill.txt");
            myWriter.write(s);
            myWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        //System.out.println("Successfully wrote to the file.");
    }
}
