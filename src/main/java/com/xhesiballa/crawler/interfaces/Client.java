package com.xhesiballa.crawler.interfaces;

import java.util.ArrayList;

public interface Client {
    ArrayList<String> getChaptersURLs(String mangaURL);

    void getChapter(int chapter, String chapterBaseURL);
}
