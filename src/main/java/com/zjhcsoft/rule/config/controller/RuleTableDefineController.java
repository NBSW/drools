package com.zjhcsoft.rule.config.controller;

import com.zjhcsoft.qin.exchange.controller.ControllerHelper;
import com.zjhcsoft.qin.exchange.dto.ResponseVO;
import com.zjhcsoft.rule.common.RuleConstants;
import com.zjhcsoft.rule.common.controller.BaseController;
import com.zjhcsoft.rule.common.database.TableColumn;
import com.zjhcsoft.rule.config.dto.RuleTableDefineQuery;
import com.zjhcsoft.rule.config.entity.RuleTableDefine;
import com.zjhcsoft.rule.config.service.RuleRelationService;
import com.zjhcsoft.rule.config.service.RuleTableDefineService;
import com.zjhcsoft.rule.datadispose.component.JDBCTemplateStore;
import com.zjhcsoft.shreport.ds.entity.DS;
import org.springframework.web.bind.annotation.*;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: XuanLubin  Date: 14-3-12  Time: 下午3:59
 */
@RestController
@RequestMapping("config/table")
public class RuleTableDefineController extends BaseController<RuleTableDefineService, RuleTableDefine> {

    @RequestMapping("{dsCode}/{table}")
    public ResponseVO getTableColumn(@PathVariable String dsCode, @PathVariable String table) {
        try {
            DS ds = JDBCTemplateStore.get(dsCode);
            TableColumn tableColumn = TableColumn.get(ds.getCode(), ds.getDriver());
            if (!tableColumn.tableExist(ds.getUsername(), table)) {
                return ControllerHelper.badRequest("表【" + table + "】不存在");
            }
            return ControllerHelper.success(tableColumn.getColumnInfo(ds.getUsername(), table));
        } catch (Exception e) {
            e.printStackTrace();
            return ControllerHelper.unknownError(e.getMessage());
        }
    }

    @Inject
    RuleRelationService ruleRelationService;

    @RequestMapping("{tableId}/param")
    public ResponseVO getParamTable(@PathVariable Long tableId) {
        try {
            List<Long> relationList = ruleRelationService.queryIdByFromIdRelType(tableId, new String[]{RuleConstants.Type.DATA_PARAM});
            return ControllerHelper.success(service.findByPkIds(relationList));
        } catch (Exception e) {
            e.printStackTrace();
            return ControllerHelper.unknownError(e.getMessage());
        }
    }

    @RequestMapping(value = "/query",method = RequestMethod.POST)
    public ResponseVO find(@RequestBody RuleTableDefineQuery query,HttpServletRequest request) {
        if (null == query)
            return ControllerHelper.success(service.findAll(latnIdHelper.getLatnId(request.getSession())));
        else
            return ControllerHelper.success(service.find(query));
    }
}
