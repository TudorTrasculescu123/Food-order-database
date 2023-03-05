package ro.tucn.assignment3.BusinessLogic;

import javafx.scene.control.TableColumn;
import ro.tucn.assignment3.DataAccess.AbstractDAO;
import ro.tucn.assignment3.DataAccess.ProductDAO;
import ro.tucn.assignment3.Model.Product;

import java.util.List;

/**
 * This class can be found in the Business Logic package. It's main use is
 * to communicate between the Controller and the Data Access classes in order
 * to make queries on the database and update the GUI accordingly. It contains
 * only one private field which is of type ProductDAO.
 */
public class ProductBLL {
    private AbstractDAO myProductDao;

    public ProductBLL() {
        myProductDao = new ProductDAO();
    }

    /**
     * @return list of products which will populate the TableView from the Controller
     */
    public List<Product> findAll(){
        List<Product> lst;
        lst = myProductDao.findAll();
        return lst;
    }

    public List <TableColumn<Product, Integer>> setFields(){
        List <TableColumn<Product, Integer>> lst = myProductDao.setFields();
        return lst;
    }

    public void insertProduct(Product product){
        myProductDao.insert(product);
    }

    public void updateProduct(Product product){
        myProductDao.update(product);
    }

    public void deleteProduct(Product product){
        myProductDao.delete(product);
    }
}
