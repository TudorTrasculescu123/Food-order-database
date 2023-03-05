package ro.tucn.assignment3.BusinessLogic.Validators;

import javafx.scene.control.Alert;
import ro.tucn.assignment3.Model.Client;

public class ClientValidator implements Validator<Client> {
    private static final int MIN_AGE = 5;
    private static final int MAX_AGE = 120;

    public int validat(Client t) {

        if (t.getAge() < MIN_AGE || t.getAge() > MAX_AGE) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error!");
            alert.setHeaderText("Age is out of range!");
            alert.showAndWait();
            return 1;
        }
        return 0;
    }
}
