package com.zjhcsoft.rule.common.entity;

import com.zjhcsoft.biap.adaptor.objectProxy.RoleInsideProxy;
import com.zjhcsoft.biap.adaptor.objectProxy.UserInsideProxy;
import com.zjhcsoft.biap.user.entity.SysRelUserStation;
import com.zjhcsoft.biap.user.entity.SysUserDataAccess;
import com.zjhcsoft.qin.exchange.security.BaseAuthedInfo;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by XuanLubin on 2014/4/21. 18:28
 */
public class BIAPAuthedInfo extends BaseAuthedInfo {
    public String loginName;
    public String userName;
    public String companyId;
    public String companyRowId;
    public String companyName;
    public String companyDefineCd;
    public Map<String, String> roles;
    public Map<String, String> areas;
    public Map<String, String> areaRows;
    public Map<String, String> posts;
    public String userType;
    public Date loginTime;

    //日志记录信息
    public Long loginAudRowId;

    public BIAPAuthedInfo(){

    }

    public BIAPAuthedInfo(UserInsideProxy userInsideProxy){
        this.PK = userInsideProxy.getSysUserRowId().toString();
        this.loginName= userInsideProxy.getLoginName();
        this.userName = userInsideProxy.getUserName();
        this.userType = userInsideProxy.getUserType();
        this.companyRowId = String.valueOf(userInsideProxy.getSysCompanyRowId());
        this.companyId = userInsideProxy.getCompanyId();
        this.companyDefineCd = userInsideProxy.getCompanyDefineCd();
        this.companyName  = userInsideProxy.getCompanyName();
        this.loginTime  = userInsideProxy.getLoginTime();
        this.loginAudRowId  = userInsideProxy.getLoginAudRowId();
        List<RoleInsideProxy> roleList = userInsideProxy.getRoleList();
        List<SysUserDataAccess> dataAccesses = userInsideProxy.getDataAccessList();
        List<SysRelUserStation> stationList = userInsideProxy.getStationList();
        if(roleList!=null&&roleList.size()>0){
            roles =  new HashMap<String,String>();
            for(RoleInsideProxy role:roleList){
                roles.put(String.valueOf(role.getSysRoleRowId()), role.getRoleName());
            }
        }
        if(dataAccesses!=null&&dataAccesses.size()>0){
            areas =  new HashMap<String,String>();
            for(SysUserDataAccess dataAccess:dataAccesses){
                areas.put(String.valueOf(dataAccess.getCompanyId()),dataAccess.getCompanyName());
            }
        }

        if(dataAccesses!=null&&dataAccesses.size()>0){
            areaRows =  new HashMap<String,String>();
            for(SysUserDataAccess dataAccess:dataAccesses){
                areaRows.put(String.valueOf(dataAccess.getSysCompanyRowId()),dataAccess.getCompanyName());
            }
        }

        if(stationList!=null&&stationList.size()>0){
            posts =  new HashMap<String,String>();
            for(SysRelUserStation station:stationList){
                posts.put(String.valueOf(station.getSysStaRowId()),station.getSysStaName());
            }
        }
    }
}
