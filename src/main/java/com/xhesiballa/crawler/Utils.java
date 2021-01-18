package com.xhesiballa.crawler;

import org.jsoup.Connection;
import org.jsoup.Jsoup;

import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class Utils {
    private Config config;

    Utils(Config config) {
        this.config = config;
    }

    public void downloadImage(String imageURL, String savePath, String fileName) {
        String saveLocation = config.getSaveLocation();
        String FILE_EXTENTION = config.getFileExtension();

        String fullPath = saveLocation + savePath + "/" + fileName + FILE_EXTENTION;

        try {
            Connection.Response resultImageResponse = Jsoup.connect(imageURL).ignoreContentType(true).execute();
    		System.out.println("Downloading img:" + imageURL);
            if (Files.exists(Paths.get(fullPath))) {
                Files.delete(Paths.get(fullPath));
            }
            Files.createDirectories(Paths.get(fullPath).getParent());

            Files.copy(resultImageResponse.bodyStream(), Paths.get(fullPath));
    	    System.out.println("Saved to:" + fullPath);
        } catch (MalformedURLException e) {
            System.out.println("-----Malformed Exception-----");
            e.printStackTrace();
        } catch (IOException e) {
            System.out.println("-----IO Exception-----");
            e.printStackTrace();
        }
    }
}
