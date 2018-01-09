package com.xhesiballa.crawler;

import com.xhesiballa.crawler.interfaces.Client;
import com.xhesiballa.crawler.ui.components.ClientsTable;
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Scene;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;


public class App extends Application
{
	private static final String SAVE_LOCATION = "C:/Users/user/Desktop/";
	private static final String FILE_EXTENSION = ".jpg";

	private static Config config;
	private static ClientFactory clientFactory;
	
    public static void main( String[] args )
    {
		config = new Config(SAVE_LOCATION, FILE_EXTENSION);
		Utils utils = new Utils(config);
		clientFactory = new ClientFactory(utils);

		launch(args);
    }

    @Override
	public void start(Stage primaryStage) {

		StackPane root = new StackPane();
		GridPane grid = new GridPane();
		root.getChildren().add(grid);

		ClientsTable clientsTable = new ClientsTable(clientFactory.getRegisteredClients());

		grid.add(clientsTable, 0, 0);

		Scene scene = new Scene(root, 500, 250);

		primaryStage.setTitle("Manga Crawler!");
		primaryStage.setScene(scene);
		primaryStage.show();
	}
}
 