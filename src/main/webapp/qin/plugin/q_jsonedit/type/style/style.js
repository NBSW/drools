var Definition = {
    '文字': {
        'text-align': {
            label: '对齐',
            widget: 'select',
            data: {
                left: '左对齐',
                right: '右对齐',
                center: '居中'
            }
        },
        color: {
            label: '颜色',
            widget: 'color'
        },
        'font-size': {
            label: '大小',
            widget: 'text'
        },
        'font-family': {
            label: '字体',
            widget: 'select',
            data: {
                '宋体': '宋体',
                '黑体': '黑体',
                '微软雅黑': '微软雅黑'
            }
        },
        'font-weight': {
            label: '加粗',
            widget: 'bool',
            data: {
                'checked': 'bold',
                'unchecked': 'normal'
            }
        },
        'font-style': {
            label: '斜体',
            widget: 'bool',
            data: {
                'checked': 'italic',
                'unchecked': 'normal'
            }}
    },
    '单元格': {
        width: {
            label: '宽度',
            widget: 'text'
        },
        height: {
            label: '高度',
            widget: 'text'
        },
        'background-color': {
            label: '背景颜色',
            widget: 'color'
        },
        test1: {
            label: 'test1',
            widget: 'date'
        }}
}

function packagePreview() {
    return '<div id="style_preview" style="margin: auto; border: 1px solid #000000">当前效果</div>';
}

function rendering() {
    $('#style_preview').css(EditJson);
}

var title = '样式编辑';