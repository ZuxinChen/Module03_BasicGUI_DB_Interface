package com.example.module03_basicgui_db_interface;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;
import java.util.stream.Collectors;


public class DB_GUI_Controller implements Initializable {

    @FXML
    TextField first_name, last_name, department, major;
    @FXML
    private TableView<Person> tv;
    @FXML
    private TableColumn<Person, Integer> tv_id;
    @FXML
    private TableColumn<Person, String> tv_fn, tv_ln, tv_dept, tv_major;

    @FXML
    private VBox left_pane;
    @FXML
    private HBox bottom_pane;
    @FXML
    private VBox center_pane;

    @FXML
    ImageView img_view;

    ConnDbOps connDbOps = new ConnDbOps();

    private ObservableList<Person> data = connDbOps.showsAllPersons();
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        if(!connDbOps.connectToDatabase()){
            System.out.println("Fail Connect to Database");
        }

        tv_id.setCellValueFactory(new PropertyValueFactory<>("id"));
        tv_fn.setCellValueFactory(new PropertyValueFactory<>("firstName"));
        tv_ln.setCellValueFactory(new PropertyValueFactory<>("lastName"));
        tv_dept.setCellValueFactory(new PropertyValueFactory<>("dept"));
        tv_major.setCellValueFactory(new PropertyValueFactory<>("major"));

        tv.setItems(data);

        // keyboard shortcut
        tv.setOnKeyPressed(event -> {
            if (event.isShiftDown()){
                switch (event.getCode()){
                    case D -> deleteRecord();
                    case C -> clearForm();
                    case F -> showImage();
                    case S -> themeSwitch();
                }
                event.consume();
            }
        });


    }


    @FXML
    protected void addNewRecord() {

        Person person = new Person(
                data.size()+1,
                first_name.getText(),
                last_name.getText(),
                department.getText(),
                major.getText()
        );
        data.add(person);
        connDbOps.addPerson(person); // add date to database

    }

    @FXML
    protected void clearForm() {
        first_name.clear();
        last_name.setText("");
        department.setText("");
        major.setText("");
    }

    @FXML
    protected void closeApplication() {
        System.exit(0);
    }


    @FXML
    protected void editRecord() {
        Person p= tv.getSelectionModel().getSelectedItem();
        int c=data.indexOf(p);
        Person p2= new Person();
        p2.setId(c+1);
        p2.setFirstName(first_name.getText());
        p2.setLastName(last_name.getText());
        p2.setDept(department.getText());
        p2.setMajor(major.getText());
        data.remove(c);
        data.add(c,p2);
        tv.getSelectionModel().select(c);
        connDbOps.editPerson(p2); // update the data in database
    }

    @FXML
    protected void deleteRecord() {
        Person p= tv.getSelectionModel().getSelectedItem();
        data.remove(p);
        connDbOps.deletePerson(p); // delete the date from database
    }



    @FXML
    protected void showImage() {
        File file= (new FileChooser()).showOpenDialog(img_view.getScene().getWindow());
        if(file!=null){
            img_view.setImage(new Image(file.toURI().toString()));

        }
    }





    @FXML
    protected void selectedItemTV() {

        Person p= tv.getSelectionModel().getSelectedItem();
        if(p != null) {
            first_name.setText(p.getFirstName());
            last_name.setText(p.getLastName());
            department.setText(p.getDept());
            major.setText(p.getMajor());
        }

    }

    /**
     * shows message about this program
     */
    @FXML
    protected void AboutMessage() {
        Stage stage = new Stage();
        Group root = new Group();
        Scene scene = new Scene(root, 450, 400);

        TextFlow textFlow = new TextFlow();
        textFlow.setLayoutX(10);
        textFlow.setLayoutY(10);

        // Title setting
        Text title = new Text("Azure DB Configuration and UI Enhancement");
        title.getStyleClass().add("about-title");
        textFlow.getChildren().add(title);


        String aboutMessage = """
        \n
        For CSC311 Week 07 Homework
        Create by ZuXin Chen.
        ---------------------------------------------------------------------
        1. add new person\s
            + chick button Add
        2. delete a person\s
            + select item first and chick button Delete,
                or using shift + D
        3. clear all Text Fields\s
            + chick button Clear, or using shirt + C
        4. Edit a person\s
            + select item first and change info in text fields
        5. Change your profile picture\s
            + check the profile picture and select a image from your computer
            or using shrift + F\s
        5. Change themes color between red and green\s
            + select Themes Switch which under Settings
            or using shrift + S
        
        Have Fun!!!!
        """;

        //text setting
        Text text = new Text(aboutMessage);
        text.getStyleClass().add("about-text");
        textFlow.getChildren().add(text);

        //stage setting
        root.getChildren().add(textFlow);
        stage.setTitle("About");
        stage.setScene(scene);
        // Keep window on top and wait be close
        stage.setAlwaysOnTop(true);
        stage.setResizable(false);
        stage.showAndWait();

    }

    boolean themes = true;

    /**
     * switch theme color between red and green
     */
    @FXML
    void themeSwitch() {
        if(themes) {
            left_pane.setStyle("-fx-background-color: #5cfc00;");
            bottom_pane.setStyle("-fx-background-color: #5cfc00;");
            center_pane.setStyle("-fx-background-color: #a3f592;");
            themes = false;
        }else {
            left_pane.setStyle("-fx-background-color: #f88;");
            bottom_pane.setStyle("-fx-background-color: #f88;");
            center_pane.setStyle("-fx-background-color: #eccbcb;");
            themes = true;
        }
    }

    /**
     * print all date on window
     */
    @FXML
    void PrintAll() {

        Stage stage = new Stage();
        Group root = new Group();
        Scene scene = new Scene(root, 450, 400);

        TextFlow textFlow = new TextFlow();
        textFlow.setLayoutX(10);
        textFlow.setLayoutY(10);

        // Title setting
        Text title = new Text("Database Data\n");
        title.getStyleClass().add("about-title");
        textFlow.getChildren().add(title);

        List<Person> persons = connDbOps.showsAllPersons();
        String message = persons.stream()
                                .map(Person::toString)
                                .collect(Collectors.joining("\n"));


        //text setting
        Text text = new Text(message);
        text.getStyleClass().add("about-text");
        textFlow.getChildren().add(text);

        //stage setting
        root.getChildren().add(textFlow);
        stage.setTitle("Pint all from DATABASE");
        stage.setScene(scene);
        // Keep window on top and wait be close
        stage.setAlwaysOnTop(true);
        stage.setResizable(false);
        stage.showAndWait();

    }
}