package com.zjhcsoft.rule.config.entity;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.io.Serializable;

/**
 * Created with IntelliJ IDEA.
 * User: XuanLubin  Date: 14-3-3  Time: 下午4:42
 */
@Embeddable
public class RuleRelationPK  implements Serializable{

    @Column(name = "from_Id")
    private Long fromId;
    @Column(name = "target_Id")
    private Long targetId;
    @Column(name = "rel_Type")
    private String relType;

    public RuleRelationPK() {
    }

    public RuleRelationPK(Long fromId, Long targetId, String relType) {
        this.fromId = fromId;
        this.targetId = targetId;
        this.relType = relType;
    }

    public Long getFromId() {
        return fromId;
    }

    public void setFromId(Long fromId) {
        this.fromId = fromId;
    }

    public Long getTargetId() {
        return targetId;
    }

    public void setTargetId(Long targetId) {
        this.targetId = targetId;
    }

    public String getRelType() {
        return relType;
    }

    public void setRelType(String relType) {
        this.relType = relType;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        RuleRelationPK relation = (RuleRelationPK) o;

        if (fromId != null ? !fromId.equals(relation.fromId) : relation.fromId != null) return false;
        if (relType != relation.relType) return false;
        if (targetId != null ? !targetId.equals(relation.targetId) : relation.targetId != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = fromId != null ? fromId.hashCode() : 0;
        result = 31 * result + (targetId != null ? targetId.hashCode() : 0);
        result = 31 * result + (relType != null ? relType.hashCode() : 0);
        return result;
    }
}
