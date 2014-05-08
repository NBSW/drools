(function ($) {
    $.fn.QTextarea = function (options) {
        var defaults = {
        };
        var opts = $.extend({}, defaults, options || {});

        var $textarea = $(this);
        var $this;
        var $frame;

        function initLayout() {
            var flag = $textarea.attr('id') ? $textarea.attr('id') : $textarea.attr('name') ? $textarea.attr('name') : new Date().getTime();
            $this = $('<div id="' + flag + '_container" class="q_textarea_container"><p class="q_textarea_formatbar"></p><iframe id="q_textarea_frame_' + flag + '" class="q_textarea_frame" frameborder="0"></iframe><p class="q_textarea_toolbar"><span class="q_textarea_toolbar_reduce">-</span><span class="q_textarea_toolbar_add">+</span></p></div>');
            $textarea.before($this);
            $frame = $this.children('.q_textarea_frame');
            $frame[0].contentWindow.document.designMode = 'On';
            $frame[0].contentWindow.focus();
        }

        function setSize() {
            var width = $textarea.width();
            var height = $textarea.height();
            $this.css({'width': width});
            $frame.css({'width': width, 'height': height - $this.children('.q_textarea_formatbar').outerHeight() - $this.children('.q_textarea_toolbar').outerHeight()});
            $textarea.hide();
        }

        function fillFormatBar() {
            if (opts.format && 0 != opts.format.length) {
                var item;
                for (var i in opts.format) {
                    item = '<label>' + opts.format[i].label + '</label><select class="format_select_container" data-wrap="' + opts.format[i].wrap + '">';
                    for (var j in opts.format[i].value) {
                        item += '<option value="' + opts.format[i].value[j].code + '">' + opts.format[i].value[j].label + '</option>';
                    }
                    $this.children('.q_textarea_formatbar').append(item + '</select>');
                }
            }
        }

        function fillContent() {
            var content = $textarea.val();
            if (content) {
                $frame.contents().find('body').html(parseContent(content));
            }
        }

        function parseContent(content) {
            if (opts.format && 0 != opts.format.length) {
                var reg;
                var label;
                for (var i in opts.format) {
                    reg = new RegExp(opts.format[i].key, 'gmi');
                    content = content.replace(reg, function (a, b, c) {
                        label = _.filter(opts.format[i].value, function (v) {
                            return b === v.code;
                        });
                        label= label&& 0< label.length? label[0].label:'';
                        return '&nbsp;<span data-value="' + a + '" class="item" style="padding: 2px;cursor: pointer;font-size:10pt;' + opts.format[i].css + '">' + label + '</span>&nbsp;';
                    });
                }
            }
            return content;
        }

        function insertContent(value) {
            if (navigator.userAgent.indexOf("MSIE") > 0) {
                $frame[0].document.selection.createRange().pasteHTML(value);
            } else {
                var rng = $frame[0].contentWindow.getSelection().getRangeAt(0);
                var frg = rng.createContextualFragment(value);
                rng.insertNode(frg);
            }
            $frame.contents().find('body').blur();
        }

        function regEvent() {
            $frame.contents().on('click', '.item', function () {
                $(this).remove();
            });
            $frame.contents().find('body').on('blur', function () {
                $textarea.val(getCode());
            });
            $this.on('change', '.format_select_container', function () {
                var wrap = $(this).attr('data-wrap');
                insertContent(parseContent(wrap.replace('*', $(this).val())));
                $(this).val('');
            });
            $this.find('.q_textarea_toolbar_add').click(function () {
                $frame.css({'height': $frame.height() + 50});
            });
            $this.find('.q_textarea_toolbar_reduce').click(function () {
                $frame.css({'height': $frame.height() - 50});
            });
        }

        function getCode() {
            var content = $frame.contents().find('body').html();
            content = content.replace(/<span(.+?)<\/span>/ig, function (a, b, c) {
                b = b.substring(b.indexOf('data-value="') + 'data-value="'.length);
                b = b.substring(0, b.indexOf('"'));
                return b;
            });
            content = content.replace(/&nbsp;/ig, ' ').replace(/<.+?>/ig, '').replace(/<.+?\/>/ig, ' ');
            return content;
        }

        initLayout();
        fillFormatBar();
        fillContent();
        setSize();
        regEvent();

        //============================Public Method============================

        this.reSize = function () {
            setSize();
        }

        this.get = function () {
            getCode();
        }

        return this;
    };
})(jQuery);