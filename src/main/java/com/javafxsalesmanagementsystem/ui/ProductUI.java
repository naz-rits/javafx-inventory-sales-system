package com.javafxsalesmanagementsystem.ui;


import com.javafxsalesmanagementsystem.entity.Customer;
import com.javafxsalesmanagementsystem.entity.Product;
import com.javafxsalesmanagementsystem.service.ProductService;
import com.javafxsalesmanagementsystem.service.SaleItemService;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
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
import java.util.Optional;

@Service
public class ProductUI {

    private final SaleItemService saleItemService;
    public ConfigurableApplicationContext context;
    private String filePath;

    public ProductUI(SaleItemService saleItemService) {
        this.saleItemService = saleItemService;
    }


    public String shortenPath(String path, int maxLength) {
        if (path.length() <= maxLength) return path;

        String start = path.substring(0, maxLength / 2 - 2);
        String end = path.substring(path.length() - (maxLength / 2 - 2));
        return start + "..." + end;
    }

    public void pagination(String categories, VBox newVbox, boolean hasLogin, Optional<Customer> customer, boolean isAdmin) {

        ProductService productService = context.getBean(ProductService.class);

        newVbox.getChildren().clear();
        VBox mainContainer = new VBox();
        mainContainer.setAlignment(Pos.CENTER);
        mainContainer.setSpacing(20);


        Pagination pagination = new Pagination();
        int pageSize = 6;



        pagination.setMaxPageIndicatorCount(3);




        pagination.setPageFactory(pageIndex -> {
            if (categories.equals("Hot Coffee") || categories.equals("Iced Coffee")) {
                List<Product> products = productService.findByCategory(categories);
                int pageCount = (int) Math.ceil((double) products.size() / pageSize);
                pagination.setPageCount(pageCount == 0 ? 1 : pageCount);

                GridPane gridPane = new GridPane();
                gridPane.setAlignment(Pos.CENTER);
                gridPane.setHgap(50);
                gridPane.setVgap(20);

                int start = pageIndex * pageSize;
                int end = Math.min(start + pageSize, products.size());

                for (int i = start, currentIndex = 0; i < end; i++, currentIndex++) {
                    Product product = products.get(i);
                    VBox productBox = createProductBox(categories, product, hasLogin, customer, isAdmin, newVbox);
                    int row = currentIndex / 3;
                    int col = currentIndex % 3;
                    gridPane.add(productBox, col, row);
                }

                return gridPane;
            }

            List<Product> allProducts = productService.getAllProduct();
            int pageCount = (int) Math.ceil((double) allProducts.size() / pageSize);
            pagination.setPageCount(pageCount == 0 ? 1 : pageCount);

            GridPane gridPane = new GridPane();
            gridPane.setAlignment(Pos.CENTER);
            gridPane.setHgap(50);
            gridPane.setVgap(20);

            int start = pageIndex * pageSize;
            int end = Math.min(start + pageSize, allProducts.size());

            for (int i = start, currentIndex = 0; i < end; i++, currentIndex++) {
                Product product = allProducts.get(i);
                VBox productBox = createProductBox(categories, product, hasLogin, customer, isAdmin, newVbox);
                int row = currentIndex / 3;
                int col = currentIndex % 3;
                gridPane.add(productBox, col, row);
            }

            return gridPane;
        });

        VBox paginationContainer = new VBox(pagination);
        paginationContainer.setAlignment(Pos.BOTTOM_CENTER);
        paginationContainer.setPadding(new Insets(20, 0, 0, 0)); // Top padding

        mainContainer.getChildren().add(paginationContainer);

        newVbox.getChildren().add(mainContainer);

    }

