package com.zjhcsoft.rule.engine.util;

import java.io.Serializable;

/**
 * Created by XuanLubin on 2014/4/3. 9:52
 */
public class ResultMessage implements Serializable{
    private int count;
    private boolean status;
    private int index;

    public ResultMessage(int count, boolean status, int index) {
        this.count = count;
        this.status = status;
        this.index = index;
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }
}
