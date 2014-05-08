(function ($) {
    $.fn.QForm = function (options) {
        var defaults = {
            data: null
        };
        var opts = $.extend({}, defaults, options || {});

        var $this = $(this);
        var maxSpanInRow = 1;

        function initLayout() {
            $this.addClass('q_form');
            $this.children('dl').children('dt').addClass('q_form_title');

            $this.children('dl').children('dd').children('*').each(function () {
                if (maxSpanInRow < $(this).children('*').length) {
                    maxSpanInRow = $(this).children('*').length;
                }
            });

            $this.children('dl').children('dd').each(function () {
                var $content = $(this);
                if (0 == $content.children('*').length) {
                    $(this).addClass('q_form_section');
                } else {
                    $content.addClass('q_form_content');
                    $content.children('*').each(function () {
                        $(this).addClass('q_form_content_row');
                        $(this).after('<p class="clear"></p> ');
                    });
                    $content.children('*').children('*').each(function () {
                        var $field = $(this);
                        $field.addClass('value');
                        $field.before('<span class="label" ' + (undefined != $field.attr('data-help') ? 'title="' + $field.attr('data-help') + '"' : '') + '><label>' + $field.attr('title') + '</label></span>');
                        $field.removeAttr('title');
                        $field.removeAttr('data-help');
                    });
                }
            });
        }

        function setSize() {
            var parentWidth = $this.innerWidth() - 2;
            var labelWidth = parentWidth / maxSpanInRow * 0.4 - maxSpanInRow * 9;
            var valueWidth = parentWidth / maxSpanInRow * 0.6 - maxSpanInRow * 9;
            var $row;
            var itemCount;
            var maxHeight;
            $this.find('.q_form_content_row').each(function () {
                $row = $(this);
                itemCount = $row.children('*').length / 2;
                $row.children('.label').css({width: labelWidth});
                $row.children('.value').not(':last').css({width: valueWidth});
                $row.children('.value:last').css({width: valueWidth + (labelWidth + valueWidth) * (maxSpanInRow - itemCount)});
                $row.children('.value').each(function () {
                    var $value = $(this).find('input[type!=button][type!=radio][type!=checkbox][data-autoWidth!=false],textarea[data-autoWidth!=false]');
                    $value.css({'width': 80 / $value.length + '%'});
                });
                $row.children('.value').find('textarea[data-autoHeight!=false]').css({'height': '100px'});
                $row.children('.value').find('input[type=button]').addClass('button_default');
                maxHeight = 0;
                $row.children('*').each(function () {
                    //如果不去掉高度重设时无法获取新的高度
                    $(this).css({height: ''});
                    if (maxHeight < $(this).height()) {
                        maxHeight = $(this).height();
                    }
                });
                $row.children('.label').css({height: maxHeight});
            });
        }

        function regEvent() {
            var $section;
            $this.find('.q_form_section').click(function () {
                $section = $(this);
                if ('none' == $section.next().css('display')) {
                    $section.next().show();
                } else {
                    $section.next().hide();
                }
            });
        }

        function fillData() {
            if (opts.data) {
                Q.form.fill(opts.data, $this, null);
            }
        }

        function fillDataByExt(data) {
            if (data) {
                Q.form.fill(data, $this, null);
            }
        }

        initLayout();
        setSize();
        regEvent();
        fillData();

        Q.view.renderHelp();

        //============================Public Method============================

        $this.toJSON = function () {
            return $this.serializeJSON();
        }

        $this.validation = function () {
            return $this.validationEngine('validate');
        }

        $this.fillData = function (data) {
            fillDataByExt(data);
        }

        $this.reSize = function () {
            setSize();
        }

        return $this;
    };
})(jQuery);