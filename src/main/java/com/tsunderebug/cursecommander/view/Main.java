package com.tsunderebug.cursecommander.view;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

public class Main extends Application {

	private Stage primaryStage;
	private BorderPane rootLayout;

	public static Stage instance;

	@Override
	public void start(Stage primaryStage) throws Exception {
		this.primaryStage = primaryStage;
		this.primaryStage.setTitle("CurseCommander");

		FXMLLoader l = new FXMLLoader();
		l.setLocation(Main.class.getResource("/main.fxml"));
		rootLayout = l.load();

		Scene s = new Scene(rootLayout);
		primaryStage.setScene(s);

		instance = primaryStage;

		primaryStage.show();
	}

}
