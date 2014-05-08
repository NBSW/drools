/**
 * 行列转置处理
 */
var TableSwapProcessor = function () {

    /**
     * 表格转成二维数组<br/>
     */
    function table2Data($table, isRepetition) {
        //记录结果数据
        var data = [];
        //当前列索引
        var cColIdx;
        var colspan = 1;
        var rowspan = 1;
        $table.find('tr').has('td').each(function (rowIdx) {
            if ($(this).css('display') != 'none') {
                $(this).find('td').each(function () {
                    cColIdx = 0;
                    if (data[rowIdx] && data[rowIdx][cColIdx] != null) {
                        //当前循环的单元格已有数据，查找下一列直到找到没有数据的单元格
                        for (; cColIdx < data[rowIdx].length; cColIdx++) {
                            if (!data[rowIdx] || data[rowIdx][cColIdx] == null) {
                                break;
                            }
                        }
                    }
                    if (!data[rowIdx]) {
                        data[rowIdx] = [];
                    }
                    if ($(this).css('display') != 'none') {
                        colspan = $(this).attr('colspan');
                        rowspan = $(this).attr('rowspan');
                        if (colspan) {
                            //需要跨列
                            for (var i = 1; i < parseInt(colspan); i++) {
                                data[rowIdx][cColIdx + i] = isRepetition ? $(this).text() : '';
                            }
                        }
                        if (rowspan) {
                            //需要跨行
                            for (var i = 1; i < parseInt(rowspan); i++) {
                                if (!data[rowIdx + i]) {
                                    data[rowIdx + i] = [];
                                }
                                data[rowIdx + i][cColIdx] = isRepetition ? $(this).text() : '';
                            }
                        }
                        data[rowIdx][cColIdx] = $(this).text();
                    }
                });
            }
        });
        return data;
    }

    /**
     * 行列转置成二维数组<br/>
     */
    function swap2Data($table, isRepetition) {
        //记录结果数据
        var data = [];
        //当前行索引
        var cRowIdx;
        var colspan = 1;
        var rowspan = 1;
        $table.find('tr').has('td').each(function (colIdx) {
            $(this).find('td').each(function () {
                cRowIdx = 0;
                if (data[cRowIdx] && data[cRowIdx][colIdx] != null) {
                    //当前循环的单元格已有数据，查找下一行直到找到没有数据的单元格
                    for (; cRowIdx < data.length; cRowIdx++) {
                        if (!data[cRowIdx] || data[cRowIdx][colIdx] == null) {
                            break;
                        }
                    }
                }
                if (!data[cRowIdx]) {
                    data[cRowIdx] = [];
                }
                if ($(this).css('display') != 'none') {
                    colspan = $(this).attr('colspan');
                    rowspan = $(this).attr('rowspan');
                    if (colspan) {
                        //需要跨行（行列转置后原跨列变成跨行 ）
                        for (var i = 1; i < parseInt(colspan); i++) {
                            if (!data[cRowIdx + i]) {
                                data[cRowIdx + i] = [];
                            }
                            data[cRowIdx + i][colIdx] = isRepetition ? $(this).text() : '';
                        }
                    }
                    if (rowspan) {
                        //需要跨列
                        for (var i = 1; i < parseInt(rowspan); i++) {
                            if (!data[cRowIdx]) {
                                data[cRowIdx] = [];
                            }
                            data[cRowIdx][colIdx + i] = isRepetition ? $(this).text() : '';
                        }
                    }
                    data[cRowIdx][colIdx] = $(this).text();
                }
            });
        });
        return data;
    }

    /**
     * 行列转置成新的Table<br/>
     * 关键在于处理跨行跨列的单元格，此处使用二维矩阵标识以简化操作。
     */
    function swap2Table($table, tableClass) {
        //记录结果数据
        var swapTable = '<table class="swap_table ' + (tableClass ? tableClass : '' ) + '">';
        //记录所有的行数据
        var rows = [];
        //记录单元格数据
        var cell;
        //值二维矩阵，为true时表示此单元格已有数据
        var valMatrix = [];
        //当前行索引
        var cRowIdx;
        var isFirstRow = true;
        var hideRowIdxs = [];
        $table.find('tr').has('td').each(function (colIdx) {
            $(this).find('td').each(function (rowIdx) {
                cRowIdx = rowIdx;
                if (valMatrix[cRowIdx] && valMatrix[cRowIdx][colIdx]) {
                    //当前循环的单元格已有数据，查找下一行直到找到没有数据的单元格
                    for (; cRowIdx < valMatrix.length; cRowIdx++) {
                        if (!valMatrix[cRowIdx] || !valMatrix[cRowIdx][colIdx]) {
                            break;
                        }
                    }
                }
                if (!valMatrix[cRowIdx]) {
                    valMatrix[cRowIdx] = [];
                }
                //标识当前单元格已有数据(下面会填充数据到rows）
                valMatrix[cRowIdx][colIdx] = true;

                cell = '<td';
                if ($(this).attr('colspan')) {
                    //需要跨行（行列转置后原跨列变成跨行 ）
                    cell += ' rowspan="' + $(this).attr('colspan') + '" ';
                    for (var i = 1; i < parseInt($(this).attr('colspan')); i++) {
                        if (!valMatrix[cRowIdx + i]) {
                            valMatrix[cRowIdx + i] = [];
                        }
                        //标识所有跨行的单元格为有数据
                        valMatrix[cRowIdx + i][colIdx] = true;
                    }
                }
                if ($(this).attr('rowspan')) {
                    //需要跨列
                    cell += ' colspan="' + $(this).attr('rowspan') + '" ';
                    for (var i = 1; i < parseInt($(this).attr('rowspan')); i++) {
                        if (!valMatrix[cRowIdx]) {
                            valMatrix[cRowIdx] = [];
                        }
                        //标识所有跨列的单元格为有数据
                        valMatrix[cRowIdx][colIdx + i] = true;
                    }
                }
                cell += '>' + $(this).text();
                cell += '</td>';
                if (!rows[cRowIdx]) {
                    rows[cRowIdx] = '';
                }
                //把单元格赋值当前行
                rows[cRowIdx] += cell;
                if (isFirstRow) {
                    hideRowIdxs[cRowIdx] = $(this).css('display') == 'none';
                }
            });
            isFirstRow = false;
        });
        $.each(rows, function (k, v) {
            swapTable += '<tr ' + (hideRowIdxs[k] ? 'style="display:none;"' : '') + '>' + v + '</tr>';
        });
        swapTable += '</table>';
        return swapTable;
    }

    return{
        table2Data: function ($table) {
            return table2Data($table);
        },
        swap2Data: function ($table) {
            return swap2Data($table);
        },
        swap2Table: function ($table, tableClass) {
            return swap2Table($table, tableClass);
        }
    };
}