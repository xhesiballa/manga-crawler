package com.xhesiballa.crawler;

import com.xhesiballa.crawler.clients.MangafoxClient;

import java.util.ArrayList;
public class App 
{
	private static final  String MANGA_URL = "http://mangafox.la/manga/detective_conan/";
	private static final String SAVE_LOCATION = "C:/Users/user/Desktop";
	private static final String PROTOCOL = "http:";
	private static final String FILE_EXTENSION = ".jpg";
	private static final String MANGA_TITLE = "Detective Conan";
	
    public static void main( String[] args )
    {
    	Config config = new Config(SAVE_LOCATION, FILE_EXTENSION);
    	Utils utils = new Utils(config);

    	MangafoxClient client = new MangafoxClient(utils);
    	
    	ArrayList<String> urls = client.getChaptersURLs(MANGA_URL);
    	
    	for(int index=1; index<2; index++){
			String url = PROTOCOL + urls.get(urls.size()-index);
			int i = url.lastIndexOf("/");
	    	client.getChapter( MANGA_TITLE, index, url.substring(0, i));
    	}
    }
}   
 