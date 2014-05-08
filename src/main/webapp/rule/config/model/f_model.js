/**
 * Created by XuanLubin on 2014/3/25.
 */

var f_model_constants = {
    OTHER_PARAM : "otherParam"
};


var $model_data = {
    columns: [],
    syncFields: false,
    table_name_code_map: [],
    update_table_map: function (table) {
        if (this.table_name_code_map[table.tableDefineRowId]) {
            if (table.type == 1) {
                delete this.table_name_code_map[table.tableDefineRowId];
            } else {
                this.table_name_code_map[table.tableDefineRowId] = table;
            }
        }
    }
};

function dataBase_table(cal) {
    var dsCode = liger.get('ds2').getValue();
    var tableCode = $.trim($('#tableCode2').val());
    if (!dsCode) {
        $("#ds2").poshytip('show').poshytip('hideDelayed', 2000);
    } else if (tableCode.length < 1) {
        $("#tableCode2").poshytip("show").poshytip('hideDelayed', 2000);
    }
    if (dsCode && tableCode.length > 0) {
        if (typeof cal == 'function')
            cal(dsCode, tableCode);
        return true;
    }
    return false;
}

var $$data_model_rel_param;
var f_model = function () {

    $("#ds2").poshytip($.extend({content: '请选择数据源！'}, poshytip_option));
    $("#tableCode2").poshytip($.extend({content: '接口表不能为空！'}, poshytip_option));
    $("#dimField").poshytip($.extend({content: '数据模型必须选择结果维度字段！'}, poshytip_option));
    $("#syncColumn").poshytip($.extend({content: '请先同步字段!!！'}, poshytip_option)).unbind().bind('click', function () {
        dataBase_table(function (d, t) {
            Q.ajax.get("config/table/" + d + "/" + t, function (data) {
                $model_data.syncFields = true;
                $model_data.columns = data;
                $model.setComboBoxDate();
                $.ligerDialog.tip('字段已同步');
            });
        });
    });

    var $_comboBox_render = function (e) {
        if (!e.value || e.value.length < 1) {
            return  e.text;
        }
        return e.text + "[" + e.value + ']';
    };

    var fieldComboBox_options = {
        data: [],
        valueField: 'name',
        textField: 'label',
        cancelable: false,
        css: 'input_normal',
        selectBoxWidth: 184,
        renderItem: $_comboBox_render
    };

    $('#dateField').ligerComboBox(fieldComboBox_options);
    $('#dimField').ligerComboBox(fieldComboBox_options);
    $('#'+f_model_constants.OTHER_PARAM).ligerComboBox(fieldComboBox_options);

    $('input[type=radio][name=type]').unbind().bind('change', function () {
        if ('3' == $('input[type=radio][name=type]:checked').val()) {
            $('#data_model_rel_param').css("visibility", "hidden");
            $('#param_data_model').hide();
        } else {
            $('#data_model_rel_param').css("visibility", "visible");
            $('#param_data_model').show();
        }
    });


    function add_new_rel() {
        $$data_model_rel_param.addRow(null, null, null);
    }

    function delete_rel() {
        var rows = liger.get('data_model_rel_param').getSelectedRows();
        if (rows.length < 1) {
            liger_warn('请先选择要删除的关联');
        } else {
            $$data_model_rel_param.deleteSelectedRow();
        }
    }

    ModelDesign_add_open(true);
    var initParamTable = false;

    function get_param_tables() {
        var paramTables = [];
        if (!initParamTable)
            _syncPost('config/table/query', function (data) {
                paramTables = data;
                initParamTable = true;
                get_param_table_name(paramTables);
            }, {type: 3});
        return paramTables;
    }

    function get_param_table_name(paramTables) {
        $.each(paramTables, function (i, ii) {
            $model_data.table_name_code_map[ii.tableDefineRowId] = ii;
        });
    }

    function changeField() {
        $$data_model_rel_param.updateCell('fieldTo', '', arguments[0].record);
    }

    function getFieldData(e) {
        var rowId = e.record.tableDefineRowId;
        return {data: JSON.parse($model_data.table_name_code_map[rowId].fields).columns};
    }

    function getDataField() {
        if (!$model_data.syncFields) {
            dataBase_table(function (d, t) {
                _syncGet("config/table/" + d + "/" + t, function (data) {
                    $model_data.syncFields = true;
                    $model_data.columns = data;
                });
            });
        }
        return {data: $model_data.columns};
    }

    $$data_model_rel_param = $("#data_model_rel_param").ligerGrid({
        checkbox: true,
        columns: [
            {
                display: '关联表', name: 'tableDefineRowId', minWidth: 60, width: 208,
                editor: {type: 'select', data: get_param_tables(), cancelable: false, textField: 'tableName', valueField: 'tableDefineRowId', onChanged: changeField, renderItem: function (e) {
                    return e.data.tableName + "[" + e.data.tableCode + "]";
                }},
                render: function (rowdata, rowindex, value) {
                    if ($model_data.table_name_code_map[value])
                        return  $model_data.table_name_code_map[value].tableName;
                    return "";
                }
            },
            {
                display: '字段', name: 'fieldTo', minWidth: 60, width: 207,
                editor: {type: 'select', cancelable: false, textField: 'label', valueField: 'name', ext: getFieldData, renderItem: $_comboBox_render},
                render: function (rowdata, rowindex, value) {
                    var h = "";
                    if ($model_data.table_name_code_map[rowdata.tableDefineRowId]) {
                        $.each(JSON.parse($model_data.table_name_code_map[rowdata.tableDefineRowId].fields).columns, function (i, ii) {
                            if (ii.name == value) {
                                h = ii.label;
                                return false;
                            }
                        })
                    }
                    return h;
                }
            },
            {
                display: '关联', name: 'fieldForm', minWidth: 60, width: 207,
                editor: {type: 'select', cancelable: false, textField: 'label', valueField: 'name', ext: getDataField, renderItem: $_comboBox_render},
                render: function (rowdata, rowindex, value) {
                    var h = "";
                    $.each($model_data.columns, function (i, v) {
                        if (v.name == value) {
                            h = v.label;
                            return false;
                        }
                    });
                    return h;
                }
            }
        ],
        toolbar: {title: '计算清单关联', items: [
            { text: '添加关联', icon: 'add', click: add_new_rel},
            { text: '删除关联', icon: 'delete', click: delete_rel}
        ]},
        toolbarShowInLeft: false,
        allowHideColumn: false,
        enabledEdit: true,
        //clickToEdit: false,
        data: [],
        usePager: false
    });
};

