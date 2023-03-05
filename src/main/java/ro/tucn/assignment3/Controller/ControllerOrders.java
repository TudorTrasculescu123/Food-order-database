package ro.tucn.assignment3.Controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.stage.Stage;
import ro.tucn.assignment3.BusinessLogic.OrdersBLL;
import ro.tucn.assignment3.App;
import ro.tucn.assignment3.Model.Orders;

import java.io.IOException;
import java.util.List;

public class ControllerOrders {
    @FXML
    private Button clientButton;

    @FXML
    private Button productButton;

    @FXML
    private TableView ordersTable;

    @FXML
    private Button addButton;

    public void initialize(){
        OrdersBLL ordersBLL = new OrdersBLL();
        List<TableColumn<Orders, Integer>> lst = ordersBLL.setFields();
        ordersTable.getColumns().addAll(lst);
        List<Orders> population = ordersBLL.findAll();
        ordersTable.getItems().setAll(population);
        ordersTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
    }

    public void goToClient() throws IOException {
        FXMLLoader loader = new FXMLLoader(App.class.getResource("client-view.fxml"));
        Stage stage = (Stage) clientButton.getScene().getWindow();
        Scene scene = new Scene(loader.load(), 734, 587);
        stage.setScene(scene);
        stage.setTitle("Client table");
        stage.show();
    }

    public void goToProduct() throws IOException {
        FXMLLoader loader = new FXMLLoader(App.class.getResource("product-view.fxml"));
        Stage stage = (Stage) productButton.getScene().getWindow();
        Scene scene = new Scene(loader.load(), 734, 587);
        stage.setScene(scene);
        stage.setTitle("Product table");
        stage.show();
    }

    public void addOrder() throws IOException {
        FXMLLoader loader = new FXMLLoader(App.class.getResource("enter-orders.fxml"));
        Stage stage = (Stage) addButton.getScene().getWindow();
        Scene scene = new Scene(loader.load(), 400, 329);
        stage.setScene(scene);
        stage.setTitle("Add orders");
        stage.show();
    }
}