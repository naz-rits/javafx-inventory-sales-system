package com.javafxsalesmanagementsystem.ui;

import com.javafxsalesmanagementsystem.entity.Customer;
import javafx.application.Platform;
import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.Border;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.stereotype.Service;

import java.util.Objects;
import java.util.Optional;

@Service
public class MainUI {

    public ConfigurableApplicationContext applicationContext;

    public Stage initialStage(Optional<Customer> customer) {
        Stage primaryStage = new Stage();

        ProductUI productUI = applicationContext.getBean(ProductUI.class);
        productUI.context = applicationContext;
        LoginAndRegisterUI loginAndRegisterUI = applicationContext.getBean(LoginAndRegisterUI.class);
        loginAndRegisterUI.applicationContext = applicationContext;

        Label label = new Label();
        label.setStyle("-fx-text-fill: white;");
        label.setFont(Font.font("Verdana", FontWeight.BOLD, 20));
        boolean hasLogin = customer.isPresent();


        Button login = new Button("Login");
        if (hasLogin) {
            login.setText(customer.get().getUsername());
        }
        login.setStyle("-fx-background-color: green;" +
                "-fx-scale-z: 1.5;" +
                "-fx-padding: 10;" +
                "-fx-text-fill: white;" +
                "-fx-border-width: 50;" +
                "-fx-border-radius: 50;"
        );
        login.setFont(Font.font("Segoe UI", FontWeight.BOLD, 20));
        login.setCursor(Cursor.HAND);
        login.setTranslateX(850);
        login.setTranslateY(20);
        login.setBorder(Border.stroke(Color.BLACK));

        VBox productContainer = productUI.pagination(hasLogin);

        Runnable onRefresh = () -> {
            VBox newList = productUI.pagination(hasLogin);
            productContainer.getChildren().setAll(newList.getChildren());
        };

        login.setOnAction(e -> {
            loginAndRegisterUI.loginStage().show();
        });

        VBox root = new VBox(20, productContainer, productUI.addButton(primaryStage, onRefresh));
        root.setAlignment(Pos.CENTER);
        VBox roots = new VBox(10, login, root);
        String image = Objects.requireNonNull(getClass().getResource("/images/coffee_background.jpg")).toExternalForm();

        roots.setStyle("-fx-background-image: url('" + image + "');"
                + "-fx-background-size: cover;");


        Scene scene = new Scene(roots, 960, 750);


        primaryStage.setScene(scene);
        primaryStage.setResizable(false);

        
        return primaryStage;
    }

}
