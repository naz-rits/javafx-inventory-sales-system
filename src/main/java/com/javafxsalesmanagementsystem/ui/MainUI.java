package com.javafxsalesmanagementsystem.ui;

import com.javafxsalesmanagementsystem.entity.Customer;
import com.javafxsalesmanagementsystem.entity.Session;
import com.sun.javafx.font.freetype.HBGlyphLayout;
import com.sun.tools.javac.Main;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;
import javafx.stage.Window;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.stereotype.Service;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Slf4j
@Service
public class MainUI {

    public ConfigurableApplicationContext applicationContext;

    private final String ADMIN_USERNAME = "admin";
    private final String ADMIN_PASSWORD = "admin1234";

    public Stage initialStage(Optional<Customer> customer, boolean isAdmin) {
        Stage primaryStage = new Stage();

        ProductUI productUI = applicationContext.getBean(ProductUI.class);
        productUI.context = applicationContext;
        LoginAndRegisterUI loginAndRegisterUI = applicationContext.getBean(LoginAndRegisterUI.class);
        loginAndRegisterUI.applicationContext = applicationContext;


        Label label = new Label();
        label.setStyle("-fx-text-fill: white;");
        label.setFont(Font.font("Verdana", FontWeight.BOLD, 20));

        boolean hasLogin = customer.isPresent();



        ImageView imageView = new ImageView( new Image("images/add_to_cart.png"));
        imageView.setFitHeight(40);
        imageView.setFitWidth(40);
        Button cart = new Button("", imageView);

        cart.setTranslateY(20);
        cart.setStyle("-fx-background-color: #d9d9d9;" +
                "-fx-scale-z: 1.5;" +
                "-fx-text-fill: white;" +
                "-fx-border-width: 50;" +
                "-fx-border-radius: 50;" +
                "-fx-background-radius: 999;" +
                        "-fx-min-width: 40;" +
                        "-fx-min-height: 40;"
        );
        cart.setPrefWidth(150);
        cart.setFont(Font.font("Segoe UI", FontWeight.BOLD, 20));
        cart.setCursor(Cursor.HAND);
        cart.setTranslateX(405);
        cart.setOnAction(event -> {
            AddToCartUI addToCartUI = applicationContext.getBean(AddToCartUI.class);
            addToCartUI.context = applicationContext;
            try {
                addToCartUI.cartListVBox(customer);
            } catch (FileNotFoundException e) {
                throw new RuntimeException(e);
            }
        });

        Button cartDisabled = new Button("", imageView);

        cartDisabled.setTranslateY(20);
        cartDisabled.setStyle("-fx-background-color: #d9d9d9;" +
                "-fx-scale-z: 1.5;" +
                "-fx-text-fill: white;" +
                "-fx-border-width: 50;" +
                "-fx-border-radius: 50;" +
                "-fx-background-radius: 999;" +
                "-fx-min-width: 40;" +
                "-fx-min-height: 40;"
        );
        cartDisabled.setPrefWidth(150);
        cartDisabled.setFont(Font.font("Segoe UI", FontWeight.BOLD, 20));
        cartDisabled.setCursor(Cursor.HAND);
        cartDisabled.setTranslateX(405);

        Button logoutButton = new Button("Logout");

        logoutButton.setStyle("-fx-background-color: red;" +
                "-fx-scale-z: 1.5;" +
                "-fx-text-fill: white;" +
                "-fx-border-width: 50;" +
                "-fx-border-radius: 50;"
        );
        logoutButton.setFont(Font.font("Segoe UI", FontWeight.BOLD, 20));
        logoutButton.setCursor(Cursor.HAND);
        logoutButton.setBorder(Border.stroke(Color.BLACK));
        logoutButton.setPrefWidth(150);
        logoutButton.setTranslateX(390);
        logoutButton.setOnAction(e -> {
            List<Window> windows = new ArrayList<>(Window.getWindows());
            for (Window window : windows) {
                if (window instanceof Stage stage1 && stage1.isShowing()) {
                    stage1.close();
                }
            }
            MainUI mainUI = applicationContext.getBean(MainUI.class);

            Session.currentUser = Optional.empty();
            mainUI.initialStage(Optional.empty(), false).show();

        });

        Button login = new Button("Login");

        Label customerName = new Label();
        Label adminName = new Label("admin");

        if (hasLogin) {
            customerName = new Label(customer.get().getUsername());
            customerName.setFont(Font.font("Segoe UI", FontWeight.BOLD, 30));
            customerName.setTextFill(Color.BLACK);
            customerName.setTranslateY(30);
            customerName.setTranslateX(25);
            customerName.setBackground(Background.fill(Color.DARKGRAY));
            customerName.setPadding( new Insets(5));
            customerName.setBorder(Border.stroke(Color.DARKGRAY));
            customerName.setStyle("-fx-background-color: #d9d9d9;" +
                    "-fx-scale-z: 1.5;" +
                    "-fx-text-fill: white;" +
                    "-fx-border-width: 50;" +
                    "-fx-border-radius: 50;" +
                    "-fx-background-radius: 999;" +
                    "-fx-min-width: 50;" +
                    "-fx-min-height: 40;");
        }
        if (isAdmin) {
            adminName.setFont(Font.font("Segoe UI", FontWeight.BOLD, 30));
            adminName.setTextFill(Color.BLACK);
            adminName.setTranslateY(30);
            adminName.setTranslateX(25);
            adminName.setBackground(Background.fill(Color.DARKGRAY));
            adminName.setPadding( new Insets(5));
            adminName.setBorder(Border.stroke(Color.DARKGRAY));
            adminName.setStyle("-fx-background-color: #d9d9d9;" +
                    "-fx-scale-z: 1.5;" +
                    "-fx-text-fill: white;" +
                    "-fx-border-width: 50;" +
                    "-fx-border-radius: 50;" +
                    "-fx-background-radius: 999;" +
                    "-fx-min-width: 50;" +
                    "-fx-min-height: 40;");
            logoutButton.setTranslateX(500);
        }

        login.setStyle("-fx-background-color: green;" +
                "-fx-scale-z: 1.5;" +
                "-fx-text-fill: white;" +
                "-fx-border-width: 50;" +
                "-fx-border-radius: 50;"
        );
        login.setFont(Font.font("Segoe UI", FontWeight.BOLD, 20));
        login.setCursor(Cursor.HAND);
        login.setTranslateY(20);
        login.setBorder(Border.stroke(Color.BLACK));
        login.setPrefWidth(150);


        VBox productContainer = productUI.pagination(hasLogin, customer, isAdmin);

        Runnable onRefresh = () -> {
            VBox newList = productUI.pagination(hasLogin, customer, isAdmin);
            productContainer.getChildren().setAll(newList.getChildren());
        };

        login.setOnAction(e -> {
            loginAndRegisterUI.loginStage().show();
        });




        HBox hBox = new HBox(650, hasLogin ? cart : cartDisabled, hasLogin ? new VBox(50, customerName, logoutButton): isAdmin ? new VBox(50, adminName): login);
        VBox root = new VBox(20, productContainer, hasLogin ? logoutButton : isAdmin ? new HBox(productUI.addButton(primaryStage, onRefresh, isAdmin), logoutButton) : new Label());
        root.setAlignment(Pos.CENTER);
        VBox roots = new VBox(10, hBox, root);
        String image = Objects.requireNonNull(getClass().getResource("/images/coffee_background.jpg")).toExternalForm();

        roots.setStyle("-fx-background-image: url('" + image + "');"
                + "-fx-background-size: cover;");


        Scene scene = new Scene(roots, 960, 750);


        primaryStage.setScene(scene);
        primaryStage.setResizable(false);

        
        return primaryStage;
    }

}
