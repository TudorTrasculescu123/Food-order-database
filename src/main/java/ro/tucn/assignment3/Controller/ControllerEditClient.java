package ro.tucn.assignment3.Controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import ro.tucn.assignment3.BusinessLogic.ClientBLL;
import ro.tucn.assignment3.App;
import ro.tucn.assignment3.Model.Client;

import java.io.IOException;
import java.util.regex.Pattern;

public class ControllerEditClient {

    @FXML
    private Button cancelButton;

    @FXML
    private Button doneButton;

    @FXML
    private TextField nameText;

    @FXML
    private TextField ageText;

    @FXML
    private TextField addressText;

    @FXML
    private TextField emailText;

    @FXML
    private Label labelID;

    @FXML
    private TextField idText;
    private int chooseButton = 0;
    private int mode; // if mode = 0 -> edit was pressed
                      // if mode = 1 -> insert was pressed

    public void cancelPressed() throws IOException {
        Button currentButton;
        ClientBLL clientBLL = new ClientBLL();
        if(chooseButton == 0)
            currentButton =  cancelButton;
        else {
            currentButton = doneButton;
            if(mode == 1){ // add was pressed before
                Client client = new Client(nameText.getText(), addressText.getText(), emailText.getText(), Integer.parseInt(ageText.getText()));
                clientBLL.insertStudent(client);
            }else{ // edit was pressed before
                Client client = new Client(Integer.parseInt(idText.getText()),nameText.getText(), addressText.getText(), emailText.getText(), Integer.parseInt(ageText.getText()));
                clientBLL.updateClient(client);
            }
        }
        FXMLLoader loader = new FXMLLoader(App.class.getResource("client-view.fxml"));
        Stage stage = (Stage) currentButton.getScene().getWindow();
        Scene scene = new Scene(loader.load(), 734, 587);
        chooseButton = 0;
        stage.setScene(scene);
        stage.setTitle("Client table");
        stage.show();
    }

    public void donePressed() throws IOException {
        int errorCode = checkInput();
        if(errorCode== 0){
            chooseButton = 1;
            cancelPressed();
        }else if (errorCode == 1){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error!");
            alert.setHeaderText("Some data is missing");
            alert.showAndWait();
        }else if(errorCode == 2){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error!");
            alert.setHeaderText("Age should be a number!");
            alert.showAndWait();
        }
    }

    public int checkInput(){
        //To DO
        if(nameText.getText() == "" || ageText.getText() == "" || addressText.getText() == "" ||emailText.getText() == "")
            return 1;
        String isNumber = "^[0-9]*$";
        Pattern pattern = Pattern.compile(isNumber);
        if (!pattern.matcher(ageText.getText()).matches())
            return 2;
        ClientBLL clientBLL = new ClientBLL();
        Client client = new Client(nameText.getText(), addressText.getText(), emailText.getText(), Integer.parseInt(ageText.getText()));
        if(clientBLL.validateClient(client) == 1)
            return 3;
        return 0;
    }

    public void setMode(int mode){
        this.mode = mode;
    }

    public void setData(Client client){
        labelID.setVisible(true);
        idText.setVisible(true);
        nameText.setText(client.getName());
        ageText.setText(String.valueOf(client.getAge()));
        addressText.setText(client.getAddress());
        emailText.setText(client.getEmail());
        idText.setText(String.valueOf(client.getId()));
    }

    public void bringBackScene(){
        labelID.setVisible(false);
        idText.setVisible(false);
    }
}
