package com.xhesiballa.crawler.interfaces;

import com.xhesiballa.crawler.model.Chapter;
import com.xhesiballa.crawler.model.Manga;

import java.util.ArrayList;

public interface Client {

    String getProviderName();

    String getProviderURL();

    ArrayList<Manga> getManga();

    ArrayList<Chapter> getChapters(Manga manga);

    void getChapter(Chapter chapter);
}
