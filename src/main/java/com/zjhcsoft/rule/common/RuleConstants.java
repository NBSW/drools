package com.zjhcsoft.rule.common;

/**
 * Created with IntelliJ IDEA.
 * User: XuanLubin  Date: 14-3-5  Time: 上午10:29
 */
public class RuleConstants {
    public interface Status {
        int NORMAL = 1, FORBIDDEN = 0, FILE = 2, DELETED = 9;
        int COMPLETED = 1, CALCULATE = 0, EXCEPTION = 2;
        int READY = -1, USED = 99;
        int RUNNING = 1, RUNNINGENDING = 9;
    }

    public interface Cycle {
        int DAY = 1, WEEK = 2, MONTH = 3, SEASON = 4, YEAR = 5;
    }

    public interface Type {
        static final String RULE_GROUP_BASE_RULE_KPI = "RULE_GROUP_BASE_RULE_KPI";
        static final String RULE_GROUP_MIX_RULE_KPI = "RULE_GROUP_MIX_RULE_KPI";
        static final String MIX_BASE_KPI_REL = "KPI_KPI_REL";
        static final String MIX_BASE_KPI_REL_2 = "KPI_KPI_REL_2";
        static final String DATA_PARAM = "DATA_PARAM";
        int DATA_MODEL = 1, PARAM_MODEL = 3;
        int BASE_KPI = 0, MIX_KPI = 1;
    }

    public interface RuleJsonKey {
        static final String OTHER_PARAM = "otherParam", COLUMNS = "columns", FACT_NAME = "factName";
        static final String IS_PARAM = "isParam";
        static final String NUMBER = "number";
        static final String STRING = "string";
        static final String DATA_MODEL = "dataModel", PARAM_MODEL = "paramModel", TABLE_MODEL = "tableModel";
        static final String ID_KEY = "fact";
        static final String DATA_MODEL_ID = "data_model_id";
        static final String RULE_ARR = "ruleArr";
        static final String COL_JOIN_PARAM = "COL_JOIN_PARAM";
        static final String COL_T = "relTo";
        //RuleParser
        static final String RULES = "rules";
        static final String RULE_NAME = "ruleName";
        static final String RULE_ATTRIBUTES = "attributes";
        static final String RULE_ATTRIBUTE_NAME = "name";
        static final String RULE_ATTRIBUTE_VALUE = "value";
        static final String RULE_WHEN = "whens";
        static final String RULE_THEN = "thens";
        static final String RULE_THEN_STR = "thens_str";
        static final String RULE_OPS = "rule";

        static final String RULE_JOIN = "join";
        static final String RULE_FIELD = "field";
        static final String RULE_OP = "op";
        static final String RULE_TYPE = "type";
        static final String RULE_VALUE = "value";


        //DrlParser
        static final String RULES_GROUP = "ruleGroup";
        static final String DECLARE = "declare";
        static final String PACKAGE_NAME = "packageName";
        static final String IMPORTS = "imports";
        static final String GLOBALS = "globals";
        static final String GLOBAL_TYPE = "type";
        static final String GLOBAL_INSTANCE = "instance";

        public interface Column {
            static final String NUMBER = "number", NUMBER_TYPE = "float", NAME = "name", LABEL = "label", ONAME = "oName";
            static final String DIMENSION = "dimension";
        }

        public interface DateCd {
            static final String TYPE = "String";
            static final String INSTANCE = "i_dateCd";
        }
    }

    public interface RuleColumn {
        public interface Value {
            static final String RULE_VALUE = "RULE_VALUE", RULE_EXPR = "RULE_EXPR";
        }

        public interface Rule {
            static final String RULE_STEP = "RULE_STEP";
        }
    }

    public static final String RULE_SUPER_ADMIN_USER_TYPE = "rule_super_admin_user_type";
}
