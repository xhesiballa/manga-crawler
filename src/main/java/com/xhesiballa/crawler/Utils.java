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
	
	public void downloadImage(String imageURL, String savePath, String fileName){
		String saveLocation = config.getSaveLocation();
		String FILE_EXTENTION = config.getFileExtension();

		String fullPath = saveLocation  + savePath + fileName + FILE_EXTENTION;
		
    	try(InputStream in = new URL(imageURL).openStream()){
//    		System.out.println("Downloading img:" + url);
    		if(Files.exists(Paths.get(fullPath))){
    			Files.delete(Paths.get(fullPath));
    		}
    		Files.createDirectories(Paths.get(fullPath).getParent());
    	    Files.copy(in, Paths.get(fullPath));
//    	    System.out.println("Saved to:" + path);
    	} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
    }
}
