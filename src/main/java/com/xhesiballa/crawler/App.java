package com.xhesiballa.crawler;

import com.xhesiballa.crawler.interfaces.Client;
import com.xhesiballa.crawler.model.Chapter;
import com.xhesiballa.crawler.model.Manga;
import com.xhesiballa.crawler.ui.components.ChaptersTable;
import com.xhesiballa.crawler.ui.components.ClientsTable;
import com.xhesiballa.crawler.ui.components.MangaTable;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.collections.ObservableList;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.stage.DirectoryChooser;
import javafx.stage.Stage;

import java.io.File;
import java.net.URISyntaxException;
import java.nio.file.FileSystems;
import java.security.CodeSource;
import java.util.List;
import java.util.function.Consumer;

public class App extends Application {
    private static final String FILE_EXTENSION = ".jpg";
    private static final String SEARCH_STRING = "Search";

    private static Config config;
    private static ClientFactory clientFactory;
    private final DirectoryChooser directoryChooser = new DirectoryChooser();
    private static File saveDirectory;

    private final ProgressBar progressBar = new ProgressBar();

    {
        progressBar.setVisible(false);
    }

    private MangaTable mangaTable;
    private ChaptersTable chaptersTable;
    private Client selectedClient;
    private TextField searchTextField;

    public static void main(String[] args) {
        config = new Config();
        config.setFileExtension(FILE_EXTENSION);
        Utils utils = new Utils(config);
        clientFactory = new ClientFactory(utils);

        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        try {
            CodeSource codeSource = App.class.getProtectionDomain().getCodeSource();
            saveDirectory = new File(codeSource.getLocation().toURI().getPath()).getParentFile();
            config.setSaveLocation(saveDirectory.getAbsolutePath() + FileSystems.getDefault().getSeparator());
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }

        StackPane root = new StackPane();
        GridPane grid = new GridPane();

        ColumnConstraints c1 = new ColumnConstraints();
        c1.setPercentWidth(50);
        ColumnConstraints c2 = new ColumnConstraints();
        c2.setPercentWidth(50);

        grid.getColumnConstraints().addAll(c1, c2);
        root.getChildren().add(grid);

        //Clients table
        ClientsTable clientsTable = new ClientsTable(clientFactory.getRegisteredClients());
        clientsTable.setOnSelectedClientChange(c -> {
            selectedClient = c;
            List<Manga> manga = selectedClient.getManga();
            mangaTable.listMangas(manga);
            Platform.runLater(() ->
                    searchTextField.setDisable(false)
            );
        });
        grid.add(clientsTable, 0, 0);

        //Search box
        HBox searchMangaHBox = new HBox();
        searchMangaHBox.setSpacing(10);
        searchMangaHBox.setAlignment(Pos.CENTER_LEFT);

        Label searchLabel = new Label(SEARCH_STRING);
        searchTextField = new TextField();
        searchTextField.setDisable(true);
        searchTextField.addEventHandler(KeyEvent.KEY_RELEASED, (event -> {
            String searchText = ((TextField) event.getSource()).getText();
            mangaTable.setFilterCriteria(manga ->
                    searchText == null || searchText.isEmpty() || manga.getMangaName().toLowerCase()
                            .startsWith(searchText.toLowerCase())
            );
        }));

        searchMangaHBox.getChildren().addAll(searchLabel, searchTextField);
        grid.add(searchMangaHBox, 0, 1);

        //manga table
        mangaTable = new MangaTable();
        mangaTable.setOnSelectedMangaChange(m -> {
            List<Chapter> chapters = selectedClient.getChapters(m);
            chaptersTable.listChapters(chapters);
        });
        grid.add(mangaTable, 0, 2);

        //Chapters table
        chaptersTable = new ChaptersTable();
        grid.add(chaptersTable, 1, 0, 1, 3);

        chaptersTable.setOnMouseClicked((event) -> {
            if (event.getClickCount() == 2) {
                Chapter selectedChapter = (Chapter) chaptersTable.getSelectionModel().getSelectedItem();
                downloadChapter(selectedChapter);
            }
        });

        //Save location
        Label label = new Label("Save Location:");
        TextField saveLocationTextBox = new TextField();
        saveLocationTextBox.setText(config.getSaveLocation());
        saveLocationTextBox.setDisable(true);

        Button changeSaveDirectoryButton = new Button();
        changeSaveDirectoryButton.setText("Change");
        changeSaveDirectoryButton.setOnMouseClicked(event -> promptDownloadLocation(primaryStage, saveLocationTextBox));

        grid.add(progressBar, 1, 3);

        Button downloadButton = new Button();
        downloadButton.setText("Download");
        downloadButton.setOnMouseClicked(event -> {
            ObservableList selectedChapters = chaptersTable.getSelectionModel().getSelectedItems();
            selectedChapters.forEach(selection ->
                    downloadChapter((Chapter) selection));
        });
        HBox hBox = new HBox();
        hBox.getChildren().addAll(label, saveLocationTextBox, changeSaveDirectoryButton, downloadButton);
        hBox.setSpacing(10);
        hBox.setAlignment(Pos.CENTER_LEFT);
        grid.add(hBox, 0, 3);

        Scene scene = new Scene(root, 750, 500);

        primaryStage.setTitle("Manga Crawler!");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private void promptDownloadLocation(Stage primaryStage, TextField saveLocationTextBox) {
        if (saveDirectory != null) {
            directoryChooser.setInitialDirectory(saveDirectory);
        }
        File selectedDirectory = directoryChooser.showDialog(primaryStage);
        if (selectedDirectory != null) {
            saveDirectory = selectedDirectory;
            String newSaveLocation = selectedDirectory.getAbsolutePath() + FileSystems.getDefault().getSeparator();
            config.setSaveLocation(newSaveLocation);
            saveLocationTextBox.setText(newSaveLocation);
        }
    }

    private void downloadChapter(Chapter selectedChapter) {
        Consumer<Double> myConsumer = progress -> {
            if (progress != -1) {
                Platform.runLater(() -> progressBar.setProgress(progress));
            }
            if (progress == 1) {
                Platform.runLater(() -> progressBar.setVisible(false));
            }
        };

        progressBar.setProgress(0);
        progressBar.setVisible(true);
        new Thread(() -> {
            selectedClient.getChapter(selectedChapter, myConsumer);
        }).start();
//                progressBar.setVisible(false);
    }
}
 