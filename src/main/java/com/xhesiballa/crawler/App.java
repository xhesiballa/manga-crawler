package com.xhesiballa.crawler;

import com.xhesiballa.crawler.interfaces.Client;
import com.xhesiballa.crawler.model.Chapter;
import com.xhesiballa.crawler.model.Manga;
import com.xhesiballa.crawler.ui.components.ChaptersTable;
import com.xhesiballa.crawler.ui.components.ClientsTable;
import com.xhesiballa.crawler.ui.components.MangaTable;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.*;
import javafx.stage.DirectoryChooser;
import javafx.stage.Stage;

import java.io.File;
import java.util.List;

public class App extends Application {
    private static final String SAVE_LOCATION = "C:/Users/user/Desktop/";
    private static final String FILE_EXTENSION = ".jpg";

    private static Config config;
    private static ClientFactory clientFactory;
    private final DirectoryChooser directoryChooser = new DirectoryChooser();
    private static File saveDirectory = new File(SAVE_LOCATION);
    private final Thread thread = new Thread();

    MangaTable mangaTable;
    ChaptersTable chaptersTable;
    Client selectedClient;

    public static void main(String[] args) {
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

        clientsTable.setOnMouseClicked((event) -> {
            if (event.getClickCount() == 2) {
                Thread newThread = new Thread(() -> {
                    Client client = (Client) clientsTable.getSelectionModel().getSelectedItem();
                    selectedClient = client;
                    List<Manga> manga = client.getManga();

                    mangaTable.listMangas(manga);
                });
                newThread.start();
            }
        });

        grid.add(clientsTable, 0, 0);

        mangaTable = new MangaTable();
        grid.add(mangaTable, 0, 1);

        mangaTable.setOnMouseClicked((event) -> {
            if (event.getClickCount() == 2) {
                new Thread(() -> {
                    Manga manga = (Manga) mangaTable.getSelectionModel().getSelectedItem();
                    List<Chapter> chapters = selectedClient.getChapters(manga);
                    chaptersTable.listChapters(chapters);
                }).start();
            }
        });

        chaptersTable = new ChaptersTable();
        grid.add(chaptersTable, 1, 0, 1, 2);

        chaptersTable.setOnMouseClicked((event) -> {
            if (event.getClickCount() == 2) {
                new Thread(() -> {
                    Chapter selectedChapter = (Chapter) chaptersTable.getSelectionModel().getSelectedItem();
                    selectedClient.getChapter(selectedChapter);
                }).start();
            }
        });

        Label label = new Label("Save Location:");
        TextField saveLocationTextBox = new TextField();
        saveLocationTextBox.setText(config.getSaveLocation());
        saveLocationTextBox.setDisable(true);

        Button changeSaveDirectoryButton = new Button();
        changeSaveDirectoryButton.setText("Change");
        changeSaveDirectoryButton.setOnMouseClicked(event -> {
            directoryChooser.setInitialDirectory(saveDirectory);
            File selectedDirectory = directoryChooser.showDialog(primaryStage);
            if (selectedDirectory != null) {
                saveDirectory = selectedDirectory;
                String newSaveLocation = selectedDirectory.getAbsolutePath() + "/";
                config.setSaveLocation(newSaveLocation);
                saveLocationTextBox.setText(newSaveLocation);
            }
        });
        HBox hBox = new HBox();
        hBox.getChildren().addAll(label, saveLocationTextBox, changeSaveDirectoryButton);
        hBox.setSpacing(10);

        grid.add(hBox, 0, 2);

        Scene scene = new Scene(root, 750, 500);

        primaryStage.setTitle("Manga Crawler!");
        primaryStage.setScene(scene);
        primaryStage.show();
    }
}
 