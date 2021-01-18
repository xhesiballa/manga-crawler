package com.xhesiballa.crawler.clients;

import com.xhesiballa.crawler.Utils;
import com.xhesiballa.crawler.interfaces.Client;
import com.xhesiballa.crawler.model.Chapter;
import com.xhesiballa.crawler.model.Manga;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.function.Consumer;

public class MangaReader implements Client {
    private Utils utils;
    private String providerName = "Mangareader";
    private String providerURL = "https://www.mangareader.net";

    private String listURL = providerURL + "/alphabetical";

    private String mangaSelector = "ul.d46 li a";
    private String chapterSelector = "table.d48 tbody td a";
    private final String imgSelector = "#ci";

    public MangaReader(Utils utils) {
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
        ArrayList<Manga> mangaList = new ArrayList<>();
        try {
            Document document = getPageContent(listURL);
            Elements elements = document.select(mangaSelector);

            elements.forEach(element -> {
                String name = element.text();
                String href = element.attr("href");

                Manga manga = new Manga();
                manga.setMangaName(name);
                manga.setMangaURL(providerURL + href);
                mangaList.add(manga);
            });
        } catch (IOException e) {
            e.printStackTrace();
        }
        return mangaList;
    }

    @Override
    public ArrayList<Chapter> getChapters(Manga manga) {
        ArrayList<Chapter> chapters = new ArrayList<>();
        try {
            Document document = getPageContent(manga.getMangaURL());
            Elements elements = document.select(chapterSelector);
            elements.forEach(element -> {
                String name = element.text();
                String url = element.attr("href");

                Chapter chapter = new Chapter();
                chapter.setChapterName(name);
                chapter.setChapterURL(providerURL + url);
                chapter.setManga(manga);
                chapters.add(chapter);
            });
        } catch (IOException e) {
            e.printStackTrace();
        }
        return chapters;
    }

    @Override
    public void getChapter(Chapter chapter, Consumer<Double> setProgress) {
        String mangaTitle = chapter.getManga().getMangaName();
        String chapterName = chapter.getChapterName();
        String chapterURL = chapter.getChapterURL();

        String nextURLToDownload = chapterURL + "/1";
        Document document = null;
        try {
            document = getPageContent(nextURLToDownload);
//            int pageCount = getPageCount(document);
            int pageNumber = 1;
            setProgress.accept(-1D);
            String prevURL = null;
            while (true) {
                nextURLToDownload = chapterURL + "/" + pageNumber;
                document = getPageContent(nextURLToDownload);
                if(document == null){
                    break;
                }
                String imgURL = "https:" + document.select(imgSelector).first().attr("src");
                if(imgURL.equals(prevURL)){
                    break;
                }
                prevURL = imgURL;
                utils.downloadImage(imgURL,
                        mangaTitle + "/" + chapterName,
                        Integer.toString(pageNumber));
                pageNumber++;
//                setProgress.accept(((double) pageNumber / pageCount));
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            setProgress.accept(-2D);
            System.out.println("Finished Downloading Chapter:" + chapterName);
        }
    }

    private int getPageCount(Document document) {
        String selector = "#selectpage";
        Element element = document.select(selector).first();
        String text = element.text().split("of")[1].trim();
        return Integer.parseInt(text);
    }
}
