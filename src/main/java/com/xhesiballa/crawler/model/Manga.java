package com.xhesiballa.crawler.model;

import java.util.Set;

public class Manga implements Comparable<Manga>{

    private String mangaName;
    private String mangaURL;
    private Set<Chapter> chapters;

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

    public Set<Chapter> getChapters() {
        return chapters;
    }

    public void setChapters(Set<Chapter> chapters) {
        this.chapters = chapters;
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
