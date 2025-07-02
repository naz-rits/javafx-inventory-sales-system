package com.javafxsalesmanagementsystem;

import com.javafxsalesmanagementsystem.entity.Customer;
import com.javafxsalesmanagementsystem.ui.LoginAndRegisterUI;
import com.javafxsalesmanagementsystem.ui.MainUI;
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
import java.util.Optional;

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
        MainUI mainUI = applicationContext.getBean(MainUI.class);
        mainUI.applicationContext = applicationContext;

        Stage stage = mainUI.initialStage(Optional.empty());
        stage.show();
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
