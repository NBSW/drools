/**
 * Created by XuanLubin on 2014/3/24.
 */
var $Field_type = [
    {id: 'Number', text: '数字'},
    {id: 'String', text: '文本'},
    {id: 'Date', text: '日期'}
];


var poshytip_option = {
    className: 'tip-violet',
    showOn: 'none',
    alignX: 'inner-right',
    offsetY: 5,
    alignTo: 'target'
};

function column_beginEdit(rowid) {
    $$ModelDesignTable.beginEdit(rowid);
}
function column_endEdit(rowid) {
    $$ModelDesignTable.endEdit(rowid);
}

function column_deleteRow(rowid) {
    $.ligerDialog.confirm('确定删除该字段?', function (yes) {
        if (yes) {
            $$ModelDesignTable.deleteRow(rowid);
            $model_list.saveColumn(null, null, "字段已成功删除");
        }
    });
}
var $$ModelDesignTable;
$(function () {
    Q.view.load($('#model_left_list'), Base.getModulePath() + '/config/model/fragment/f_model_query.html', {isShowTitle: false, manage: false});
    Q.view.load($('#ModelDesign_field_box'), Base.getModulePath() + '/config/model/fragment/f_column.html', {isShowTitle: false, manage: false});
    Q.view.load($('#ModelDesign_add_box'), Base.getModulePath() + '/config/model/fragment/f_model.html', {isShowTitle: false, manage: false});

    $("#ModelDesign").ligerLayout({ leftWidth: 230, allowLeftCollapse: false, allowRightCollapse: false, allowBottomResize: false, allowLeftResize: false});
    var leftHeight = $(".l-layout-left").height() - $(".l-layout-header").outerHeight() - $(".ModelDesign_left_top").outerHeight();
    var rightHeight = $(".l-layout-center").height() - $(".l-layout-header").outerHeight();
    $(".ModelDesign_left_con").css('height', leftHeight);
    $(".l-layout-content").css('height', rightHeight);
    $(".select").ligerComboBox({
        width: 140
    });
    $(".select180").ligerComboBox({
        width: 180
    });
    $("#tree1").ligerTree({ checkbox: true });

    var dim_map = {
        "mea": "度量",
        "dim": "维度",
        "other": "模型字段"
    };

    var get_dim_define = function (e) {
        var $Dim_mea = [
            {id: "dim", text: "维度"}
        ];
        if (e.record.type == 'Number') {
            $Dim_mea.unshift({id: "mea", text: "度量"});
        }
        $Dim_mea.unshift({id: "other", text: "模型字段"});
        $Dim_mea.unshift({id: "", text: "其他"});
        return {data: $Dim_mea};
    };

    var change_dim_mea = function (e) {
        if (e.value == 'Number') {
            if (e.record.dimension == 'dim')
                $$ModelDesignTable.updateCell('dimension', 'mea', e.record);
            $$ModelDesignTable.updateCell('number', 'true', e.record);
        } else {
            $$ModelDesignTable.updateCell('number', 'false', e.record);
        }
    };

    $$ModelDesignTable = $("#ModelDesignTable").ligerGrid({
        columns: [
            { display: '字段编码', name: 'name', minWidth: 60, width: 200, align: 'left'},
            {
                display: '名称', name: 'label', minWidth: 60, width: 200, align: 'left',
                editor: {type: "text"}
            },
            {
                display: '数据类型', name: 'type', minWidth: 60, width: 100,
                editor: {type: 'select', cancelable: false, data: $Field_type, onChanged: change_dim_mea},
                render: function (row) {
                    if (row.type && "number" == row.type.toLowerCase()) {
                        return "数字";
                    }
                    if ("String" == row.type) {
                        return "文本";
                    }
                    return "日期";
                }},
            {
                display: '维度/度量', name: 'dimension', minWidth: 60, width: 100,
                editor: {type: 'select', cancelable: false, ext: get_dim_define},
                render: function (row) {
                    if (dim_map[row.dimension]) {
                        return dim_map[row.dimension];
                    }
                    return "其他";
                }},
            { display: '操作', name: 'operation', minWidth: 60, width: 120, render: function (rowdata, rowindex, value) {
                return " <a href='####' onclick='column_deleteRow(" + rowindex + ")'>删除</a>";
            }},
            {
                name: "number", hide: true
            }
        ],
        allowHideColumn: false,
        data: [],
        width: 'auto',
        height: 'auto',
        rownumbers: true,
        enabledEdit: true,
        onEndEdit: function () {
            //  $model_list.saveColumn();
        },
        usePager: false
    });


    $('#new_column').unbind().bind('click', function () {
        if ($column.newColumn()) {
            ModelDesign_field_open();
        }
    })

    $('#save_column').unbind().bind('click', function () {
        $model_list.saveColumn(null, null, "字段信息已成功更新");
    })
});