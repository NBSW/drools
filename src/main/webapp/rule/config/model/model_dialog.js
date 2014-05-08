/**
 * Created by XuanLubin on 2014/3/24.
 */
var modelDesign_add_ligerDialog;
function ModelDesign_add_open(hide) {
    if (modelDesign_add_ligerDialog) {
        modelDesign_add_ligerDialog.show();
        return;
    }
    modelDesign_add_ligerDialog = $.ligerDialog.open({
        title: "计算模型添加",
        width: 700,
        height: 500,
        target: $("#ModelDesign_add_box"),
        buttons: [
            { text: '保存', onclick: function (item, dialog) {
                $model.save(function(){
                    dialog.hidden();
                });
            } },
            { text: '取消', onclick: function (item, dialog) {
                dialog.hidden();
            } }
        ]
    });
    if (hide) {
        setTimeout("modelDesign_add_ligerDialog.hidden()", 500);
    }
}

var modelDesign_field_ligerDialog;
function ModelDesign_field_open() {
    if (modelDesign_field_ligerDialog) {
        modelDesign_field_ligerDialog.show();
        return;
    }
    modelDesign_field_ligerDialog = $.ligerDialog.open({
        title: "字段模型添加",
        width: 700,
        height: 320,
        target: $("#ModelDesign_field_box"),
        buttons: [
            { text: '保存', onclick: function (item, dialog) {
                if ($column.saveNewColumn())
                    dialog.hidden();
            } },
            { text: '取消', onclick: function (item, dialog) {
                dialog.hidden();
            } }
        ]
    });
}