var model_temp = {
    "tableDefineRowId": "",
    "tableName": "",
    "tableCode": "",
    "type": 1,
    "remark": "",
    "dsCode": "",
    "fields": "",
    "relField": "",
    "selectSql": "",
    "dimField": "",
    "dateField": "",
    "latnId": "",
    "commName": "",
    "createUserName": "",
    "createUserRowId": "",
    "createDate": ""
};

var $model = {
    latn: function (data) {
        $('#latn2').ligerComboBox(
            {
                data: data,
                css: 'input_normal',
                selectBoxWidth: 184,
                cancelable: false,
                onBeforeOpen: $$LatnId.lockBdw
            }
        );
        $$LatnId.getLatnId('latn2');
    },
    ds: function (data) {
        $('#ds2').ligerComboBox(
            {
                data: data,
                valueField: 'code',
                textField: 'name',
                cancelable: false,
                css: 'input_normal',
                selectBoxWidth: 184
            }
        );
    },
    save: function (successFun) {
        if (!dataBase_table()) {
            return;
        }
        var tableName = $.trim($('#tableName').val());
        if (tableName.length < 1) {
            $("#tableName").poshytip($.extend({content: '请填写模型名！'}, poshytip_option)).poshytip("show").poshytip('hideDelayed', 2000);
            return;
        } else {
            for (var i = 0; i < getOperator().length; i++) {
                if (tableName.indexOf(getOperator()[i]) > -1) {
                    $('#tableName').poshytip($.extend({content: "模型名请不要包括" + getOperator()}, poshytip_option)).poshytip("show").poshytip('hideDelayed', 2000);
                    return;
                }
            }
        }

        if (!$model_data.syncFields) {
            $('#syncColumn').poshytip("show").poshytip('hideDelayed', 2000);
            return;
        }
        var model = {
            "tableDefineRowId": $('#tableDefineRowId').val(),
            "tableName": tableName,
            "tableCode": $.trim($('#tableCode2').val()),
            "type": $('input[name=type][type=radio]:checked').val(),
            "remark": $.trim($('#remark').val()),
            "dsCode": liger.get('ds2').getValue(),
            "fields": {columns: $model_data.columns},
            "relField": "",
            "selectSql": $.trim($('#selectSql').val()),
            "dimField": liger.get('dimField').getValue(),
            "dateField": liger.get('dateField').getValue(),
            "latnId": liger.get('latn2').getValue(),
            "commName": liger.get('latn2').getText(),
            "createUserName": $("#createUserName").val(),
            "createUserRowId": $("#createUserRowId").val(),
            "createDate": $('#createDate').val()
        };
        if (1 == model.type) {
            //获取关联信息 并设置到 Model
            var rels = $$data_model_rel_param.getData();
            var $p = {};
            $.each(rels, function (i, ii) {
                $p[ii.fieldForm] = {fact: ii.tableDefineRowId, relTo: ii.fieldTo};
            });
            model.relField = JSON.stringify($p);
            if (!model.dimField || model.dimField.length < 1) {
                $('#dimField').poshytip("show").poshytip('hideDelayed', 2000);
                return;
            }
            var otherParam = liger.get(f_model_constants.OTHER_PARAM).getValue();
            $.each(model.fields.columns, function (i, c) {
                if (c.name == otherParam) {
                    model.fields[f_model_constants.OTHER_PARAM] = c;
                }
            });
        } else {
            delete model.fields[f_model_constants.OTHER_PARAM];
            model.dimField = "";
        }
        model.fields['isParam'] = model.type == 3;
        model.fields = JSON.stringify(model.fields);

        var doResult = function () {
            $model_list.saveModel(model, successFun);
        };

        if (model.selectSql.length < 1) {
            $.ligerDialog.confirm("还未填写数据获取SQL,自动生成？", function (y) {
                if (y) {
                    var sql = $('#selectSql');
                    sql.val("select [column]  from " + model.tableCode);
                    model.selectSql = sql.val();
                    doResult();
                }
            })
        } else {
            doResult();
        }
    },
    fillData: function (data, mode) {
        $('#tableDefineRowId').val(data.tableDefineRowId);
        liger.get('ds2').selectValue(data.dsCode);
        liger.get('latn2').selectValue(data.latnId);
        liger.get('dateField').selectValue(data.dateField);
        if (1 == data.type) {
            liger.get('dimField').selectValue(data.dimField);
            var otherParam = "";
            if (data.fields && data.fields.length > 0) {
                var _otherParam = JSON.parse(data.fields)[f_model_constants.OTHER_PARAM];
                if (_otherParam) {
                    otherParam = _otherParam.name;
                }
            }
            liger.get(f_model_constants.OTHER_PARAM).selectValue(otherParam);
        }
        $('#tableCode2').val(data.tableCode);
        $('#tableName').val(data.tableName);
        $('#selectSql').val(data.selectSql);
        $('#remark').val(data.remark);
        $('#createDate').val($.format.date(data.createDate ? new Date(parseFloat(data.createDate)) : new Date(), "yyyy-MM-dd"));
        $('input[name=type][value=' + data.type + ']').click();
        liger.get('data_model_rel_param').loadData({Rows: []});
        if ('UPDATE' == mode) {
            $("#createUserName").val(data.createUserName);
            $("#createUserRowId").val(data.createUserRowId);
        } else {
            Base.loadLoginData(function (data) {
                $("#createUserName").val(data.userName);
                $("#createUserRowId").val(data.PK);
            });
        }
        ModelDesign_add_open();
    },
    newModel: function () {
        $model_data.columns = [];
        $model_data.syncFields = false;
        this.setComboBoxDate();
        this.fillData(model_temp);
    },
    editModel: function (rowId) {
        var _that = this;
        Q.ajax.get("config/table/" + rowId, function (data) {
            if (data.fields) {
                $model_data.columns = JSON.parse(data.fields).columns;
                $model_data.syncFields = true;
            } else {
                $model_data.columns = [];
                $model_data.syncFields = false;
            }
            // todo ???
            _that.setComboBoxDate();

            _that.fillData(data, "UPDATE");
            if (data.type == 1) {
                var rel_field = JSON.parse(data.relField);
                _ajaxGet("config/table/" + data.tableDefineRowId + '/param', function (_data) {
                    var rows = [];
                    var _p_t = {};
                    $.each(_data, function (i, ii) {
                        _p_t[ii.tableDefineRowId] = ii;
                    });
                    $.each(rel_field, function (i, ii) {
                        if (_p_t[ii.fact]) {
                            rows.push({tableDefineRowId: ii.fact, fieldTo: ii.relTo, fieldForm: i});
                        }
                    });
                    $$data_model_rel_param.loadData({Rows: rows});
                });
            }
        });
    },
    setComboBoxDate: function () {
        var columns = $model_data.columns;
        var col_sep = $column.separateColumns(columns);
        var empty = {name: "", label: "--请选择--"};
        var d1 = col_sep.date.slice(0);
        d1.unshift(empty);
        var d2 = col_sep.ot.slice(0);
        d2.unshift(empty);
        var d3 = columns.slice(0);
        d3.unshift(empty);
        liger.get('dateField').setData(d1);
        liger.get('dimField').setData(d3);
        liger.get(f_model_constants.OTHER_PARAM).setData(d3);
    }
};