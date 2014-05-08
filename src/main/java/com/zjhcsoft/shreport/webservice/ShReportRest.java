package com.zjhcsoft.shreport.webservice;

import com.zjhcsoft.shreport.ds.entity.DS;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import java.util.List;

/**
 * Created by XuanLubin on 2014/4/15. 16:46
 */
@Path(value = "/")
public interface ShReportRest {
    @GET
    @Path("ds/get/{param}")
    public DS get(@PathParam("param") String dsCode);

    @GET
    @Path("ds/list")
    public List<DS> getAll();
}
