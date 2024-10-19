
package com.example.module03_basicgui_db_interface;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author zuxin chen
 */
public class ConnDbOps {
    final String MYSQL_SERVER_URL = "jdbc:mysql://csc311chenserver.mysql.database.azure.com/";
    final String DB_URL = "jdbc:mysql://csc311chenserver.mysql.database.azure.com/PersonDB";
    final String USERNAME = "csc311chenserver";
    final String PASSWORD = "Chen1500Server";

    public  boolean connectToDatabase() {
        boolean hasRegistredUsers = false;


        //Class.forName("com.mysql.jdbc.Driver");
        try {
            //First, connect to MYSQL server and create the database if not created
            Connection conn = DriverManager.getConnection(MYSQL_SERVER_URL, USERNAME, PASSWORD);
            Statement statement = conn.createStatement();
            statement.executeUpdate("CREATE DATABASE IF NOT EXISTS PersonDB");
            statement.close();
            conn.close();

            //Second, connect to the database and create the table "users" if cot created
            conn = DriverManager.getConnection(DB_URL, USERNAME, PASSWORD);
            statement = conn.createStatement();
            String sql = "CREATE TABLE IF NOT EXISTS persons ("
                    + "id INT(10) NOT NULL PRIMARY KEY AUTO_INCREMENT,"
                    + "firstName VARCHAR(200) NOT NULL,"
                    + "lastName VARCHAR(200) NOT NULL,"
                    + "dept VARCHAR(200),"
                    + "major VARCHAR(200)"
                    + ");";
            statement.executeUpdate(sql);

            //check if we have person in the table users
            statement = conn.createStatement();
            ResultSet resultSet = statement.executeQuery("SELECT COUNT(*) FROM persons");

            if (resultSet.next()) {
                int numUsers = resultSet.getInt(1);
                if (numUsers > 0) {
                    hasRegistredUsers = true;
                }
            }

            statement.close();
            conn.close();

        } catch (Exception e) {
            e.printStackTrace();
        }

        return hasRegistredUsers;
    }

    /**
     * shows all person from database and save in a list
     * @return persons list
     */
    public ObservableList<Person> showsAllPersons() {

        ObservableList<Person> persons =  FXCollections.observableArrayList();
        Person person;
        try {
            Connection conn = DriverManager.getConnection(DB_URL, USERNAME, PASSWORD);
            String sql = "SELECT * FROM persons ";
            PreparedStatement preparedStatement = conn.prepareStatement(sql);
            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                int id = resultSet.getInt("id");
                String firstName = resultSet.getString("firstName");
                String lastName = resultSet.getString("lastName");
                String dept = resultSet.getString("dept");
                String major = resultSet.getString("major");

                person= new Person(id,firstName,lastName,dept,major);
                persons.add(person);
            }

            preparedStatement.close();
            conn.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return persons;
    }

    /**
     * add person to database
     * @param person person items hold a person's information
     */
    public  void addPerson(Person person) {

        try {
            Connection conn = DriverManager.getConnection(DB_URL, USERNAME, PASSWORD);
            String sql = "INSERT INTO persons (firstName, lastName, dept, major) VALUES (?, ?, ?, ?)";
            PreparedStatement preparedStatement = conn.prepareStatement(sql);
            preparedStatement.setString(1, person.getFirstName());
            preparedStatement.setString(2, person.getLastName());
            preparedStatement.setString(3, person.getDept());
            preparedStatement.setString(4, person.getMajor());

            int row = preparedStatement.executeUpdate();

            if (row > 0) {
                System.out.println("A new person was inserted successfully.");
            }

            preparedStatement.close();
            conn.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * delete a person from database
     * @param person a person should be deleted
     */
    public void deletePerson(Person person) {
            int ID = person.getId();
        try {
            Connection conn = DriverManager.getConnection(DB_URL, USERNAME, PASSWORD);
            String sql = "DELETE FROM persons WHERE id = ?";
            PreparedStatement preparedStatement = conn.prepareStatement(sql);
            preparedStatement.setInt(1, ID);

            int row = preparedStatement.executeUpdate();

            if (row == 0) {
                System.out.println("A person was deleted successfully.");
            }

            preparedStatement.close();
            conn.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * update the info in a person
     * @param person new person info
     */
    public  void editPerson(Person person) {

        try {
            Connection conn = DriverManager.getConnection(DB_URL, USERNAME, PASSWORD);
            String sql = "UPDATE persons Set firstName = ?, lastName = ?, dept = ?, major = ? WHERE id = ? ";
            PreparedStatement preparedStatement = conn.prepareStatement(sql);
            preparedStatement.setString(1, person.getFirstName());
            preparedStatement.setString(2, person.getLastName());
            preparedStatement.setString(3, person.getDept());
            preparedStatement.setString(4, person.getMajor());
            preparedStatement.setInt(5, person.getId());

            preparedStatement.close();
            conn.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


}