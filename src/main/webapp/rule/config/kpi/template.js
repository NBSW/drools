/**
 * Created by XuanLubin on 14-3-18.
 */
var CONSTANT = {
    tr:$('<tr style="display: none;"><td t="con" class="rule_td1"></td><td><a href="####" onclick="newCondition(this)">添加条件</a></td><td class="rule_td3"><d style="display: none"><a href="####" onclick="editCondition(this)">编辑</a> | <a href="####" onclick="delCondition(this)">删除</a></d></td></tr>'),
    li:$("<li>" +
        "   <span>\n" +
        "         规则名:<input name=\"\" value=\"\" type=\"text\" class=\"input_normal rule_con_title\" style=\"margin-left: 5px;\"/>\n" +
        "         <input type='button' class='rule_del_group' style='display: none' onclick='$ruleContainer.delRule(this)' title='删除该条规则'/>"+
        "         <a style='cursor: pointer;position: relative;float: right;margin-right: 10px;display: none;' onclick='$ruleContainer.switchRule(this);$(\"#rule1\").hide()' title='修改该条规则'>修改</a>"+
        "   </span><br/>\n" +
        "   <table rule=\"true\" class=\"rule_table_condition\">\n" +
        "   </table>\n" +
        "   <div class=\"rule_input_box\">那么：结果=\n" +
        "        <input name=\"\" type=\"text\" id=\"\" class=\"rule_input\" onclick=\"$when.open(this)\"/>\n" +
        "        <span></span>\n" +
        "   </div>\n" +
        "   <input type=\"button\" value=\"保存\" class=\"l-button save-button\"/>\n" +
        "   <input type=\"button\" value=\"取消\" class=\"l-button del-button\" onclick=\"$('#rule1').show()\" style=\" z-index: 0; \"/>\n" +
        "</li>"),
    rule:"<tr cid='{cid}' rid=\"{rid}\">" +
        "    <td>\n" +
        "        {blank}\n" +
        "        <input id=\"field_{rid}\" class=\"select100\">\n" +
        "        <input type=\"text\" id=\"op_{rid}\" class=\"select50\"/>\n" +
        "        <input type=\"text\" id=\"val_{rid}\" class=\"rule_add_input\" style='width: 150px;'/>\n" +
        "        <input type=\"hidden\" id=\"type_{rid}\"/>\n" +
        "        <input type=\"button\" value=\"X\" title=\"删除条件\" class=\"l-button rule_del role-rule-del\"/>\n" +
        "    </td>\n" +
        "</tr>",
    group:"<tr cid='{cid}' gid=\"{gid}\">\n" +
        "    <td>\n" +
        "        <table class=\"rule_table\">\n" +
        "            <tbody>\n" +
        "            <tr>\n" +
        "                <td>\n" +
        "                    <input type=\"button\" value=\"增加分组\" class=\"l-button role-group-add\">\n" +
        "                    <input type=\"button\" value=\"增加条件\" class=\"rule_add role-rule-add\">\n" +
        "                    <input type=\"button\" value=\"删除分组\" title=\"删除分组\" class=\"rule_del_group\">\n" +
        "                </td>\n" +
        "            </tr>\n" +
        "            </tbody>\n" +
        "        </table>\n" +
        "    </td>\n" +
        "</tr>",
    groupFirst:"<tr cid='{cid}' gid=\"{gid}\"><td><input id=\"join_{gid}\" class=\"select50\"></td></tr>"
};