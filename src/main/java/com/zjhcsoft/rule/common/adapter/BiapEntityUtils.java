package com.zjhcsoft.rule.common.adapter;

import com.zjhcsoft.biap.login.entity.LoginSessionObject;
import com.zjhcsoft.rule.common.entity.BIAPAuthedInfo;


/**
 * 公告版块对应的实体类
 * @author lvxing
 *
 */
public class BiapEntityUtils implements java.io.Serializable{
	
	
	public static LoginSessionObject getOperator(BIAPAuthedInfo biapAuthedInfo){
        LoginSessionObject operator = new LoginSessionObject();
        operator.setUserRowId(Long.parseLong(biapAuthedInfo.PK));
        operator.setLoginName(biapAuthedInfo.loginName);
        operator.setUserType(biapAuthedInfo.userType);
        operator.setCompanyRowId(Long.parseLong(biapAuthedInfo.companyRowId));
        operator.setLoginAudRowId(biapAuthedInfo.loginAudRowId);
        operator.setLoginTime(biapAuthedInfo.loginTime);
        operator.setCompanyRowId(Long.parseLong(biapAuthedInfo.companyRowId));
        return operator;
    }

}
