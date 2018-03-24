package com.xhesiballa.crawler.clients;

import java.io.IOException;
import java.util.ArrayList;
import java.util.function.Consumer;
import java.util.function.Predicate;

import com.xhesiballa.crawler.Utils;
import com.xhesiballa.crawler.interfaces.Client;
import com.xhesiballa.crawler.model.Chapter;
import com.xhesiballa.crawler.model.Manga;
import javafx.scene.control.ProgressBar;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.jsoup.nodes.Element;

public class MangafoxClient implements Client {

    private static final String PROTOCOL = "http:";

    private static final String MANGA_BASE_URL = "http://mangafox.la/";
    private static final String MANGA_LIST_URL = MANGA_BASE_URL + "manga";

    private static final String MANGA_LIST_SELECTOR = "div.manga_list li a.series_preview";
    private static final String IMG_SELECTOR = ".read_img>a>img";
    private static final String CHAPTER_SELECTOR = ".chlist a.tips";
    private static final String BTN_NEXT_PAGE_SELECTOR = ".btn.next_page";
    private static final String NEXT_PAGE_ATTR = "onclick";
    private static final String NEXT_CHAPTER_METHOD = "next_chapter()";
    private static final String CHAPTERS_DDL_SELECTOR = "select.m";

    private Utils utils;

    private String providerName = "Manga Fox";
    private String providerURL = "www.http://mangafox.la";

    public MangafoxClient(Utils utils) {
        this.utils = utils;
    }

    @Override
    public String getProviderName() {
        return providerName;
    }

    @Override
    public String getProviderURL() {
        return providerURL;
    }

    @Override
    public ArrayList<Manga> getManga() {
        ArrayList<Manga> mangasURL = new ArrayList<>();
        try {
            Document document = getPageContent(MANGA_LIST_URL);
            Elements mangaList = document.select(MANGA_LIST_SELECTOR);


            for (Element element : mangaList) {
                String name = element.text();
                String url = element.attr("href");

                if (!url.isEmpty()) {
                    Manga manga = new Manga();

                    manga.setMangaName(name);
                    manga.setMangaURL(PROTOCOL + url);

                    mangasURL.add(manga);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return mangasURL;
    }

    @Override
    public ArrayList<Chapter> getChapters(Manga manga) {
        ArrayList<Chapter> chapterURLs = new ArrayList<>();

        try {
            Document document = getPageContent(manga.getMangaURL());
            Elements chapterLinks = document.select(CHAPTER_SELECTOR);

            for (Element element : chapterLinks) {

                String chapterName = element.text();
                String chapterURL = element.attr("href");

                if (!chapterURL.isEmpty()) {
                    Chapter chapter = new Chapter();

                    chapter.setChapterName(chapterName);
                    chapter.setChapterURL(PROTOCOL + chapterURL);
                    chapter.setManga(manga);

                    chapterURLs.add(chapter);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return chapterURLs;
    }

    @Override
    public void getChapter(Chapter chapter, Consumer<Double> setProgress) {
        String mangaTitle = chapter.getManga().getMangaName();
        String chapterName = chapter.getChapterName();
        String chapterURL = chapter.getChapterURL();
        String chapterBaseURL = chapterURL.substring(0, chapterURL.length() - "/1.html".length());

        String nextURLToDownload = chapterURL;
        Document document;
        int pageCount = -1;
        try {
            document = getPageContent(nextURLToDownload);
            pageCount = document.select(CHAPTERS_DDL_SELECTOR).first().childNodeSize();
        } catch (IOException e) {
            e.printStackTrace();
        }
        int pageNumber = 1;
        while (true) {
            try {
//				System.out.println(nextURLToDownload);
                document = getPageContent(nextURLToDownload);
                String imgURL = document.select(IMG_SELECTOR).first().attr("src");

                utils.downloadImage(imgURL,
                        mangaTitle + "/" + chapterName,
                        Integer.toString(pageNumber));

                nextURLToDownload = getNextPageURL(document);
                if (nextURLToDownload == null) {
                    System.out.println(String.format("Chapter %s finished downloading!!", chapter.getChapterName()));
                    setProgress.accept(new Double(1));
                    return;
                }

                nextURLToDownload = chapterBaseURL + "/" + nextURLToDownload;
            } catch (IOException e) {
                e.printStackTrace();
            }finally {
                if (pageCount != -1) {
                    setProgress.accept(new Double((double)pageNumber / pageCount));
                } else{
                    setProgress.accept(new Double(-1));
                }
                pageNumber++;
            }
        }
    }

    private Document getPageContent(String pageURL) throws IOException {
        return Jsoup.connect(pageURL).get();
    }

    private String getNextPageURL(Document document) {
        Element button = document.select(BTN_NEXT_PAGE_SELECTOR).first();

        if (button.hasAttr(NEXT_PAGE_ATTR) && button.attr(NEXT_PAGE_ATTR).equals(NEXT_CHAPTER_METHOD)) {
            //end of chapter
            return null;
        } else {
            return button.attr("href");
        }
    }
}
