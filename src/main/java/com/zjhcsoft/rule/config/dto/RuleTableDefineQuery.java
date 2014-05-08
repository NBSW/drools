package com.zjhcsoft.rule.config.dto;

import java.io.Serializable;

/**
 * @since 2014-02-28
 */
public class RuleTableDefineQuery implements Serializable {

    private static final long serialVersionUID = 139357159929208944L;

    private String tableCode;
    private Integer type;
    private String dsCode;
    private String latnId;

    public String getTableCode() {
        return tableCode;
    }

    public void setTableCode(String tableCode) {
        this.tableCode = tableCode;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public String getDsCode() {
        return dsCode;
    }

    public void setDsCode(String dsCode) {
        this.dsCode = dsCode;
    }

    public String getLatnId() {
        return latnId;
    }

    public void setLatnId(String latnId) {
        this.latnId = latnId;
    }
}