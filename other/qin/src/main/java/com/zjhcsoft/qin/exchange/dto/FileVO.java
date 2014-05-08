package com.zjhcsoft.qin.exchange.dto;

public class FileVO {

    private String[] fileNames;
    private String basePath;

    public FileVO() {
    }

    public FileVO(String[] fileNames, String basePath) {
        this.fileNames = fileNames;
        this.basePath = basePath;
    }

    public String[] getFileNames() {
        return fileNames;
    }

    public void setFileNames(String[] fileNames) {
        this.fileNames = fileNames;
    }

    public String getBasePath() {
        return basePath;
    }

    public void setBasePath(String basePath) {
        this.basePath = basePath;
    }
}
