package com.javafxsalesmanagementsystem.ui;


import javafx.geometry.Orientation;
import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Modality;
import javafx.stage.Stage;
import org.springframework.stereotype.Service;


@Service
public class LoginAndRegisterUI {


    public Stage loginStage() {
        String image = getClass().getResource("/images/coffee_background.jpg").toExternalForm();

        Stage stage = new Stage();
        stage.setTitle("Login");


        TextField username = new TextField();
        PasswordField password = new PasswordField();

        Hyperlink register = new Hyperlink(" Sign in");
        register.setTranslateY(-2.5);
        register.setTranslateX(-4);
        register.setFont(Font.font("Segoe UI", FontWeight.NORMAL, 8));
        register.setOnAction(e -> {
            registerStage().show();
        });

        Button login  = new Button("Login");
        login.setCursor(Cursor.HAND);
        login.setStyle("-fx-background-color: green;" +
                "-fx-scale-z: 1.5;" +
                "-fx-text-fill: white;" +
                "-fx-border-width: 50;" +
                "-fx-border-radius: 50;"
        );
        login.setTranslateX(49);
        login.setOnAction(e -> {
            // customer entity validation
        });

        Label dontHaveAccount = new Label("Don't have an account?");
        dontHaveAccount.setFont(Font.font("Segoe UI", FontWeight.NORMAL, 8));

        HBox labelLink = new HBox();
        labelLink.getChildren().addAll(dontHaveAccount, register);

        ToolBar toolBar = new ToolBar();
        toolBar.getItems().addAll(new Label("Username"), username, new Separator(),
                                  new Label("Password"), password, new Separator(),
                                  labelLink, new Separator(), login);
        toolBar.setOrientation(Orientation.VERTICAL);
        toolBar.setStyle(
                "-fx-border-color: black;" +
                        "-fx-border-width: 1;" +
                        "-fx-border-radius: 10;" +
                        "-fx-background-radius: 10;" +
                        "-fx-padding-y: 40;" +
                        "-fx-background-color: #D9D9D9;"

        );
        toolBar.setScaleX(1.5);
        toolBar.setScaleY(1.5);

        VBox root = new VBox();
        root.setAlignment(Pos.CENTER);
        root.setSpacing(10);
        root.getChildren().addAll(toolBar);

        root.setStyle("-fx-background-image: url('" + image + "');"
                + "-fx-background-size: cover;");

        Scene scene = new Scene(root, 400, 600);
        stage.setScene(scene);
        stage.setResizable(false);
        stage.initModality(Modality.APPLICATION_MODAL);

        return stage;
    }

    public Stage registerStage(){
        Stage stage = new Stage();
        stage.setTitle("Register");
        VBox root = new VBox();
        String image = getClass().getResource("/images/coffee_background.jpg").toExternalForm();

        root.setStyle("-fx-background-image: url('" + image + "');"
                + "-fx-background-size: cover;");
        root.setAlignment(Pos.CENTER);
        root.setSpacing(10);

        stage.setScene(new Scene(root, 400, 600));
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.setResizable(false);
        return stage;
    }
}
