package com.xhesiballa.crawler.interfaces;

import com.xhesiballa.crawler.model.Manga;

import java.util.ArrayList;

public interface Client {
    ArrayList<Manga> getManga();

    ArrayList<String> getChaptersURL(String mangaURL);

    void getChapter(String mangaTitle, int chapter, String chapterBaseURL);
}
