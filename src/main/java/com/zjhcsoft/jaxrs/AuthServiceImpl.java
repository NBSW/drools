package com.zjhcsoft.jaxrs;

import com.zjhcsoft.biap.adaptor.objectProxy.UserInsideProxy;
import com.zjhcsoft.biap.adaptor.service.ObjectAccessor;
import com.zjhcsoft.qin.exchange.security.BaseAuthedInfo;
import com.zjhcsoft.qin.exchange.service.ServiceHelper;
import com.zjhcsoft.qin.exchange.utils.PropertyHelper;
import com.zjhcsoft.shreport.webservice.ShReportRest;
import org.apache.cxf.jaxrs.client.JAXRSClientFactory;

/**
 * Created by XuanLubin on 2014/4/16. 19:31
 */
public class AuthServiceImpl extends AuthService<ShReportRest> {

    private ObjectAccessor objectAccessor;

    @Override
    public ShReportRest create() {
        if (SH_REPORT_REST_THREAD_LOCAL.get() == null) {
            BaseAuthedInfo authedInfo = ServiceHelper.getCurrentAuthedInfo();
            if(null==authedInfo){
                authedInfo = new BaseAuthedInfo();
                authedInfo.PK = PropertyHelper.get("biap.admin.rowid");
            }
            UserInsideProxy proxy = objectAccessor.getUserNoSecure(Long.parseLong(authedInfo.PK));
            SH_REPORT_REST_THREAD_LOCAL.set(JAXRSClientFactory.create(serviceUrl,serviceClass,proxy.getLoginName(),proxy.getPassword(),null));
        }
        return SH_REPORT_REST_THREAD_LOCAL.get();
    }

    public void setObjectAccessor(ObjectAccessor objectAccessor) {
        this.objectAccessor = objectAccessor;
    }

    private static final ThreadLocal<ShReportRest> SH_REPORT_REST_THREAD_LOCAL = new ThreadLocal<>();
}
