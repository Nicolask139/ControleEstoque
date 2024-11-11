package com.controleEstoque;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class MainApp extends Application {

	//Defini o primeiro palco da aplicacao
	@Override
	public void start(Stage primaryStage) throws Exception {
		FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/Login.fxml"));
		Parent root = loader.load();
		primaryStage.setTitle("ControleEstoque");
		primaryStage.setScene(new Scene(root));
		primaryStage.show();
	}

	//inicia o javaFX
	public static void main(String[] args) {
		launch(args);
	}
}
