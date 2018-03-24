package com.xhesiballa.crawler;

public class Config {
    private String saveLocation;
    private String fileExtension;

    public Config(String saveLocation, String fileExtension) {
        this.saveLocation = saveLocation;
        this.fileExtension = fileExtension;
    }

    public String getFileExtension() {
        return fileExtension;
    }

    public String getSaveLocation() {
        return saveLocation;
    }

    public void setSaveLocation(String saveLocation) {
        this.saveLocation = saveLocation;
    }
}
