(function ($) {
    $.fn.QPanel = function (options) {
        var defaults = {
            onPostInit: null,
            onClick: null
        };
        var opts = $.extend({}, defaults, options || {});

        var $this = $(this);

        function initLayout() {
            $this.addClass('q_panel');
            var $section;
            $this.children('div[title]').each(function () {
                $section = $(this);
                $section.before('<div class="q_panel_section">' + $section.attr('title') + '</div>');
                $section.addClass('q_panel_content');
            });
            $this.children('.q_panel_content:gt(0)').hide();
            setShowContentCSS($this.children('.q_panel_content:first'));
        }

        function setSize() {

        }

        function regEvent() {
            var $section;
            $this.find('.q_panel_section').click(function () {
                $section = $(this);
                $this.children('.q_panel_content').hide();
                setShowContentCSS($section.next().show());
                opts.onClick($section.next());
            });
        }

        function setShowContentCSS($content) {
            $content.css({'width': '100%', 'height': $this.innerHeight() - $this.children('.q_panel_section:first').outerHeight(true) * $this.children('.q_panel_section').length});
        }

        initLayout();
        setSize();
        regEvent();

        opts.onPostInit && opts.onPostInit();

        //============================Public Method============================

        $this.reSize = function () {
            setSize();
        }

        return $this;
    };
})(jQuery);