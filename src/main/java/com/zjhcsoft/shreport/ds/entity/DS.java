package com.zjhcsoft.shreport.ds.entity;

import com.zjhcsoft.qin.exchange.entity.SecurityEntity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "DS")
public class DS extends SecurityEntity {

    private String name;
    private String driver;
    private String url;
    private String username;
    private String password;
    private int initialSize;
    private int maxActive;
    private int minIdle;
    private int maxIdle;
    private int maxWait;
    private String[] appCodes;

    private String[] roleCodes;
    private String[] positionCodes;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDriver() {
        return driver;
    }

    public void setDriver(String driver) {
        this.driver = driver;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public int getInitialSize() {
        return initialSize;
    }

    public void setInitialSize(int initialSize) {
        this.initialSize = initialSize;
    }

    public int getMaxActive() {
        return maxActive;
    }

    public void setMaxActive(int maxActive) {
        this.maxActive = maxActive;
    }

    public int getMinIdle() {
        return minIdle;
    }

    public void setMinIdle(int minIdle) {
        this.minIdle = minIdle;
    }

    public int getMaxIdle() {
        return maxIdle;
    }

    public void setMaxIdle(int maxIdle) {
        this.maxIdle = maxIdle;
    }

    public int getMaxWait() {
        return maxWait;
    }

    public void setMaxWait(int maxWait) {
        this.maxWait = maxWait;
    }

    public String[] getAppCodes() {
        return appCodes;
    }

    public void setAppCodes(String[] appCodes) {
        this.appCodes = appCodes;
    }

    public String[] getRoleCodes() {
        return roleCodes;
    }

    public void setRoleCodes(String[] roleCodes) {
        this.roleCodes = roleCodes;
    }

    public String[] getPositionCodes() {
        return positionCodes;
    }

    public void setPositionCodes(String[] positionCodes) {
        this.positionCodes = positionCodes;
    }
}
