package com.xhesiballa.crawler.model;

public class Chapter implements Comparable<Chapter> {

    private String chapterName;
    private String chapterURL;
    private Manga manga;
    private int pageCount;

    public String getChapterName() {
        return chapterName;
    }

    public void setChapterName(String chapterName) {
        this.chapterName = chapterName;
    }

    public String getChapterURL() {
        return chapterURL;
    }

    public void setChapterURL(String chapterURL) {
        this.chapterURL = chapterURL;
    }

    public Manga getManga() {
        return manga;
    }

    public void setManga(Manga manga) {
        this.manga = manga;
    }

    public int getPageCount() {
        return pageCount;
    }

    public void setPageCount(int pageCount) {
        this.pageCount = pageCount;
    }

    @Override
    public String toString() {
        return "Chapter{" +
                "chapterName='" + chapterName + '\'' +
                ", chapterURL='" + chapterURL + '\'' +
                '}';
    }


    @Override
    public int compareTo(Chapter chapter) {
        return chapterName.compareTo(chapter.getChapterName());
    }
}
