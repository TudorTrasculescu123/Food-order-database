package ro.tucn.assignment3.DataAccess;

import javafx.scene.control.Alert;
import javafx.scene.control.TableColumn;
import javafx.scene.control.cell.PropertyValueFactory;
import ro.tucn.assignment3.Connection.ConnectionFactory;
import java.beans.IntrospectionException;
import java.beans.PropertyDescriptor;
import java.lang.reflect.*;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * @Author: Technical University of Cluj-Napoca, Romania Distributed Systems
 *          Research Laboratory, http://dsrl.coned.utcluj.ro/
 * @Since: Apr 03, 2017
 * @Source http://www.java-blog.com/mapping-javaobjects-database-reflection-generics
 */
public class AbstractDAO<T> {
    protected static final Logger LOGGER = Logger.getLogger(AbstractDAO.class.getName());

    private final Class<T> type;

    @SuppressWarnings("unchecked")
    public AbstractDAO() {
        this.type = (Class<T>) ((ParameterizedType) getClass().getGenericSuperclass()).getActualTypeArguments()[0];
    }

    /**
     * @param field name of the field
     * @return String which represents the query
     */
    private String createSelectQuery(String field) {
        StringBuilder sb = new StringBuilder();
        sb.append("SELECT ");
        sb.append(" * ");
        sb.append(" FROM ");
        sb.append(type.getSimpleName());
        sb.append(" WHERE " + field + " =?");
        return sb.toString();
    }

    /**
     * The method will generate a String format for the Insert query
     * @return String representing Insert query
     */
    private String createInsertQuery(){
        StringBuilder sb = new StringBuilder();
        sb.append("INSERT INTO " + type.getSimpleName().toLowerCase() + " (");
        int foundId = 0;
        for(Field field: type.getDeclaredFields()){
            if(!field.getName().toLowerCase().equals("id")){
                sb.append('"' + field.getName() + '"' + ", ");
            }else{
                foundId = 1;
            }
        }
        sb.delete(sb.length() - 2, sb.length() - 1);
        sb.append(")");
        sb.append(" values (");
        for(int i = 0; i < type.getDeclaredFields().length - foundId; i++) {
            sb.append("?, ");
        }
        sb.delete(sb.length() - 2, sb.length() - 1);
        sb.append(")");
        return sb.toString();
    }

    /**
     * The method will generate a String format for the Update query
     * @return String representing the Update query
     */
    private String createUpdateQuery(){
        StringBuilder sb = new StringBuilder();
        sb.append("UPDATE " + type.getSimpleName().toLowerCase() + " SET ");
        for(Field field: type.getDeclaredFields()){
            if(!field.getName().toLowerCase().equals("id")){
                sb.append('"' + field.getName() +  '"' + " = ?, ");
            }
        }
        sb.delete(sb.length() - 2, sb.length() -1);
        sb.append(" WHERE id = ?");
        return sb.toString();
    }

    /**
     * The method will generate a String format for the Delete query.
     * @return String representing the Delete query
     */
    private String createDeleteQuery(){
        StringBuilder sb = new StringBuilder();
        sb.append("DELETE FROM " + type.getSimpleName().toLowerCase() + " ");
        sb.append("WHERE id = ?");
        return sb.toString();
    }

