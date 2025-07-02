package com.javafxsalesmanagementsystem;

import com.javafxsalesmanagementsystem.ui.LoginAndRegisterUI;
import com.javafxsalesmanagementsystem.ui.ProductUI;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.Border;
import javafx.scene.layout.VBox;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.Media;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.ConfigurableApplicationContext;

import java.io.File;

public class JavaFXApplication extends Application {

    public ConfigurableApplicationContext applicationContext;

    public void playBackgroundMusic(String filepath) {
        File file = new File(filepath);
        Media media = new Media(file.toURI().toString());
        MediaPlayer mediaPlayer = new MediaPlayer(media);
        mediaPlayer.setCycleCount(MediaPlayer.INDEFINITE);
        mediaPlayer.setVolume(0.5);
        mediaPlayer.play();
    }

    public void start(Stage primaryStage) throws Exception {
        // playBackgroundMusic("C:\\Users\\Rich Nazareta\\Desktop\\JavaFX\\JavaFXSalesManagementSystem\\src\\main\\java\\com\\javafxsalesmanagementsystem\\bgmusic\\Iframe burbank - sorry i like you [GgVcgbtHY9k].mp3");

        ProductUI productUI = applicationContext.getBean(ProductUI.class);
        productUI.context = applicationContext;
        LoginAndRegisterUI loginAndRegisterUI = applicationContext.getBean(LoginAndRegisterUI.class);

        Button login = new Button("Login");
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
        VBox productContainer = productUI.pagination();

        Runnable onRefresh = () -> {
            VBox newList = productUI.pagination();
            productContainer.getChildren().setAll(newList.getChildren());
        };

        login.setOnAction(e -> {
            loginAndRegisterUI.loginStage().show();
        });

        VBox root = new VBox(20, productContainer, productUI.addButton(primaryStage, onRefresh));
        root.setAlignment(Pos.CENTER);
        VBox roots = new VBox(10, login, root);
        String image = getClass().getResource("/images/coffee_background.jpg").toExternalForm();

        roots.setStyle("-fx-background-image: url('" + image + "');"
                + "-fx-background-size: cover;");


        Scene scene = new Scene(roots, 960, 700);


        primaryStage.setScene(scene);
        primaryStage.setResizable(false);
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
