package com.zjhcsoft.qin.exchange.dto;

import java.util.List;

/**
 * 分页辅助类
 */
public class PageDTO<E> {
    //start with 1
    private long pageNumber;
    private long pageSize;
    private long pageTotal;
    private long recordTotal;
    private List<E> objects;

    public long getPageNumber() {
        return pageNumber;
    }

    public void setPageNumber(long pageNumber) {
        this.pageNumber = pageNumber;
    }

    public long getPageSize() {
        return pageSize;
    }

    public void setPageSize(long pageSize) {
        this.pageSize = pageSize;
    }

    public long getPageTotal() {
        return pageTotal;
    }

    public void setPageTotal(long pageTotal) {
        this.pageTotal = pageTotal;
    }

    public long getRecordTotal() {
        return recordTotal;
    }

    public void setRecordTotal(long recordTotal) {
        this.recordTotal = recordTotal;
    }

    public List<E> getObjects() {
        return objects;
    }

    public void setObjects(List<E> objects) {
        this.objects = objects;
    }
}
