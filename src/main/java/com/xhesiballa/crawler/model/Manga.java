package com.xhesiballa.crawler.model;

public class Manga implements Comparable<Manga>{

    private String mangaName;
    private String mangaURL;

    public String getMangaName() {
        return mangaName;
    }

    public void setMangaName(String mangaName) {
        this.mangaName = mangaName;
    }

    public String getMangaURL() {
        return mangaURL;
    }

    public void setMangaURL(String mangaURL) {
        this.mangaURL = mangaURL;
    }

    @Override
    public String toString() {
        return "Manga{" +
                "mangaName='" + mangaName + '\'' +
                ", mangaURL='" + mangaURL + '\'' +
                '}';
    }

    @Override
    public int compareTo(Manga manga) {
        return this.mangaName.compareTo(manga.getMangaName());
    }
}
