package com.xhesiballa.crawler.ui.components;

import com.xhesiballa.crawler.model.Chapter;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

import java.util.List;

public class ChaptersTable extends TableView {
    public ChaptersTable() {
        super();

        TableColumn<Chapter, String> nameCol = new TableColumn<>("Name");
        nameCol.setCellValueFactory(
                new PropertyValueFactory<>("chapterName"));

        nameCol.prefWidthProperty().bind(widthProperty().multiply(0.9));

        TableColumn<Chapter, Number> indexColumn = new TableColumn<>("#");
        indexColumn.setSortable(false);
        indexColumn.setCellValueFactory(column-> new ReadOnlyObjectWrapper<>(getItems().indexOf(column.getValue()) + 1));
        indexColumn.prefWidthProperty().bind(widthProperty().multiply(0.1));

        getColumns().addAll(indexColumn, nameCol);
    }

    public void listChapters(List<Chapter> chapters){
        ObservableList<Chapter> chaptersObservableList = FXCollections.observableArrayList(chapters);
        setItems(chaptersObservableList);
    }
}
