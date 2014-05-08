/**
 * Created by XuanLubin on 2014/3/25.
 */
var $tableDefine = {};
var $modelQuery = {
    getSelected: function (warn) {
        var $a = $('#table_result_ul').find('>li>a.ModelDesign_hover');
        if ($a.length < 1) {
            //todo :修改
            if (!warn)
                $.ligerDialog.warn('请先选择数据模型');
        } else {
            return $a.closest('li').attr('rowid');
        }
    },
    selectRow: function (rowId) {
        if (rowId) {
            var preSelect = $('#table_result_ul>li[rowid=' + rowId + ']>a');
            if (preSelect.length > 0) {
                preSelect.addClass('ModelDesign_hover');
            } else {
                $model_list.clearGird();
            }
        }
    },
    getTableDefine: function () {
        if ($modelQuery.getSelected()) {
            return $tableDefine;
        }
    },
    renderColumn: function () {
        var rowId = $modelQuery.getSelected();
        if (rowId) {
            Q.ajax.get("config/table/" + rowId, function (data) {
                $tableDefine = data;
                if (!$tableDefine.fields) {
                    Q.ajax.get('config/table/' + data.dsCode + "/" + data.tableCode, function (data) {
                        $tableDefine.fields = data;
                    })
                } else {
                    $model_list.renderGrid($tableDefine.fields);
                }
            });
        }
    },
    searchTableDefine: function () {
        var typeBox = $('input[name=Mtypebox]:checked');
        var type = 0;
        $.each(typeBox, function (i, ii) {
            type += parseInt($(ii).val());
        });
        var param = {};
        try {
            param = {
                dsCode: liger.get('ds').getValue(),
                tableCode: $.trim($('#tableCode').val()),
                latnId: liger.get('latn').getValue(),
                type: type > 3 || type < 1 ? null : type
            }
        } catch (e) {

        }
        var cal = function (data) {
            var $ul = $('#table_result_ul');
            var rowId = $modelQuery.getSelected(true);
            $ul.html('');
            $.each(data, function (i, ii) {
                $ul.append('<li rowid="' + ii.tableDefineRowId + '"><a href="####">' + ii.tableName + '[' + ii.tableCode + ']</a><div><span type="edit">修改</span>|<span type="delete">删除</span></div></li>')
            });
            table_result_li_click();
            $modelQuery.selectRow(rowId);
        };
        _ajaxPost('config/table/query', cal, param);
    }
};

var f_model_query = function () {
    $('#tableCode').bind('change', function () {
        $modelQuery.searchTableDefine();
    });
    $('input[name=Mtypebox]').bind('change', function () {
        $modelQuery.searchTableDefine();
    });
    $('#new_model').bind('click', function () {
        $model.newModel();
        ModelDesign_add_open();
    });
    _ajaxGet("comm/bdw", function (data) {
        data.unshift({id: "", text: "--请选择--"});
        $model.latn(data);
        $('#latn').ligerComboBox(
            {
                data: data,
                cancelable: false,
                css: 'input_normal input_normal_e',
                selectBoxWidth: 140,
                onSelected: function () {
                    $modelQuery.searchTableDefine();
                },
                onBeforeOpen: $$LatnId.lockBdw
            }
        );
        $$LatnId.getLatnId('latn');
    });
    _ajaxGet("ds", function (data) {
        data.unshift({code: "", name: "--请选择--"});
        $model.ds(data);
        $('#ds').ligerComboBox(
            {
                data: data,
                valueField: 'code',
                textField: 'name',
                cancelable: false,
                css: 'input_normal input_normal_e',
                selectBoxWidth: 140,
                onSelected: function () {
                    $modelQuery.searchTableDefine();
                }
            }
        );
    });
    $modelQuery.searchTableDefine();
};
var table_result_li_click = function () {
    var $lis = $('#table_result_ul').find('>li');
    $lis.find(">a").bind('click', function () {
        var $this = $(this);
        if (!$this.hasClass('ModelDesign_hover')) {
            $model_list.column_edit_lock.switchAble(function () {
                $lis.find(">a").removeClass('ModelDesign_hover');
                $this.addClass("ModelDesign_hover");
                $modelQuery.renderColumn();
            });
        }
    });
    $lis.find("div>span").unbind().bind('click', function () {
        var $this = $(this);
        var rowId = $this.closest('li').attr('rowid');
        if ('edit' == $this.attr('type')) {
            $model.editModel(rowId);
        } else {
            $model_list.deleteModel(rowId);
        }
    });
    setTimeout("reHeight()", 500);
};
var reHeight = function () {
    var listHeight = $("#model_left_list").innerHeight();
    $('#model_left_list div.ModelDesign_left_con').css('height', listHeight - 124);
};