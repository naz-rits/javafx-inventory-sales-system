package com.javafxsalesmanagementsystem.ui;

import com.javafxsalesmanagementsystem.entity.Customer;
import com.javafxsalesmanagementsystem.entity.Product;
import com.javafxsalesmanagementsystem.entity.Sale;
import com.javafxsalesmanagementsystem.entity.SaleItem;
import com.javafxsalesmanagementsystem.service.CustomerService;
import com.javafxsalesmanagementsystem.service.SaleItemService;
import com.javafxsalesmanagementsystem.service.SaleService;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Cursor;
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
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.stereotype.Service;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.time.LocalDateTime;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicReference;

@Service
public class AddToCartUI {

    public ConfigurableApplicationContext context;
    private Stage currentCartStage;
    private ScrollPane currentCartScrollPane;

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

        double totalPrice = product.getPrice() * quantity.getValue();
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
                }
            }
        });

        Text total = new Text();

        total.setFont(Font.font("Arial", FontWeight.BOLD, 18));
        total.setText("₱" + totalPrice);
        total.setTranslateY(20);

        quantity.getEditor().textProperty().addListener((obs, oldText, newText) -> {
            try {
                int qty = Integer.parseInt(newText);
                if (qty > 0 && qty <= product.getQuantity()) {
                    total.setText("₱" + String.format("%.2f", product.getPrice() * qty));
                }
                else {
                    total.setText("Maximum limit reached");
                }
            } catch (NumberFormatException e) {
                total.setText("₱ 0.00"); // Or show error/placeholder
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

    private VBox createCartVBox(Optional<Customer> customer) {
        VBox root = new VBox(20);
        root.setTranslateX(15);
        AtomicReference<Double> totalPrice = new AtomicReference<>(0.0);

        if (customer.isPresent()) {

            CustomerService customerService = context.getBean(CustomerService.class);
            Customer freshCustomer = customerService.findCustomerById(customer.get().getId()).orElse(null);

            if (freshCustomer != null) {
                for (Sale sale : freshCustomer.getSales()) {
                    for (SaleItem saleItem : sale.getSaleItems()) {
                        CheckBox checkBox = new CheckBox();

                        Product product = saleItem.getProduct();
                        String productName = product.getProductName();
                        int quantity = saleItem.getQuantity();
                        double totalAmount = quantity * product.getPrice();

                        checkBox.setText("Product: " + productName +
                                ", Qty: " + quantity +
                                ", Subtotal: ₱" + String.format("%.2f", totalAmount));

                        checkBox.setOnAction(event -> {
                            if (checkBox.isSelected()) {
                                totalPrice.updateAndGet(v -> v + totalAmount);
                            } else {
                                totalPrice.updateAndGet(v -> v - totalAmount);
                            }
                            System.out.println("Running Total: ₱" + String.format("%.2f", totalPrice.get()));
                        });

                        root.getChildren().addAll(new Separator(), checkBox, new Separator());
                    }
                }
            }
        }

        return root;
    }

    public void cartListVBox(Optional<Customer> customer) {
        VBox cartContent = createCartVBox(customer);
        cartListScroll(cartContent);
    }

    public void cartListScroll(VBox root) {
        ScrollPane scrollPane = new ScrollPane(root);
        scrollPane.setFitToWidth(true);

        currentCartScrollPane = scrollPane;
        cartListStage(scrollPane);
    }

    public void cartListStage(ScrollPane root) {
        if (currentCartStage == null) {
            currentCartStage = new Stage();
            currentCartStage.setScene(new Scene(root, 400, 600));
        } else {
            currentCartStage.setScene(new Scene(root, 400, 600));
        }
        currentCartStage.show();
    }

    public void refreshCart(Optional<Customer> customer) {
        if (currentCartStage != null && currentCartStage.isShowing()) {
            VBox updatedCart = createCartVBox(customer);
            currentCartScrollPane.setContent(updatedCart);
        }
    }

    private void showAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle("Warning");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
