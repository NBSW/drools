package com.zjhcsoft.rule.common.util;

import com.zjhcsoft.biap.adaptor.service.ObjectAccessor;
import com.zjhcsoft.qin.exchange.service.ServiceHelper;
import com.zjhcsoft.qin.exchange.utils.PropertyHelper;
import com.zjhcsoft.rule.common.RuleConstants;
import com.zjhcsoft.rule.common.entity.BIAPAuthedInfo;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by XuanLubin on 2014/4/4. 10:33
 */
@Component
public class LatnIdHelper {

    @Inject
    private ObjectAccessor objectAccessor;

    private static final Map<String, String> COMPANY_CD_2_LATN_TD = new HashMap<>();
    private static final Map<String, String> LATN_TD_2_COMPANY = new HashMap<>();
    private static final List<Map<String, String>> LATN_INFO = new ArrayList<>();

    @PostConstruct
    private void init() {
        int latn_size = Integer.parseInt(PropertyHelper.get("LATN_SIZE"));
        for (int i = 1; i <= latn_size; i++) {
            String latn_info = PropertyHelper.get("LATN_" + i);
            String[] _latn_info = latn_info.split(",");
            COMPANY_CD_2_LATN_TD.put(_latn_info[2], _latn_info[1]);
            LATN_TD_2_COMPANY.put(_latn_info[1], _latn_info[0]);
            Map<String,String> latnInfo = new HashMap<>();
            latnInfo.put("id",_latn_info[1]);
            latnInfo.put("text",_latn_info[0]);
            LATN_INFO.add(latnInfo);
        }
    }


    public String getLatnId(HttpSession session) {
        BIAPAuthedInfo biapAuthedInfo = ServiceHelper.getCurrentAuthedInfo();
        if (!biapAuthedInfo.userType.equals(PropertyHelper.get(RuleConstants.RULE_SUPER_ADMIN_USER_TYPE))) {
            return COMPANY_CD_2_LATN_TD.get(biapAuthedInfo.companyDefineCd);
        } else {
            return null;
        }
    }

    public String getLatnInfo(String latnId){
        return LATN_TD_2_COMPANY.get(latnId);
    }

    public Object getLatnInfo(){
        return LATN_INFO;
    }
}
