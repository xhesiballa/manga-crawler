package com.xhesiballa.crawler;

import com.xhesiballa.crawler.interfaces.Client;
import com.xhesiballa.crawler.model.Chapter;
import com.xhesiballa.crawler.model.Manga;
import com.xhesiballa.crawler.ui.components.ChaptersTable;
import com.xhesiballa.crawler.ui.components.ClientsTable;
import com.xhesiballa.crawler.ui.components.MangaTable;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

import java.util.List;


public class App extends Application
{
	private static final String SAVE_LOCATION = "C:/Users/user/Desktop/";
	private static final String FILE_EXTENSION = ".jpg";

	private static Config config;
	private static ClientFactory clientFactory;

    MangaTable mangaTable;
    ChaptersTable chaptersTable;
    Client selectedClient;
	
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

		ColumnConstraints c1 = new ColumnConstraints();
		c1.setPercentWidth(50);
		ColumnConstraints c2 = new ColumnConstraints();
		c2.setPercentWidth(50);

		grid.getColumnConstraints().addAll(c1, c2);
		root.getChildren().add(grid);

		ClientsTable clientsTable = new ClientsTable(clientFactory.getRegisteredClients());
		clientsTable.setRowSelectListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                Client client = (Client) newSelection;
				selectedClient = client;
                List<Manga> manga = client.getManga();

                mangaTable.listMangas(manga);
            }
        });

		grid.add(clientsTable, 0, 0);

        mangaTable = new MangaTable();
        grid.add(mangaTable, 0, 1);

		mangaTable.setRowSelectListener((obs, oldSelection, newSelection) -> {
			if (newSelection != null) {
				Manga manga = (Manga) newSelection;
				List<Chapter> chapters = selectedClient.getChapters(manga);
				chaptersTable.listChapters(chapters);
			}
		});

		chaptersTable = new ChaptersTable();
		grid.add(chaptersTable, 1, 0, 1, 2);

		Scene scene = new Scene(root, 750, 500);

		primaryStage.setTitle("Manga Crawler!");
		primaryStage.setScene(scene);
		primaryStage.show();
	}
}
 