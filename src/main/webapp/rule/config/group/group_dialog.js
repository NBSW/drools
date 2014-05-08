/**
 * Created by XuanLubin on 2014/4/4.
 */
var $$Model_Dialog = function () {
    var $dialog = undefined;

    var commitFun = undefined;

    var cancelFun = undefined;

    var open = function () {
        $modelQuery.setLatnId($$Group_form.latnId());
        $modelQuery.searchTableDefine();
        if ($dialog) {
            $dialog.show();
            return;
        }
        $dialog = $.ligerDialog.open({
            title: "计算模型选择",
            width: 700,
            height: 500,
            target: $("#model_container"),
            buttons: [
                { text: '选择', onclick: function (item, dialog) {
                    var tid = $modelQuery.getSelected();
                    if (tid) {
                        commitFun(tid);
                    }
                } },
                { text: '取消', onclick: function (item, dialog) {
                    dialog.hidden();
                } }
            ]
        });
    };

    function hide() {
        if ($dialog) {
            $dialog.hide();
        }
    }

    return {
        setCommit:function(fun){
            commitFun = fun;
        },
        open: function () {
            open();
        },
        close: function () {
            hide();
        },
        setLatn:function(latnId){
            $modelQuery.setLatnId(latnId);
        }
    }
}();