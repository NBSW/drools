/**
 * Created by XuanLubin on 2014/3/28.
 */
$(function () {
    Q.view.load($('#group_list_container'), Base.getModulePath() + '/config/group/fragment/f_group_list.html', {isShowTitle: true, manage: true});
    Q.view.load($('#cron_field'), Base.getModulePath() + '/common/html/fragment/f_cron_script.html');
    Q.view.load($('#model_container'), Base.getModulePath() + '/config/model/fragment/f_model_query_base.html');
    $("#group_layout").ligerLayout({
        leftWidth: 180,
        height: 500
    });

    var id = setInterval(function () {
        if ($Group_list) {
            $Group_list.setClickCal($$Group_form.setFormData);
            clearInterval(id);
        }
    }, 1000);

    var id2 = setInterval(function () {
        if ($$Cron_expression) {
            $$Group_form.initForm();
            //$$Cron_expression.setChangeCal($$Group_form.cronChangeCall);
            clearInterval(id2);
        }
    }, 1000);

    Base.loadLoginData(function (data) {
        $$Group_form.setAuthUser(data);
    });

    $$Kpi_Grid.init('group_kpi_grid');

    $$Model_Dialog.setCommit(function (tid) {
        var gid = $Group_list.getSelected();
        $$Kpi_Grid.create(tid, gid);
    })
});