    /**
     * This method is used to return a list of values which is the result of
     * the select all from the table T which could be (Client or Order). This
     * list is generated using reflection.
     * @return A list of values according to the fields of each table which.
     */
    public List<T> findAll() {
        Connection dbConnection = ConnectionFactory.getConnection();
        PreparedStatement findStatement = null;
        ResultSet rs = null;
        String statement = "Select * From " + type.getSimpleName().toLowerCase();
        try {
            findStatement = dbConnection.prepareStatement(statement);
            rs = findStatement.executeQuery();
            return createObjects(rs);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * @param id the id to be found in the table
     * @return The touple which is found at a certain ID, if the ID couldn't
     * be found then the method will return a null value.
     */
    public T findById(int id) {
        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        String query = createSelectQuery("id");
        try {
            connection = ConnectionFactory.getConnection();
            statement = connection.prepareStatement(query);
            statement.setInt(1, id);
            resultSet = statement.executeQuery();
            List<T> list = createObjects(resultSet);
            if(list.size() >= 1)
                return list.get(0);
            return null;
        } catch (SQLException e) {
            LOGGER.log(Level.WARNING, type.getName() + "DAO:findById " + e.getMessage());
        } finally {
            ConnectionFactory.close(resultSet);
            ConnectionFactory.close(statement);
            ConnectionFactory.close(connection);
        }
        return null;
    }

    public List<T> createObjects(ResultSet resultSet) {
        List<T> list = new ArrayList<T>();
        Constructor[] ctors = type.getDeclaredConstructors();
        Constructor ctor = null;
        for (int i = 0; i < ctors.length; i++) {
            ctor = ctors[i];
            if (ctor.getGenericParameterTypes().length == 0)
                break;
        }
        try {
            while (resultSet.next()) {
                ctor.setAccessible(true);
                T instance = (T)ctor.newInstance();
                for (Field field : type.getDeclaredFields()) {
                    String fieldName = field.getName();
                    Object value = resultSet.getObject(fieldName);
                    PropertyDescriptor propertyDescriptor = new PropertyDescriptor(fieldName, type);
                    Method method = propertyDescriptor.getWriteMethod();
                    method.invoke(instance, value);
                }
                list.add(instance);
            }
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (SecurityException e) {
            e.printStackTrace();
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (IntrospectionException e) {
            e.printStackTrace();
        }
        return list;
    }

    /**
     * Inside this method, one can see that an iteration through all the fields is made
     * and each column from the Table view will be addressed a certain field.
     * @return Special list containing the field names from the database which will be further
     * used to populate the Table View from the GUI.
     */
    public List <TableColumn<T, Integer>> setFields(){
        Constructor[] ctors = type.getDeclaredConstructors();
        Constructor ctor = null;
        for (int i = 0; i < ctors.length; i++) {
            ctor = ctors[i];
            if (ctor.getGenericParameterTypes().length == 0)
                break;
        }
        List <TableColumn<T, Integer>> lst = new ArrayList<>();
        try {
            T instance = (T)ctor.newInstance();
            for (Field field : type.getDeclaredFields()) {
                String fieldName = field.getName();
                TableColumn<T, Integer> column =  new TableColumn<>(fieldName);
                column.setCellValueFactory(new PropertyValueFactory<>(fieldName));
                lst.add(column);
            }
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
        return lst;
    }

    /**
     * This method takes as parameter an object of type T. Inside the method an
     * iteration through all the fields of the specific Class is made and specific
     * getters from each class are called through reflection. The main goal of this
     * method is to replace the question marks from the query which inserts in the
     * database. The insertion is done through reflection by calling the getters of
     * different fields according to each class. After the question marks are replaced
     * the query will be executed and if there are no errors the new CLient, Order or
     * Product will be inserted.
     * @param t this is an object which could be of type Orders Product or Client
     */
    public void insert(T t) {
        // TODO:
        /*INSERT INTO table_name
        VALUES (value1, value2, value3, ...);*/
        Constructor[] ctors = type.getDeclaredConstructors();
        Constructor ctor = null;
        for (int i = 0; i < ctors.length; i++) {
            ctor = ctors[i];
            if (ctor.getGenericParameterTypes().length == 0)
                break;
        }
        Connection dbConnection = ConnectionFactory.getConnection();
        PreparedStatement findStatement = null;
        String query = createInsertQuery();
        int index = 1;
        try {
            findStatement = dbConnection.prepareStatement(query);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        for(Field field: type.getDeclaredFields()){
            if(!field.getName().toLowerCase().equals("id")){
                PropertyDescriptor propertyDescriptor = null;
                try {
                    propertyDescriptor = new PropertyDescriptor(field.getName(), type);
                    Method method = propertyDescriptor.getReadMethod();
                    if (field.getType() == int.class) {
                        findStatement.setInt(index, (int) method.invoke(t));
                    }
                    else if (field.getType() == String.class) {
                        findStatement.setString(index, (String) method.invoke(t));
                    }
                    index ++;
                } catch (IntrospectionException e) {
                    e.printStackTrace();
                } catch (SQLException throwables) {
                    throwables.printStackTrace();
                } catch (InvocationTargetException e) {
                    e.printStackTrace();
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
            }
        }
        try {
            //System.out.println(findStatement.toString());
            findStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error!");
            alert.setHeaderText("Duplicate fields are not allowed!");
            alert.showAndWait();
        }
        ConnectionFactory.close(findStatement);
        ConnectionFactory.close(dbConnection);
    }


    /**
     * Similarly to the insertion method, the getters of each field will be
     * invoked and each question mark inside the initial query will be replaced
     * using reflection.
     * @param t this is an object which could be of type Product or Client
     */
    public void update(T t) {
        // TODO:
        Connection dbConnection = ConnectionFactory.getConnection();
        PreparedStatement findStatement = null;
        int index = 1;
        String query = createUpdateQuery();
        try {
            findStatement = dbConnection.prepareStatement(query);
            for(Field field: type.getDeclaredFields()){
                if(!field.getName().toLowerCase().equals("id")){
                    PropertyDescriptor propertyDescriptor = new PropertyDescriptor(field.getName(), type);
                    Method method = propertyDescriptor.getReadMethod();
                    if(field.getType() == int.class){
                        findStatement.setInt(index, (int) method.invoke(t));
                    }else if(field.getType() == String.class){
                        findStatement.setString(index, (String) method.invoke(t));
                    }
                    index ++;
                }
            }
        } catch (SQLException | IntrospectionException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }finally{
            try {
                PropertyDescriptor propertyDescriptor = new PropertyDescriptor(type.getDeclaredFields()[0].getName(), type);
                Method method = propertyDescriptor.getReadMethod();
                findStatement.setInt(index, (int) method.invoke(t));
                findStatement.executeUpdate();
            } catch (SQLException e) {
                e.printStackTrace();
            } catch (IntrospectionException e) {
                e.printStackTrace();
            } catch (InvocationTargetException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
        //System.out.println(findStatement.toString());
    }

    /**
     * Similar to update and insert, a product or a client will be deleted
     * from the database if there are no errors that occur which could cause
     * am exception which will be caught by the program.
     * @param t this is an object which could be of type Product or Client
     */
    public void delete(T t){
        Connection dbConnection = ConnectionFactory.getConnection();
        PreparedStatement findStatement = null;
        int index = 1;
        String query = createDeleteQuery();
        try {
            findStatement = dbConnection.prepareStatement(query);
            PropertyDescriptor propertyDescriptor = new PropertyDescriptor(type.getDeclaredFields()[0].getName(), type);
            Method method = propertyDescriptor.getReadMethod();
            findStatement.setInt(index, (int) method.invoke(t));
            findStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (IntrospectionException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        //System.out.println(findStatement.toString());
    }
}
