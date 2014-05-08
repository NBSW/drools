/**
 * Created by XuanLubin on 2014/3/25.
 */

var $model_list = {
    column_edit_lock: {
        switchAble: function (successFun) {
            var d;//= $$ModelDesignTable.getDeleted(); // todo 暂时有问题
            var u = $$ModelDesignTable.getUpdated();
            if ((d && d.length > 0) || (u && u.length > 0)) {
                $.ligerDialog.confirm("有字段更新还未保存,是否保存?", function (yes) {
                    if (yes) {
                        $model_list.saveColumn(null, successFun, "字段信息已成功更新");
                    } else {
                        successFun()
                    }
                });
                return false;
            }
            successFun();
            return true;
        }
    },
    renderGrid: function (data) {
        if (typeof data == 'string') {
            data = JSON.parse(data);
        }
        $$ModelDesignTable.loadData({Rows: data.columns});
    },
    clearGird: function () {
        $$ModelDesignTable.loadData({Rows: []});
    },
    saveColumn: function (column, callback, content) {
        var rowId = $modelQuery.getSelected(true);
        if (rowId) {
            var oColumns = $$ModelDesignTable.getData().slice(0);
            if (column)
                oColumns.push(column);
            var cal = function (data) {
                $model_list.renderGrid(data);
            };
            if (callback)
                cal = callback;
            _ajaxGet("config/table/" + rowId, function (table) {
                if (table.fields) {
                    var fields = JSON.parse(table.fields);
                    fields.columns = oColumns;
                    table.fields = JSON.stringify(fields);
                } else {
                    table.fields = JSON.stringify({columns: oColumns, isParam: table.type == 3});
                }
                _ajaxPut("config/table/" + table.tableDefineRowId, function (data) {
                    liger_warn(!content ? "操作成功" : content, function () {
                        cal(data.fields);
                    });
                }, table);
            });
        }
    },
    update: function (data) {
        $model_data.update_table_map(data);
        $modelQuery.searchTableDefine();
    },
    saveModel: function (table, successFun) {
        if (typeof table.fields == "object") {
            table.fields = JSON.stringify(table.fields);
        }
        var _that = this;
        if (table.tableDefineRowId) {
            Q.ajax.put("config/table/" + table.tableDefineRowId, table, function (data) {
                successFun();
                _that.update(data);
            });
        } else {
            Q.ajax.post("config/table/", table, function (data) {
                successFun();
                _that.update(data);
            });
        }
    },
    deleteModel: function (rowId) {
        $.ligerDialog.confirm("确定删除该考核主体?", function (y) {
            if (y) {
                Q.ajax.del("config/table/" + rowId, function (data) {
                    $modelQuery.searchTableDefine();
                });
            }
        });
    }
};