Q.devData = {
   /* 'auth/users/': {
        get: [
            {loginName: 'zs', userName: '张三', roles: [
                {code: 'user', name: '普通用户'}
            ], positions: [
                {code: 'hz_manage', name: '杭州政企经理'}
            ]},
            {loginName: 'ls', userName: '李四', roles: [
                {code: 'admin', name: '管理员'},
                {code: 'user', name: '普通用户'}
            ], positions: [
                {code: 'tz_manage', name: '台州政企经理'},
                {code: 'hz_manage', name: '杭州政企经理'}
            ]}
        ]
    },
    'auth/roles/': {
        get: [
            {code: 'admin', name: '管理员'},
            {code: 'user', name: '普通用户'}
        ]
    },
    'auth/positions/': {
        get: [
            {code: 'tz_manage', name: '台州政企经理'},
            {code: 'hz_manage', name: '杭州政企经理'}
        ]
    },
    'app/organizations/': {
        get: [
            {code: 'tz', name: '台州'},
            {code: 'hz', name: '杭州'}
        ]
    },
    'app/modules/': {
        get: [
            {code: 'report', name: '报表'},
            {code: 'list', name: '清单'},
            {code: 'config', name: '配置项'}
        ]
    },
    'apps/': {
        get: [
            {code: 'tz', name: '台州'},
            {code: 'hz', name: '杭州'},
            {code: '1', name: '台州报表', organizationCode: 'tz', moduleCode: 'report', roles: ['admin', 'user'], positions: [ 'tz_manage']},
            {code: '2', name: '台州清单', organizationCode: 'tz', moduleCode: 'list', roles: null, positions: null},
            {code: '3', name: '杭州配置项0', organizationCode: 'hz', moduleCode: 'config', roles: null, positions: null }
        ]
    },
    'ds/': {
        get: {
            '杭州': [
                {code: '1', name: 'aaa'},
                {code: '2', name: 'bbb'},
                {code: '3', name: 'ccc'}
            ],
            '台州': [
                {code: '4', name: 'ddd'},
                {code: '5', name: 'eee'},
                {code: '6', name: 'ffff'}
            ]
        }
    },
    'meta/\\w+/items/query/': {
        get: {"Rows": [
            {"code": "1", "name": "SALE_SHOW_LVL2_NAME", "label": "二级销售组织", "relColumn": "SALE_SHOW_LVL2_NAME"},
            {"code": "2", "name": "SALE_SHOW_LVL2_ID", "label": "二级销售ID", "relColumn": "SALE_SHOW_LVL2_ID"},
            {"code": "3", "name": "SALE_SHOW_LVL3_NAME", "label": "三级销售组织", "relColumn": "SALE_SHOW_LVL3_NAME"},
            {"code": "4", "name": "SALE_SHOW_LVL3_ID", "label": "三级销售ID", "relColumn": "SALE_SHOW_LVL3_ID"},
            {"code": "5", "name": "SALE_SHOW_LVL4_NAME", "label": "四级销售组织", "relColumn": "SALE_SHOW_LVL4_NAME"},
            {"code": "6", "name": "SALE_SHOW_LVL4_ID", "label": "四级展现ID", "relColumn": "SALE_SHOW_LVL4_ID"},
            {"code": "7", "name": "SALE_SHOW_LVL5_NAME", "label": "五级销售组织", "relColumn": "SALE_SHOW_LVL5_NAME"},
            {"code": "8", "name": "SALE_SHOW_LVL5_ID", "label": "五级销售ID", "relColumn": "SALE_SHOW_LVL5_ID"},
            {"code": "9", "name": "MSU_CDMA_NEW", "label": "移动用户新增", "relColumn": "MSU_CDMA_NEW"},
            {"code": "10", "name": "MSU_CDMA_NEW_MON", "label": "移动用户累计新增", "relColumn": "MSU_CDMA_NEW_MON"},
            {"code": "11", "name": "MSU_CDMA_NEW_MON_BEF", "label": "上期移动用户累计新增", "relColumn": "MSU_CDMA_NEW_MON_BEF"},
            {"code": "12", "name": "MSU_G3_NEW", "label": "3G智能机新增", "relColumn": "MSU_G3_NEW"},
            {"code": "13", "name": "MSU_G3_NEW_MON", "label": "3G智能机累计新增", "relColumn": "MSU_G3_NEW_MON"},
            {"code": "14", "name": "MSU_G3_NEW_MON_BEF", "label": "上期3G智能机累计新增", "relColumn": "MSU_G3_NEW_MON_BEF"},
            {"code": "15", "name": "MSU_LX3G_NEW_MON", "label": "乐享3G套餐累计新增", "relColumn": "MSU_LX3G_NEW_MON"},
            {"code": "16", "name": "MSU_LX3G_NEW_MON_BEF", "label": "上期乐享3G套餐累计新增", "relColumn": "MSU_LX3G_NEW_MON_BEF"},
            {"code": "17", "name": "MSU_E9_NEW", "label": "E9套餐新增", "relColumn": "MSU_E9_NEW"},
            {"code": "18", "name": "MSU_E9_NEW_MON", "label": "E9套餐累计新增", "relColumn": "MSU_E9_NEW_MON"},
            {"code": "19", "name": "MSU_E9_NEW_MON_BEF", "label": "上期E9套餐累计新增", "relColumn": "MSU_E9_NEW_MON_BEF"},
            {"code": "20", "name": "MSU_A6A8_NEW", "label": "A6A8套餐新增", "relColumn": "MSU_A6A8_NEW"},
            {"code": "21", "name": "MSU_A6A8_NEW_MON", "label": "A6A8套餐累计新增", "relColumn": "MSU_A6A8_NEW_MON"},
            {"code": "22", "name": "MSU_A6A8_NEW_MON_BEF", "label": "上期A6A8套餐累计新增", "relColumn": "MSU_A6A8_NEW_MON_BEF"},
            {"code": "23", "name": "MSU_YUNKA_NEW", "label": "云卡新增", "relColumn": "MSU_YUNKA_NEW"},
            {"code": "24", "name": "MSU_YUNKA_NEW_MON", "label": "云卡累计新增", "relColumn": "MSU_YUNKA_NEW_MON"},
            {"code": "25", "name": "MSU_YUNKA_NEW_MON_BEF", "label": "上期云卡累计新增", "relColumn": "MSU_YUNKA_NEW_MON_BEF"},
            {"code": "26", "name": "MSU_YOUNG_NEW", "label": "飞扬新增", "relColumn": "MSU_YOUNG_NEW"},
            {"code": "27", "name": "MSU_YOUNG_NEW_MON", "label": "飞扬累计新增", "relColumn": "MSU_YOUNG_NEW_MON"},
            {"code": "28", "name": "MSU_YOUNG_NEW_MON_BEF", "label": "上期飞扬累计新增", "relColumn": "MSU_YOUNG_NEW_MON_BEF"},
            {"code": "29", "name": "MSU_HEYUE_NEW", "label": "合约新增", "relColumn": "MSU_HEYUE_NEW"},
            {"code": "30", "name": "MSU_HEYUE_NEW_MON", "label": "合约累计新增", "relColumn": "MSU_HEYUE_NEW_MON"},
            {"code": "31", "name": "MSU_HEYUE_NEW_MON_BEF", "label": "上期合约累计新增", "relColumn": "MSU_HEYUE_NEW_MON_BEF"},
            {"code": "32", "name": "REPORT_LVL", "label": "报表层级", "relColumn": "REPORT_LVL"},
            {"code": "33", "name": "MSU_LX3G_NEW", "label": "乐享3G套餐新增", "relColumn": "MSU_LX3G_NEW"}
        ], "Total": 50}
    },
    'meta/\\w+/items/result/': {
        get: {"Rows": [
            {"code": "1", "name": "SALE_SHOW_LVL2_NAME", "label": "二级销售组织", "relColumn": "SALE_SHOW_LVL2_NAME"},
            {"code": "2", "name": "SALE_SHOW_LVL2_ID", "label": "二级销售ID", "relColumn": "SALE_SHOW_LVL2_ID"},
            {"code": "3", "name": "SALE_SHOW_LVL3_NAME", "label": "三级销售组织", "relColumn": "SALE_SHOW_LVL3_NAME"},
            {"code": "4", "name": "SALE_SHOW_LVL3_ID", "label": "三级销售ID", "relColumn": "SALE_SHOW_LVL3_ID"},
            {"code": "5", "name": "SALE_SHOW_LVL4_NAME", "label": "四级销售组织", "relColumn": "SALE_SHOW_LVL4_NAME"},
            {"code": "6", "name": "SALE_SHOW_LVL4_ID", "label": "四级展现ID", "relColumn": "SALE_SHOW_LVL4_ID"},
            {"code": "7", "name": "SALE_SHOW_LVL5_NAME", "label": "五级销售组织", "relColumn": "SALE_SHOW_LVL5_NAME"},
            {"code": "8", "name": "SALE_SHOW_LVL5_ID", "label": "五级销售ID", "relColumn": "SALE_SHOW_LVL5_ID"},
            {"code": "9", "name": "MSU_CDMA_NEW", "label": "移动用户新增", "relColumn": "MSU_CDMA_NEW"},
            {"code": "10", "name": "MSU_CDMA_NEW_MON", "label": "移动用户累计新增", "relColumn": "MSU_CDMA_NEW_MON"},
            {"code": "11", "name": "MSU_CDMA_NEW_MON_BEF", "label": "上期移动用户累计新增", "relColumn": "MSU_CDMA_NEW_MON_BEF"},
            {"code": "12", "name": "MSU_G3_NEW", "label": "3G智能机新增", "relColumn": "MSU_G3_NEW"},
            {"code": "13", "name": "MSU_G3_NEW_MON", "label": "3G智能机累计新增", "relColumn": "MSU_G3_NEW_MON"},
            {"code": "14", "name": "MSU_G3_NEW_MON_BEF", "label": "上期3G智能机累计新增", "relColumn": "MSU_G3_NEW_MON_BEF"},
            {"code": "15", "name": "MSU_LX3G_NEW_MON", "label": "乐享3G套餐累计新增", "relColumn": "MSU_LX3G_NEW_MON"},
            {"code": "16", "name": "MSU_LX3G_NEW_MON_BEF", "label": "上期乐享3G套餐累计新增", "relColumn": "MSU_LX3G_NEW_MON_BEF"},
            {"code": "17", "name": "MSU_E9_NEW", "label": "E9套餐新增", "relColumn": "MSU_E9_NEW"},
            {"code": "18", "name": "MSU_E9_NEW_MON", "label": "E9套餐累计新增", "relColumn": "MSU_E9_NEW_MON"},
            {"code": "19", "name": "MSU_E9_NEW_MON_BEF", "label": "上期E9套餐累计新增", "relColumn": "MSU_E9_NEW_MON_BEF"},
            {"code": "20", "name": "MSU_A6A8_NEW", "label": "A6A8套餐新增", "relColumn": "MSU_A6A8_NEW"},
            {"code": "21", "name": "MSU_A6A8_NEW_MON", "label": "A6A8套餐累计新增", "relColumn": "MSU_A6A8_NEW_MON"},
            {"code": "22", "name": "MSU_A6A8_NEW_MON_BEF", "label": "上期A6A8套餐累计新增", "relColumn": "MSU_A6A8_NEW_MON_BEF"},
            {"code": "23", "name": "MSU_YUNKA_NEW", "label": "云卡新增", "relColumn": "MSU_YUNKA_NEW"},
            {"code": "24", "name": "MSU_YUNKA_NEW_MON", "label": "云卡累计新增", "relColumn": "MSU_YUNKA_NEW_MON"},
            {"code": "25", "name": "MSU_YUNKA_NEW_MON_BEF", "label": "上期云卡累计新增", "relColumn": "MSU_YUNKA_NEW_MON_BEF"},
            {"code": "26", "name": "MSU_YOUNG_NEW", "label": "飞扬新增", "relColumn": "MSU_YOUNG_NEW"},
            {"code": "27", "name": "MSU_YOUNG_NEW_MON", "label": "飞扬累计新增", "relColumn": "MSU_YOUNG_NEW_MON"},
            {"code": "28", "name": "MSU_YOUNG_NEW_MON_BEF", "label": "上期飞扬累计新增", "relColumn": "MSU_YOUNG_NEW_MON_BEF"},
            {"code": "29", "name": "MSU_HEYUE_NEW", "label": "合约新增", "relColumn": "MSU_HEYUE_NEW"},
            {"code": "30", "name": "MSU_HEYUE_NEW_MON", "label": "合约累计新增", "relColumn": "MSU_HEYUE_NEW_MON"},
            {"code": "31", "name": "MSU_HEYUE_NEW_MON_BEF", "label": "上期合约累计新增", "relColumn": "MSU_HEYUE_NEW_MON_BEF"},
            {"code": "32", "name": "REPORT_LVL", "label": "报表层级", "relColumn": "REPORT_LVL"},
            {"code": "33", "name": "MSU_LX3G_NEW", "label": "乐享3G套餐新增", "relColumn": "MSU_LX3G_NEW"}
        ], "Total": 50}
    } */
}