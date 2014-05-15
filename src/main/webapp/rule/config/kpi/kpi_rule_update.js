/**
 * Created by XuanLubin on 2014/3/31.
 */
var $$Edit_Kpi = {
    renderHtml: function (kpiDefine) {
        $$Edit_Kpi.setForm(kpiDefine);
        var scriptData = JSON.parse(kpiDefine.scriptData);
        var rule_button_div = $('#rule1');
        rule_button_div.css('visibility', 'hidden');
        $.each(scriptData, function (i, rule) {
            var ruleId = $ruleContainer.newRule();
            $ruleContainer.renderHtml(ruleId, rule);
            $$RuleFilter.ruleName(rule.ruleName);
            $.each(rule.whens, function (j, when) {
                var tr = newCondition(ruleId);
                //tr.find('td:eq(1)').html(rule.whens_str[j]);
                $$RuleFilter.renderHtml(when);
                saveCondition();
            });
            $ruleContainer.saveRule(ruleId);
        });
        rule_button_div.css('visibility', 'visible');
    },
    setForm: function (kpiDefine) {
        $('#ruleKpiDefineRowId').val(kpiDefine.ruleKpiDefineRowId);
        $('#kpiCode').val(kpiDefine.kpiCode);
        $('#kpiName').val(kpiDefine.kpiName);
        $('#classId').val(kpiDefine.classCode);
        $('#remark').val(kpiDefine.remark);
        if (kpiDefine.type == 1) {
            $('#zh').click();
        }
        $('#createUserName').val(kpiDefine.createUserName);
        $('#createUserRowId').val(kpiDefine.createUserRowId);
        $('#status').val(kpiDefine.status);
        if(kpiDefine[f_kpi_constants.OTHER_PARAM]){
            $('#'+f_kpi_constants.OTHER_PARAM).val(kpiDefine[f_kpi_constants.OTHER_PARAM]);
        }
        if(!kpiDefine.summary){
            $('#summary').click();
        }
    }
};