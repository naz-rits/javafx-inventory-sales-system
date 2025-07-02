package com.javafxsalesmanagementsystem.ui;


import com.javafxsalesmanagementsystem.JavaFXApplication;
import com.javafxsalesmanagementsystem.entity.Customer;
import com.javafxsalesmanagementsystem.entity.Session;
import com.javafxsalesmanagementsystem.service.CustomerService;
import javafx.geometry.Orientation;
import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.Window;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;


@Service
public class LoginAndRegisterUI {

    public ConfigurableApplicationContext applicationContext;


    public Stage loginStage() {
        String image = Objects.requireNonNull(getClass().getResource("/images/coffee_background.jpg")).toExternalForm();
        CustomerService customerService = applicationContext.getBean(CustomerService.class);

        MainUI mainUI = applicationContext.getBean(MainUI.class);

        Stage stage = new Stage();
        stage.setTitle("Login");


        TextField username = new TextField();
        PasswordField password = new PasswordField();

        Hyperlink register = new Hyperlink(" Sign in");
        register.setTranslateY(-2.5);
        register.setTranslateX(-4);
        register.setFont(Font.font("Segoe UI", FontWeight.NORMAL, 8));
        register.setOnAction(e -> {
            registerStage().show();
        });

        Button login  = new Button("Login");
        login.setCursor(Cursor.HAND);
        login.setStyle("-fx-background-color: green;" +
                "-fx-scale-z: 1.5;" +
                "-fx-text-fill: white;" +
                "-fx-border-width: 50;" +
                "-fx-border-radius: 50;"
        );
        login.setTranslateX(49);
        login.setOnAction(e -> {
            Optional<Customer> customer = customerService.findCustomerByUsernameAndPassword(username.getText(), password.getText());
            if (customer.isPresent()) {
                Stage loginStage = (Stage) ((Node) e.getSource()).getScene().getWindow();
                loginStage.close();

                List<Window> windows = new ArrayList<>(Window.getWindows());
                for (Window window : windows) {
                    if (window instanceof Stage stage1 && stage1.isShowing()) {
                        stage1.close();
                    }
                }


                Session.currentUser = customer;
                mainUI.initialStage(customer).show();
                username.clear();
                password.clear();
            }
        });

        Label dontHaveAccount = new Label("Don't have an account?");
        dontHaveAccount.setFont(Font.font("Segoe UI", FontWeight.NORMAL, 8));

        HBox labelLink = new HBox();
        labelLink.getChildren().addAll(dontHaveAccount, register);

        ToolBar toolBar = new ToolBar();
        toolBar.getItems().addAll(new Label("Username"), username, new Separator(),
                                  new Label("Password"), password, new Separator(),
                                  labelLink, new Separator(), login);
        toolBar.setOrientation(Orientation.VERTICAL);
        toolBar.setStyle(
                "-fx-border-color: black;" +
                        "-fx-border-width: 1;" +
                        "-fx-border-radius: 10;" +
                        "-fx-background-radius: 10;" +
                        "-fx-padding-y: 40;" +
                        "-fx-background-color: #D9D9D9;"

        );
        toolBar.setScaleX(1.5);
        toolBar.setScaleY(1.5);

        VBox root = new VBox();
        root.setAlignment(Pos.CENTER);
        root.setSpacing(10);
        root.getChildren().addAll(toolBar);

        root.setStyle("-fx-background-image: url('" + image + "');"
                + "-fx-background-size: cover;");

        Scene scene = new Scene(root, 400, 600);
        stage.setScene(scene);
        stage.setResizable(false);
        stage.initModality(Modality.APPLICATION_MODAL);

        return stage;
    }

    public Stage registerStage(){
        CustomerService customerService = applicationContext.getBean(CustomerService.class);

        Customer customer = new Customer();

        Stage stage = new Stage();
        String image = getClass().getResource("/images/coffee_background.jpg").toExternalForm();

        TextField firstName = new TextField();
        TextField lastName = new TextField();
        TextField contactNumber = new TextField();
        TextField username = new TextField();
        PasswordField password = new PasswordField();

        Button register = new Button("Register");
        register.setStyle("-fx-background-color: green;" +
                "-fx-scale-z: 1.5;" +
                "-fx-text-fill: white;" +
                "-fx-border-width: 50;" +
                "-fx-border-radius: 50;");
        register.setTranslateX(49);
        register.setCursor(Cursor.HAND);
        register.setOnAction(e -> {
            customer.setFirstName(firstName.getText());
            customer.setLastName(lastName.getText());
            customer.setContactNumber(contactNumber.getText());
            customer.setUsername(username.getText());
            customer.setPassword(password.getText());

            customerService.addCustomer(customer);
            stage.close();
        });
        ToolBar toolBar = new ToolBar();
        toolBar.setScaleY(1.5);
        toolBar.setScaleX(1.5);
        toolBar.getItems().addAll(new Label("First Name"), firstName, new Separator(),
                                  new Label("Last Name"), lastName, new Separator(),
                                  new Label("Contact Number"), contactNumber, new Separator(),
                                  new Label("Username"), username, new Separator(),
                                  new Label("Password"), password, new Separator(),
                                  register);
        toolBar.setOrientation(Orientation.VERTICAL);
        toolBar.setStyle(
                "-fx-border-color: black;" +
                        "-fx-border-width: 1;" +
                        "-fx-border-radius: 10;" +
                        "-fx-background-radius: 10;" +
                        "-fx-padding-y: 40;" +
                        "-fx-background-color: #D9D9D9;"

        );

        VBox root = new VBox(toolBar);


        root.setStyle("-fx-background-image: url('" + image + "');"
                + "-fx-background-size: cover;");
        root.setAlignment(Pos.CENTER);
        root.setSpacing(10);

        stage.setTitle("Register");
        stage.setScene(new Scene(root, 400, 600));
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.setResizable(false);
        return stage;
    }
}
