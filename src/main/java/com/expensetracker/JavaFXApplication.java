package com.expensetracker;


import org.springframework.boot.SpringApplication;
import org.springframework.context.ConfigurableApplicationContext;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;


public class JavaFXApplication extends Application {

    private ConfigurableApplicationContext springContext;
    private Parent root;

    @Override
    public void init() throws Exception {
        springContext = SpringApplication.run(ExpenseTrackerApplication.class);
        SpringContextHolder.setApplicationContext(springContext);

        FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/login.fxml"));
        loader.setControllerFactory(springContext::getBean);
        root = loader.load();
    }


    @Override
    public void start(Stage primaryStage) throws Exception {
        primaryStage.setTitle("Expense Tracker");
        primaryStage.setScene(new Scene(root, 800, 600));
        primaryStage.show();
    }


    @Override
    public void stop() throws Exception {
        springContext.close();
    }


    public static void main(String[] args) {
        launch(args);
    }
}