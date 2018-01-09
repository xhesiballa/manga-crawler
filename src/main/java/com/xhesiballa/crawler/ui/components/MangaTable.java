package com.xhesiballa.crawler.ui.components;

import com.xhesiballa.crawler.interfaces.Client;
import com.xhesiballa.crawler.model.Manga;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.beans.value.ChangeListener;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

import java.util.List;

public class MangaTable extends TableView {

    public MangaTable() {
        super();

        TableColumn<Client, String> nameCol = new TableColumn<>("Name");
        nameCol.setCellValueFactory(
                new PropertyValueFactory<>("mangaName"));

        nameCol.prefWidthProperty().bind(widthProperty().multiply(0.9));

        TableColumn<Client, Number> indexColumn = new TableColumn<>("#");
        indexColumn.setSortable(false);
        indexColumn.setCellValueFactory(column-> new ReadOnlyObjectWrapper<>(getItems().indexOf(column.getValue()) + 1));
        indexColumn.prefWidthProperty().bind(widthProperty().multiply(0.1));

        getColumns().addAll(indexColumn, nameCol);
    }

    public void listMangas(List<Manga> mangas){
        ObservableList<Manga> mangaObservableList = FXCollections.observableArrayList(mangas);
        setItems(mangaObservableList);
    }

    public void setRowSelectListener(ChangeListener listener){
        getSelectionModel().selectedItemProperty().addListener(listener);
    }
}
