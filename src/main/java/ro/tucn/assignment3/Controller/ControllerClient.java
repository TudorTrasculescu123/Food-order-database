package ro.tucn.assignment3.Controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import ro.tucn.assignment3.BusinessLogic.ClientBLL;
import ro.tucn.assignment3.Model.Client;
import ro.tucn.assignment3.App;

import java.io.IOException;
import java.util.List;

public class ControllerClient {
    @FXML
    private Button productButton;

    @FXML
    private Button ordersButton;

    @FXML
    private TableView clientTable;

    @FXML
    private Button editButton;

    @FXML
    private Button addButton;

    /**
     * This method is called every time the application is switched to the
     * Client. It's main use is to load all the necessary data inside the
     * Table View. The name of the fields and the content of the table is
     * generated through reflection.
     */
    public void initialize(){
        ClientBLL clientBLL = new ClientBLL();
        List <TableColumn<Client, Integer>> lst = clientBLL.setFields();
        clientTable.getColumns().addAll(lst);
        List<Client> population = clientBLL.findAll();
        clientTable.getItems().setAll(population);
        clientTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        clientTable.getSortOrder().add(clientTable.getColumns().get(0));
    }

    /**
     * Method used to switch to the Product table
     * @throws IOException
     */
    public void goToProduct() throws IOException {
        FXMLLoader loader = new FXMLLoader(App.class.getResource("product-view.fxml"));
        Stage stage = (Stage) productButton.getScene().getWindow();
        Scene scene = new Scene(loader.load(), 734, 587);
        stage.setScene(scene);
        stage.setTitle("Product table");
        stage.show();
    }

    /**
     * Method used to switch to the Orders table
     * @throws IOException
     */
    public void goToOrders() throws IOException {
        FXMLLoader loader = new FXMLLoader(App.class.getResource("orders-view.fxml"));
        Stage stage = (Stage) ordersButton.getScene().getWindow();
        Scene scene = new Scene(loader.load(), 734, 587);
        stage.setScene(scene);
        stage.setTitle("Orders table");
        stage.show();
    }

    public void editClient() throws IOException {
        handleClient(0);
    }

    public void addClient() throws IOException {
        handleClient(1);
    }

    /**
     * The method is used to switch to the View where a Client will be
     * generated or modified based on the button which the user pressed
     * before.
     * @param mode is 1 if the "addClient" button was pressed and 0 if
     *             the "editClient" button was pressed.
     * @throws IOException
     */
    public void handleClient(int mode) throws IOException {
        FXMLLoader loader = new FXMLLoader(App.class.getResource("enter-client.fxml"));
        Stage stage = (Stage) addButton.getScene().getWindow();
        Client selectedItem = (Client) clientTable.getSelectionModel().getSelectedItem();
        Scene scene = new Scene(loader.load(), 393, 498);
        ((ControllerEditClient)loader.getController()).setMode(mode);
        if(mode == 0) {
            if (selectedItem != null) {
                ((ControllerEditClient) loader.getController()).setData(selectedItem);
            } else {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error!");
                alert.setHeaderText("Please select a row that you want to edit");
                alert.showAndWait();
                return;
            }
        }
        stage.setScene(scene);
        if(mode == 1) {
            stage.setTitle("Add client");
            ((ControllerEditClient)loader.getController()).bringBackScene();
        }
        else
            stage.setTitle("Edit client");
        stage.show();
    }

    /**
     * Method used to remove a CLient or to show an alert with a suggestive
     * message if the operation could not occur.
     */
    public void removeClient(){
        Client selectedItem = (Client) clientTable.getSelectionModel().getSelectedItem();
        if(selectedItem != null){
            ClientBLL clientBLL = new ClientBLL();
            clientBLL.deleteClient(selectedItem);
            clientTable.getItems().clear();
            clientTable.getColumns().clear();
            initialize();
        }else{
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error!");
            alert.setHeaderText("Please select a row that you want to delete");
            alert.showAndWait();
        }
    }
}