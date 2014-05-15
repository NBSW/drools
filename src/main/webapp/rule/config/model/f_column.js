/**
 * Created by XuanLubin on 2014/3/25.
 */
var f_column = function(){
    $("#column_code").poshytip($.extend({content: '请填写字段编码！'}, poshytip_option));
    $("#column_name").poshytip($.extend({content: '请字段中文名！'}, poshytip_option));
    $("#column_type").poshytip($.extend({content: '请先选择字段类型！'}, poshytip_option));
};
var $column = {
    setBase: function (table) {
        var $table = $('#f_column_table');
        $table.find('tr:eq(0)>td:eq(1)').html(table.tableName);
        $table.find('tr:eq(1)>td:eq(1)').html(table.tableCode);
        _ajaxGet("comm/bdw/" + table.latnId, function (data) {
            $table.find('tr:eq(2)>td:eq(1)').html(data);
        });
        $('#column_code').val(''),
        $('#column_name').val(''),
        $('#column_type').val(''),
        $('input[name=dm][type=radio]:eq(0)').click();
        $("#number").val('')
    },
    newColumn: function () {
        var tableDefine = $modelQuery.getTableDefine();
        if (tableDefine) {
            $column.setBase(tableDefine)
            return true;
        }
    },
    getData: function () {
         var data = {
             name: $.trim($('#column_code').val().toUpperCase()),
             label: $.trim($('#column_name').val()),
             type:$('#column_type').val(),
             dimension:$('input[name=dm][type=radio]:checked').val(),
             number:$("#number").val()
         };
        if(data.name.length<1){
            $("#column_code").poshytip('show').poshytip('hideDelayed', 2000);
            return;
        }
        if(data.label.length<1){
            $("#column_name").poshytip('show').poshytip('hideDelayed', 2000);
            return;
        }
        if(data.type.length<1){
            $("#column_type").poshytip('show').poshytip('hideDelayed', 2000);
            return;
        }
        return data;
    },
    saveNewColumn:function(){
        var column = this.getData();
        if(column){
            $model_list.saveColumn(column,null,"字段已成功保存");
            return true;
        }
    },
    separateColumns: function (columns) {
        var col_sep = {date: [], ot: []};
        $.each(columns, function (i, c) {
            if (c.type && c.type.toLowerCase() != "number") {
                col_sep.date.push(c);
            } else {
                col_sep.ot.push(c);
            }
        });
        return col_sep;
    }
};

function type_change_handler(type) {
    $('#number').val(type_is_number(type.value));
}