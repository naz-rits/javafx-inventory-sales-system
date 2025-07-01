package com.javafxsalesmanagementsystem;

import com.javafxsalesmanagementsystem.ui.ProductUI;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.layout.VBox;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.Media;
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
        ProductUI productUI = applicationContext.getBean(ProductUI.class);
        productUI.context = applicationContext;

        playBackgroundMusic("C:\\Users\\Rich Nazareta\\Desktop\\JavaFX\\JavaFXSalesManagementSystem\\src\\main\\java\\com\\javafxsalesmanagementsystem\\bgmusic\\Iframe burbank - sorry i like you [GgVcgbtHY9k].mp3");

        VBox productContainer = productUI.pagination();
        Runnable onRefresh = () -> {
            VBox newList = productUI.pagination();
            productContainer.getChildren().setAll(newList.getChildren());
        };



        VBox root = new VBox(20, productContainer, productUI.addButton(primaryStage, onRefresh));
        root.setStyle("-fx-background-color: #D9D9D9;");
        root.setAlignment(Pos.CENTER);
        Scene scene = new Scene(root, 960, 650);

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
