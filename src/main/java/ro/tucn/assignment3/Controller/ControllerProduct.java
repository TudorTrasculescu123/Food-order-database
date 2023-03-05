package ro.tucn.assignment3.Controller;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.stage.Stage;
import ro.tucn.assignment3.BusinessLogic.ProductBLL;
import ro.tucn.assignment3.App;
import ro.tucn.assignment3.Model.Product;
import java.io.IOException;
import java.util.List;

public class ControllerProduct {
    @FXML
    private Button clientButton;

    @FXML
    private Button ordersButton;

    @FXML
    private TableView productTable;

    @FXML
    private Button addButton;

    public void initialize(){
        ProductBLL productBLL = new ProductBLL();
        List<TableColumn<Product, Integer>> lst = productBLL.setFields();
        productTable.getColumns().addAll(lst);
        List<Product> population = productBLL.findAll();
        productTable.getItems().setAll(population);
        productTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        productTable.getSortOrder().add(productTable.getColumns().get(0));
    }

    public void goToClient() throws IOException {
        FXMLLoader loader = new FXMLLoader(App.class.getResource("client-view.fxml"));
        Stage stage = (Stage) clientButton.getScene().getWindow();
        Scene scene = new Scene(loader.load(), 734, 587);
        stage.setScene(scene);
        stage.setTitle("Client table");
        stage.show();
    }

    public void goToOrders() throws IOException {
        FXMLLoader loader = new FXMLLoader(App.class.getResource("orders-view.fxml"));
        Stage stage = (Stage) ordersButton.getScene().getWindow();
        Scene scene = new Scene(loader.load(), 734, 587);
        stage.setScene(scene);
        stage.setTitle("Orders table");
        stage.show();
    }

    public void editProduct() throws IOException {
        handleProduct(0);
    }

    public void addProduct() throws IOException {
        handleProduct(1);
    }

    public void handleProduct(int mode) throws IOException {
        FXMLLoader loader = new FXMLLoader(App.class.getResource("enter-product.fxml"));
        Stage stage = (Stage) addButton.getScene().getWindow();
        Product selectedItem = (Product) productTable.getSelectionModel().getSelectedItem();
        Scene scene = new Scene(loader.load(), 399, 416);
        ((ControllerEditProduct)loader.getController()).setMode(mode);
        if(mode == 0){
            if(selectedItem != null){
                ((ControllerEditProduct)loader.getController()).setData(selectedItem);
            }else{
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error!");
                alert.setHeaderText("Please select a row that you want to edit");
                alert.showAndWait();
                return;
            }
        }
        stage.setScene(scene);
        if(mode == 1){
            stage.setTitle("Add product");
            ((ControllerEditProduct)loader.getController()).bringBackScene();
        }else{
            stage.setTitle("Edit product");
        }
        stage.show();
    }

    public void removeProduct(){
        Product selectedItem = (Product) productTable.getSelectionModel().getSelectedItem();
        if(selectedItem != null){
            ProductBLL productBLL = new ProductBLL();
            productBLL.deleteProduct(selectedItem);
            productTable.getItems().clear();
            productTable.getColumns().clear();
            initialize();
        }else{
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error!");
            alert.setHeaderText("Please select a row that you want to delete");
            alert.showAndWait();
        }
    }
}
