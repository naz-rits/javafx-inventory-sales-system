package com.javafxsalesmanagementsystem.ui;

import com.javafxsalesmanagementsystem.entity.Product;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;
import org.springframework.stereotype.Service;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.Objects;
import java.util.stream.IntStream;

@Service
public class AddToCartUI {

    public Stage addToCart(Product product) throws FileNotFoundException {
        String backgroundImage = Objects.requireNonNull(getClass().getResource("/images/coffee_background.jpg")).toExternalForm();

        FileInputStream image = new FileInputStream(product.getImageUrl());
        ImageView productImage = new ImageView(new Image(image));
        productImage.setFitHeight(150);
        productImage.setFitWidth(150);
        productImage.setPreserveRatio(false);

        Stage stage = new Stage();

        Rectangle border = new Rectangle();
        border.setStroke(Color.WHITE);
        border.setFill(Color.TRANSPARENT);
        border.setStrokeWidth(20);
        border.setArcWidth(10);
        border.setArcHeight(15);


        border.widthProperty().bind(productImage.fitWidthProperty());
        border.heightProperty().bind(productImage.fitHeightProperty());

        StackPane root = new StackPane(border, productImage);
        root.setTranslateY(50);

        Rectangle background = new Rectangle(300, 300);
        background.setStroke(Color.web("#D9D9D9"));
        background.setFill(Color.web("#D9D9D9"));
        background.setStrokeWidth(20);
        background.setArcWidth(10);
        background.setArcHeight(15);
        background.setTranslateX(46);
        background.setTranslateY(69);

        // Product Name Label
        Label nameLabel = new Label(product.getProductName());
        nameLabel.setFont(Font.font("Arial", FontWeight.BOLD, 18));
        nameLabel.setTextFill(Color.web("#333"));

        // Description Text
        Text descriptionText = new Text(product.getProductDescription());
        descriptionText.setFont(Font.font("Arial", 14));
        descriptionText.setWrappingWidth(300);
        descriptionText.setFill(Color.web("#444"));

        Text price = new Text("₱" + product.getPrice().toString());
        price.setFont(Font.font("Arial", 14));
        price.setWrappingWidth(300);
        price.setFill(Color.web("#444"));

        Label quantityText = new Label("Quantity");
        quantityText.setFont(Font.font("Arial", FontWeight.BOLD, 18));
        quantityText.setTextFill(Color.web("#333"));
        quantityText.setTranslateY(20);

        ComboBox<Integer> quantity = new ComboBox<>();
        quantity.setEditable(true);
        quantity.setTranslateY(20);
        quantity.setValue(1);


        Button button = new Button("Add");
        button.setStyle("-fx-background-color: green;" +
                "-fx-scale-z: 1.5;" +
                "-fx-padding: 10;" +
                "-fx-text-fill: white;" +
                "-fx-border-width: 50;" +
                "-fx-border-radius: 50;"
                );
        button.setFont(Font.font("Arial", FontWeight.BOLD, 18));
        button.setTranslateY(20);

        Text total = new Text();
        total.setFont(Font.font("Arial", FontWeight.BOLD, 18));
        total.setText("₱" + (product.getPrice() * quantity.getValue()));
        total.setTranslateY(20);

        quantity.getEditor().textProperty().addListener((obs, oldText, newText) -> {
            try {
                int qty = Integer.parseInt(newText);
                total.setText("₱" + String.format("%.2f", product.getPrice() * qty));
            } catch (NumberFormatException e) {
                total.setText("₱ 0.00"); // Or show error/placeholder
            }
        });

        // VBox container for info
        VBox infoBox = new VBox(10, nameLabel, price, descriptionText, quantityText,
                                       quantity, total, button);
        infoBox.setTranslateY(75);
        infoBox.setPadding(new Insets(40));
        infoBox.setAlignment(Pos.TOP_LEFT);
        infoBox.setPrefWidth(380);
        infoBox.setStyle("""
        -fx-background-color: rgba(255,255,255,0.9);
        -fx-background-radius: 20;
        -fx-effect: dropshadow(gaussian, rgba(0,0,0,0.2), 10, 0.5, 0, 4);
    """);

        VBox roots = new VBox(root, infoBox);
        roots.setStyle("-fx-background-image: url('" + backgroundImage + "');"
                + "-fx-background-size: cover;");


        stage.setTitle("Add To Cart");
        stage.setResizable(false);
        stage.setScene(new Scene(roots, 400, 600));
        stage.initModality(Modality.APPLICATION_MODAL);
        return stage;
    }
}
