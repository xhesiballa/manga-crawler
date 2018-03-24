package com.xhesiballa.crawler.interfaces;

import com.xhesiballa.crawler.model.Chapter;
import com.xhesiballa.crawler.model.Manga;
import javafx.scene.control.ProgressBar;

import java.util.ArrayList;
import java.util.function.Consumer;
import java.util.function.Predicate;

public interface Client {

    String getProviderName();

    String getProviderURL();

    ArrayList<Manga> getManga();

    ArrayList<Chapter> getChapters(Manga manga);

    void getChapter(Chapter chapter, Consumer<Double> setProgress);
}
