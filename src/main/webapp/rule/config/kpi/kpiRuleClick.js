/**
 * Created by XuanLubin on 14-3-19.
 */
var $ligerDialog;
var $$tr;

function term_open(tr) {
    var dataForComboBox = beforeOpenDialog();
    $$tr = tr;
    if (dataForComboBox.length == 0) {
        table$$RuleFilter();
        liger_warn("无可用数据模型");
        return;
    } else {
        $$tableLigerComboBox.setData(dataForComboBox);
    }
    if ($ligerDialog) {
        $ligerDialog.show()
    } else {
        $ligerDialog = $.ligerDialog.open({
            title: "添加条件",
            width: 700,
            height: 430,
            target: $("#term_box"),
            buttons: [
                { text: '保存', onclick: function (item, dialog) {
                    if (saveCondition())
                        dialog.hidden();
                } },
                { text: '取消', onclick: function (item, dialog) {
                    table$$RuleFilter();
                    dialog.hidden();
                } }
            ]
        });
        $('table.l-dialog-table div.l-dialog-close').bind('click', function () {
            table$$RuleFilter();
        });
    }
}

//处理已打开的规则编辑table>tr
function table$$RuleFilter() {
    if (!$$tr) {
        return;
    }
    if (!$$RuleFilter.validate()) {
        if (undefined == $$RuleFilter.showDesc() || $$RuleFilter.showDesc().length < 1) {
            delCondition($$tr.find('td'));
        }
    } else {
        $$tr.attr('valid', 'true');
    }
}

function saveCondition() {
    if (!$$RuleFilter.save())
        return false;
    var cid = $$RuleFilter.getCid();
    var tr = $('table.rule_table_condition tr[rid=' + cid + ']');
    tr.find('td:eq(2)').children().show();
    tr.find('td:eq(1)').html($$RuleFilter.showDesc());
    tr.attr('valid', 'true');
    var table = tr.closest('table');
    table.find('tr').find('td:eq(0)').html('');
    table.find('tr:first').find('td:eq(0)').html('如果：');
    table.find('tr:last').show();
    return true;
}

function newCondition(e) {
    var tr;
    var rid;
    var openDialog = true;
    if(typeof e == 'object'){
        tr = $(e).closest('tr');
        rid = tr.attr('rid');
    }else{
       // rid = e;
        openDialog = false;
        tr= $('li[li_rid='+e+']>table tr:last');
    }
    if (!rid) {
        rid = $$RuleFilter.newCondition();
        tr.attr('rid', rid);
        tr.find('td:eq(1)').html('');
        tr.find('td:eq(2)').children().show();
        CONSTANT.tr.clone(true).insertAfter(tr);
        $.each(TableDefines.canUseTable, function (i, item) {
            if ($$RuleFilter.table().indexOf(item) < 0) {
                liger.get('tableSelect').selectValue(item);
                return false;
            } else if (TableDefines.canUseTable.length == i + 1) {
                liger.get('tableSelect').selectValue('');
            }
        });
        if(openDialog) {
            term_open(tr);
        }else{
            return tr;
        }
    }

}
function editCondition(td) {
    var tr = $(td).closest('tr');
    tr.removeAttr('valid');
    var rid = tr.attr('rid');
    if (rid != $$RuleFilter.getCid())
        $$RuleFilter.switchCondition(rid);
    term_open(tr);
}
function delCondition(td) {
    var tr = $(td).closest('tr');
    var table = tr.closest('table');
    var rid = tr.attr('rid');
    $$RuleFilter.delCon(rid);
    tr.remove();
    table.find('tr').find('td:eq(0)').html('');
    table.find('tr:first').find('td:eq(0)').html('如果：');
    table.find('tr:last').show();
}