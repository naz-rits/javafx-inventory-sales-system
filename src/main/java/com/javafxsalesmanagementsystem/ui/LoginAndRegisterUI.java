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
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.TextAlignment;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.Window;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.stereotype.Service;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;


@Service
public class LoginAndRegisterUI {

    private final CustomerService customerService;
    public ConfigurableApplicationContext applicationContext;

    public LoginAndRegisterUI(CustomerService customerService) {
        this.customerService = customerService;
    }


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
        login.setPrefWidth(150);
        Hyperlink hyperLinkForgotPassword = new Hyperlink("Forgot Password");
        hyperLinkForgotPassword.setTextAlignment(TextAlignment.CENTER);
        hyperLinkForgotPassword.setCursor(Cursor.HAND);
        hyperLinkForgotPassword.setFont(Font.font("Segoe UI", FontWeight.NORMAL, 10));
        hyperLinkForgotPassword.setTranslateX(36);
        hyperLinkForgotPassword.setOnAction(e -> {
            stage.close();
            try {
                forgotPasswordStage().show();
            } catch (FileNotFoundException ex) {
                throw new RuntimeException(ex);
            }
        });
        VBox root = new VBox();

        Label errorLogin = new Label();
        errorLogin.setStyle("-fx-text-fill: red;");
        errorLogin.setFont(Font.font("Segoe UI", FontWeight.NORMAL, 20));
        errorLogin.setTextAlignment(TextAlignment.CENTER);
        errorLogin.setTranslateY(60);
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
                mainUI.initialStage(customer, false).show();
                username.clear();
                password.clear();
            }
            if (username.getText().isBlank() || password.getText().isBlank()) {
                errorLogin.setText("Please fill all the fields");
                if (!root.getChildren().contains(errorLogin)) {
                    root.getChildren().add(errorLogin);
                }
                return;
            }

            if (customer.isEmpty()) {
                errorLogin.setText("Invalid username or password");
                if (!root.getChildren().contains(errorLogin)) {
                    root.getChildren().add(errorLogin);
                }
                return;
            }

            if (username.getText().equals(MainUI.ADMIN_USERNAME) && password.getText().equals(MainUI.ADMIN_PASSWORD)) {
                Stage loginStage = (Stage) ((Node) e.getSource()).getScene().getWindow();
                loginStage.close();

                List<Window> windows = new ArrayList<>(Window.getWindows());
                for (Window window : windows) {
                    if (window instanceof Stage stage1 && stage1.isShowing()) {
                        stage1.close();
                    }
                }
                Session.currentUser = Optional.empty();
                mainUI.initialStage(Optional.empty(), true).show();
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
                                  labelLink, new Separator(), hyperLinkForgotPassword, new Separator(),
                                login);
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

    private Stage forgotPasswordStage() throws FileNotFoundException {
        Stage stage = new Stage();

        String image = Objects.requireNonNull(getClass().getResource("/images/coffee_background.jpg")).toExternalForm();
        TextField username = new TextField();
        PasswordField password = new PasswordField();
        PasswordField newPassword = new PasswordField();
        PasswordField confirmPassword = new PasswordField();
        Button saveChanges = new Button("Save Changes");
        saveChanges.setStyle("-fx-background-color: green;" +
                "-fx-scale-z: 1.5;" +
                "-fx-text-fill: white;" +
                "-fx-border-width: 50;" +
                "-fx-border-radius: 50;"
        );
        saveChanges.setPrefWidth(150);
        saveChanges.setCursor(Cursor.HAND);

        ToolBar toolBar = new ToolBar();
        toolBar.getItems().addAll(new Label("Username"), username, new Separator(),
                                  new Label("Password"), password, new Separator(),
                                  new Label("New Password"), newPassword, new Separator(),
                                  new Label("Confirm Password"), confirmPassword, new Separator(),
                                  saveChanges);
        toolBar.setOrientation(Orientation.VERTICAL);
        toolBar.setStyle(
                "-fx-border-color: black;" +
                        "-fx-border-width: 1;" +
                        "-fx-border-radius: 10;" +
                        "-fx-background-radius: 10;" +
                        "-fx-padding-y: 40;" +
                        "-fx-background-color: #D9D9D9;"

        );
        toolBar.setScaleX(1.3);
        toolBar.setScaleY(1.3);

        VBox root = new VBox(toolBar);
        root.setAlignment(Pos.CENTER);
        root.setStyle("-fx-background-image: url('" + image + "');"
                + "-fx-background-size: cover;");

        Label customerNotFound = new Label();
        customerNotFound.setStyle("-fx-text-fill: red;");
        customerNotFound.setFont(Font.font("Segoe UI", FontWeight.BOLD, 16));
        customerNotFound.setTranslateY(50);
        saveChanges.setOnAction(e -> {
            Optional<Customer> freshCustomer = customerService.findCustomerByUsernameAndPassword(username.getText(), password.getText());
            if (freshCustomer.isPresent() && (newPassword.getText().equals(confirmPassword.getText()))) {
                Customer customer = freshCustomer.get();
                customer.setPassword(newPassword.getText());
                customerService.addCustomer(customer);
                stage.close();
            }
            if (username.getText().isBlank() || password.getText().isBlank() ||
                newPassword.getText().isBlank() || confirmPassword.getText().isBlank()) {
                customerNotFound.setText("Please fill all the fields");
                if (!root.getChildren().contains(customerNotFound)) {
                    root.getChildren().add(customerNotFound);
                }
                return;
            }
            if (freshCustomer.isEmpty()) {
                customerNotFound.setText("Customer Not Found");
                if (!root.getChildren().contains(customerNotFound)) {
                    root.getChildren().add(customerNotFound);
                }
            }
        });
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.setScene(new Scene(root, 400, 600));
        return stage;
    }

    public Stage registerStage(){
        CustomerService customerService = applicationContext.getBean(CustomerService.class);

        Customer customer = new Customer();

        Stage stage = new Stage();
        String image = Objects.requireNonNull(getClass().getResource("/images/coffee_background.jpg")).toExternalForm();

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
        register.setPrefWidth(150);
        register.setCursor(Cursor.HAND);
        VBox root = new VBox();

        Label errorLabel = new Label("Username has already been taken");
        errorLabel.setStyle("-fx-text-fill: red;");
        errorLabel.setFont(Font.font("Segoe UI", FontWeight.NORMAL, 20));
        errorLabel.setTextAlignment(TextAlignment.CENTER);
        errorLabel.setTranslateY(80);
        register.setOnAction(e -> {
            Optional<Customer> customers = customerService.findCustomerByUsername(username.getText());
            if (firstName.getText().isBlank() || lastName.getText().isBlank()
                    || contactNumber.getText().isBlank() ||username.getText().isBlank()
                    || password.getText().isBlank()) {
                errorLabel.setText("Please fill all the fields");
                if (!root.getChildren().contains(errorLabel)) {
                    root.getChildren().add(errorLabel);
                }
                return;
            }


            if (customers.isPresent()) {
                errorLabel.setText("Username has already been taken");
                if (!root.getChildren().contains(errorLabel)) {
                    root.getChildren().add(errorLabel);
                }
                return;
            }

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
        Label createAnAccount = new Label("Create an Account");
        createAnAccount.setFont(Font.font("Segoe UI", FontWeight.BOLD, 17));
        toolBar.getItems().addAll(createAnAccount, new Separator(), new Label("First Name"), firstName, new Separator(),
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

        root.getChildren().addAll(toolBar);

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
