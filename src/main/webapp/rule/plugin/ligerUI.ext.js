function mergecell($gridid, columnid,parentMap) //参数:表格id和列索引关键字，根据表格具体值来动态合并单元格
{
    var map={};
    var rowspan = 1;
    var currentVal = "";
    var cellname = "";
    var prevCellName="";
    $("td[id$='|" + columnid + "']", $gridid).each(
        function (idx) {
            if (currentVal == $("div", this).text()&&(!parentMap||prevCellName==parentMap[idx]&&prevCellName!='')) {
                $(this).addClass("l-remove");
                rowspan++;
                $("td[id='" + cellname + "']", $gridid).attr("rowspan", rowspan.toString());
            }
            else {
                currentVal = $("div", this).text();
                prevColumnVal="";
                cellname = $(this).attr("id");//得到点击处的id
                rowspan = 1;
            }
            map[idx]=currentVal;
            prevCellName=parentMap&&parentMap[idx];
        });
    $(".l-remove").remove();
    return map;
}
$.extend($.ligerMethos.ComboBox,
    {
        selectAll: function () {
            var g = this, p = this.options;
            $("table .l-checkbox", g.selectBox).each(function () {
                $(this).addClass('l-checkbox-checked');
            });
            g.isSelectAll = true;
            g.selectValue(g.data[0][p.valueField]);
            g.inputText.val('-全部-');
        },
        initSelect: function (defaultValue) {
            var g = this, p = this.options;
            if (g.data && g.data.length) {
                if (g.data[0][p.textField] === '-全部-') {
                    if (p.isMultiSelect) {
                        var $allTr = g.selectBox.find('table tr').first();
                        $allTr.children('td').first().find('*').hide();
                        $allTr.children('td').eq(1).css('cursor', 'pointer').click(function () {
                            g.selectAll();
                        });
                        if (defaultValue) {
                            for (var i = 0; i < defaultValue.length; i++) {
                                for (var j = 0; j < g.data.length; j++) {
                                    if (g.data[j][p.valueField] == defaultValue[i]) {
                                        $("table .l-checkbox", g.selectBox).eq(j).addClass('l-checkbox-checked');
                                        break;
                                    }
                                }
                            }
                            g.selectValue(defaultValue.join(';'));
                        } else {
                            g.selectAll();
                        }
                    } else {
                        g.isSelectAll = true;
                        if (defaultValue) {
                            g.selectValue(defaultValue.join(';'));
                        } else {
                            g.selectValue(g.data[0][p.valueField]);
                            g.inputText.val('-全部-');
                        }
                    }
                } else {
                    if (p.isMultiSelect) {
                        if (defaultValue) {
                            for (var i = 0; i < defaultValue.length; i++) {
                                for (var j = 0; j < g.data.length; j++) {
                                    if (g.data[j][p.valueField] == defaultValue[i]) {
                                        $("table .l-checkbox", g.selectBox).eq(j).addClass('l-checkbox-checked');
                                        break;
                                    }
                                }
                            }
                        } else {
                            $("table .l-checkbox", g.selectBox).eq(0).addClass('l-checkbox-checked');
                        }
                    }
                    g.selectValue(defaultValue ? defaultValue.join(';') : g.data[0][p.valueField]);
                }
            }
        },
        selectAble: function () {
            if (this.isSelectAll) {
                this.isSelectAll = false;
                return false;
            }
            return true;
        }
    }
);