package com.javafxsalesmanagementsystem.ui;

import com.javafxsalesmanagementsystem.entity.Customer;
import com.sun.javafx.font.freetype.HBGlyphLayout;
import javafx.application.Platform;
import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Border;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.stereotype.Service;

import java.io.FileNotFoundException;
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

        Button login = new Button("Login");

        if (hasLogin) {
            login.setText(customer.get().getUsername());
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


        VBox productContainer = productUI.pagination(hasLogin, customer);

        Runnable onRefresh = () -> {
            VBox newList = productUI.pagination(hasLogin, customer);
            productContainer.getChildren().setAll(newList.getChildren());
        };

        login.setOnAction(e -> {
            loginAndRegisterUI.loginStage().show();
        });

        HBox hBox = new HBox(650, hasLogin ? cart : cartDisabled, login);
        VBox root = new VBox(20, productContainer, hasLogin ? new VBox() : productUI.addButton(primaryStage, onRefresh));
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
