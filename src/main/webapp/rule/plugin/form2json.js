(function ($) {
    /**
     *
     * @param isAll:是否将所有控件值都拼装,下拉控件有级联时,只会将最底层的控件值拼装,参数为true时上级也将拼装
     * @returns {{}}
     */
    $.fn.serializeObjectToJson = function (isAll) {
        var json = {};
        var value;
        var flag;
        this.find('*[name],*[data-serialize=true]').each(function () {
            value = null;
            flag = $(this).attr('name') ? $(this).attr('name') : $(this).attr('id');
            if (this.nodeName.toLowerCase() != 'select' && ($(this).attr("type").toLowerCase() == 'radio' || $(this).attr("type").toLowerCase() == 'checkbox')) {
                if ($(this).is(":checked")) {
                    value = $(this).val();
                }
            } else {
                value = $(this).val();
                if ((!value || value == '') && isAll) {
                    value = $(this).attr('oriValue');
                }
            }
            if (value) {
                if (json[flag]) {
                    if (!$.isArray(json[flag])) {
                        var oldVal = json[flag];
                        json[flag] = [];
                        json[flag].push(oldVal);
                    }
                    json[flag].push({value: value, match: $(this).attr('data-match')});
                } else {
                    json[flag] = {value: value, match: $(this).attr('data-match')};
                }
            }
        });
        return json;
    };

    $.fn.unSerializeObjectFromJson = function (json) {
        if (!json) {
            return;
        }
        var items;
        var isFind;
        var value;
        var flag;
        this.find('*[name],*[data-serialize=true]').each(function () {
            isFind = false;
            flag = $(this).attr('name') ? $(this).attr('name') : $(this).attr('id');
            items = json[flag];
            value = '';
            if (items) {
                if ($.isArray(items)) {
                    for (var i = 0; i < items.length; i++) {
                        if ($(this).attr('data-match') === items[i].match) {
                            if (this.nodeName.toLowerCase() != 'select' && (($(this).attr("type").toLowerCase() == 'radio' || $(this).attr("type").toLowerCase() == 'checkbox')) && $(this).val() != items[i].value) {
                                continue;
                            }
                            isFind = true;
                            value = items[i].value;
                            break;
                        }
                    }
                } else {
                    if ($(this).attr('data-match') === items.match) {
                        if (this.nodeName.toLowerCase() != 'select' && ($(this).attr("type").toLowerCase() == 'radio' || $(this).attr("type").toLowerCase() == 'checkbox')) {
                            if ($(this).val() == items.value) {
                                value = items.value;
                                isFind = true;
                            }
                        } else {
                            value = items.value;
                            isFind = true;
                        }
                    }
                }
                if (isFind) {
                    if (this.nodeName.toLowerCase() != 'select' && ($(this).attr("type").toLowerCase() == 'radio' || $(this).attr("type").toLowerCase() == 'checkbox')) {
                        $(this).attr('checked', true);
                    } else {
                        $(this).val(value);
                    }
                } else {
                    if (this.nodeName.toLowerCase() != 'select' && ($(this).attr("type").toLowerCase() == 'radio' || $(this).attr("type").toLowerCase() == 'checkbox')) {
                        $(this).attr('checked', false);
                    } else {
                        $(this).val('');
                    }
                }
            }
        });
    };
})(jQuery);