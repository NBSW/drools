/**
 * Created by XuanLubin on 14-3-13.
 */
var pageConfig = {
    folder: "config/kpi/"
};

function create() {
    saveOrUpdate();
}

function saveOrUpdateFormData(type, data, url, height, width, successFun) {
    var ajaxFun;
    if (type.toUpperCase() === 'UPDATE') {
        ajaxFun = Q.ajax.put;
    } else {
        ajaxFun = Q.ajax.post;
    }

    $.ligerDialog.open({content: "确认保存", height: height, width: width, buttons: [
        { text: '确定', onclick: function (item, dialog) {
            ajaxFun(url, data, function (rule) {
                successFun && successFun(rule);
                dialog.close();
            });
        } },
        { text: '取消', onclick: function (item, dialog) {
            dialog.close();
        } }
    ] });
}

function saveOrUpdate() {
    var data = {
        ruleKpiDefineRowId: $('#ruleKpiDefineRowId').val(),
        kpiCode: $('#kpiCode').val(),
        kpiName: $('#kpiName').val(),
        classCode: $('#classId').val(),
        remark: $.trim($('#remark').val()),
        type: $('#zh').is(":checked") ? 1 : 0,
        ruleTableDefine: {tableDefineRowId: getTableDefineData()},
        scriptData: "",
        scriptRule: "",
        latnId: $('#latnId').val(),
        commName: $('#latn').val(),
        ruleRemark: "",
        createUserName: $('#createUserName').val(),
        createUserRowId: $('#createUserRowId').val(),
        status: $('#status').val(),
        summary:$('#summary').is(":checked")
    };

    data[f_kpi_constants.OTHER_PARAM] = $.trim($('#'+f_kpi_constants.OTHER_PARAM).val());
    data.relKPIs = $Kpi_Rel_KPI.selected();
    var createDate = $('#createDate').val();
    if (createDate.length > 0) {
        data.createDate = createDate;
    } else {
        data.createDate = new Date();
    }
    var isSave = false;
    if (data.ruleKpiDefineRowId.length < 1) {
        data.ruleKpiDefineRowId = null;
        data.createUserName = AuthUser.userName;
        data.createUserRowId = AuthUser.PK;
        isSave = true;
    }
    var scriptData = {};
    scriptData['ruleArr'] = $ruleContainer.getRule();
    scriptData['tableModel'] = TableDefines.table_id_arr;
    data.scriptData = JSON.stringify(scriptData);
    var isNewRule = data.ruleKpiDefineRowId == undefined;
    var funAfterSuccess = function (rule) {
        $.ligerDialog.confirm("保存成功,关闭窗口?", function (y) {
            if (y) {
                $$TabManager.closeActive();
            }
            if(isNewRule){
                window.location.replace(Base.getModulePath()+"/config/kpi/kpi_list.html?r=" +rule.ruleKpiDefineRowId+"&latnId="+rule.latnId)
            }
        });
    };
    if (isSave) {
        saveOrUpdateFormData("POST", data, pageConfig.folder + "group/" + getGroupId(), 120, 400, funAfterSuccess);
    } else {
        saveOrUpdateFormData("UPDATE", data, pageConfig.folder + data.ruleKpiDefineRowId, 120, 400, funAfterSuccess);
    }
}

var AuthUser = {};

$(function () {
    //获取登入人信息
    AuthUser = Base.loadLoginData();
});