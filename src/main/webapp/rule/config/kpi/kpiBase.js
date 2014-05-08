/**
 * Created by XuanLubin on 14-3-19.
 */
var zh = false;
var $$tableLigerComboBox;
var $$ruleConfigContainer;
var $$ruleConfigContainerButton = {};
var $$RuleFilter;

var $global = {
    hasOtherParam: false
};

var f_kpi_constants = {
    OTHER_PARAM: "otherParam"
};

//规则属于哪个活动g 使用哪个数据模型t rowId
var G_T_PARAM = {
    t: undefined,
    g: undefined,
    ruleDefine: undefined //修改时原规则定义，创建时不存在
};

var TableDefines = {
    data: {},
    params: [],
    fields: {},
    fieldMap: {
        f_s: {},//字段 - 字段中文名 按表ID分组
        s_f: {},//字段中文名 - 字段 按表ID分组
        update: function (tid, field, fieldStr) {
            if (!this.f_s[tid]) {
                this.f_s[tid] = {};
            }
            this.f_s[tid][field] = fieldStr;
            if (!this.s_f[tid]) {
                this.s_f[tid] = {};
            }
            this.s_f[tid][fieldStr] = field;
        }
    },//表字段 字段中文名定义 映射
    table: [],
    tableField: {}, //参与计算
    tableMap: {},//表定义ID->中文名 映射
    tableMap2: {},//表定义ID<-中文名 映射
    paramTable: [],
    canUseTable: [],
    field_param_map: {},
    field_data_map: {},
    table_id_arr: [],//表RowId表定义
    getTableCode: function (tableName) {
        return this.tableMap2[tableName];
    },
    getTableName: function (tableId) {
        return this.tableMap[tableId];
    },
    getFieldMap: function (tableId) {
        //return this.fieldMap.t_f_s_m[tableId];
    },
    getFieldByStr: function (tableId, fieldStr) {
        try {
            return this.fieldMap.s_f[tableId][fieldStr];
        } catch (e) {
        }
    },
    getFieldStr: function (tableId, field) {
        try {
            return this.fieldMap.f_s[tableId][field];
        } catch (e) {
        }
    },
    spliceUsedTable: function (usedTable, exclude) {
        var show = [];
        $.each(this.table, function (i, t) {
            if (usedTable.indexOf(t.tableDefineRowId) < 0 || (exclude && exclude == t.tableDefineRowId)) {
                // notice 组合指标不显示数据模型表
                //if (!zh || (zh && t.type == 3))
                show.push(t);
            }
        });
        return show;
    }
};

var $$BasicRule = function () {
    var basic = [];
    var ruleNameCodeMap = {};
    var ruleCodeNameMap = {};
    var update = function (data) {
        basic = data;
        $.each(data, function (i, d) {
            ruleNameCodeMap[d.kpiName] = d.kpiCode;
            ruleCodeNameMap[d.kpiCode] = d.kpiName;
        });
    };
    return{
        update: function (data) {
            update(data);
        },
        getData: function () {
            return basic;
        },
        getKpiName: function (code) {
            return ruleCodeNameMap[code];
        },
        getKpiCode: function (name) {
            return ruleNameCodeMap[name];
        }
    }
}();

function packageFields(data, param) {
    var fieldsConfig = JSON.parse(data.fields);
    var fields = fieldsConfig.columns;
    fieldsConfig['fact'] = data.tableDefineRowId;
    var _t = []; //条件字段
    var _t2 = []; //计算字段
    TableDefines.table_id_arr.push(data.tableDefineRowId);
    //todo 过滤不参与条件过滤的字段

    $.each(fields, function (i, item) {
        var _i = {'text': item.label, 'id': item.name, 'tid': data.tableDefineRowId};
        if (item.number)
            _i['type'] = 'number';

        TableDefines.fieldMap.update(data.tableDefineRowId, item.name, item.label);

        if ('mea' == item.dimension) {
            _t2.push(_i);
        } else if ('dim' == item.dimension) {
            _t.push(_i);
        }
    });
    if (_t.length > 0)
        TableDefines.fields[data.tableDefineRowId] = _t;
    if (_t2.length > 0) {
        TableDefines.tableField[data.tableDefineRowId] = _t2;
        if (!param) {
            TableDefines.field_data_map[data.tableDefineRowId] = _t2;
        } else {
            TableDefines.field_param_map[data.tableDefineRowId] = _t2;
        }
    }
    return {d: _t.length > 0, p: _t2.length > 0};
}

var _get = 2;
var $$tableLigerComboBox_is_ready = false;
function comboBox(id, cal) {
    if (--_get == 0) {
        if (cal)
            cal(TableDefines.fieldMap);
        $$tableLigerComboBox = $("#" + id).ligerComboBox(
            {
                data: TableDefines.table,
                valueField: 'tableDefineRowId',
                textField: 'tableName',
                selectBoxWidth: 560,
                width: 560,
                css: 'mTop2',
                cancelable: false,
                onBeforeOpen: beforeOpen,
                onBeforeSelect: function (v, t) {
                    if ($$tableLigerComboBox.getValue() == v)
                        return false;
                    if (beforeSelect) {
                        var r = beforeSelect(v, t);
                        if (!r) {
                            //todo tip can't select
                        }
                        return r;
                    }
                    return true;
                },
                onSelected: function (key) {
                    if (fieldCall)
                        fieldCall(TableDefines.fields[key]);
                },
                renderItem: function (e) {
                    var data = e.data;
                    var out = [];
                    out.push('<div>' + data.tableName + "-" + data.tableCode + '</div>');
                    out.push('<div class="desc">表说明:' + data.remark + '</div>');
                    return out.join('');
                }
            });
        $$tableLigerComboBox_is_ready = true;
    }
}


