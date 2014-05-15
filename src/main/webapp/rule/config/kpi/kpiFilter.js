/**
 * Created by XuanLubin on 14-3-17.
 */
var Operators = {'string': [
    {id: '==', text: '等于'},
    {id: 'in', text: '包含在'},
    {id: 'not in', text: '不包含'}
], 'number': [
    {id: '==', text: '等于'},
    {id: '>', text: '大于'},
    {id: '<', text: '小于'},
    {id: '>=', text: '大于等于'},
    {id: '<=', text: '小于等于'},
    {id: 'in', text: '包含在'},
    {id: 'not in', text: '不包含'}
]};

var Operators_map = {
    '==': '=',
    'in': '包含在',
    'not in': '不包含',
    '>': '>',
    '<': '<',
    '<=': '<=',
    '>=': '>='
};

var FieldMap = {
    getName: function (key) {
        return TableDefines.getFieldStr($$RuleFilter.tableId($$RuleFilter.getCid()), key);
    }
};

var Join_Option = [
    {id: "&&", text: "并且"},
    {id: "||", text: "或者"}
];

var Join_Operation_map = {
    '&&': "且",
    '||': "或"
};

function fieldCall(data) {
    $$RuleFilter.setFields(data);
}

//选表限制判断
function beforeSelect(v, t) {
    if ($$RuleFilter.table().indexOf(v) > -1) {
        liger_warn_manual("该数据模型在该规则内已被使用不能选择！");
    }
    return $$RuleFilter.table().indexOf(v) < 0;
}

function beforeOpen() {
    if ($$RuleFilter.validate()) {
        liger_warn_manual("规则校验已通过,不能修改数据模型！如需更改,先删除下方已添加条件");
        return false;
    }
    return true;
}


function beforeOpenDialog() {
    return TableDefines.spliceUsedTable($$RuleFilter.table(), $$RuleFilter.tableId($$RuleFilter.getCid()));
}


