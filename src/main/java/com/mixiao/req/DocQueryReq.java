package com.mixiao.req;

//继承了分页请求参数封装 分页作为父类这样 调用的请求参数就不用改
public class DocQueryReq extends PageReq {

    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "DocQueryReq{} " + super.toString();
    }

}