function executeOtherParam(table) {
    var baseForm = $('#baseSetForm');
    var fields = JSON.parse(table.fields);
    if (!fields[f_kpi_constants.OTHER_PARAM]) {
        baseForm.find('td[tname=' + f_kpi_constants.OTHER_PARAM + ']:eq(0)').attr('colspan', 3);
        baseForm.find('td[tname=' + f_kpi_constants.OTHER_PARAM + ']:not(:eq(0))').hide();
    } else {
        baseForm.find('td[tname=' + f_kpi_constants.OTHER_PARAM + ']:eq(1)').append(fields[f_kpi_constants.OTHER_PARAM].label);
        $global.hasOtherParam = true;
    }
}


function getTableDefine(id, cal, r) {
    TableDefines.fields = {};
    Q.ajax.get("config/table/" + getTableDefineData(), function (data) {
        TableDefines.data = data;
        var r = packageFields(data, false);
        if (r.d) {
            TableDefines.table.push(data);
            TableDefines.canUseTable.push(data.tableDefineRowId);
        }
        TableDefines.tableMap[data.tableDefineRowId] = data.tableName;
        TableDefines.tableMap2[data.tableName] = data.tableDefineRowId;
        executeOtherParam(data);
        comboBox(id, cal);
    });
    Q.ajax.get("config/table/" + getTableDefineData() + "/param", function (data) {
        TableDefines.params = data;
        $.each(data, function (i, table) {
            var r = packageFields(table, true);
            if (r.d) {
                TableDefines.table.push(table);
                TableDefines.canUseTable.push(table.tableDefineRowId);
            }
            TableDefines.tableMap[table.tableDefineRowId] = table.tableName;
            TableDefines.tableMap2[table.tableName] = table.tableDefineRowId;

        });
        comboBox(id, cal);
    });
    Q.ajax.get("config/kpi/exist/" + getGroupId() + "/" + r, function (data) {
        $$BasicRule.update(data);
    })
}

function dealOName(field, tid) {
    if (field)
        return "$p" + tid + "." + field;
    else return "";
}


//测试用
var getTableDefineData = function () {
    //return 173;
    return G_T_PARAM.t;
};
var getGroupId = function () {
    //return 167;
    return G_T_PARAM.g;
};

function setLatn(latnId) {
    $("#latnId").val(latnId);
    _ajaxGet("comm/bdw/" + latnId, function (data) {
        $("#latn").val(data);
    });
}

$(function () {
    var ruleRowId = Q.utils.getRequestParam("r");
    if (ruleRowId) {
        $('body').append("<script type='text/javascript' src='kpi_rule_update.js'></script>");
        Q.ajax.get('config/kpi/' + ruleRowId, function (data) {
            G_T_PARAM.ruleDefine = data.ruleKpiDefine;
            G_T_PARAM.t = data.ruleKpiDefine.ruleTableDefine.tableDefineRowId;
            if (data.ruleGroup.length > 0)
                G_T_PARAM.g = data.ruleGroup[0].ruleGroupRowId;
            getTableDefine('tableSelect', undefined, ruleRowId);
            var setIntervalId = setInterval(function () {
                if ($$tableLigerComboBox_is_ready) {
                    clearInterval(setIntervalId);
                    $$Edit_Kpi.renderHtml(G_T_PARAM.ruleDefine);
                    7
                }
            }, 500);
        })
    } else {
        G_T_PARAM.t = Q.utils.getRequestParam('t');
        G_T_PARAM.g = Q.utils.getRequestParam('g');
        getTableDefine('tableSelect', undefined, "0");
    }

    //
    $$RuleFilter = new RuleFilter();
    //
    /*_ajaxGet("comm/bdw", function (data) {
     $('#latn').ligerComboBox(
     {
     data: JSON.parse(data),
     css: 'input_normal',
     cancelable: false,
     onBeforeOpen: function(){
     return false;
     }
     }
     );
     liger.get("latn").selectValue(Q.utils.getRequestParam("latnId"));
     //$$LatnId.getLatnId('latn');
     });*/

    setLatn(Q.utils.getRequestParam("latnId"));


    $$ruleConfigContainer = $('#main_rule_group');
    $('#zh').bind('click', function (event) {
        if ($ruleContainer.kpiTypeCanChange()) {
            zh = this.checked;
            ruleInputCon();
        } else {
            event.preventDefault();
        }
    });
    $('#kpiCode').bind('input propertychange', function () {
        if (this.value != $.trim(this.value)) {
            this.value = $.trim(this.value)
        }
        $('#classId').val(this.value);
    }).bind('blur', function () {
        if (this.value.length < 1) {
            return;
        }
        _ajaxGet(pageConfig.folder + this.value + "/check/", function (data) {
            if (!data)
                $('#kpiCode').poshytip('show').poshytip('showDelayed', 2000);
        });
    }).bind('focus', function () {
        $('#kpiCode').poshytip('hide');
    }).poshytip({
        className: 'tip-green',
        content: '指标编码重复',
        showOn: 'none',
        alignTo: 'target',
        alignX: 'right',
        alignY: 'center',
        offsetX: 10,
        offsetY: 5
    });
});