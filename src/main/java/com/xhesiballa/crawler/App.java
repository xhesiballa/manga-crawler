package com.xhesiballa.crawler;

import com.xhesiballa.crawler.clients.MangafoxClient;

import java.util.ArrayList;


/**
 * Hello world!
 *
 */
public class App 
{
	private static final  String MANGA_URL = "http://mangafox.la/manga/detective_conan/";
	private static final String SAVE_LOCATION = "C:/Users/user/Desktop";
	private static final String PROTOCOL = "http:";
	
    public static void main( String[] args )
    {
    	Utils utils = new Utils("Detective Conan", SAVE_LOCATION);
    	MangafoxClient client = new MangafoxClient(utils);
    	
    	ArrayList<String> urls = client.getChaptersURLs(MANGA_URL);
    	
    	for(int index=1; index<6; index++){
			String url = PROTOCOL + urls.get(urls.size()-index);
			int i = url.lastIndexOf("/");
	    	client.getChapter(index, url.substring(0, i));
    	}
    }
}   
 