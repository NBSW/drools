package com.zjhcsoft.qin.exchange.controller;


import com.zjhcsoft.qin.exchange.dto.MessageTracker;
import com.zjhcsoft.qin.exchange.dto.ResponseVO;
import com.zjhcsoft.qin.inner.ConfigContainer;
import com.zjhcsoft.qin.inner.controller.UniformCode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

/**
 * <h1>辅助类</h1>
 */
public class ControllerHelper {

    /**
     * 分页解析
     */
    public static int[] getPageParameters(HttpServletRequest httpServletRequest) {
        int pageNumber = httpServletRequest.getParameterMap().containsKey(ConfigContainer.FLAG_PAGE_NUMBER) ? Integer.valueOf(httpServletRequest.getParameter(ConfigContainer.FLAG_PAGE_NUMBER)) : -1;
        int pageSize = httpServletRequest.getParameterMap().containsKey(ConfigContainer.FLAG_PAGE_SIZE) ? Integer.valueOf(httpServletRequest.getParameter(ConfigContainer.FLAG_PAGE_SIZE)) : -1;
        if (-1 == pageNumber) {
            return null;
        }
        return new int[]{pageNumber, pageSize};
    }


    /**
     * 上传所有文件
     *
     * @param realPath        上传根目录
     * @param uploadAssembler
     * @param isTimeStamp     是否加上时间戳
     * @param files           要上传的文件
     * @return 上传文件路径
     */
    public static String[] upload(String realPath, UploadAssembler uploadAssembler, boolean isTimeStamp, MultipartFile... files) throws Exception {
        List<String> result = new ArrayList<>();
        String fileName;
        if (uploadAssembler.onPreUpload(files)) {
            List<File> realFiles = new ArrayList<>();
            File realFile;
            String path = realPath + File.separatorChar + uploadAssembler.getUploadPath();
            Files.createDirectories(Paths.get(path));
            for (MultipartFile file : files) {
                if (null != uploadAssembler.getUploadAllowType() && !uploadAssembler.getUploadAllowType().contains(file.getContentType())) {
                    continue;
                }
                fileName = file.getOriginalFilename();
                if (isTimeStamp) {
                    fileName = fileName.substring(0, fileName.lastIndexOf(".")) + "_" + System.nanoTime() + fileName.substring(fileName.lastIndexOf("."));
                }
                realFile = new File(path + File.separatorChar + fileName);
                file.transferTo(realFile);
                realFiles.add(realFile);
                result.add(uploadAssembler.getUploadPath() + File.separatorChar + fileName);
            }
            uploadAssembler.onPostUpload(realFiles);
            return result.toArray(new String[result.size()]);
        }
        return null;
    }

    /**
     * 成功
     */
    public static ResponseVO success() {
        return new ResponseVO(UniformCode.SUCCESS.getCode(), "Request Success.", null);
    }

    /**
     * 成功，带对象
     *
     * @param result 返回对象
     */
    public static <T> ResponseVO success(T result) {
        return new ResponseVO(UniformCode.SUCCESS.getCode(), "Request Success.", result);
    }

    /**
     * 没有找到资源
     */
    public static ResponseVO notFound() {
        return new ResponseVO(UniformCode.NOT_FOUND.getCode(), "The resource is not found!", null);
    }

    /**
     * 没有找到资源
     */
    public static ResponseVO notFound(String message) {
        return new ResponseVO(UniformCode.NOT_FOUND.getCode(), message, null);
    }

    /**
     * 请求错误，如参数有问题
     */
    public static ResponseVO badRequest() {
        return new ResponseVO(UniformCode.BAD_REQUEST.getCode(), "The request is bad!", null);
    }

    /**
     * 请求错误，如参数有问题
     */
    public static ResponseVO badRequest(String message) {
        return new ResponseVO(UniformCode.BAD_REQUEST.getCode(), message, null);
    }

    /**
     * 请求错误，如参数有问题
     */
    public static ResponseVO badRequest(MessageTracker messageTracker) {
        return new ResponseVO(UniformCode.BAD_REQUEST.getCode(), "Exist some problems.", messageTracker);
    }

    /**
     * 服务不可用
     */
    public static ResponseVO unavailable() {
        return new ResponseVO(UniformCode.SERVICE_UNAVAILABLE.getCode(), "The service is unavailable!", null);
    }

    /**
     * 认证错误
     */
    public static ResponseVO unauthorized() {
        return new ResponseVO(UniformCode.UNAUTHORIZED.getCode(), "The request is unauthorized!", null);
    }

    /**
     * 未知错误
     */
    public static ResponseVO unknownError() {
        return new ResponseVO(UniformCode.UNKNOWN_ERROR.getCode(), "Unknown error!", null);
    }

    /**
     * 未知错误
     */
    public static ResponseVO unknownError(String msg) {
        return new ResponseVO(UniformCode.UNKNOWN_ERROR.getCode(), msg, null);
    }

    private static final Logger logger = LoggerFactory.getLogger(ControllerHelper.class);
}
