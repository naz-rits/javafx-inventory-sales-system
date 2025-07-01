package com.javafxsalesmanagementsystem.ui;


import com.javafxsalesmanagementsystem.JavaFXApplication;
import com.javafxsalesmanagementsystem.entity.Product;
import com.javafxsalesmanagementsystem.service.ProductService;
import javafx.geometry.Orientation;
import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.Node;
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
import java.util.ArrayList;
import java.util.List;

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
        Pagination pagination = new Pagination();


        List<Product> allProducts = productService.getAllProduct();


        int totalProducts = productService.getAllProduct().size();

        int pageSize = 6;
        int pageCount = (int) Math.ceil((double) totalProducts / pageSize);
        pagination.setPageCount(pageCount == 0 ? 1 : pageCount);
        pagination.setCurrentPageIndex(3);
        pagination.setMaxPageIndicatorCount(3);

        pagination.setPageFactory((pageIndex) -> {
            GridPane gridPane = new GridPane();
            gridPane.setAlignment(Pos.CENTER);
            gridPane.setHgap(10);
            gridPane.setVgap(10);

            int start = pageIndex * pageSize;
            int end = Math.min(start + pageSize, allProducts.size());
            int currentIndex = 0;

            for (int i = start; i < end; i++) {
                Product product = allProducts.get(i);

                VBox productBox = new VBox(5);
                productBox.setAlignment(Pos.CENTER);


                Image image = null;
                try {
                    image = new Image(new FileInputStream(product.getImageUrl()));
                } catch (FileNotFoundException e) {
                    throw new RuntimeException(e);
                }

                ImageView imageView = new ImageView(image);
                imageView.setFitHeight(150);
                imageView.setFitWidth(150);
                imageView.setPreserveRatio(true);

                Label nameLabel = new Label(product.getProductName());
                Label priceLabel = new Label("₱" + product.getPrice());

                productBox.getChildren().addAll(imageView, nameLabel, priceLabel);

                int row = currentIndex / 3;
                int col = currentIndex % 3;
                gridPane.add(productBox, col, row);
                currentIndex++;
            }

            return gridPane;
        });


        VBox vBox = new VBox(pagination);
        vBox.setAlignment(Pos.CENTER);

        return vBox;


    }

    public Button addButton(Stage ownerStage, Runnable onSuccess) {
        Button button = new Button("Add Product");
        button.setStyle("-fx-background-color: green;" +
                        "-fx-scale-z: 1.5;" +
                        "-fx-padding: 10;" +
                        "-fx-text-fill: white;" +
                        "-fx-font: bold;");
        button.setCursor(Cursor.HAND);
        button.setOnAction(e -> {
            addStage(ownerStage, onSuccess).show();

        });

        return button;
    }

    public Stage addStage(Stage ownerStage, Runnable onSuccess) {
        ProductService productService = context.getBean(ProductService.class);
        ProductUI productUI = context.getBean(ProductUI.class);

        Stage stage = new Stage();
        Label addProduct = new Label("Add Product");
        addProduct.setFont(Font.font("Segoe UI", FontWeight.BOLD, 20));

        Label noFile = new Label("Choose a file");
        Label noFile2 = new Label("Choose a file");

        Button productImage = new Button("Product Image");
        productImage.setCursor(Cursor.HAND);

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
        TextArea productDescription = new TextArea();
        TextField productPrice = new TextField();
        TextField productQuantity = new TextField();

        SplitMenuButton categories = new SplitMenuButton();
        categories.setText("No Category Selected");
        categories.setCursor(Cursor.HAND);

        MenuItem iced = new MenuItem("Iced Coffee");
        MenuItem hot = new MenuItem("Hot Coffee");
        categories.getItems().addAll(iced, hot);
        iced.setOnAction(e -> {
            categories.setText(iced.getText());
        });
        hot.setOnAction(e -> {
            categories.setText(hot.getText());
        });

        Button buttonSave = new Button("Add");
        buttonSave.setStyle("-fx-background-color: green;" +
                               "-fx-scale-z: 1.5");
        buttonSave.setCursor(Cursor.HAND);
        buttonSave.setTextFill(Color.WHITE);
        buttonSave.setFont(Font.font("Segoe UI", FontWeight.BOLD, 20));
        buttonSave.setAlignment(Pos.BASELINE_CENTER);
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
        ToolBar toolBar = new ToolBar();
        toolBar.setOrientation(Orientation.VERTICAL);
        toolBar.getItems().addAll(addProduct, new Separator(),
                                 new Label("Product Image"), productImage, noFile, new Separator(),
                                 new Label("Product Name"), productName, new Separator(),
                                 new Label("Product Description"), productDescription, new Separator(),
                                 new Label("Product Price"), productPrice, new Separator(),
                                 new Label("Product Quantity"), productQuantity, new Separator(),
                                 new Label("Category"), categories, new Separator(),
                                 buttonSave);

        VBox vBox = new VBox(toolBar);
        vBox.setAlignment(Pos.CENTER);
        Scene scene = new Scene(vBox);
        stage.setScene(scene);

        stage.initOwner(ownerStage);
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.setResizable(false);
        return stage;
    }
}
