(function ($) {
    $.fn.piece = function (options) {
        var defaults = {
            title: '',
            isShowTitle: true,
            type: null,
            contentId:null,
            isSearchSupport: false,
            isShowCheckBox: false,
            isMultiSelect: false,
            isSelectParentNode: false,
            idField: 'code',
            textField: 'name',
            pidField: 'pCode',
            columns: null,
            action: {
                c: {url: null, onPreClick: null, onPostClick: null, onClick: null, async: true},
                u: {url: null, onPreClick: null, onPostClick: null, onClick: null, async: true },
                d: {url: null, onPreClick: null, onPostClick: null, onClick: null, async: true },
                r: {url: null, onPreClick: null, onPostClick: null, onClick: null, async: true},
                l: {url: null, onPreLoad: null, onPostLoad: null, onLoad: null, async: false}
            },
            //[{name:'',onClick:null,icon:null}]
            customActions: null,
            onSelected: null
        };
        var opts = $.extend({}, defaults, options || {});
        var $this = $(this);
        var $contentObject;
        var currentTypeFunctions;
        var typeFunctions = {
            tree: {
                load: loadTreeType,
                create: createCommonType,
                update: updateCommonType,
                del: delCommonType,
                refresh: refreshTreeType,
                getSelected: getSelectedDataTreeType,
                setSelected: setSelectedDataTreeType,
                setSize: setSizeDataTreeType
            },
            list: {
                load: loadListType,
                create: createCommonType,
                update: updateCommonType,
                del: delCommonType,
                refresh: refreshListType,
                getSelected: getSelectedDataListType,
                setSelected: setSelectedDataListType,
                setSize: setSizeDataListType
            },
            table: {
                load: loadTableType,
                create: createCommonType,
                update: updateCommonType,
                del: delCommonType,
                refresh: refreshTableType,
                getSelected: getSelectedDataTableType,
                setSelected: setSelectedDataTableType,
                setSize: setSizeDataTableType
            },
            form: {
                load: loadFormType,
                create: createFormType,
                update: updateFormType,
                del: delFormType,
                refresh: null,
                getSelected: null,
                setSelected: null,
                setSize: setSizeFormType
            },
            custom: {
                load: null,
                create: null,
                update: null,
                del: null,
                refresh: null,
                getSelected: null,
                setSelected: null,
                setSize: null
            }
        };

        function initLayout() {
            $this.addClass('piece');
            if (0 == $this.children('.piece_title').length) {
                $this.prepend('<div class="piece_title" style="' + (false == opts.isShowTitle ? 'display:none;' : '') + '">' + opts.title + '</div>');
            }
            if (0 == $this.children('.piece_tools').length) {
                $this.children('.piece_title').after('<div class="piece_tools"></div>');
            }
            if (0 == $this.children('.piece_content').length) {
                $this.append('<div class="piece_content"></div>');
            }
            var tools = [];
            if (opts.action.c && (opts.action.c.url || opts.action.c.onClick)) {
                tools.push({text: '新增', click: create, icon: 'add'}, { line: true });
            }
            if (opts.action.u && (opts.action.u.url || opts.action.u.onClick)) {
                tools.push({text: '更新', click: update, icon: 'save'}, { line: true });
            }
            if (opts.action.d && (opts.action.d.url || opts.action.d.onClick)) {
                tools.push({text: '删除', click: del, icon: 'delete'}, { line: true });
            }
            if (opts.action.r && (opts.action.r.url || opts.action.r.onClick)) {
                tools.push({text: '刷新', click: refresh, icon: 'refresh'}, { line: true });
            }
            if (opts.customActions) {
                var item;
                for (var i in opts.customActions) {
                    item = {text: opts.customActions[i].name, click: opts.customActions[i].onClick};
                    if (opts.customActions[i].icon) {
                        item.icon = opts.customActions[i].icon;
                    }
                    tools.push(item, { line: true });
                }
            }
            if (tools.length > 0) {
                tools.pop();
            }
            $this.children(".piece_tools").ligerToolBar({ items: tools});
        }

        function setSize() {
            $this.children('.piece_title').css({'width': $this.innerWidth()-8 + 'px'});
            $this.children('.piece_tools').css({'width': $this.innerWidth() + 'px'});
            $this.children('.piece_content').css({'width': $this.innerWidth() + 'px'});
            var toolsRows = 1;
            var tmpHeight = null;
            $this.children('.piece_tools').children('div').each(function () {
                if (null == tmpHeight) {
                    tmpHeight = $(this).offset().top;
                } else if ($(this).offset().top - tmpHeight > 10) {
                    toolsRows++;
                    tmpHeight = $(this).offset().top;
                }
            });
            $this.children('.piece_tools').css({'height': 31 * toolsRows + 'px'});
            $this.children('.piece_content').css({'height': $this.innerHeight() - (opts.isShowTitle ? $this.children('.piece_title').outerHeight() : 0) - $this.children('.piece_tools').outerHeight() - 2 + 'px'});
        }

        function initType() {
            switch (opts.type) {
                case 'tree':
                    currentTypeFunctions = typeFunctions.tree;
                    break;
                case 'list':
                    currentTypeFunctions = typeFunctions.list;
                    break;
                case 'table':
                    currentTypeFunctions = typeFunctions.table;
                    break;
                case 'form':
                    currentTypeFunctions = typeFunctions.form;
                    break;
                case 'custom':
                    currentTypeFunctions = typeFunctions.custom;
                    break;
                default :
                    $.ligerDialog.warn('类型错误，可选: [ tree,table,list,form,custom ] 。');
            }
        }

        function load() {
            var $content = $this.children('.piece_content');
            if (undefined !== opts.action && undefined !== opts.action.l && opts.action.l.onLoad) {
                opts.action.l.onLoad($content);
            } else {
                var data = null
                if (undefined !== opts.action && undefined !== opts.action.l && opts.action.l.onPreLoad) {
                    data = opts.action.l.onPreLoad($content);
                }
                if (false !== data) {
                    if (currentTypeFunctions.load) {
                        if (undefined !== opts.action && undefined !== opts.action.l && undefined !== opts.action.l.url && null != opts.action.l.url) {
                            if (undefined == opts.action.l.async || opts.action.l.async) {
                                Q.ajax.get(opts.action.l.url, function (data) {
                                    $contentObject = currentTypeFunctions.load($content, data);
                                });
                            } else {
                                var data = Q.ajax.getSync(opts.action.l.url);
                                $contentObject = currentTypeFunctions.load($content, data);
                            }
                        } else {
                            $contentObject = currentTypeFunctions.load($content, null);
                        }
                    }
                    if (undefined !== opts.action && undefined !== opts.action.l && opts.action.l.onPostLoad) {
                        opts.action.l.onPostLoad(data);
                    }
                }
            }
        }

        function create() {
            if (opts.action.c.onClick) {
                opts.action.c.onClick();
            } else {
                var data = null
                if (opts.action.c.onPreClick) {
                    data = opts.action.c.onPreClick();
                }
                if (false !== data) {
                    currentTypeFunctions.create(data, function (result) {
                        if (opts.action.c.onPostClick) {
                            opts.action.c.onPostClick(result);
                        }
                    });
                }
            }
        }

        function update() {
            if (opts.action.u.onClick) {
                opts.action.u.onClick();
            } else {
                var data = null
                if (opts.action.u.onPreClick) {
                    data = opts.action.u.onPreClick();
                }
                if (false !== data) {
                    currentTypeFunctions.update(data, function (result) {
                        if (opts.action.u.onPostClick) {
                            opts.action.u.onPostClick(result);
                        }
                    });
                }
            }
        }

        function refresh() {
            if (opts.action.r.onClick) {
                opts.action.r.onClick();
            } else {
                var isDo = true
                if (opts.action.r.onPreClick) {
                    isDo = opts.action.r.onPreClick();
                }
                if (isDo) {
                    if (undefined == opts.action.r.async || opts.action.r.async) {
                        Q.ajax.get(opts.action.r.url, function (result) {
                            currentTypeFunctions.refresh(result);
                            if (opts.action.r.onPostClick) {
                                opts.action.r.onPostClick(result);
                            }
                        });
                    } else {
                        var result = Q.ajax.getSync(opts.action.r.url);
                        currentTypeFunctions.refresh(result);
                        if (opts.action.r.onPostClick) {
                            opts.action.r.onPostClick(result);
                        }
                    }
                }
            }
        }

        function del() {
            if (opts.action.d.onClick) {
                opts.action.d.onClick();
            } else {
                var isDo = true
                if (opts.action.d.onPreClick) {
                    isDo = opts.action.d.onPreClick();
                }
                if (isDo) {
                    currentTypeFunctions.del(function () {
                        if (opts.action.c.onPostClick) {
                            opts.action.c.onPostClick();
                        }
                    });

                }
            }
        }

        initLayout();
        setSize();
        initType();
        load();

        //============================Type Setting============================


        function loadTreeType($content, data) {
            return $content.ligerTree({
                id:null!=opts.contentId?opts.contentId:null,
                data: data,
                checkbox: opts.isShowCheckBox,
                idFieldName: opts.idField,
                textFieldName: opts.textField,
                parentIDFieldName: opts.pidField,
                nodeWidth: 175,     //TODO with config
                onSelect: function (obj) {
                    if (null != obj && null != obj.data && (opts.isSelectParentNode || undefined != obj.data[opts.pidField])) {
                        opts.onSelected && opts.onSelected();
                    }
                }
            });
        }

        function loadTableType($content, data) {
            Piece.table = Piece._table(opts);
            return $content.ligerGrid({
                    id: null != opts.contentId ? opts.contentId : null,
                    checkbox: opts.isShowCheckBox,
                    columns: opts.columns,
                    width: $content.innerWidth() - 2,
                    height: $content.innerHeight() - 2,
                    pkName: opts.idField,
                    usePager: false,//TODO 在自定义获取数据的情况下 分页参数Total无效。
                    pageSize: 20,
                    data: Piece.table.packageTreeData(data),
                    isChecked: function (rowdata) {
                        if (Piece.table.findCheckedItem(rowdata) == -1) {
                            return false;
                        }
                        return true;
                    },
                    onCheckRow: function (checked, data) {
                        if (checked) {
                            Piece.table.addCheckedItem(data);
                        }
                        else {
                            Piece.table.removeCheckedItem(data);
                        }
                    },
                    onCheckAllRow: function (checked) {
                        for (var rowid in this.records) {
                            if (checked) {
                                Piece.table.addCheckedItem(this.records[rowid]);
                            }
                            else {
                                Piece.table.removeCheckedItem(this.records[rowid]);
                            }
                        }
                    }
                }
            );
        }

        function loadListType($content, data) {
            return $content.ligerListBox({
                id: null != opts.contentId ? opts.contentId : null,
                isShowCheckBox: opts.isShowCheckBox,
                isMultiSelect: opts.isMultiSelect,
                valueField: opts.idField,
                textField: opts.textField,
                data: data,
                width: $content.innerWidth(),
                height: $content.innerHeight(),
                onSelected: function () {
                    opts.onSelected && opts.onSelected();
                }
            });
        }

        function loadFormType($content, data) {
            return  $content.find('form').QForm({data: data});
        }

        function refreshTreeType(data) {
            $contentObject.setData(_.clone(data));
            setSelectedDataTreeType(null);
        }

        function refreshTableType(data) {
            $contentObject.set({ data: Piece.table.packageTreeData(data) });
            setSelectedDataTableType(null);
            Piece.table.removeChecked();
        }

        function refreshListType(data) {
            $contentObject.setData(_.clone(data));
            setSelectedDataListType(null);
        }

        function getSelectedDataTreeType() {
            var selNodes = [];
            if (opts.isShowCheckBox) {
                var notes = $contentObject.getChecked();
                if (null != notes && 0 < notes.length) {
                    for (var i in notes) {
                        if (opts.isSelectParentNode || undefined != notes[i].data[opts.pidField]) {
                            selNodes.push(notes[i].data);
                        }
                    }
                }
            } else {
                if (null != $contentObject.getSelected() && (opts.isSelectParentNode || undefined != $contentObject.getSelected().data[opts.pidField])) {
                    selNodes.push($contentObject.getSelected().data);
                }
            }
            if (0 == selNodes.length) {
                return null;
            }
            if (null != selNodes && 0 < selNodes.length) {
                for (var i in selNodes) {
                    selNodes[i] = _.omit(selNodes[i], 'treedataindex', '__status')
                }
            }
            return selNodes;
        }

        function getSelectedDataTableType() {
            //TODO check select
            return Piece.table.getChecked();
        }

        function getSelectedDataListType() {
            var codes = $contentObject.getValue();
            if (null == codes || '' == $.trim(codes)) {
                return null;
            }
            var items = [];
            if (-1 != codes.indexOf(';')) {
                codes = codes.split(';');
                var node;
                for (var i in codes) {
                    node = $contentObject.getDataByValue(codes[i]);
                    if (null != node) {
                        items.push(node);
                    }
                }
            } else {
                var node = $contentObject.getDataByValue(codes);
                if (null != node) {
                    items.push(node);
                }
            }
            return items;
        }

        function setSelectedDataTreeType(codes) {
            if (null == codes) {
                codes = [];
            }
            $contentObject.selectNode(function (d) {
                return _.contains(codes, d[opts.idField]);
            });
        }

        function setSelectedDataTableType(codes) {
            //TODO 表格目前无需此方法
        }

        function setSelectedDataListType(codes) {
            $contentObject.setValue(null == codes ? null : codes.join(';'));
        }

        function setSizeDataTreeType($content) {
            //TODO
        }

        function setSizeDataListType($content) {
            //TODO
        }

        function setSizeDataTableType($content) {
            $contentObject.setWidth($content.innerWidth() - 2);
            $contentObject.setHeight($content.innerHeight() - 2);
        }

        function setSizeFormType($content) {
            $content.css({'width': $content.innerWidth(), 'height': $content.innerHeight()});
            $contentObject.reSize();
        }

        function createCommonType(data, postFun) {
            if (undefined == opts.action.c.async || opts.action.c.async) {
                Q.ajax.post(opts.action.c.url, data, function () {
                    refresh();
                    postFun(data);
                });
            } else {
                Q.ajax.postSync(opts.action.c.url, data);
                refresh();
                postFun(data);
            }
        }

        function updateCommonType(data, postFun) {
            var url = opts.action.u.url.replace(new RegExp('({pk})', 'ig'), data[opts.idField]);
            if (undefined == opts.action.u.async || opts.action.u.async) {
                Q.ajax.put(url, data, function () {
                    refresh();
                    postFun(data);
                });
            } else {
                Q.ajax.putSync(url, data);
                refresh();
                postFun(data);
            }
        }

        function delCommonType(postFun) {
            var items = $this.getSelectedItems();
            if (null != items && 0 < items.length) {
                var codes;
                var label;
                if (1 == items.length) {
                    codes = items[0][opts.idField];
                    label = '[ ' + items[0][opts.textField] + ' ]';
                } else {
                    codes = _.pluck(items, opts.idField).join(',');
                    label = '选择的项目';
                }
                $.ligerDialog.confirm('确认删除' + label + '？', function (yes) {
                    if (yes) {
                        var url = opts.action.d.url.replace(new RegExp('({pk})', 'ig'), codes);
                        if (undefined == opts.action.d.async || opts.action.d.async) {
                            Q.ajax.del(url, function () {
                                refresh();
                                postFun();
                            });
                        } else {
                            Q.ajax.delSync(url);
                            refresh();
                            postFun();
                        }
                    }
                });
            } else {
                $.ligerDialog.warn('[ ' + opts.title + ' ] 没有选中的项目！');
            }
        }

        function createFormType(data, postFun) {
            if ($contentObject.validation()) {
                if (undefined == opts.action.c.async || opts.action.c.async) {
                    Q.ajax.post(opts.action.c.url, _.extend($contentObject.toJSON(), data), function (result) {
                        postFun(result);
                    });
                } else {
                    var result = Q.ajax.postSync(opts.action.c.url, _.extend($contentObject.toJSON(), data));
                    postFun(result);
                }
            }
        }

        function updateFormType(data, postFun) {
            if ($contentObject.validation()) {
                if (undefined == opts.action.u.async || opts.action.u.async) {
                    Q.ajax.put(opts.action.u.url, _.extend($contentObject.toJSON(), data), function (result) {
                        postFun(result);
                    });
                } else {
                    var result=Q.ajax.putSync(opts.action.u.url, _.extend($contentObject.toJSON(), data));
                    postFun(result);
                }
            }
        }

        function delFormType(postFun) {
            $.ligerDialog.confirm('确认删除？', function (yes) {
                if (yes) {
                    if (undefined == opts.action.d.async || opts.action.d.async) {
                        Q.ajax.del(opts.action.d.url, function () {
                            postFun();
                        });
                    } else {
                        Q.ajax.delSync(opts.action.d.url);
                        postFun();
                    }
                }
            });
        }

        //============================Public Method============================

        $this.refresh = function () {
            refresh();
        }

        $this.getContent = function () {
            return $contentObject;
        }


        $this.getSelectedItems = function () {
            return currentTypeFunctions.getSelected();
        }

        $this.getSelectedItem = function (isShowDefaultPrompt) {
            var items = $this.getSelectedItems();
            if (!items || 0 == items.length) {
                if (undefined == isShowDefaultPrompt || isShowDefaultPrompt == true) {
                    $.ligerDialog.warn('[ ' + opts.title + ' ] 没有选中的项目！');
                }
                return null;
            } else if (1 != items.length) {
                if (undefined == isShowDefaultPrompt || isShowDefaultPrompt == true) {
                    $.ligerDialog.warn('[ ' + opts.title + ' ] 存在多个选中的项目！');
                }
                return null;
            }
            return items[0];
        }

        $this.getSelectedCodes = function () {
            var items = $this.getSelectedItems();
            if (null == items) {
                return null;
            }
            return _.pluck(items, opts.idField);
        }

        $this.getSelectedCode = function () {
            var item = $this.getSelectedItem();
            if (null == item) {
                return null;
            }
            return item[opts.idField];
        }

        $this.setSelectedCodes = function (codes) {
            if (!$.isArray(codes)) {
                codes = [codes];
            }
            currentTypeFunctions.setSelected(codes);
        }

        $this.reSize = function () {
            setSize();
            currentTypeFunctions.setSize($this.children('.piece_content'));
        }

        return $this;
    };
})(jQuery);

