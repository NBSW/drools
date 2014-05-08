package com.zjhcsoft.rule.common.database;

/**
 * Created with IntelliJ IDEA.
 * User: XuanLubin  Date: 14-3-13  Time: 上午8:55
 */
public class Column {
    private String name;
    private String label;
    private String type;
    private boolean isNumber;

    public Column() {
    }

    public Column(String name, String label, String type) {
        this.name = name;
        this.label = null == label||"".equals(label) ? name : label;
        this.isNumber = "NUMBER".contains(type);
        if (isNumber) {
            this.type = "Number";
        } else if (type.indexOf("VARCHAR") > -1 || type.indexOf("CLOB") > -1 || type.indexOf("BLOB") > -1) {
            this.type = "String";
        } else {
            this.type = "Date";
        }
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public boolean isNumber() {
        return isNumber;
    }

    public void setNumber(boolean isNumber) {
        this.isNumber = isNumber;
    }
}
