package com.zjhcsoft.qin.exchange.controller;

import java.util.ArrayList;
import java.util.List;

public class FileType {

    public static final List<String> OFFICE=new ArrayList<String>();
    public static final List<String> COMPRESS=new ArrayList<String>();
    public static final List<String> IMAGE =new ArrayList<String>();
    public static final List<String> AUDUO=new ArrayList<String>();
    public static final List<String> VIDEO=new ArrayList<String>();
    public static final List<String> TXT=new ArrayList<String>();

    static{

        TXT.add(FILE_TYPE.TXT.getCode());

        OFFICE.add(FILE_TYPE.DOC.getCode());
        OFFICE.add(FILE_TYPE.DOCX.getCode());
        OFFICE.add(FILE_TYPE.XLS1.getCode());
        OFFICE.add(FILE_TYPE.XLS2.getCode());
        OFFICE.add(FILE_TYPE.XLSX.getCode());
        OFFICE.add(FILE_TYPE.PPT1.getCode());
        OFFICE.add(FILE_TYPE.PPT2.getCode());
        OFFICE.add(FILE_TYPE.PPTX.getCode());

        COMPRESS.add(FILE_TYPE.ZIP1.getCode());
        COMPRESS.add(FILE_TYPE.ZIP2.getCode());
        COMPRESS.add(FILE_TYPE.GZIP.getCode());
        COMPRESS.add(FILE_TYPE.SevenZ1.getCode());
        COMPRESS.add(FILE_TYPE.SevenZ2.getCode());
        COMPRESS.add(FILE_TYPE.RAR1.getCode());
        COMPRESS.add(FILE_TYPE.RAR2.getCode());

        IMAGE.add(FILE_TYPE.GIF.getCode());
        IMAGE.add(FILE_TYPE.JPG1.getCode());
        IMAGE.add(FILE_TYPE.JPG2.getCode());
        IMAGE.add(FILE_TYPE.PNG.getCode());
        IMAGE.add(FILE_TYPE.BMP1.getCode());
        IMAGE.add(FILE_TYPE.BMP2.getCode());

        AUDUO.add(FILE_TYPE.MP3.getCode());
        AUDUO.add(FILE_TYPE.WAV.getCode());
        AUDUO.add(FILE_TYPE.WMA.getCode());

        VIDEO.add(FILE_TYPE.MP4.getCode());
        VIDEO.add(FILE_TYPE.MOV.getCode());
        VIDEO.add(FILE_TYPE.AVI.getCode());
        VIDEO.add(FILE_TYPE.MOVIE.getCode());
        VIDEO.add(FILE_TYPE.WEBM.getCode());
        VIDEO.add(FILE_TYPE.RM.getCode());
        VIDEO.add(FILE_TYPE.RMVB.getCode());
    }

    public enum FILE_TYPE {
        HTML("text/html"),
        XML("text/xml"),
        TXT("text/plain"),
        JS("application/x-javascript"),
        CSS("text/css"),

        DOC("application/msword"),
        DOCX("application/vnd.openxmlformats-officedocument.wordprocessingml.document"),
        XLS1("application/x-xls"),
        XLS2("application/vnd.ms-excel"),
        XLSX("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"),
        PPT1("application/x-ppt"),
        PPT2("application/vnd.ms-powerpoint"),
        PPTX("application/vnd.openxmlformats-officedocument.presentationml.presentation"),

        ZIP1("application/zip"),
        ZIP2("application/x-zip-compressed"),
        GZIP("application/gzip"),
        SevenZ1("application/x-7z-compressed"),
        SevenZ2("application/octet-stream"),
        RAR1("application/rar"),
        RAR2("application/x-rar-compressed"),

        GIF("image/gif"),
        JPG1("image/jpeg"),
        JPG2("image/pjpeg"),
        PNG("image/png"),
        BMP1("application/x-bmp"),
        BMP2("image/bmp"),

        MP3("audio/mp3"),
        WAV("audio/wav"),
        WMA("audio/x-ms-wma"),

        MP4("video/mpeg4"),
        MOV("video/quicktime"),
        AVI("video/avi"),
        MOVIE("video/x-sgi-movie"),
        WEBM("audio/webm"),
        RM("audio/x-pn-realaudio"),
        RMVB("application/vnd.rn-realmedia-vbr");

        private String code;

        private FILE_TYPE(String code) {
            this.code = code;
        }

        public String getCode(){
            return code;
        }

    }
}