var $$Group_form = function () {
    var _authUser = undefined;
    var _form = undefined;

    var createMode = true;

    var bdw = [];
    _syncGet("comm/bdw", function (data) {
        bdw = data;
    });
    //int DAY = 1, WEEK = 2, MONTH = 3, SEASON = 4, YEAR = 5;
    var cycle = [
        { id: 1, text: '天'},
        { id: 2, text: '周'},
        { id: 3, text: '月'}/*,
         { id: 4, text: '季度'},
         { id: 5, text: '年'}*/
    ];

    //visible field
    var visibleFields = [
        { display: "活动编码", labelAlign: 'right', name: "taskCode", newline: true, type: "text", validate: {required: true}, group: "基础信息"},
        { display: "活动名称", labelAlign: 'right', name: "taskName", newline: false, type: "text", validate: {required: true}},
        { display: "本地网", labelAlign: 'right', name: "latnId", newline: false, type: "select", validate: {required: true}, comboboxName: "latnIdSelect", options: {data: bdw, cancelable: false, onBeforeOpen: $$LatnId.lockBdw}, width: 170 },
        { display: "周期", labelAlign: 'right', name: "cycle", newline: true, type: "radiolist", options: {data: cycle, rowSize: 10, value: 1}},
        { display: "执行时间", labelAlign: 'right', name: "createCron", newline: false, type: "text", options: {readonly: true}}
    ];

    //hidden field
    var hiddenFields = [
        { name: "status", type: "hidden" },
        { name: "createUserRowId", type: "hidden" },
        { name: "commId", type: "hidden" },
        { name: "commName", type: "hidden" },
        { name: "ruleGroupRowId", type: "hidden" },
        { name: "createUserName", type: "hidden"},
        { name: "createDate", type: "hidden"}
    ];

    var initForm = function () {

        var changeCycle = function (cycle) {
            $$Cron_expression.changeView(cycle);
        };

        var form = $("#group_form");

        var form_option = {
            inputWidth: 150, labelWidth: 70, space: 40,
            fields: visibleFields.concat(hiddenFields),
            validate: {},
            buttons: [
                /*
                 {text: "归档", click: function () {

                 }}*/
                {text: "保存", click: function () {
                    if (validForm()) {
                        if (!createMode) {
                            var group = getFormData();
                            Q.ajax.put('config/group/' + group.ruleGroupRowId, group, function (data) {
                                $Group_list.updateList(data.ruleGroupRowId);
                            })
                        } else {
                            form.find("input[name=createUserName]").val(_authUser.userName);
                            form.find("input[name=createUserRowId]").val(_authUser.PK);
                            form.find("input[name=status]").val(1);
                            form.find("input[name=createDate]").val($.format.date(new Date(), 'yyyy-MM-dd HH:mm'));
                            form.find("input[name=commId]").val(_authUser.companyId);
                            form.find("input[name=commName]").val(_authUser.companyName);
                            Q.ajax.post('config/group', getFormData(true), function (data) {
                                $Group_list.updateList(data.ruleGroupRowId);
                            });
                        }
                    }
                }}
            ]
        };

        if($view_mode){
            form_option.buttons = [];
        }


        _form = form.ligerForm(form_option);
        $$LatnId.getLatnId('latnIdSelect');

        var createCron = form.find("input[name=createCron]");
        createCron.closest('li').hide();
        /*createCron.addClass('pointer').bind('click', function () {
         $('#cron_field').show();
         });*/
        form.find('input[name=cycle]').bind('click', function () {
            changeCycle($(this).val());
        });
        form.find('ul.l-form-buttons').addClass('form_button_custom');
        $('#cron_field').appendTo(createCron.closest('li.l-fieldcontainer').parent()).show();
        $$Cron_expression.setSingle();

        var poshytip_option = {
            className: 'tip-violet',
            showOn: 'none',
            alignX: 'inner-right',
            offsetY: 5,
            alignTo: 'target'
        };

        $('input[ligeruiid=taskCode]').poshytip($.extend({content: '考核编码至少5位！'}, poshytip_option));
        $('input[ligeruiid=taskName]').poshytip($.extend({content: '考核名称不能为空！'}, poshytip_option));
        $('input[ligeruiid=latnIdSelect]').poshytip($.extend({content: '本地网必选！'}, poshytip_option));
    };
    var cronChangeCall = function (exp) {
        $("#group_form").find('input[name=createCron]').val(exp);
    };

    var setFormData = function (data) {
        if (!isNaN(data.createDate)) {
            data.createDate = $.format.date(data.createDate ? new Date(parseFloat(data.createDate)) : new Date(), "yyyy-MM-dd HH:mm");
        }
        _form.setData(data);
        createMode = false;
        var form = $("#group_form");
        //why LigerUI Form RadioList setValue Bug
        form.find("input:radio[name='cycle']").each(function () {
            this.checked = this.value == data.cycle;
        });
        form.find('input[name=cycle]:checked').click();
        $$Cron_expression.setExp(data.createCron);
        $$Kpi_Grid.loadData(data.ruleGroupRowId);
        $$Kpi_Grid.groupId = data.ruleGroupRowId;
    };

    var getFormData = function (create) {
        //CronExpression 处理
        cronChangeCall($$Cron_expression.getExp());
        var data = _form.getData();
        if (create) {
            data.ruleGroupRowId = undefined;
        }
        if (data.createDate) {
            try {
                data.createDate = new Date(data.createDate);
            } catch (e) {
                data.createDate = new Date();
            }
        } else {
            data.createDate = new Date();
        }
        return data;
    };

    var validForm = function () {
        var data = _form.getData();
        if ($.trim(data.taskCode).length < 5) {
            $('input[ligeruiid=taskCode]').poshytip("show").poshytip('hideDelayed', 2000);
            return false;
        } else if ($.trim(data.taskName).length < 1) {
            $('input[ligeruiid=taskName]').poshytip("show").poshytip('hideDelayed', 2000);
            return false;
        } else if (data.latnId.length < 1) {
            $('input[ligeruiid=latnIdSelect]').poshytip("show").poshytip('hideDelayed', 2000);
            return false;
        }
        return true;
    };

    var enableEditMode = function (editAble) {
        var method = ".setDisabled()";
        if (editAble) {
            method = ".setEnabled()";
        }
        $.each(visibleFields, function (i, field) {
            eval('liger.get("' + field.name + '")' + method);
        });
    };

    $("#create_group_new").unbind().bind('click', function () {
        Q.view.log(_form.getData());
        createMode = true;
        $('input[ligeruiid=taskCode]').val('');
        $('input[ligeruiid=taskName]').val('');
        $("#group_form").find('input[name=cycle]:eq(0)').click();
        return event.preventDefault();
    });

    return{
        initForm: function () {
            initForm();
        },
        setFormData: function (data) {
            setFormData(data);
        },
        cronChangeCall: function (exp) {
            cronChangeCall(exp);
        },
        setAuthUser: function (user) {
            _authUser = user;
        },
        enablEdit: function () {
            enableEditMode(true);
        },
        disableEdit: function () {
            enableEditMode(false);
        },
        latnId: function () {
            return liger.get('latnIdSelect').getValue();
        }
    }
}();