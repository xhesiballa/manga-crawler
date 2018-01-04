package com.xhesiballa.crawler.interfaces;

import java.util.ArrayList;

public interface Client {
    ArrayList<String> getChaptersURLs(String mangaURL);

    void getChapter(String mangaTitle, int chapter, String chapterBaseURL);
}
