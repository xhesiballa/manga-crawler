package com.xhesiballa.crawler.clients;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.xhesiballa.crawler.Utils;
import com.xhesiballa.crawler.interfaces.Client;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.jsoup.nodes.Element;

public class MangafoxClient implements Client {

	private static final String MANGA_BASE_URL = "http://mangafox.la/";
	private static final String MANGA_LIST_URL = MANGA_BASE_URL + "/manga";

	private static final String MANGA_LIST_SELECTOR = "div.manga_list li a.series_preview";
	private static final String IMG_SELECTOR = ".read_img>a>img";
	private static final String CHAPTER_SELECTOR = ".chlist a.tips";
	private static final String BTN_NEXT_PAGE_SELECTOR = ".btn.next_page";
	private static final String NEXT_PAGE_ATTR = "onclick";
	private static final String NEXT_CHAPTER_METHOD = "next_chapter()";
	
	private Utils utils;
	
	public MangafoxClient(Utils utils){
		this.utils = utils;
	}


	@Override
	public ArrayList<String> getManga() {
		ArrayList<String> mangasURL = new ArrayList<>();
		try {
			Document document = getPageContent(MANGA_LIST_URL);
			Elements mangaList = document.select(MANGA_LIST_SELECTOR);


			for ( Element element:mangaList ) {
				String url = element.attr("href");
				if( !url.isEmpty() ){
					mangasURL.add(url);
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}

		return mangasURL;
	}

	@Override
	public ArrayList<String> getChaptersURL(String mangaURL){
		ArrayList<String> chapterURLs = new ArrayList<>();
		
		try {
			Document document = getPageContent(mangaURL);
			Elements chapterLinks = document.select(CHAPTER_SELECTOR);
			
			for (Element element : chapterLinks) {
				String url = element.attr("href");
				if( !url.isEmpty() ){
					chapterURLs.add(url);
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return chapterURLs;
	}

	@Override
	public void getChapter(String mangaTitle, int chapter, String chapterBaseURL){
		String nextURLToDownload = chapterBaseURL + "/1.html";
		int pageNumber = 1;
		while(true){
			Document document;
			try {
				System.out.println(nextURLToDownload);
				document = getPageContent(nextURLToDownload);
				String imgURL = document.select(IMG_SELECTOR).first().attr("src");
				
				utils.downloadImage(imgURL,
						mangaTitle + "/" + Integer.toString(chapter),
						Integer.toString(pageNumber++));
				
				nextURLToDownload = getNextPageURL(document);
				if( nextURLToDownload == null){
					System.out.println(String.format("Chapter %d finished downloading!!", chapter));
					return;
				}
				
				nextURLToDownload = chapterBaseURL + "/" + nextURLToDownload;
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	private Document getPageContent(String pageURL) throws IOException{
		return Jsoup.connect(pageURL).get();
	}
	
	private String getNextPageURL(Document document){
		Element button = document.select(BTN_NEXT_PAGE_SELECTOR).first();
		
		if (button.hasAttr(NEXT_PAGE_ATTR) && button.attr(NEXT_PAGE_ATTR).equals(NEXT_CHAPTER_METHOD)){
			//end of chapter
			return null;
		}else{
			return button.attr("href");
		}
	}
}
