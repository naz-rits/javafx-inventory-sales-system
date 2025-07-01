package com.javafxsalesmanagementsystem;

import com.javafxsalesmanagementsystem.service.ProductService;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.stage.Stage;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.ConfigurableApplicationContext;

public class JavaFXApplication extends Application {

    public ConfigurableApplicationContext applicationContext;


    public void start(Stage primaryStage) throws Exception {
        ProductService productService = applicationContext.getBean(ProductService.class);


        primaryStage.show();
    }


    public void init() throws Exception {
        applicationContext = new SpringApplicationBuilder(JavaFxSalesManagementSystemApplication.class).run();

    }

    public void stop() throws Exception {
        applicationContext.close();
        Platform.exit();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
