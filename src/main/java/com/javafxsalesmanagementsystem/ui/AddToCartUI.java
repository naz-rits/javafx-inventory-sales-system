package com.javafxsalesmanagementsystem.ui;

import com.javafxsalesmanagementsystem.entity.Customer;
import com.javafxsalesmanagementsystem.entity.Product;
import com.javafxsalesmanagementsystem.entity.Sale;
import com.javafxsalesmanagementsystem.entity.SaleItem;
import com.javafxsalesmanagementsystem.service.CustomerService;
import com.javafxsalesmanagementsystem.service.SaleItemService;
import com.javafxsalesmanagementsystem.service.SaleService;
import javafx.beans.Observable;
import javafx.beans.binding.Bindings;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.stereotype.Service;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.time.LocalDateTime;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.Callable;
import java.util.concurrent.atomic.AtomicReference;

@Service
public class AddToCartUI {

    private final SaleService saleService;
    private final SaleItemService saleItemService;
    private final CustomerService customerService;
    public ConfigurableApplicationContext context;
    private Stage currentCartStage;
    private ScrollPane currentCartScrollPane;
    DoubleProperty totalPrice = new SimpleDoubleProperty(0.0);


    public AddToCartUI(SaleService saleService, SaleItemService saleItemService, CustomerService customerService) {
        this.saleService = saleService;
        this.saleItemService = saleItemService;
        this.customerService = customerService;
    }