    private VBox createProductBox(String categories, Product product, boolean hasLogin, Optional<Customer> customer, boolean isAdmin, VBox newVbox) {

        ProductUI productUI = context.getBean(ProductUI.class);


        Runnable onRefresh = () -> {
            productUI.pagination(categories, newVbox, hasLogin, customer, isAdmin);
        };

        AddToCartUI addToCartUI = context.getBean(AddToCartUI.class);
        addToCartUI.context = context;

        VBox productBox = new VBox(10);
        productBox.setAlignment(Pos.CENTER);

        Button addToCartButtonDisabled = new Button("Log in first");
        addToCartButtonDisabled.setDisable(true);
        addToCartButtonDisabled.setPrefWidth(150);

        Button addToCartButton = new Button("Buy");
        addToCartButton.setStyle("-fx-background-color: green;" +
                "-fx-scale-z: 1.5;" +
                "-fx-text-fill: white;" +
                "-fx-border-width: 50;" +
                "-fx-border-radius: 50;");
        addToCartButton.setCursor(Cursor.HAND);
        addToCartButton.setPrefWidth(150);

        addToCartButton.setOnAction(e -> {
            try {
                addToCartUI.addToCart(product, customer).show();
            } catch (FileNotFoundException ex) {
                throw new RuntimeException(ex);
            }
        });

        addToCartButtonDisabled.setStyle("-fx-background-color: green;" +
                "-fx-scale-z: 1.5;" +
                "-fx-text-fill: white;" +
                "-fx-border-width: 50;" +
                "-fx-border-radius: 50;");


        Button deleteButton = new Button("Delete");
        deleteButton.setStyle("-fx-background-color: red;" +
                "-fx-scale-z: 1.5;" +
                "-fx-text-fill: white;" +
                "-fx-border-width: 50;" +
                "-fx-border-radius: 50;");
        deleteButton.setCursor(Cursor.HAND);
        deleteButton.setPrefWidth(150);
        try {
            ImageView imageView = new ImageView(new Image(new FileInputStream(product.getImageUrl())));
            imageView.setFitHeight(150);
            imageView.setFitWidth(150);
            imageView.setPreserveRatio(false);

            productBox.getChildren().addAll(
                    imageView,
                    Objects.equals(product.getCategory(), "Hot Coffee") ? new Label("☕ " + product.getProductName()) : new Label("❄️ " + product.getProductName()),
                    new Label("₱" + product.getPrice()),
                    hasLogin ? addToCartButton : isAdmin ? deleteButton : addToCartButtonDisabled);

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

        deleteButton.setOnAction(e -> {
            try {
                deleteStage(product, onRefresh).show();
            } catch (FileNotFoundException ex) {
                throw new RuntimeException(ex);
            }

        });
        return productBox;
    }

    public Button addButton(Stage ownerStage, Runnable onSuccess, boolean isAdmin) {
        Button button = new Button("Add Product");
        button.setStyle("-fx-background-color: green;" +
                        "-fx-scale-z: 1.5;" +
                        "-fx-padding: 10;" +
                        "-fx-text-fill: white;");
        button.setPrefWidth(300);
        button.setFont(Font.font("Verdana", FontWeight.BOLD, 15));
        button.setCursor(Cursor.HAND);
        button.setOnAction(e -> {
            addStage(ownerStage, onSuccess).show();

        });

        if (isAdmin) {
            button.setTranslateX(330);
        }
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

    public Stage deleteStage(Product product, Runnable onSuccess) throws FileNotFoundException{
        Stage stage = new Stage();
        SaleItemService saleItemService = context.getBean(SaleItemService.class);
        ProductService productService = context.getBean(ProductService.class);

        String backgroundImage = Objects.requireNonNull(getClass().getResource("/images/coffee_background.jpg")).toExternalForm();

        FileInputStream image = new FileInputStream(product.getImageUrl());
        ImageView productImage = new ImageView(new Image(image));
        productImage.setFitHeight(150);
        productImage.setFitWidth(150);
        productImage.setPreserveRatio(false);


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

        Rectangle background = new Rectangle(200, 600);
        background.setStroke(Color.web("#D9D9D9"));
        background.setFill(Color.web("#D9D9D9"));
        background.setStrokeWidth(20);
        background.setArcWidth(5);
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
        price.setTranslateX(130);

        Button button = new Button("Remove product");
        button.setStyle("-fx-background-color: red;" +
                "-fx-scale-z: 1.5;" +
                "-fx-padding: 10;" +
                "-fx-text-fill: white;" +
                "-fx-border-width: 50;" +
                "-fx-border-radius: 50;"
        );
        button.setFont(Font.font("Arial", FontWeight.BOLD, 18));
        button.setTranslateY(5);
        button.setCursor(Cursor.HAND);
        button.setPrefWidth(200);

        VBox infoBox = new VBox(10, nameLabel, price, descriptionText, button);
        infoBox.setTranslateY(75);
        infoBox.setPadding(new Insets(30));
        infoBox.setAlignment(Pos.BASELINE_CENTER);
        infoBox.setPrefWidth(200);
        infoBox.setStyle("""
        -fx-background-color: rgba(255,255,255,0.9);
        -fx-background-radius: 20;
        -fx-effect: dropshadow(gaussian, rgba(0,0,0,0.2), 10, 0.5, 0, 4);
    """);

        button.setOnAction(event -> {
            boolean activeProduct = saleItemService.canDeleteProduct(product);

            if (activeProduct) {
                productService.removeProduct(product);
                stage.close();

                onSuccess.run();
            }
            else {
                Label cautionLabel = new Label("Can't delete product, this product is still in someone's cart.");
                cautionLabel.setStyle("-fx-text-fill: red;" );
                cautionLabel.setFont(Font.font("Arial", 12));
                cautionLabel.setTranslateY(15);
                infoBox.getChildren().add(cautionLabel);
            }
        });

        VBox roots = new VBox(root, infoBox);
        roots.setStyle("-fx-background-image: url('" + backgroundImage + "');"
                + "-fx-background-size: cover;");

        stage.setScene(new Scene(roots, 400, 600));
        stage.setResizable(false);
        stage.initModality(Modality.APPLICATION_MODAL);

        return stage;
    }
}
