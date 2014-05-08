/**
 * Created by XuanLubin on 2014/3/24.
 */
var _ajaxGet = function (url, successFun, data) {
    Q.ajax.original('GET', url, data, successFun, null, false, 'JSON', 'JSON', true, null, null, null)
};

var _syncGet = function (url, successFun, data) {
    Q.ajax.original('GET', url, data, successFun, null, false, 'JSON', 'JSON', false, null, null, null)
};

var _ajaxPut = function (url, successFun, data) {
    Q.ajax.original('PUT', url, data, successFun, null, false, 'JSON', 'JSON', true, null, null, null)
};

var _syncPut = function (url, successFun, data) {
    Q.ajax.original('PUT', url, data, successFun, null, false, 'JSON', 'JSON', false, null, null, null)
};

var _ajaxPost = function (url, successFun, data) {
    Q.ajax.original('POST', url, data, successFun, null, false, 'JSON', 'JSON', true, null, null, null)
};

var _syncPost = function (url, successFun, data) {
    Q.ajax.original('POST', url, data, successFun, null, false, 'JSON', 'JSON', false, null, null, null)
};

var type_is_number = function (type) {
    if ("Number" == type) {
        return true;
    }
    return false;
};


var $$liger_tip;
var $$timer_liger_tip;
function liger_tip(content) {
    if (!$$liger_tip) {
        $$liger_tip = $.ligerDialog.tip({ title: '', content: content });
    } else {
        try {
            clearTimeout($$timer_liger_tip);
        } catch (e) {
        }
        $$liger_tip.set('content', content);
        var v = $$liger_tip.get('visible');
        if (!v)
            $$liger_tip.show();
    }
    $$timer_liger_tip = setTimeout(function () {
        $$liger_tip.hide();
    }, 1000);
}

function liger_warn(content, call) {
    var _w = $.ligerDialog.waitting(content);
    setTimeout(function () {
        _w.close();
        if (call) {
            call();
        }
    }, 1000);
}
function liger_warn_manual(content, call) {
    $.ligerDialog.warn(content);
}

function getOperator() {
    return "+-*/()";
}

var $$TabManager = function () {
    var instance = undefined;

    function f_addTab(tabid, text, url) {
        if (!instance && parent.MenuTabProcessInst) {
            instance = parent.MenuTabProcessInst;
        }
        if (instance) {
            //ligerUi 自身 tab
            /*if ($$TabManager.isTabItemExist(tabid)) {
             $$TabManager.selectTabItem(tabid);
             } else {
             $$TabManager.addTabItem({tabid: tabid, text: text, url: Q.config.webContext+url});
             }*/
            //新收服制tab
            instance.addTab({id: tabid, title: text, url: Q.config.webContext + url})
        } else {
            window.open(Q.config.webContext + url, text);
        }
    }

    function f_closeCurrentActiveTab() {
        if (!instance && parent.MenuTabProcessInst) {
            instance = parent.MenuTabProcessInst;
        }
        if (instance) {
            instance.closeCurrentActiveTab();
        } else {
            window.close();
        }
    }

    return{
        add: function (tabid, text, url) {
            f_addTab(tabid, text, url);
        },
        closeActive: function () {
            f_closeCurrentActiveTab();
        }
    }

}();
var $$LatnId = function () {

    var latnId = undefined;
    var latnLock = undefined;

    function checkLatnIdSync() {
        Q.ajax.getSync("comm/user/bdw", function (data) {
            latnId = data.body;
            latnLock = data.body != undefined;
        });
    }

    function checkLatnId(call) {
        Q.ajax.get("comm/user/bdw", function (data) {
            call(data);
            latnId = data;
            latnLock = data != undefined;
        });
    }

    function getLatnIdSync() {
        if (latnLock == undefined) {
            checkLatnIdSync();
        }
        return latnId;
    }

    function getLatnId(call) {
        if (latnLock) {
            call(latnId);
        } else {
            checkLatnId(call);
        }
    }

    function checkLockLatnIdSync() {
        if (latnLock == undefined) {
            checkLatnIdSync();
        }
        return latnLock;
    }


    return{
        getLatnId: function (comboBox) {
            if (comboBox) {
                getLatnId(function (data) {
                    liger.get(comboBox).selectValue(data);
                });
            } else {
                return getLatnIdSync();
            }
        },
        lockBdw: function () {
            return !checkLockLatnIdSync();
        }
    }
}();


var $formatDate = function (v, f) {
    if (!v) {
        return '';
    } else {
        if (!f) {
            f = 'yyyy-MM-dd HH:mm';
        }
        return $.format.date(new Date(v), f);
    }
};