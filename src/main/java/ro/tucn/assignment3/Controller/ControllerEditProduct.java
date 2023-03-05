package ro.tucn.assignment3.Controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import ro.tucn.assignment3.BusinessLogic.ProductBLL;
import ro.tucn.assignment3.App;
import ro.tucn.assignment3.Model.Product;

import java.io.IOException;
import java.util.regex.Pattern;

public class ControllerEditProduct {
    @FXML
    private Button cancelButton;

    @FXML
    private Button doneButton;

    @FXML
    private TextField idText;

    @FXML
    private TextField stockText;

    @FXML
    private TextField foodText;

    @FXML
    private TextField priceText;

    @FXML
    private Label labelID;
    private int chooseButton = 0;
    private int mode; // if mode = 0 -> edit was pressed
                      // if mode = 1 -> insert was pressed;

    public void cancelPressed() throws IOException {
        Button currentButton;
        ProductBLL productBLL = new ProductBLL();
        if(chooseButton == 0){
            currentButton = cancelButton;
        }else{
            currentButton = doneButton;
            if(mode == 1){ // add was pressed before
                Product product = new Product(foodText.getText(), Integer.parseInt(stockText.getText()),Integer.parseInt(priceText.getText()));
                productBLL.insertProduct(product);
            }else{ // edit was pressed before
                Product product = new Product(Integer.parseInt(idText.getText()), foodText.getText(), Integer.parseInt(stockText.getText()),Integer.parseInt(priceText.getText()));
                productBLL.updateProduct(product);
            }
        }
        FXMLLoader loader = new FXMLLoader(App.class.getResource("product-view.fxml"));
        Stage stage = (Stage) currentButton.getScene().getWindow();
        Scene scene = new Scene(loader.load(), 734, 587);
        chooseButton = 0;
        stage.setScene(scene);
        stage.setTitle("Product table");
        stage.show();
    }

    public void donePressed() throws IOException {
        int errorCode = checkInput();
        if(errorCode == 0){
            chooseButton = 1;
            cancelPressed();
        }else if(errorCode == 1){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error!");
            alert.setHeaderText("Some data is missing");
            alert.showAndWait();
        }else if(errorCode == 2){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error!");
            alert.setHeaderText("Wrong stock!");
            alert.showAndWait();
        }else if(errorCode == 3){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error!");
            alert.setHeaderText("Wrong price!");
            alert.showAndWait();
        }
    }

    public int checkInput(){
        //TODO
        if(stockText.getText() == "" || foodText.getText() == "" || priceText.getText() == ""){
            return 1;
        }
        String isNumber = "^[0-9]*$";
        Pattern pattern = Pattern.compile(isNumber);
        if (!pattern.matcher(stockText.getText()).matches() || Integer.parseInt(stockText.getText()) < 0)
            return 2;
        if (!pattern.matcher(priceText.getText()).matches() || Integer.parseInt(priceText.getText()) < 0)
            return 3;
        return 0;
    }

    public void setMode(int mode){
        this.mode = mode;
    }

    public void setData(Product product){
        idText.setVisible(true);
        labelID.setVisible(true);
        idText.setText(String.valueOf(product.getID()));
        stockText.setText(String.valueOf(product.getCurrentStock()));
        foodText.setText(product.getFoodName());
        priceText.setText(String.valueOf(product.getPrice()));
    }

    public void bringBackScene(){
        idText.setVisible(false);
        labelID.setVisible(false);
    }
}
