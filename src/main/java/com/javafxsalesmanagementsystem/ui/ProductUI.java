package com.javafxsalesmanagementsystem.ui;


import com.javafxsalesmanagementsystem.entity.Product;
import com.javafxsalesmanagementsystem.service.ProductService;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

@Service
public class ProductUI {

    public ConfigurableApplicationContext context;
    private String filePath;


    public String shortenPath(String path, int maxLength) {
        if (path.length() <= maxLength) return path;

        String start = path.substring(0, maxLength / 2 - 2);
        String end = path.substring(path.length() - (maxLength / 2 - 2));
        return start + "..." + end;
    }

    public VBox pagination() {
        ProductService productService = context.getBean(ProductService.class);


        VBox mainContainer = new VBox();
        mainContainer.setAlignment(Pos.CENTER);
        mainContainer.setSpacing(20);


        Pagination pagination = new Pagination();
        pagination.setCursor(Cursor.HAND);
        List<Product> allProducts = productService.getAllProduct();
        int pageSize = 6;
        int pageCount = (int) Math.ceil((double) allProducts.size() / pageSize);

        pagination.setPageCount(pageCount == 0 ? 1 : pageCount);
        pagination.setMaxPageIndicatorCount(3);


        VBox paginationContainer = new VBox(pagination);
        paginationContainer.setAlignment(Pos.BOTTOM_CENTER);
        paginationContainer.setPadding(new Insets(20, 0, 0, 0)); // Top padding


        pagination.setPageFactory(pageIndex -> {
            GridPane gridPane = new GridPane();
            gridPane.setAlignment(Pos.CENTER);
            gridPane.setHgap(50);
            gridPane.setVgap(20);

            int start = pageIndex * pageSize;
            int end = Math.min(start + pageSize, allProducts.size());

            for (int i = start, currentIndex = 0; i < end; i++, currentIndex++) {
                Product product = allProducts.get(i);
                VBox productBox = createProductBox(product);
                int row = currentIndex / 3;
                int col = currentIndex % 3;
                gridPane.add(productBox, col, row);
            }

            return gridPane;
        });


        mainContainer.getChildren().add(paginationContainer);
        return mainContainer;
    }

    private VBox createProductBox(Product product) {
        VBox productBox = new VBox(10);
        productBox.setAlignment(Pos.CENTER);

        try {
            ImageView imageView = new ImageView(new Image(new FileInputStream(product.getImageUrl())));
            imageView.setFitHeight(150);
            imageView.setFitWidth(150);
            imageView.setPreserveRatio(false);

            productBox.getChildren().addAll(
                    imageView,
                    Objects.equals(product.getCategory(), "Hot Coffee") ? new Label("☕ " + product.getProductName()) : new Label("❄ " + product.getProductName()),
                    new Label("₱" + product.getPrice())
            );

            productBox.setStyle(
                    "-fx-border-color: #ccc;" +
                            "-fx-border-width: 1;" +
                            "-fx-background-color: #ffffff;" +
                            "-fx-padding: 10;" +
                            "-fx-border-radius: 8;" +
                            "-fx-background-radius: 8;" +
                            "-fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.1), 5, 0, 0, 2);"
            );

        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }

        return productBox;
    }

    public Button addButton(Stage ownerStage, Runnable onSuccess) {
        Button button = new Button("Add Product");
        button.setStyle("-fx-background-color: green;" +
                        "-fx-scale-z: 1.5;" +
                        "-fx-padding: 10;" +
                        "-fx-text-fill: white;");
        button.setFont(Font.font("Verdana", FontWeight.BOLD, 15));
        button.setCursor(Cursor.HAND);
        button.setOnAction(e -> {
            addStage(ownerStage, onSuccess).show();

        });

        return button;
    }

    public Stage addStage(Stage ownerStage, Runnable onSuccess) {
        ProductService productService = context.getBean(ProductService.class);

        Stage stage = new Stage();
        VBox vBox = new VBox(15); // spacing between fields
        vBox.setAlignment(Pos.CENTER);
        vBox.setStyle("-fx-background-color: #f4f4f4; -fx-padding: 30;");

        Label addProduct = new Label("Add Product");
        addProduct.setFont(Font.font("Segoe UI", FontWeight.BOLD, 24));
        addProduct.setTextFill(Color.web("#2c3e50"));


        Button productImage = new Button("📷 Choose Image");
        productImage.setCursor(Cursor.HAND);
        productImage.setStyle("-fx-background-color: #2980b9; -fx-text-fill: white;");
        Label noFile = new Label("No image selected");
        Label noFile2 = new Label(); // hidden storage for full path

        productImage.setOnAction(e -> {
            FileChooser fileChooser = new FileChooser();
            File selectedFile = fileChooser.showOpenDialog(stage);
            if (selectedFile != null) {
                filePath = selectedFile.getAbsolutePath();
                noFile2.setText(filePath);
                noFile.setText(shortenPath(filePath, 50));
            }
        });


        TextField productName = new TextField();
        productName.setPromptText("Product Name");

        TextArea productDescription = new TextArea();
        productDescription.setPromptText("Product Description");
        productDescription.setPrefRowCount(3);

        TextField productPrice = new TextField();
        productPrice.setPromptText("Price");

        TextField productQuantity = new TextField();
        productQuantity.setPromptText("Quantity");

        SplitMenuButton categories = new SplitMenuButton();
        categories.setText("Select Category");
        MenuItem iced = new MenuItem("Iced Coffee");
        MenuItem hot = new MenuItem("Hot Coffee");
        categories.getItems().addAll(iced, hot);
        iced.setOnAction(e -> categories.setText(iced.getText()));
        hot.setOnAction(e -> categories.setText(hot.getText()));
        categories.setStyle("-fx-background-color: #ecf0f1;");


        Button buttonSave = new Button("✅ Add Product");
        buttonSave.setCursor(Cursor.HAND);
        buttonSave.setStyle("-fx-background-color: #27ae60; -fx-text-fill: white; -fx-font-size: 16px;");
        buttonSave.setOnAction(e -> {
            Product product = new Product();
            product.setImageUrl(noFile2.getText());
            product.setProductName(productName.getText());
            product.setLowStockLimit(1);
            product.setProductDescription(productDescription.getText());
            product.setPrice(Double.parseDouble(productPrice.getText()));
            product.setQuantity(Integer.parseInt(productQuantity.getText()));
            product.setCategory(categories.getText());
            product.setCreatedAt(LocalDateTime.now());
            productService.addProduct(product);

            stage.close();
            onSuccess.run();

            productName.clear();
            productDescription.clear();
            productPrice.clear();
            productQuantity.clear();
        });


        vBox.getChildren().addAll(
                addProduct,
                productImage,
                noFile,
                productName,
                productDescription,
                productPrice,
                productQuantity,
                categories,
                buttonSave
        );


        Scene scene = new Scene(vBox, 400, 600);
        stage.setScene(scene);
        stage.setTitle("Add Product");
        stage.initOwner(ownerStage);
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.setResizable(false);
        return stage;



    }
}
