var Base = function () {

    var config = {
        isDevEnv: true,
        webContext: '/sfz',
        modelContext: '/sfz/rule',
        baseAjax: '/sfz/api',
        tokenName: '_token'
    };

    function _init() {
        Q.load(config);
        document.write('<link href="' + config.modelContext + '/base.css" rel="stylesheet" type="text/css"\/>');
        document.write('<link href="' + config.modelContext + '/plugin/piece/piece.css" rel="stylesheet" type="text/css"\/>');
        document.write('<link href="' + config.modelContext + '/plugin/ligerUI/skins/Aqua/css/ligerui-all.css" rel="stylesheet" type="text/css"\/>');
        document.write('<link href="' + config.modelContext + '/plugin/ligerUI/skins/Gray/css/grid.css" rel="stylesheet" type="text/css"\/>');
        document.write('<link href="' + config.modelContext + '/plugin/ligerUI/skins/ligerui-icons.css" rel="stylesheet" type="text/css"\/>');
        document.write('<link href="' + Q.config.webContext + '/qin/plugin/jquery.poshytip/tip-green/tip-green.css" rel="stylesheet" type="text/css"\/>');
        document.write('<link href="' + Q.config.webContext + '/qin/plugin/jquery.poshytip/tip-violet/tip-violet.css" rel="stylesheet" type="text/css"\/>');
        document.write('<script src="' + config.modelContext + '/data.js" type="text/javascript"><\/script>');
        document.write('<script src="' + config.modelContext + '/plugin/jquery-validation/jquery.validate.min.js" type="text/javascript"><\/script>');
        document.write('<script src="' + config.modelContext + '/plugin/jquery-validation/jquery.metadata.js" type="text/javascript"><\/script>');
        document.write('<script src="' + config.modelContext + '/plugin/jquery-validation/messages_cn.js" type="text/javascript"><\/script>');
        document.write('<script src="' + config.modelContext + '/plugin/ligerUI/js/ligerui.min.js" type="text/javascript"><\/script>');
        document.write('<script src="' + config.modelContext + '/plugin/ligerUI/js/plugins/ligerGrid.js" type="text/javascript"><\/script>');
        document.write('<script src="' + config.modelContext + '/plugin/ligerUI.ext.js" type="text/javascript"><\/script>');
        document.write('<script src="' + config.modelContext + '/plugin/piece/piece.js" type="text/javascript"><\/script>');
    }

    _init();

    function initLayout() {
        var $layout = $('#layout');
        if ($layout && 1 == $layout.length) {
            $layout.ligerLayout({
                leftWidth: 280,
                height: $('body').innerHeight(),
                onEndResize: function () {
                    //TODO
                }
            });
            $('#list_wrap,#config_wrap').css({'height': $('body').innerHeight() - 30 + 'px'});
        }
    }

    function regEvent() {
        $('body').on('mouseenter', '.l-link', function () {
            $(this).addClass("l-link-over");
        });
        $('body').on('mouseleave', '.l-link', function () {
            $(this).removeClass("l-link-over");
        });
    }

    function saveOrUpdateFormData(type, formId, data, url, height, width, successFun) {
        var $form = liger.get(formId);
        var ajaxFun;
        $form.setData(data);
        if (type.toUpperCase() === 'UPDATE') {
            ajaxFun = Q.ajax.put;
        } else {
            ajaxFun = Q.ajax.post;
        }
        var $oldContainer = $('#' + formId).parent();
        $.ligerDialog.open({  target: $('#' + formId), height: height, width: width, buttons: [
            { text: '确定', onclick: function (item, dialog) {
                if ($form.valid()) {
                    ajaxFun(url, $form.getData(), function () {
                        successFun && successFun();
                        removeFormError(formId);
                        $oldContainer.append($('#' + formId));
                        dialog.close();
                    });
                }
                else {
                    $form.showInvalid();
                }
            } },
            { text: '取消', onclick: function (item, dialog) {
                removeFormError(formId);
                $oldContainer.append($('#' + formId));
                dialog.close();
            } }
        ] });
    }

    function deleteFormData(pk, label, url, successFun) {
        if (null == pk || '' == pk) {
            $.ligerDialog.warn('请先选择一条记录。');
            return;
        }
        $.ligerDialog.confirm('确认删除[ ' + label + ' ]？', function (yes) {
            if (yes) {
                Q.ajax.del(url, function () {
                    successFun && successFun();
                });
            }
        });
    }

    function removeFormError(formId) {
        $('#' + formId).find('*').each(function () {
            $(this).removeClass("l-textarea-invalid");
            $(this).removeClass("l-text-invalid");
            $(this).removeAttr("title").ligerHideTip();
        });
    }

    $(function () {
        initLayout();
        regEvent();
    });

    /* function loadAuthInfo() {
     $('body').append('<div id="auth_config_wrap_button">权限设置</div>');
     $('body').append('<div id="auth_config_wrap" style="display: none;">' +
     '<div id="auth_config_control">关闭</div>' +
     '<div id="role_list"></div>' +
     '<div id="position_List" ></div>' +
     '</div>');
     Q.view.load($('#role_list'), Base.getModulePath() + '/common/fragment/f_role.html');
     Q.view.load($('#position_List'), Base.getModulePath() + '/common/fragment/f_position.html');
     $('body').on('click', '#auth_config_wrap_button', function () {
     $('#auth_config_wrap_button').hide();
     $('#auth_config_wrap').show();
     $Role.reSize();
     $Position.reSize();
     })
     $('body').on('click', '#auth_config_control', function () {
     $('#auth_config_wrap').hide();
     $('#auth_config_wrap_button').show();
     })
     }*/

    function loadLoginData(fun) {
        if (fun) {
            Q.ajax.get('qin/auth/loginInfo/', function (data) {
                if (data) {
                    fun(data);
                }
            });
        } else {
            return  Q.ajax.getSync('qin/auth/loginInfo/');
        }
    }

    function getFormatCode(code) {
        if (code != null) {
            var formatCodes = [];
            code = code + '';
            var codes = code.split(';');
            for (var i = 0; i < codes.length; i++) {
                if (!/^'.+'$/ig.test(codes[i])) {
                    formatCodes.push("'" + codes[i] + "'");
                } else {
                    formatCodes.push(codes[i]);
                }
            }
            return formatCodes.toString();
        }
    }

    function userBdwBelong(){
        return  Q.ajax.getSync('comm/user/bdw');
    }

    return {
        getConfig: function () {
            return config;
        },
        getModulePath: function () {
            return Q.config.webContext + '/rule';
        },
        saveFormData: function (formId, data, url, height, width, successFun) {
            saveOrUpdateFormData('ADD', formId, data, url, height, width, successFun);
        },
        updateFormData: function (formId, data, url, height, width, successFun) {
            saveOrUpdateFormData('UPDATE', formId, data, url, height, width, successFun);
        },
        deleteFormData: function (pk, label, url, successFun) {
            deleteFormData(pk, label, url, successFun);
        },
        loadAuthInfo: function () {
            //loadAuthInfo();
        },
        loadLoginData: function (fun) {
            return loadLoginData(fun);
        },
        getFormatCode: function (code) {
            return getFormatCode(code);
        },
        getUserBdw:function(){
            return userBdwBelong();
        }
    };
}();




