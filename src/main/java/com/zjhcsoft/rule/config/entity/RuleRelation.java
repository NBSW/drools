package com.zjhcsoft.rule.config.entity;

import com.zjhcsoft.rule.common.entity.Schema;

import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * Created with IntelliJ IDEA.
 * User: XuanLubin  Date: 14-3-4  Time: 下午3:26
 */
@Entity
@Table(name = "RULE_RELATION")
public class RuleRelation extends Schema {

    @EmbeddedId
    private RuleRelationPK id;

    public RuleRelation() {
    }

    public RuleRelation(RuleRelationPK id) {
        this.id = id;
    }

    public RuleRelationPK getId() {
        return id;
    }

    public void setId(RuleRelationPK id) {
        this.id = id;
    }
}
