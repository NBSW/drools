package com.zjhcsoft.qin.exchange.utils;


import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;

/**
 * 通用文件下载类.
 * 
 * @author longquan .
 * 
 */
public class DownloadFileHelper {
	
	public static final String XLS=".xls";
	
	
	
	/**
	* 提供文件下载
	* @Title: downLoad
	* @param  downloadName  用户所看到得下载名称
	* @param  filePath  虚拟文件路径
	* @param  fileName  虚拟文件名称
	* @return String    返回类型
	* @throws
	 */
	public static void downLoad(HttpServletResponse response, HttpServletRequest request,String downloadName,String filePath,String fileName){
		BufferedInputStream bis = null;
		BufferedOutputStream bos = null;
		try {
			File file = new File(filePath + fileName);
			response.setContentType("application/x-msdownload");
			if (request.getHeader("User-Agent").indexOf("MSIE 5.5") != -1) {
				// IE5.5特别处理
				response.setHeader("Content-disposition", "filename="+new String(downloadName.getBytes("gb2312"),"iso8859-1"));
			} else {
				// 其它的Header设定方式
				response.setHeader("Content-disposition","attachment; filename="+new String(downloadName.getBytes("gb2312"),"iso8859-1"));
			}
			try {
				bis = new BufferedInputStream(new FileInputStream(file));
				bos = new BufferedOutputStream(response.getOutputStream());
				byte[] buff = new byte[4096];
				int bytesRead;
				while (-1 != (bytesRead = bis.read(buff, 0, buff.length))) {
					bos.write(buff, 0, bytesRead);
				}
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				if (bis != null)
					bis.close();
				if (bos != null)
					bos.close();
			}

		} catch (Throwable e) {
			e.printStackTrace();
			try {
				throw new ServletException(e.toString());
			} catch (ServletException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		}
		
	}
	
}
