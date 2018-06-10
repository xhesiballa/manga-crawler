package com.xhesiballa.crawler.interfaces;

import com.xhesiballa.crawler.model.Chapter;
import com.xhesiballa.crawler.model.Manga;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.IOException;
import java.util.ArrayList;
import java.util.function.Consumer;

public interface Client {
    String USER_AGENT = "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 " +
            "(KHTML, like Gecko) Chrome/65.0.3325.181 Safari/537.36";

    String getProviderName();

    String getProviderURL();

    ArrayList<Manga> getManga();

    ArrayList<Chapter> getChapters(Manga manga);

    void getChapter(Chapter chapter, Consumer<Double> setProgress);

    default Document getPageContent(String pageURL) throws IOException {
        return Jsoup.connect(pageURL)
                .userAgent(USER_AGENT)
                .header("Accept-Encoding", "gzip, deflate")
                .maxBodySize(0)
                .timeout(600000)
                .get();
    }
}