var Piece = {};
Piece.table = null;
Piece._table = function (opts) {

    var checkedItems = [];

    function findCheckedItem(item) {
        for (var i = 0; i < checkedItems.length; i++) {
            if (checkedItems[i][opts.idField] == item[[opts.idField]]) return i;
        }
        return -1;
    }

    function addCheckedItem(item) {
        if (findCheckedItem(item) == -1) {
            checkedItems.push(item);
        }
    }

    function removeCheckedItem(item) {
        var i = findCheckedItem(item);
        if (i == -1) {
            return;
        }
        checkedItems.splice(i, 1);
    }

    function getChecked() {
        return checkedItems;
    }

    function removeChecked(){
        checkedItems=[];
    }

    function packageTreeData(data) {
        var total;
        var rows;
        if (null == data) {
            total = 0;
            rows = [];
        } else if ($.isArray(data)) {
            total = data.length;
            rows = data;
        } else if (undefined != data.objects) {
            //PageDTO
            total = data.recordTotal;
            rows = data.objects;
        }
        return  {Rows: rows, Total: total};
    }

    return {
        getChecked: function () {
            return getChecked();
        },
        removeChecked: function () {
            removeChecked();
        },
        findCheckedItem: function (item) {
            return findCheckedItem(item);
        },
        addCheckedItem: function (item) {
            addCheckedItem(item);
        },
        removeCheckedItem: function (item) {
            removeCheckedItem(item);
        },
        packageTreeData:function(data){
            return packageTreeData(data);
        }
    };
};