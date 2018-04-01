package com.xhesiballa.crawler.ui.components;

import com.xhesiballa.crawler.model.Manga;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Predicate;

public class MangaTable extends TableView {
    private FilteredList<Manga> filteredMangasList;
    private Consumer<Manga> onSelectedMangaChange;

    public MangaTable() {
        super();

        TableColumn<Manga, String> nameCol = new TableColumn<>("Name");
        nameCol.setCellValueFactory(
                new PropertyValueFactory<>("mangaName"));

        nameCol.prefWidthProperty().bind(widthProperty().multiply(0.9));

        TableColumn<Manga, Number> indexColumn = new TableColumn<>("#");
        indexColumn.setSortable(false);
        indexColumn.setCellValueFactory(column -> new ReadOnlyObjectWrapper<>(getItems().indexOf(column.getValue()) + 1));
        indexColumn.prefWidthProperty().bind(widthProperty().multiply(0.1));

        getColumns().addAll(indexColumn, nameCol);

        setOnMouseClicked((event) -> {
            if (event.getClickCount() == 2) {
                new Thread(() -> {
                    Manga manga = (Manga) getSelectionModel().getSelectedItem();
                    onSelectedMangaChange.accept(manga);
                }).start();
            }
        });
    }

    public void setOnSelectedMangaChange(Consumer<Manga> onSelectedMangaChange) {
        this.onSelectedMangaChange = onSelectedMangaChange;
    }

    public void listMangas(List<Manga> mangas) {
        ObservableList<Manga> mangaObservableList = FXCollections.observableArrayList(mangas);
        filteredMangasList = new FilteredList<>(mangaObservableList, p -> true);
        setItems(filteredMangasList);
    }

    public void setFilterCriteria(Predicate<Manga> p) {
        filteredMangasList.setPredicate(p);
    }
}
