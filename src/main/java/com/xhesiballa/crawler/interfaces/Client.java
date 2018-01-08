package com.xhesiballa.crawler.interfaces;

import java.util.ArrayList;

public interface Client {
    ArrayList<String> getManga();

    ArrayList<String> getChaptersURL(String mangaURL);

    void getChapter(String mangaTitle, int chapter, String chapterBaseURL);
}
