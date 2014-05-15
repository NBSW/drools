/**
 * Created by XuanLubin on 14-3-18.
 */

function ruleConHide(li) {
    li.find('>input.l-button').hide();
    li.attr('new_li', 'false');
    li.attr('edit', false);
    li.find('>span>input:not(.rule_del_group)').attr('disabled', true).addClass('not_edit_input');
    var i = li.find('>div>input').hide();
    li.find('>div>span').html(i.val());
    li.find('>span>a').show();
    li.find('>span>input.rule_del_group').show();
    li.find('>table td>a:not(:hidden)').attr('need', true).hide();
    li.find('>table td>d:not(:hidden)').attr('need', true).hide();
    $when.hide();
}

var $ruleContainer = {
    rules: {},
    kpiTypeCanChange: function () {
        var ruleCount = 0;
        $.each(this.rules, function (i, ii) {
            if (ii.validate())
                ruleCount++;
        });
        if (ruleCount > 0)
            liger_warn("已有规则合法,不能更改公式类型！");
        return ruleCount < 1;
    },
    newRule: function () {
        var id = M_uuid();
        if ($$RuleFilter) {
            $$RuleFilter.hideAll();
        }
        $$RuleFilter = new RuleFilter();
        var _li = CONSTANT.li.clone(true).attr('li_rid', id);
        var _tr = CONSTANT.tr.clone(true);
        _tr.show();
        _tr.find('td:eq(0)').html("如果：");
        _li.find('table').append(_tr);
        _li.find('>input.save-button').unbind().bind('click', function () {
            $ruleContainer.saveRule(this);
        });
        _li.find('>span>input:eq(0)').bind('input propertychange', function () {
            $$RuleFilter.ruleName(this);
        });
        _li.find('>input.del-button').unbind().bind('click', function () {
            $ruleContainer.cancelRule(this);
        });
        $("ul.rule_list").append(_li);
        $ruleContainer.rules[id] = $$RuleFilter;
        return id;
    },
    switchRule: function (e) {
        var li = $(e).closest('li');
        var id = li.attr('li_rid');
        if ($$RuleFilter) {
            $$RuleFilter.hideAll();
        }
        table$$RuleFilter();
        $$RuleFilter = $ruleContainer.rules[id];
        var ali = $("ul.rule_list").find('li[edit!=false]');
        ruleConHide(ali);
        li.find('>input.l-button').show();
        li.attr('edit', true);
        li.find('>span>input:not(.rule_del_group)').removeAttr('disabled').removeClass('not_edit_input');
        li.find('>div>input').show();
        li.find('>div>span').html('');
        li.find('>span>a').hide();
        li.find('>span>input.rule_del_group').hide();
        li.find('>table td>a:hidden[need=true]').show();
        li.find('>table td>d:hidden[need=true]').show();
    },
    saveRule: function (e) {
        var li;
        if (typeof e == 'object') {
            li = $(e).closest('li');
        } else {
            li = $('ul.rule_list>li[li_rid=' + e + ']');
        }
        if ($$RuleFilter) {
            //条件框隐藏条件
            $$RuleFilter.hideAll();
        }
        ruleConHide(li);
        $('#rule1').show();
        //todo save Rule Html
    },
    renderHtml: function (rid, ruleData) {
        var li = $('ul.rule_list>li[li_rid=' + rid + ']');
        li.find('>span>input:eq(0)').val(ruleData.ruleName);
        $$RuleFilter.ruleName(ruleData.ruleName);
        li.find('>div>input').val(ruleData.thens_str);
    },
    cancelRule: function (e) {
        var li = $(e).closest('li');
        if ($$RuleFilter) {
            $$RuleFilter.hideAll();
        }
        if ('false' != li.attr('new_li')) {
            $ruleContainer.delRule(e);
        } else {
            $ruleContainer.saveRule(e);
        }
    },
    delRule: function (e) {
        var li = $(e).closest('li');
        var id = li.attr('li_rid');
        $ruleContainer.rules[id].deleteThis();
        delete $ruleContainer.rules[id];
        li.remove();
    },
    getRule: function () {
        var ruleList = $("ul.rule_list");
        var rules = [];
        $.each($ruleContainer.rules, function (k, v) {
            var $t = {'ruleName': v.ruleName()};
            $t['whens'] = v.getSaveRule();
            $t['whens_str'] = v.getShowList();
            var $li = ruleList.find('li[li_rid=' + k + ']');
            var $then = $li.find('>div.rule_input_box>input.rule_input').val();
            $t['thens'] = $when.getWhen($then);
            $t['thens_str'] = $then;
            rules.push($t);
        });
        return rules;
    }
};

