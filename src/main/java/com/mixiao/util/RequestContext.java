package com.mixiao.util;


import java.io.Serializable;
//线程本地变量 看作一条线 某IP 做IP获取 每个IP只能点赞一次
public class RequestContext implements Serializable {

    private static ThreadLocal<String> remoteAddr = new ThreadLocal<>();

    public static String getRemoteAddr() {
        return remoteAddr.get();
    }

    public static void setRemoteAddr(String remoteAddr) {
        RequestContext.remoteAddr.set(remoteAddr);
    }

}
