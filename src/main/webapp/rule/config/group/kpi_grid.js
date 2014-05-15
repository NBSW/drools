/**
 * Created by XuanLubin on 2014/3/31.
 */
var $$Kpi_Grid = {
    grid: undefined,
    groupId: undefined,
    edit: function (id, ruleName) {
        $$TabManager.add('kpiEdit' + id, "计算公式[" + ruleName + "]", "/rule/config/kpi/kpi_list.html?r=" + id + "&latnId=" + $$Group_form.latnId());
        event.preventDefault();
    },
    create: function (tid, gid) {
        $$TabManager.add("createNewRuleDefine_" + tid + "_" + gid, "新建规则_" + tid + "_" + gid, "/rule/config/kpi/kpi_list.html?t=" + tid + "&g=" + gid + "&latnId=" + $$Group_form.latnId());
        event.preventDefault();
    },
    del: function (rid) {
        $.ligerDialog.confirm("确定删除该公式?", function (y) {
            if (y) {
                Q.ajax.del("config/kpi/" + rid, function () {
                    $$Kpi_Grid.loadData($$Kpi_Grid.groupId);
                });
            }
        });
        event.preventDefault();
    },
    init: function (gid) {
        var liger_option = {
            columns: [
                { display: '指标编码', name: 'kpiCode', align: 'left', width: 120 },
                { display: '指标名称', name: 'kpiName', align: 'left', minWidth: 60, render: function (row, t, val) {
                    return "<span title='" + row.ruleKpiDefineRowId + "'>" + val + "</span>"
                }},
                { display: '基础/组合', name: 'type', width: 80, render: function (row, t, val) {
                    var m = "";
                    if (val == 1) {
                        m = "组合";
                    } else if (val == 0) {
                        m = "基础";
                        if (row.summary) {
                            m += "[汇总]"
                        } else {
                            m += "[清单]";
                        }
                    }
                    return m;
                }},
                {display: '运行状态', name: 'status', render: function (i, j, k) {
                    if (!i.message)
                        i.message = '';
                    var html = "<span title='" + i.message + "'>";
                    if (1 == k) {
                        html += "正在运行";
                    } else if (9 == k) {
                        html += "完成";
                    } else if (9 == k) {
                        html += "发生异常";
                    }
                    return html + "</span>";
                }},
                {display: "账期", name: 'dateCd'},
                {display: '开始时间', width: 140, name: 'startTime', render: function (i, j, k) {
                    return $formatDate(k);
                }},
                {display: '结束时间', width: 140, name: 'endTime', render: function (i, j, k) {
                    return $formatDate(k);
                }},
                { display: '操作', width: 90, render: function (row) {
                    var html = '<a href="" onclick="$$Kpi_Grid.edit(' + row.ruleKpiDefineRowId + ',\'' + (row.kpiName + '-' + row.kpiCode) + '\')">修改</a>';
                    html += '&nbsp;|&nbsp;<a href="" onclick="$$Kpi_Grid.del(' + row.ruleKpiDefineRowId + ',' + gid + ')">删除</a>';
                    return html;
                }}
            ],
            toolbar: {title: '公式列表', items: [
                {text: '新增', icon: 'add', click: function () {
                    var gid = $Group_list.getSelected();
                    if (gid == undefined) {
                        liger_tip("请选择活动!");
                    } else {
                        $$Model_Dialog.open();
                    }
                }}
            ]},
            toolbarShowInLeft: false,
            pageSize: 100,
            height: 360,
            data: [],
            dataAction: 'server',
            onToPrev: function () {
                Q.view.log(arguments);
            },
            onToNext: function () {
                Q.view.log(arguments);
            },
            onToFirst: function () {
                Q.view.log(arguments);
            },
            onToLast: function () {
                Q.view.log(arguments);
            },
            onReload: function () {
                Q.view.log(arguments);
            }
        };
        if ($view_mode) {
            liger_option.toolbar = {};
            delete liger_option.columns.pop();
        }
        $$Kpi_Grid.grid = $("#" + gid).ligerGrid(liger_option);
    },
    loadData: function (gid) {
        if (gid) {
            _ajaxGet("config/group/" + gid + "/kpiList?pageNumber=0&pageSize=100", function (data) {
                data.Rows = data.objects;
                delete data.objects;
                $$Kpi_Grid.grid.loadData(data);
            }, {pageNumber: 1, pageSize: 100})
        }
    }
};