function fieldHtml(container, data) {
    $.each(data, function (t, fs) {
        var _h = $('<div class="rule_btn_right_in"></div>');
        _h.append('<h4>' + TableDefines.tableMap[t] + '：</h4><br>');
        $.each(fs, function (i, item) {
            _h.append('<a type="when_item" title="' + item.text + '" href="####" code="' + item.id + '" tid="' + item.tid + '">' + item.text + '</a>');
        });
        container.append(_h);
    });
}

function ruleInputCon() {
    var input_con = $('div.rule_input_con');
    input_con.find('div.rule_btn_right_in').remove();
    var container = input_con.find('>div:eq(1)>ul');
    if (zh && $$BasicRule.getData().length > 0) {
        var _h = $('<div class="rule_btn_right_in"></div>');
        _h.append('<h4>指标：</h4><br>');
        $.each($$BasicRule.getData(), function (i, item) {
            _h.append('<a type="when_kpi" title="' + item.remark + '" href="####" code="' + item.kpiCode + '">' + item.kpiName + '</a>');
        });
        container.append(_h);
    }
    fieldHtml(container, TableDefines.field_data_map);
    fieldHtml(container, TableDefines.field_param_map);
    input_con.find('a[type=when_item]').bind('click', function () {
        $when.click(this, true);
    });
    input_con.find('a[type=when_kpi]').bind('click', function () {
        $when.click(this, false);
    });
    input_con.attr('init', 'true');
    if (zh) {
        $Kpi_Rel_KPI.show();
    } else {
        $Kpi_Rel_KPI.hide();
    }
}

var $when = {
    open: function (dom) {
        var input_con = $('div.rule_input_con');
        if ('true' != input_con.find('init')) {
            ruleInputCon();
        }
        input_con.insertAfter($(dom));
        input_con.show();
    },
    click: function (e, item) {
        var _e = $(e);
        var _i = _e.closest('div.rule_input_con').prev('input');
        var tableName = "";
        if (item) {
            var tid = _e.attr('tid');
            tableName = TableDefines.tableMap[tid] + ".";
        }
        _i.val(_i.val() + tableName + _e.html());
    },
    hide: function () {
        $('div.rule_input_con').hide();
    },
    operator: function () {
        $('div.rule_input_con').find('a[type=when_op]').bind('click', function () {
            $when.click(this, false);
        });
    },
    getWhen: function (str) {
        var fStr = "";
        var _i = 0;
        for (var i = 0; i < str.length; i++) {
            if (getOperator().indexOf(str[i]) > -1) {
                fStr += str[i];
                _i = i + 1;
            } else if (i + 1 == str.length || getOperator().indexOf(str[i + 1]) > -1) {
                var _s = str.substr(_i, i - _i + 1);
                var $s = _s.split(".");
                if ($s.length == 2) {
                    var tableCode = TableDefines.getTableCode($s[0]);
                    var field = TableDefines.getFieldByStr(tableCode, $s[1]);
                    if (field) {
                        fStr += dealOName(field, tableCode);
                    } else {
                        fStr = appendStr(fStr, _s);
                    }
                } else {
                    if (zh && $$BasicRule.getKpiCode(_s)) {
                        fStr += ("[" + $$BasicRule.getKpiCode(_s) + "]");
                    } else {
                        fStr = appendStr(fStr, _s);
                    }
                }
            }
        }
        return fStr;
    }
};

function appendStr(o, a) {
    if (!isNaN(a))
        o += a;
    else {
        o += ("\"" + a + "\"");
    }
    return o;
}

$(function () {
    $$ruleConfigContainerButton.addGroupButton = $$ruleConfigContainer.find("input.role-group-add").unbind().bind('click', function () {
        var g = _addGroup(1, $$RuleFilter.data().length == 0, $$ruleConfigContainer, $$RuleFilter, $$RuleFilter.getCid());
        $$RuleFilter.pushData(g);
    });
    $$ruleConfigContainerButton.addRuleButton = $$ruleConfigContainer.find("input.role-rule-add").unbind().bind('click', function () {
        var r = _addRule($$RuleFilter.data().length == 0, $$ruleConfigContainer, $$RuleFilter, $$RuleFilter.getCid());
        $$RuleFilter.pushData(r);
    });
    $when.operator();
});