package com.zjhcsoft.rule;

import com.zjhcsoft.rule.common.RuleConstants;
import com.zjhcsoft.rule.config.entity.RuleGroupTask;
import com.zjhcsoft.rule.config.service.RuleGroupTaskService;
import com.zjhcsoft.rule.datadispose.component.JDBCTemplateStore;
import com.zjhcsoft.rule.datadispose.service.RuleService;
import com.zjhcsoft.rule.datadispose.util.RuleServiceFactory;
import com.zjhcsoft.test.BaseTest2;
import org.apache.commons.codec.digest.DigestUtils;
import org.junit.Test;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;

import javax.inject.Inject;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Calendar;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * Created with IntelliJ IDEA.
 * User: XuanLubin  Date: 14-3-5  Time: 上午10:13
 */
public class RuleRunTest extends BaseTest2 {

    @Inject
    RuleGroupTaskService ruleGroupTaskService;

    @Test
    public void testRun() {
        //不分本地网查询
        List<RuleGroupTask> ruleGroupTaskList = ruleGroupTaskService.queryByStatus(RuleConstants.Status.READY,null);
        for (RuleGroupTask ruleGroupTask : ruleGroupTaskList) {
            RuleService ruleService = RuleServiceFactory.createBaseKpiService(ruleGroupTask);
            if (null != ruleService) {
                ruleGroupTask.setStartTime(Calendar.getInstance().getTime());
                ruleGroupTaskService.update(ruleGroupTask);
                ruleService.start();
            }
        }
        try {
            TimeUnit.DAYS.sleep(1l);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }


    @Test
    public void t() {
        JdbcTemplate template = JDBCTemplateStore.getJDBCTemplate("8a8a8aef448fce4901448fd41aa40004");
        template.setFetchSize(3000);
        template.setMaxRows(2000000);
        final List<Map<String, Object>> ids = template.queryForList("select distinct(t.ware_id),t.ware_name,t.stat_cycle_id from T_SALES_DETAIL t");
        template.batchUpdate("insert into T_PARAM(classid,jl,RM,DATE_CD) values(?,?,?,?)", new BatchPreparedStatementSetter() {
            @Override
            public void setValues(PreparedStatement ps, int i) throws SQLException {
                ps.setObject(1, ids.get(i).get("WARE_ID"));
                ps.setObject(2, Math.random() * 100);
                ps.setObject(3, ids.get(i).get("WARE_NAME"));
                ps.setObject(4, ids.get(i).get("STAT_CYCLE_ID"));
            }

            @Override
            public int getBatchSize() {
                return ids.size();
            }
        });
    }

    @Test
    public void t2() {
        JdbcTemplate template = JDBCTemplateStore.getJDBCTemplate("8a8a8a6c44f8683e0144f86c1cda0003");
        template.setFetchSize(3000);
        template.setMaxRows(10000);
        while(true) {
            final List<Map<String, Object>> ids = template.queryForList("select t.rowid from T_SALES_DETAIL t where serv_id = 99");
            if (ids.size()==0)
                break;
            template.batchUpdate("update T_SALES_DETAIL t set t.serv_id = ? where t.rowid = ?", new BatchPreparedStatementSetter() {
                @Override
                public void setValues(PreparedStatement ps, int i) throws SQLException {
                    ps.setObject(1, Math.random() * 100000 + 1000000);
                    ps.setObject(2, ids.get(i).get("ROWID"));
                }

                @Override
                public int getBatchSize() {
                    return ids.size();
                }
            });
        }
    }

    public static void main(String[] args) {
        String ruleContent = "package com.zjhcsoft.engine.pkg174\n" +
                "\n" +
                "dialect \"mvel\"\n" +
                "\n" +
                "declare FactType173\n" +
                "    SERV_ID:int\n" +
                "    WARE_ID:String\n" +
                "    SALES_FLAG:double\n" +
                "    ROWID:oracle.sql.ROWID\n" +
                "    RULE_VALUE:double\n" +
                "    RULE_EXPR:String=\"\"\n" +
                "    RULE_STEP:int=0\n" +
                "end\n" +
                "\n" +
                "declare FactType172\n" +
                "     CLASSID:String\n" +
                "     JL:double\n" +
                "     RM:String\n" +
                "end\n" +
                "\n" +
                "rule \"r01\"\n" +
                "when\n" +
                "     $d:FactType173($d1:WARE_ID)\n" +
                "     $p:FactType172(CLASSID==$d1)\n" +
                "then\n" +
                "     $d.RULE_VALUE = $d.SALES_FLAG * $p.JL;\n" +
                "     $d.RULE_EXPR = $d.RULE_EXPR + $p.RM;\n" +
                "     retract($d);\n" +
                "end";
        System.out.println(DigestUtils.sha1Hex(ruleContent.getBytes()));
        ruleContent = "package com.zjhcsoft.engine.pkg175\n" +
                "\n" +
                "dialect \"mvel\"\n" +
                "\n" +
                "declare FactType176\n" +
                "    SERV_ID:int\n" +
                "    WARE_ID:String\n" +
                "    SALES_FLAG:double\n" +
                "    ROWID:oracle.sql.ROWID\n" +
                "    RULE_VALUE:double\n" +
                "    RULE_EXPR:String=\"\"\n" +
                "    RULE_STEP:int=0\n" +
                "end\n" +
                "\n" +
                "declare FactType172\n" +
                "     CLASSID:String\n" +
                "     JL:double\n" +
                "     RM:String\n" +
                "end\n" +
                "\n" +
                "rule \"r01\"\n" +
                "when\n" +
                "     $d:FactType176($d1:WARE_ID)\n" +
                "     $p:FactType172(CLASSID==$d1)\n" +
                "then\n" +
                "     $d.RULE_VALUE = $d.SALES_FLAG * $p.JL;\n" +
                "     $d.RULE_EXPR = $d.RULE_EXPR + $p.RM;\n" +
                "     retract($d);\n" +
                "end";
        System.out.println(DigestUtils.sha1Hex(ruleContent.getBytes()));
        String ddd = "<li id=\"rule2\" style=\"display:none;\">\n" +
                "                    <span>\n" +
                "                        规则名:<input name=\"\" value=\"\" type=\"text\" class=\"input_normal rule_con_title\" style=\"margin-left: 5px;\"/>\n" +
                "                    </span>\n" +
                "                    <br/>\n" +
                "                    <table rule=\"true\" class=\"rule_table_condition\">\n" +
                "                        <tr>\n" +
                "                            <td t=\"con\" class=\"rule_td1\">如果：</td>\n" +
                "                            <td><a href=\"####\" onclick=\"newCondition(this)\">添加条件</a></td>\n" +
                "                            <td class=\"rule_td3\">\n" +
                "                                <d style=\"display: none\"><a href=\"####\" onclick=\"editCondition(this)\">编辑</a> | <a\n" +
                "                                        href=\"####\" onclick=\"delCondition(this)\">删除</a></d>\n" +
                "                            </td>\n" +
                "                        </tr>\n" +
                "                    </table>\n" +
                "                    <div class=\"rule_input_box\">那么：最终积分=\n" +
                "                        <input name=\"\" type=\"text\" id=\"\" class=\"rule_input\" onclick=\"$when.open(this)\"/>\n" +
                "                    </div>\n" +
                "                    <input type=\"button\" value=\"保存\" class=\"l-button\"/>\n" +
                "                    <input type=\"button\" value=\"取消\" class=\"l-button\"\n" +
                "                           onclick=\"$('#rule1').show(),$('#rule2').hide()\"/>\n" +
                "                </li>";
    }

}
