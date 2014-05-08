/**
 * Created by XuanLubin on 2014/3/25.
 */
var $tableDefine = {};
var $modelQuery = function () {
    var getSelected = function (warn) {
        var $a = $('#table_result_ul').find('>li.li_selected');
        if ($a.length < 1) {
            //todo :修改
            if (!warn)
                $.ligerDialog.warn('请先选择数据模型');
        } else {
            return $a.closest('li').attr('rowid');
        }
    };
    var selectRow = function (rowId) {
        if (rowId) {
            var preSelect = $('#table_result_ul>li[rowid=' + rowId + ']');
            if (preSelect.length > 0) {
                preSelect.addClass('li_selected');
            }
        }
    };
    var getTableDefine = function () {
        if ($modelQuery.getSelected()) {
            return $tableDefine;
        }
    };

    var latnId = undefined;

    var searchTableDefine = function () {
        var param = {};
        try {
            param = {
                dsCode: liger.get('ds').getValue(),
                tableCode: $.trim($('#tableCode').val())
            }
        } catch (e) {

        }
        param.type = 1;
        param.latnId = latnId;
        Q.view.log(param);
        var cal = function (data) {
            var $ul = $('#table_result_ul');
            var rowId = $modelQuery.getSelected(true);
            $ul.html('');
            $.each(data, function (i, ii) {
                $ul.append('<li rowid="' + ii.tableDefineRowId + '" class="model_li">' + ii.tableName + '[' + ii.tableCode + ']</li>')
            });
            table_result_li_click();
            $modelQuery.selectRow(rowId);
        };
        _ajaxPost('config/table/query', cal, param);
    };

    return{
        getSelected: function (warn) {
            return getSelected(warn);
        },
        selectRow: function (rowId) {
            selectRow(rowId);
        },
        getTableDefine: function () {
            return getTableDefine();
        },
        searchTableDefine: function () {
            searchTableDefine();
        },
        setLatnId: function (_latnId) {
            latnId = _latnId;
        }
    }
}();

var f_model_query_base = function () {
    $('#tableCode').bind('change', function () {
        $modelQuery.searchTableDefine();
    });
    $('input[name=Mtypebox]').bind('change', function () {
        $modelQuery.searchTableDefine();
    });

    _ajaxGet("ds", function (data) {
        data.unshift({code: "", name: "--请选择--"});
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
    var $lis = $('#table_result_ul>li');
    $lis.bind('click', function () {
        var $this = $(this);
        if (!$this.hasClass('li_selected')) {
            $lis.removeClass('li_selected');
            $this.addClass("li_selected");
        }
    });
    $('#table_result_ul>li:odd').addClass('model_li_odd');
    setTimeout("reHeight()", 500);
};
var reHeight = function () {
    var listHeight = $("#model_left_list").innerHeight();
    $('#model_left_list div.ModelDesign_left_con').css('height', listHeight - 124);
};