Rule = function (cid, data, delFun, first, dataObj) {
    var id = M_uuid();
    var jdom;
    var that = this;
    var g = dataObj instanceof Group;
    var type = 'string';
    var value;

    this.setF = function () {
        first = true;
        that.getJdom().find('#join_' + id).remove();
        liger.get('join_' + id).destroy();
        RuleFilter.showDesc('rj', id, g ? 'rrs' : null, g ? dataObj.id() : null, "", 1)
    };

    var valueField;

    function _init() {
        var _html = CONSTANT.rule;
        _html = _html.replace("{blank}", first ? "<strong></strong>" : "<input id=\"join_{rid}\" class=\"select50\">");
        jdom = $(_html.replace(/{rid}/g, id).replace(/{cid}/g, cid));
        jdom.find("#op_" + id).ligerComboBox({ data: null, width: 80, cancelable: false, isMultiSelect: false, onSelected: function (index) {
            if (index == 'in' || index == 'not in') {
                valueField.trigger('inTrigger');
            }
            RuleFilter.showDesc('ro', id, g ? 'rrs' : null, g ? dataObj.id() : null, Operators_map[index], 1);
        }});
        valueField = jdom.find("#val_" + id);
        jdom.find("#field_" + id).ligerComboBox({data: data, width: 150, cancelable: false, onSelected: function (index) {
            type = 'string';
            $.each(data, function (i, item) {
                if (item.id == index) {
                    if (item.type && item.type.toLowerCase() == 'number') {
                        type = 'number';
                    }
                    return false;
                }
            });
            _rs('rr', id, type);
            liger.get("op_" + id).setData(Operators[type]);
            liger.get("op_" + id).selectValue(Operators[type][0].id);
            if (FieldMap.getName(index))
                RuleFilter.showDesc('rf', id, g ? 'rrs' : null, g ? dataObj.id() : null, FieldMap.getName(index), 1);
            if ('number' == type) {
                valueField.trigger('numberTrigger');
            }
        }
        });

        var inProcess = function (that) {
            var ton = type == "string" ? "\"" : "";
            var _val = [];
            var _val2 = that.value.split(/\||,/g);
            $.each(_val2, function (i) {
                if ('number' == type && isNaN(_val2[i])) {
                    _val2[i] = 0;
                }
                _val.push(ton + _val2[i] + ton);
            });
            that.value = _val2.join(",");
            value = "(" + _val.join(",") + ")";
            RuleFilter.showDesc('rv', id, g ? 'rrs' : null, g ? dataObj.id() : null, value, 1);
        };

        valueField.bind('inTrigger', function () {
            _rs('rr', id, "number");
            inProcess(this);
        });

        valueField.bind('numberTrigger input propertychange editTrigger', function () {
            value = this.value;
            var op = liger.get('op_' + id).getValue();
            if (op == 'in' || op == 'not in') {
                inProcess(this);
            } else {
                if ('number' == type && ('' == value || isNaN(value))) {
                    this.value = 0;
                    value = 0;
                }
                RuleFilter.showDesc('rv', id, g ? 'rrs' : null, g ? dataObj.id() : null, value, 1);
            }

        }).val('number' == type ? 0 : '');

        if (data && data.length > 0) {
            liger.get("field_" + id).selectValue(data[0].id);
        }
        if (!first)
            jdom.find("#join_" + id).ligerComboBox({data: Join_Option, width: 50, cancelable: false, isMultiSelect: false, onSelected: function (index) {
                RuleFilter.showDesc('rj', id, g ? 'rrs' : null, g ? dataObj.id() : null, Join_Operation_map[index], 1);
            }}).selectValue(Join_Option[0].id);
        jdom.find("input.role-rule-del").bind('click', function () {
            delFun(id, dataObj, 'rr', id);
        }).ligerButton({
            width: 24
        });
        RuleFilter.showDesc('rr', null, g ? 'rrs' : null, g ? dataObj.id() : null, _getHtml(), 1);
    }

    var _rs = function (t, id, type, rv) {
        var _s = type == 'string';
        if (t && id) {
            var p = $(t + '[d=' + id + ']');
            if (_s && p.find('rs').length == 0) {
                rv = p.find('rv');
                $('<rs>"</rs>').insertBefore(rv);
                $('<rs>"</rs>').insertAfter(rv);
            } else if (!_s) {
                p.find('rs').remove();
            }
        } else if (rv && _s) {
            $('<rs>"</rs>').insertBefore(rv);
            $('<rs>"</rs>').insertAfter(rv);
        }
    };

    var _getHtml = function () {
        var html = $('<rr class="r" d="' + id + '"><rj class="r" d="' + id + '"></rj><rf class="r" d="' + id + '"></rf><ro class="r" d="' + id + '"></ro><rv d="' + id + '"></rv></rr>');
        var r = that.getRule();
        if (r.join) {
            html.find('rj').html(Join_Operation_map[r.join]);
        } else {
            html.find('rj').removeClass('r');
        }
        html.find('rf').html(FieldMap.getName(r.field));
        html.find('ro').html(Operators_map[r.op]);
        var rv = html.find('rv');
        _rs(null, null, r.type, rv);
        rv.html(r.value);
        return html;
    };

    this.getJdom = function (init) {
        if (init)
            return jdom;
        return $('tr[rid=' + id + '][cid=' + cid + ']');
    };

    this.getRule = function () {
        var j = {};
        if (!first) {
            j['join'] = liger.get('join_' + id).getValue();
        }
        var $$f = liger.get('field_' + id).getValue();
        j['field'] = $$f;
        j['op'] = liger.get('op_' + id).getValue();
        j['type'] = type;
        if (j['op'] == 'in' || j['op'] == 'not in') {
            /* var ton = type == "string" ? "\"" : "";
             var _val = [];
             $.each(value.split(/\||,/g), function (i, v) {
             _val.push(ton + v + ton);
             });
             j['value'] = "(" + _val.join(',') + ")";*/
            j['type'] = "original";
        }
        /* else {

         }*/
        j['value'] = value;
        j['oName'] = dealOName($$f, $$RuleFilter.tableId(cid));
        return j;
    };

    this.id = function () {
        return id;
    };

    this.validate = function () {
        return true;
    };

    _init();

    this.renderHtml = function (rule_op) {
        if (!first && rule_op.join) {
            liger.get('join_' + id).selectValue(rule_op.join);
        }
        liger.get('field_' + id).selectValue(rule_op.field);
        liger.get('op_' + id).selectValue(rule_op.op);
        var _val = rule_op.value;
        if (rule_op.op == 'in' || rule_op.op == 'not in') {
            _val = _val.replace(/\(|\)|"/g, '');
            valueField.val(_val);
            valueField.trigger('inTrigger');
        } else {
            valueField.val(_val);
            valueField.trigger('editTrigger');
        }

    };
};

Group = function (cid, lvl, gDelFun, gAddFun, rAddFun, first, _getRule, dataObj) {
    var data = [];
    var jdom, tdom;
    var id = M_uuid();
    var that = this;

    this.setF = function () {
        first = true;
        that.getJdom().find('#join_' + id).closest('tr').remove();
        liger.get('join_' + id).destroy();
        RuleFilter.showDesc('rj', id, g ? 'rrs' : null, g ? dataObj.id() : null, "", 1)
    };

    var g = dataObj instanceof Group;

    var addGroupButton, addRuleButtom;

    function _init() {
        var _html = CONSTANT.group;
        if (!first) {
            _html = CONSTANT.groupFirst + _html;
        }
        jdom = $(_html.replace(/{gid}/g, id).replace(/{cid}/g, cid));
        tdom = jdom.find("table");
        if (lvl % 2 == 1) {
            tdom.addClass('rule_bg_gray');
        }
        if (!first) {
            jdom.find('#join_' + id).ligerComboBox({data: Join_Option, width: 50, cancelable: false, isMultiSelect: false, onSelected: function (index) {
                RuleFilter.showDesc('rj', id, g ? 'rrs' : null, g ? dataObj.id() : null, Join_Operation_map[index], 1);
            }}).selectValue(Join_Option[0].id);
        }
        addGroupButton = jdom.find("input.role-group-add").bind('click', function () {
            data.push(gAddFun(lvl + 1, data.length == 0, tdom, that, cid));
        });
        jdom.find("input.rule_del_group").bind('click', function () {
            gDelFun(id, dataObj, 'rg', id);
        });
        addRuleButtom = jdom.find("input.role-rule-add").bind('click', function () {
            data.push(rAddFun(data.length == 0, tdom, that, cid));
        });
        RuleFilter.showDesc('rg', null, g ? 'rrs' : null, g ? dataObj.id() : null, _getHtml(), 1);
    }

    var _getHtml = function () {
        var html = $('<rg class="r" d="' + id + '"><rj class="r" d="' + id + '"></rj><rk class="r">(</rk><rrs class="r" d="' + id + '"></rrs><rk class="r">)</rk></rg>');
        var r = that.getRule();
        if (r.join) {
            html.find('rj').html(Join_Operation_map[r.join]);
        } else {
            html.find('rj').removeClass('r');
        }
        return html;
    };

    this.removeData = function (id) {
        $.each(data, function (i, item) {
            if (id == item.id()) {
                data.splice(i, 1);
                if (i == 0 && data.length > 0) {
                    data[0].setF();
                }
                return false;
            }
        });
    };

    this.getJdom = function (init) {
        if (init)
            return jdom;
        return $('tr[gid=' + id + '][cid=' + cid + ']');
    };

    this.getRule = function () {
        return _getRule(data, first, jdom, id);
    };

    this.id = function () {
        return id;
    };

    this.validate = function () {
        var vali = true;
        if (data.length == 0)
            return false;
        $.each(data, function (i, item) {
            if (!item.validate()) {
                vali = false;
                return false;
            }
        });
        return vali;
    };
    _init();

    this.renderHtml = function (group) {
        if (!first && group.join) {
            liger.get('join_' + id).selectValue(group.join);
        }
        $.each(group.rules, function (i, r) {
            if (r.rules) {
                addGroupButton.click();
            } else {
                addRuleButtom.click();
            }
            var $nd = data[data.length - 1];
            $nd.renderHtml(r);
        })
    };
};


//条件分组添加
var _addRule = function (first, container, dataObj, pid) {
    if (!$$RuleFilter.tableId($$RuleFilter.getCid())) {
        if (!$$RuleFilter.currTable()) {
            liger_warn("请先选择模型表！");
            return;
        }
        $$RuleFilter.lockCurrentTable();
    }
    var r = new Rule(pid, TableDefines.fields[$$tableLigerComboBox.getValue()], _delRule, first, dataObj);
    r.getJdom(true).insertBefore(container.find("tr:last"));
    return r;
};
var _addGroup = function (lvl, first, container, dataObj, pid) {
    var g = new Group(pid, lvl, _delGroup, _addGroup, _addRule, first, _getRule, dataObj);
    g.getJdom(true).insertBefore(container.find("tr:last"));
    return g;
};
var _delRule = function (rid, dataObj, t, id) {
    $('tr[rid=' + rid + ']').remove();
    dataObj.removeData(rid);
    RuleFilter.showDesc(t, id);
    if (!$$RuleFilter.validate()) {
        $$RuleFilter.unLockCurrentTable();
    }
    return rid;
};
var _delGroup = function (gid, dataObj, t, id) {
    $('tr[gid=' + gid + ']').remove();
    dataObj.removeData(gid);
    RuleFilter.showDesc(t, id);
    return gid;
};

var _getRule = function (data, first, jdom, id) {
    var j = {};
    if (!first) {
        j['join'] = liger.get('join_' + id).getValue();
    }
    $.each(data, function (i, item) {
        if (!j['rules'])j['rules'] = [];
        j['rules'].push(item.getRule());
    });
    return j;
};


RuleFilter = function () {
    var fields;
    var data = [];
    var that = this;

    this.setFields = function (data) {
        fields = data
    };

    this.removeData = function (id) {
        $.each(data, function (i, item) {
            if (id == item.id()) {
                data.splice(i, 1);
                if (i == 0 && data.length > 0) {
                    data[0].setF();
                }
                return false;
            }
        });
    };

    this.deleteThis = function () {
        $.each(conditions, function (i, item) {
            $$ruleConfigContainer.find('tr[cid=' + i + ']').remove();
        });
    };

    this.getRule = function () {
        return _getRule(data, true);
    };

    this.getSaveRule = function () {
        var rule = [];
        $.each(cidList, function (i, k) {
            var $t = {'fact': tableMap[k]};
            _switchCondition(k);
            $t['rule'] = _getRule(data, true)['rules'];
            rule.push($t);
        });
        return rule;
    };

    this.getShowList = function () {
        var _show = [];
        $.each(cidList, function (i, k) {
            _show.push(showMap[k].replace(/ d=".*?"/g, ''));
        });
        return _show;
    };

    this.pushData = function (d) {
        data.push(d);
    };

    this.data = function () {
        return data
    };

    var cidList = [];
    var conditions = {};
    var tableMap = {};
    var showMap = {};
    var cid;

    var _save = function (hide) {
        if (cid) {
            conditions[cid] = data;
            if (hide)
                $$ruleConfigContainer.find('>tbody>tr[cid=' + cid + ']').hide();
            showMap[cid] = RuleFilter.resultDom.html();
            if ($.inArray(cid, cidList) < 0)
                cidList.push(cid);
        }
    };

    this.save = function () {
        var vali = that.validate();
        if (vali)
            _save();
        return vali;
    };

    this.showDesc = function () {
        return RuleFilter.resultDom.html() || showMap[cid];
    };

    this.newCondition = function () {
        _save(true);
        cid = M_uuid();
        RuleFilter.resultDom.html('');
        conditions[cid] = [];
        data = conditions[cid];
        return cid;
    };

    this.getJdom = function () {

    };

    var _switchCondition = function (id) {
        data = conditions[id];
        $$tableLigerComboBox.selectValue(tableMap[id]);
        RuleFilter.resultDom.html(showMap[id]);
        cid = id;
        $$ruleConfigContainer.find('>tbody>tr:not(:last)').hide();
        $$ruleConfigContainer.find('>tbody>tr[cid=' + id + ']').show();
    };

    this.switchCondition = function (id) {
        _save();
        _switchCondition(id);
    };

    this.hideAll = function () {
        _save(true);
        RuleFilter.resultDom.html('');
        $$ruleConfigContainer.find('>tbody>tr:not(:last)').hide();
        cid = null;
    };

    this.delCon = function (id) {
        delete conditions[id];
        delete tableMap[id];
        delete showMap[id];
        cidList = $.grep(cidList, function (_id) {
            return _id != id;
        });
        if (cid == id) {
            cid = null;
        }
        $$ruleConfigContainer.find('>tbody>tr[cid=' + id + ']').remove();
    };

    this.getCid = function () {
        return cid;
    };

    this.tableId = function ($$cid) {
        return tableMap[$$cid];
    };

    this.currTable = function () {
        return $$tableLigerComboBox.getValue();
    };

    this.lockCurrentTable = function () {
        tableMap[cid] = that.currTable();
    };

    this.unLockCurrentTable = function () {
        delete tableMap[cid];
    };

    this.validate = function () {
        var vali = true;
        if (data.length == 0)
            return false;
        $.each(data, function (i, item) {
            if (!item.validate()) {
                vali = false;
                return false;
            }
        });
        return vali;
    };

    this.table = function () {
        var t = [];
        $.each(tableMap, function (i, item) {
            t.push(item);
        });
        return t.join("|");
    };

    var ruleName;
    this.ruleName = function (input) {
        if (input) {
            if (typeof input == 'object') {
                ruleName = input.value;
            } else {
                ruleName = input;
            }
        } else {
            return ruleName;
        }
    };


    this.renderHtml = function (when) {
        $$tableLigerComboBox.selectValue(when.fact);
        $.each(when.rule, function (ii, op) {
            if (op.rules) {
                $$ruleConfigContainerButton.addGroupButton.click();
            } else {
                $$ruleConfigContainerButton.addRuleButton.click();
            }
            var $nd = that.data()[that.data().length - 1];
            $nd.renderHtml(op);
        });
        that.save();
    }
};

RuleFilter.showDesc = function (t, id, tp, pid, val, m) {
    var container = RuleFilter.resultDom;
    if (pid) {
        container = RuleFilter.resultDom.find(tp + '[d=' + pid + ']');
    }

    if (id) {
        if (m) {
            var o = container.find(t + '[d=' + id + ']').html(val);
            if (!val || val.length == 0) {
                o.removeClass('r');
            }
        } else {
            container.find(t + '[d=' + id + ']').remove();
        }
    } else {
        container.append(val);
    }
};

$(function () {
    RuleFilter.resultDom = $('#term_box .rule_result>span');
});