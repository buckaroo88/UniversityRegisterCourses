package application;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.stage.Stage;
import javafx.scene.Parent;
import javafx.scene.Scene;

public class Main extends Application {
	@Override
	public void start(Stage primaryStage) {
		try {
			Parent root = FXMLLoader.load(getClass().getResource("/view/login.fxml"));
			Scene scene = new Scene(root);
			primaryStage.setTitle("미래 대학교 수강신청");
			primaryStage.setScene(scene);
			primaryStage.setResizable(false);
			primaryStage.show();
		} catch (Exception e) {
			System.out.println("ㅎㅎ되나요>?");
			e.printStackTrace();
		}
	}

	public static void main(String[] args) {
		launch(args);
	}
}