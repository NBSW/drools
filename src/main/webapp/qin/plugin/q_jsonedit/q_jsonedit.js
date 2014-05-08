var EditJson;

function regEvent() {
    $('#save').click(function () {
        var SaveCallbackFun = Q.utils.getRequestParam('callback');
        if (SaveCallbackFun) {
            var fun = eval('parent.' + SaveCallbackFun);
            fun(EditJson);
        } else {
            parent.EditJson = EditJson;
        }
        window.close();
    });
}

function regWidgetAction() {
    $('#edit_form').on('blur', '.number', function () {
        if (isNaN($(this).val())) {
            $(this).val('');
        }
    });
    $('#edit_form').on('change', '*[name]', function () {
        var $this = $(this);
        if (undefined !== $this.attr('type') && 'checkbox' === $this.attr('type')) {
            if ($this.prop('checked')) {
                changeValue($this.attr('name'), $this.val());
            } else if ($this.attr('data-unchecked')) {
                changeValue($this.attr('name'), $this.attr('data-unchecked'));
            } else {
                changeValue($this.attr('name'), null);
            }
        } else {
            changeValue($(this).attr('name'), $(this).val());
        }
    });
}

function changeValue(key, value) {
    EditJson[key] = value;
    rendering();
}

function getEditJson() {
//Support two ways to get edit json.
    EditJson = Q.utils.getRequestParam('json');
    if (EditJson) {
        EditJson = $.parseJSON(decodeURIComponent(EditJson));
    } else {
        EditJson = parent.EditJson;
    }
}

function loadType() {
    var currentType = Q.utils.getRequestParam('type');
    var url;
    if (-1 === currentType.indexOf('.js')) {
        //Inner style
        url = window.location.href.split('q_jsonedit.html')[0] + 'type/' + currentType + '/' + currentType + '.js';
    } else {
        url = currentType;
    }
    $.getScript(url, function () {
        $('#title').html(title);
        $('#preview_container').append(packagePreview());
        packageEditDom(Definition);
        Q.form.fill(EditJson, $('#edit_form'), null);
        regSpecialWidget();
    });
}

function packageEditDom(definition) {
    var i = 0;
    $.each(definition, function (groupName, groupContent) {
        $('#accordion').append('' +
            '<div class="panel panel-info">' +
            '    <div class="panel-heading">' +
            '        <h4 class="panel-title">' +
            '            <a data-toggle="collapse" data-toggle="collapse" data-parent="#accordion" href="#collapse_' + i + '">' +
            groupName +
            '            </a>' +
            '        </h4>' +
            '    </div>' +
            '    <div id="collapse_' + i + '" class="panel-collapse collapse ' + (0 == i ? 'in' : '') + '">' +
            '        <div class="panel-body"></div>' +
            '    </div>' +
            '</div>');
        $.each(groupContent, function (key, value) {
            $('#collapse_' + i).append('<div id="edit_item_' + key + '" data-id="' + key + '"  class="form-group"></div>');
            packageEditItemDom(key, value, $('#edit_item_' + key));
        });
        i++;
    });
}

function packageEditItemDom(key, value, $container) {
    $container.append('<label class="col-sm-2 control-label">' + value.label + '</label>');
    var dom = '';
    dom += packageEditItemWidget(value.widget, value.data, key);
    $container.append('<div  class="col-sm-9">' + dom + '</div>');
}

function packageEditItemWidget(widget, data, domName) {
    var dom = '';
    switch (widget) {
        case 'text':
            dom = '<div><input name="' + domName + '" type="text" class="form-control"/></div>';
            break;
        case 'number':
            dom = '<div><input name="' + domName + '" type="text" class="form-control number"/></div>';
            break;
        case 'date':
            dom = '<div class="input-group date form_date" data-link-format="yyyy-mm-dd" data-link-field="' + domName + '"><input name="' + domName + '" id="' + domName + '" type="text" class="form-control"/><span class="input-group-addon"><span class="glyphicon glyphicon-remove"></span></span><span class="input-group-addon"><span class="glyphicon glyphicon-calendar"></span></span></div>';
            break;
        case 'datetime':
            dom = '<div class="input-group date form_datetime" data-link-format="yyyy-mm-dd hh:ii" data-link-field="' + domName + '"><input name="' + domName + '" id="' + domName + '" type="text" class="form-control"/><span class="input-group-addon"><span class="glyphicon glyphicon-remove"></span></span><span class="input-group-addon"><span class="glyphicon glyphicon-calendar"></span></span></div>';
            break;
        case 'time':
            dom = '<div class="input-group date form_time" data-link-format="hh:ii" data-link-field="' + domName + '"><input name="' + domName + '" id="' + domName + '" type="text" class="form-control"/><span class="input-group-addon"><span class="glyphicon glyphicon-remove"></span></span><span class="input-group-addon"><span class="glyphicon glyphicon-calendar"></span></span></div>';
            break;
        case 'textarea':
            dom = '<div><div class="input-group"><textarea name="' + domName + '" class="form-control"/></div>';
            break;
        case 'select':
            dom = '<div><select name="' + domName + '" class="form-control">';
            if (data) {
                $.each(data, function (k, v) {
                    dom += '<option value="' + k + '">' + v + '</option>';
                });
            }
            dom += '</select></div>';
            break;
        case 'color':
            dom = '<div class="input-group color_container"><input name="' + domName + '" type="text" class="form-control"/> <span class="input-group-addon"><i></i></span></div>';
            break;
        case 'bool':
            dom = '<div class="input-group">';
            if (data) {
                dom += '<input name="' + domName + '" value="' + data.checked + '"  data-unchecked="' + data.unchecked + '"  type="checkbox"/>';
            }
            dom += '</div>';
            break;
        case 'checkbox':
            dom = '<div class="input-group">';
            if (data) {
                $.each(data, function (k, v) {
                    dom += '<input name="' + domName + '" value="' + k + '" type="checkbox"/>' + v + '&nbsp;';
                });
            }
            dom += '</div>';
            break;
        case 'radio':
            dom = '<div class="input-group">';
            if (data) {
                $.each(data, function (k, v) {
                    dom += '<input name="' + domName + '" value="' + k + '" type="radio"/>' + v + '&nbsp;';
                });
            }
            dom += '</div>';
            break;
    }
    return dom;
}

function regSpecialWidget() {
    $('.form_datetime').datetimepicker({
        language: 'zh-CN',
        todayBtn: 1,
        autoclose: 1,
        todayHighlight: 1,
        startView: 2,
        forceParse: 0,
        showMeridian: 1
    });
    $('.form_date').datetimepicker({
        language: 'zh-CN',
        todayBtn: 1,
        autoclose: 1,
        todayHighlight: 1,
        startView: 2,
        minView: 2,
        forceParse: 0
    });
    $('.form_time').datetimepicker({
        language: 'zh-CN',
        todayBtn: 1,
        autoclose: 1,
        todayHighlight: 1,
        startView: 1,
        minView: 0,
        maxView: 1,
        forceParse: 0
    });
    $('.color_container').colorpicker();
}

$(function () {
    regEvent();
    regWidgetAction();
    getEditJson();
    loadType();
});