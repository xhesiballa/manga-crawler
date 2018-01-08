package com.xhesiballa.crawler.interfaces;

import java.util.ArrayList;

public interface Client {
    ArrayList<String> getChaptersURL();

    void getChapter(String mangaTitle, int chapter, String chapterBaseURL);
}
