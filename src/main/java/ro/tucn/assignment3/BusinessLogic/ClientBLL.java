package ro.tucn.assignment3.BusinessLogic;

import javafx.scene.control.TableColumn;
import ro.tucn.assignment3.DataAccess.AbstractDAO;
import ro.tucn.assignment3.DataAccess.ClientDAO;
import ro.tucn.assignment3.BusinessLogic.Validators.ClientValidator;
import ro.tucn.assignment3.BusinessLogic.Validators.EmailValidator;
import ro.tucn.assignment3.Model.Client;
import ro.tucn.assignment3.BusinessLogic.Validators.Validator;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

/**
 * This class can be found inside the Business logic package.
 * As the name of the package suggests, its purpose is to take data
 * from the Controller, to validate the input regarding the clients
 * and if the input is in the correct format to pass it to the Data Access
 * package where it will be processed further. One can see that there are 2
 * private fields, one for the validators and the other is an object which
 * refers to the Data Access in order to make queries on the data base.
 */
public class ClientBLL {

    private List<Validator<Client>> validators;
    private AbstractDAO myClientDao;

    public ClientBLL() {
        myClientDao = new ClientDAO();
        validators = new ArrayList<>();
        validators.add((Validator<Client>) new EmailValidator());
        validators.add((Validator<Client>) new ClientValidator());
    }


    /**
     * This method is used to find the entire population which will be returned
     * to the controller in order to populate the TableView.
     * @return list of population found in the database
     */
    public List<Client> findAll(){
        List<Client> lst;
        lst = myClientDao.findAll();
        return lst;
    }

    /**
     * This method uses the Validators in order to validate the data from a
     * possible client.
     * @param client client from the Model packageC
     * @return an int which will be interpreted further: if the returned value
     * is 1, then the input was in the wrong format otherwise the input is correct.
     */
    public int validateClient(Client client){
        if(validators.get(0).validat(client) == 1)
            return 1;
        if(validators.get(1).validat(client) == 1)
            return 1;
        return 0;
    }

    /**
     * This method is used to take all the fields from the Data Access package
     * and transmit them further to the Controller where the TableView will be
     * completed.
     * @return list of fields
     */
    public List <TableColumn<Client, Integer>> setFields(){
        List <TableColumn<Client, Integer>> lst = myClientDao.setFields();
        return lst;
    }

    public void insertStudent(Client student) {
        myClientDao.insert(student);
    }

    public void updateClient(Client client){
        myClientDao.update(client);
    }

    public void deleteClient(Client client){
        myClientDao.delete(client);
    }

}
