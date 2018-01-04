package com.xhesiballa.crawler;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;

public class Utils {
	private static final String FILE_EXTENTION = ".jpg";
	
	private final String saveLocation;
	private final String mangaName;

	Utils(String mangaName, String saveLocation){
		this.mangaName = mangaName;
		this.saveLocation = saveLocation;
	}
	
	public void downloadImage(String url, int chapter, int pageNumber){
		String path = saveLocation + "/" + mangaName + "/chapter" + 
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
