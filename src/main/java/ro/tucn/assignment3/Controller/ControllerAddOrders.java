package ro.tucn.assignment3.Controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import ro.tucn.assignment3.BusinessLogic.OrdersBLL;
import ro.tucn.assignment3.App;
import ro.tucn.assignment3.Model.Orders;

import java.io.IOException;

public class ControllerAddOrders {

    @FXML
    private TextField clientText;

    @FXML
    private TextField productText;

    @FXML
    private TextField quantityText;

    @FXML
    private Button cancelButton;

    @FXML
    private Button doneButton;

    public void donePressed() throws IOException {
        Orders order = new Orders(Integer.parseInt(clientText.getText()), Integer.parseInt(productText.getText()), Integer.parseInt(quantityText.getText()));
        OrdersBLL orderBLL = new OrdersBLL();
        orderBLL.addOrder(order);
        cancelPressed();
    }

    public void cancelPressed() throws IOException {
        FXMLLoader loader = new FXMLLoader(App.class.getResource("orders-view.fxml"));
        Stage stage = (Stage) cancelButton.getScene().getWindow();
        Scene scene = new Scene(loader.load(), 734, 587);
        stage.setScene(scene);
        stage.setTitle("Orders table");
        stage.show();
    }
}
