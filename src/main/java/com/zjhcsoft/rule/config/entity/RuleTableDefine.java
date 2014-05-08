package com.zjhcsoft.rule.config.entity;

import com.zjhcsoft.rule.common.entity.Schema;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

/**
 * @since 2014-02-28
 */
@Entity
@Table(name = "rule_table_define")
public class RuleTableDefine extends Schema implements Serializable {

    private static final long serialVersionUID = 139357159929208944L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE,generator = "rule_seq_generator")
    @SequenceGenerator(name="rule_seq_generator",sequenceName="rule_seq_row_id",allocationSize=1)
    @Column(name = "table_Define_Row_Id")
    private Long tableDefineRowId;
    @Column(name = "table_Name",length = 50)
    private String tableName;
    @Column(name = "table_Code",length = 200)
    private String tableCode;
    @Column(name = "type")
    private Integer type;
    @Column(name = "remark",length = 500)
    private String remark;
    @Column(name = "ds_code",length = 50)
    private String dsCode;
    @Column(name = "fields",columnDefinition = "clob")
    private String fields;
    @Column(name = "rel_Field",columnDefinition = "clob")
    private String relField;
    @Column(name = "select_Sql",columnDefinition = "clob")
    private String selectSql;
    @Column(name = "dim_field",length = 200)
    private String dimField;
    @Column(name = "date_Field",length = 50)
    private String dateField;
    @Column(name = "latn_Id")
    private String latnId;
    @Column(name = "comm_Name",length = 50)
    private String commName;
    @Column(name = "create_User_Name",length = 50)
    private String createUserName;
    @Column(name = "create_User_Row_Id")
    private Long createUserRowId;
    @Column(name = "create_Date")
    private Date createDate;

    public String getDimField() {
        return dimField;
    }

    public void setDimField(String dimField) {
        this.dimField = dimField;
    }

    public Long getTableDefineRowId() {
        return tableDefineRowId;
    }

    public void setTableDefineRowId(Long tableDefineRowId) {
        this.tableDefineRowId = tableDefineRowId;
    }

    public String getTableName() {
        return tableName;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

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

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getFields() {
        return fields;
    }

    public void setFields(String fields) {
        this.fields = fields;
    }

    public String getDsCode() {
        return dsCode;
    }

    public void setDsCode(String dsCode) {
        this.dsCode = dsCode;
    }

    public String getRelField() {
        return relField;
    }

    public void setRelField(String relField) {
        this.relField = relField;
    }

    public String getSelectSql() {
        return selectSql;
    }

    public void setSelectSql(String selectSql) {
        this.selectSql = selectSql;
    }

    public String getDateField() {
        return dateField;
    }

    public void setDateField(String dateField) {
        this.dateField = dateField;
    }

    public String getLatnId() {
        return latnId;
    }

    public void setLatnId(String latnId) {
        this.latnId = latnId;
    }

    public String getCommName() {
        return commName;
    }

    public void setCommName(String commName) {
        this.commName = commName;
    }

    public String getCreateUserName() {
        return createUserName;
    }

    public void setCreateUserName(String createUserName) {
        this.createUserName = createUserName;
    }

    public Long getCreateUserRowId() {
        return createUserRowId;
    }

    public void setCreateUserRowId(Long createUserRowId) {
        this.createUserRowId = createUserRowId;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    @Override
    public String toString() {
        return "RuleTableDefine{" +
                "tableDefineRowId=" + tableDefineRowId +
                ", tableName='" + tableName + '\'' +
                ", tableCode='" + tableCode + '\'' +
                ", dsCode='" + dsCode + '\'' +
                ", fields='" + fields + '\'' +
                ", relField='" + relField + '\'' +
                ", selectSql='" + selectSql + '\'' +
                ", dimField='" + dimField + '\'' +
                ", dateField='" + dateField + '\'' +
                ", latnId='" + latnId + '\'' +
                ", commName='" + commName + '\'' +
                ", type=" + type +
                '}';
    }
}