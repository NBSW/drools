/**
 * Created by XuanLubin on 2014/5/11.
 */
var $Kpi_Rel_KPI = function () {
    var rel_kpi_list = undefined;
    var init = function () {
        rel_kpi_list = $('#rel_kpi_list').ligerComboBox(
            {
                data: [],
                valueField: 'kpiCode',
                textField: 'kpiName',
                cancelable: false,
                css: 'input_normal input_normal_e mItop',
                isMultiSelect: true,
                split:',',
                selectBoxWidth:180
            }
        );
        show('hidden');
    };

    var show = function (visibility) {
        $('#rel_kpi_list').closest('div.l-text-wrapper').css("visibility", visibility);
    };

    var setData = function (data) {
        rel_kpi_list.setData(data);
    };

    var freshRels = function (rid) {
        Q.ajax.get('/config/kpi/rel/' + rid, function (data) {
            $.each(data, function (i, d) {
                rel_kpi_list.selectValue(d);
            });
        })
    };

    init();

    return {
        setData: function (data, ruleDefine) {
            if (data) {
                setData(data);
            }
            if (ruleDefine) {
                freshRels(ruleDefine.ruleKpiDefineRowId);
            }
        },
        show: function () {
            show('visible');
        },
        hide: function () {
            show('hidden');
        },
        selected: function () {
            return rel_kpi_list.getValue();
        }
    }
}();