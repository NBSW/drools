package com.zjhcsoft.qin.exchange.controller;

import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.util.List;

/**
 * 上传组装器
 */
public interface UploadAssembler {

    String getUploadPath();

    List<String> getUploadAllowType();

    boolean onPreUpload(MultipartFile[] files);

    void onPostUpload(List<File> realFiles);

}
