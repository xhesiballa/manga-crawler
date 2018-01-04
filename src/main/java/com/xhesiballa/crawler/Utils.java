package com.xhesiballa.crawler;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;

public class Utils {
	private Config config;

	public Utils(Config config){
		this.config = config;
	}
	
	public void downloadImage(String url, String mangaTitle, int chapter, int pageNumber){
		String saveLocation = config.getSaveLocation();
		String FILE_EXTENTION = config.getFileExtension();

		String path = saveLocation + "/" + mangaTitle + "/chapter" +
				Integer.toString(chapter) + "/" + Integer.toString(pageNumber) + FILE_EXTENTION;
		
    	try(InputStream in = new URL(url).openStream()){
//    		System.out.println("Downloading img:" + url);
    		if(Files.exists(Paths.get(path))){
    			Files.delete(Paths.get(path));
    		}
    		Files.createDirectories(Paths.get(path).getParent());
    	    Files.copy(in, Paths.get(path));
//    	    System.out.println("Saved to:" + path);
    	} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
    }
}