    public Stage addToCart(Product product, Optional<Customer> customer) throws FileNotFoundException {
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

        Label quantityText = new Label("Quantity");
        quantityText.setFont(Font.font("Arial", FontWeight.BOLD, 18));
        quantityText.setTextFill(Color.web("#333"));
        quantityText.setTranslateY(20);

        ComboBox<Integer> quantity = new ComboBox<>();
        quantity.setEditable(true);
        quantity.setTranslateY(20);
        quantity.setValue(1);

        Button button = new Button("Add to cart");
        button.setStyle("-fx-background-color: green;" +
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

        button.setOnAction(event -> {
            if (customer.isPresent()) {
                String value = quantity.getEditor().getText();

                try {
                    int qty = Integer.parseInt(value);
                    if (qty <= 0 || qty > product.getQuantity()) {
                        showAlert("Invalid quantity");
                        return;
                    }

                    Customer customer1 = customer.get();
                    Sale sale = new Sale();
                    SaleItem saleItem = new SaleItem();

                    sale.setTotalAmount(product.getPrice() * qty);
                    sale.setCustomerName(customer1);
                    sale.setDate(LocalDateTime.now());

                    saleItem.setPrice(product.getPrice());
                    saleItem.setQuantity(qty);
                    saleItem.setProduct(product);
                    saleItem.setSale(sale);

                    // Save to database
                    SaleService saleService = context.getBean(SaleService.class);
                    SaleItemService saleItemService = context.getBean(SaleItemService.class);
                    CustomerService customerService = context.getBean(CustomerService.class);

                    saleService.addSales(sale);
                    saleItemService.saveSaleItem(saleItem);

                    // Refresh the customer data
                    Customer updatedCustomer = customerService.findCustomerById(customer1.getId()).orElse(null);
                    Optional<Customer> updatedCustomerOpt = Optional.ofNullable(updatedCustomer);

                    // Refresh the cart display
                    refreshCart(updatedCustomerOpt);

                    // Close the add to cart window
                    ((Stage) button.getScene().getWindow()).close();

                } catch (NumberFormatException e) {
                    showAlert("Please enter a valid number for quantity");
                } catch (FileNotFoundException e) {
                    throw new RuntimeException(e);
                }
            }
        });

        Text total = new Text();

        total.setFont(Font.font("Arial", FontWeight.BOLD, 18));
        total.setText("₱" + this.totalPrice.get());
        total.setTranslateY(20);

        quantity.getEditor().textProperty().addListener((obs, oldText, newText) -> {
            try {
                int qty = Integer.parseInt(newText);
                if (qty > 0 && qty <= product.getQuantity()) {
                    double updatedTotal = product.getPrice() * qty;
                    total.setText("₱" + String.format("%.2f", updatedTotal));
                } else {
                    total.setText("Maximum limit reached");
                    this.totalPrice.set(0.0);
                }
            } catch (NumberFormatException e) {
                total.setText("₱ 0.00");
                this.totalPrice.set(0.0);
            }
        });


        // VBox container for info
        VBox infoBox = new VBox(10, nameLabel, price, descriptionText, quantityText,
                                       quantity, total, button);
        infoBox.setTranslateY(75);
        infoBox.setPadding(new Insets(30));
        infoBox.setAlignment(Pos.BASELINE_CENTER);
        infoBox.setPrefWidth(200);
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

    private VBox createCartVBox(Optional<Customer> customer, Label runningTotalLabel) throws FileNotFoundException {
        VBox root = new VBox(20);
        root.setTranslateX(15);

        this.totalPrice.set(0.0);
        if (customer.isPresent()) {

            CustomerService customerService = context.getBean(CustomerService.class);
            Customer freshCustomer = customerService.findCustomerById(customer.get().getId()).orElse(null);


            if (freshCustomer != null) {
                runningTotalLabel.setFont(Font.font("Arial", FontWeight.BOLD, 20));
                runningTotalLabel.setTextFill(Color.web("#333"));
                for (Sale sale : freshCustomer.getSales()) {
                    for (SaleItem saleItem : sale.getSaleItems()) {
                        CheckBox checkBox = new CheckBox();
                        checkBox.setSelected(false);
                        checkBox.setScaleX(2);
                        checkBox.setScaleY(2);
                        checkBox.setTranslateY(12);
                        checkBox.setCursor(Cursor.HAND);
                        
                        Product product = saleItem.getProduct();
                        ImageView imageView = new ImageView(new Image(new FileInputStream(product.getImageUrl())));
                        imageView.setFitHeight(40);
                        imageView.setFitWidth(40);

                        String productName = product.getProductName();

                        Text productName2 = new Text(productName);
                        productName2.setFont(Font.font("Arial", FontWeight.BOLD, 18));
                        productName2.setTranslateY(12);


                        int quantity = saleItem.getQuantity();

                        Text quantityText = new Text(Integer.toString(quantity) + " piece/s");
                        quantityText.setFont(Font.font("Arial", FontWeight.BOLD, 18));
                        quantityText.setTranslateY(12);

                        double totalAmount = quantity * product.getPrice();

                        Text totalAmountText = new Text("₱" + String.format("%.2f", totalAmount));
                        totalAmountText.setFont(Font.font("Arial", FontWeight.BOLD, 18));
                        totalAmountText.setTranslateY(12);


                        checkBox.setOnAction(event -> {
                            if (checkBox.isSelected()) {
                                this.totalPrice.set(this.totalPrice.get() + totalAmount);
                            }
                            else {
                                this.totalPrice.set(this.totalPrice.get() - totalAmount);
                            }
                            runningTotalLabel.setText("₱" + String.format("%.2f", this.totalPrice.get()));
                            System.out.println("Running Total: ₱" + String.format("%.2f", this.totalPrice.get()));
                        });
                        VBox vBox = new VBox(10, productName2, quantityText);
                        vBox.setTranslateY(-15);

                        HBox hBox = new HBox(30, checkBox, imageView, vBox, totalAmountText);

                        ImageView imageView2 = new ImageView(new Image(new FileInputStream("src/main/resources/images/trashbin.png")));
                        imageView2.setFitHeight(30);
                        imageView2.setFitWidth(30);

                        Button removeFromCartButton = new Button("", imageView2);

                        removeFromCartButton.setStyle("-fx-background-color: red;" +
                                                      "-fx-text-fill: white;");
                        removeFromCartButton.setCursor(Cursor.HAND);
                        removeFromCartButton.setPrefWidth(150);
                        removeFromCartButton.setTranslateX(110);
                        removeFromCartButton.setOnAction(event -> {
                            if (checkBox.isSelected()) {
                                this.totalPrice.set(this.totalPrice.get() + totalAmount);
                                runningTotalLabel.setText("₱" + String.format("%.2f", this.totalPrice.get()));
                            }
                            saleService.removeSales(sale);
                            saleItemService.removeSaleItem(saleItem);


                            try {
                                refreshCart(customer);
                            } catch (FileNotFoundException e) {
                                throw new RuntimeException(e);
                            }

                            Customer updatedCustomer = customerService.findCustomerById(freshCustomer.getId()).orElse(null);
                            Optional<Customer> updatedCustomerOpt = Optional.ofNullable(updatedCustomer);

                            // Refresh the cart display
                            try {
                                refreshCart(updatedCustomerOpt);
                            } catch (FileNotFoundException e) {
                                throw new RuntimeException(e);
                            }
                            
                        });
                        root.getChildren().addAll(new Separator(), hBox, removeFromCartButton, new Separator());
                    }
                }
            }
        }

        return root;
    }

    public void cartListVBox(Optional<Customer> customer) throws FileNotFoundException {
        Label runningTotalLabel = new Label("₱" + String.format("%.2f", this.totalPrice.get()));
        runningTotalLabel.setFont(Font.font("Arial", FontWeight.BOLD, 20));
        runningTotalLabel.setTextFill(Color.web("#333"));

        VBox cartContent = createCartVBox(customer, runningTotalLabel);
        cartListScroll(cartContent, customer, runningTotalLabel);
    }

    public void cartListScroll(VBox root, Optional<Customer> customer, Label runningTotalLabel) throws FileNotFoundException {
        ScrollPane scrollPane = new ScrollPane(root);
        scrollPane.setFitToWidth(true);

        currentCartScrollPane = scrollPane;
        cartListStage(scrollPane, customer, runningTotalLabel);
    }

    public void cartListStage(ScrollPane root, Optional<Customer> customer, Label runningTotalLabel) throws FileNotFoundException {


        Button button = placeOrderButton(customer);

        VBox totalBox = new VBox(30, runningTotalLabel, button);
        totalBox.setTranslateX(-10);
        totalBox.setTranslateY(-20);
        totalBox.setAlignment(Pos.CENTER);
        totalBox.setPadding(new Insets(10));

        VBox layout = new VBox(40, root, totalBox);

        if (currentCartStage == null) {
            currentCartStage = new Stage();
            currentCartStage.initModality(Modality.APPLICATION_MODAL);
        }

        currentCartStage.setScene(new Scene(layout, 400, 550));
        currentCartStage.setResizable(false);
        currentCartStage.setTitle("Cart");
        currentCartStage.show();
    }

    public void refreshCart(Optional<Customer> customer) throws FileNotFoundException {
        if (currentCartStage != null && currentCartStage.isShowing()) {
            Label runningTotalLabel = new Label("₱" + String.format("%.2f", this.totalPrice.get()));
            runningTotalLabel.setFont(Font.font("Arial", FontWeight.BOLD, 20));
            runningTotalLabel.setTextFill(Color.web("#333"));

            VBox updatedCart = createCartVBox(customer, runningTotalLabel);
            ScrollPane updatedScrollPane = new ScrollPane(updatedCart);
            updatedScrollPane.setFitToWidth(true);
            currentCartScrollPane = updatedScrollPane;

            Button button = placeOrderButton(customer);

            VBox totalBox = new VBox(30, runningTotalLabel, button);
            totalBox.setTranslateX(-10);
            totalBox.setTranslateY(-20);
            totalBox.setAlignment(Pos.CENTER);
            totalBox.setPadding(new Insets(10));

            VBox layout = new VBox(40, updatedScrollPane, totalBox);

            currentCartStage.setScene(new Scene(layout, 400, 550));
        }
    }

    private void showAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle("Warning");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    public Button placeOrderButton(Optional<Customer> customer) throws FileNotFoundException {

        Customer freshCustomer = customerService.findCustomerById(customer.get().getId()).orElse(null);
        Button button = new Button("Place Order");
        button.setOnAction(event -> {
            
        });
        // In placeOrderButton
        assert  freshCustomer != null;
        button.disableProperty().bind(Bindings.createBooleanBinding(() ->
                        totalPrice.get() == 0.0 || freshCustomer.getSales().isEmpty(),
                 totalPrice
        ));

        button.setFont(Font.font("Arial", FontWeight.BOLD, 18));
        button.setTranslateX(10);
        button.setPrefWidth(380);
        button.setPrefHeight(40);
        button.setTranslateY(-20);
        button.setStyle("-fx-background-color: green;" +
                "-fx-scale-z: 1.5;" +
                "-fx-text-fill: white;" +
                "-fx-border-width: 50;" +
                "-fx-border-radius: 50;");
        button.setCursor(Cursor.HAND);


        return button;
    }
}
