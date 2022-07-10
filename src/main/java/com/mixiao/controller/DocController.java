package com.mixiao.controller;

import com.mixiao.req.DocQueryReq;
import com.mixiao.req.DocSaveReq;
import com.mixiao.resp.CommonResp;
import com.mixiao.resp.DocQueryResp;
import com.mixiao.resp.PageResp;
import com.mixiao.service.DocService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.Valid;
import java.util.Arrays;
import java.util.List;

@RestController //返回字符串 或JSON对象(更多是返回JSON对象)
@Controller //返回一个页面
@RequestMapping("/doc")//doc/list
public class DocController {

    @Resource //注入进来
    private DocService docService;
    //@Valid //控制层接收参数在进行绑定注解校验规则的时候出现的异常后抛出
    @GetMapping("/all/{ebookId}") //定义URL路径 表示这个接口支持所以请求方式 POST GET...
    public CommonResp all(@PathVariable Long ebookId){//req-DocReq 请求 设置统一请求这样可以写很多不同类型参数
        CommonResp<List<DocQueryResp>> resp = new CommonResp<>();
        List<DocQueryResp> list = docService.all(ebookId);
        resp.setContent(list);
        return resp;
    }
    @GetMapping("/list") //定义URL路径 表示这个接口支持所以请求方式 POST GET...
    public CommonResp list(@Valid DocQueryReq req){//req-DocReq 请求 设置统一请求这样可以写很多不同类型参数
        //要返回CommonResp的统一类型 new他的对象
        /**接口定义的泛型所以他里面也要是list<Doc>(自定义类型),注入的控制层将掉到的数据返回回来给List
            resp.存储list数据 返回resp
         **/
        CommonResp<PageResp<DocQueryResp>> resp = new CommonResp<>();
        PageResp<DocQueryResp> list = docService.list(req);
        resp.setContent(list);
        return resp;
    }
    //保存编辑数据
    @PostMapping("/save") //定义URL路径 表示这个接口支持所以请求方式 POST GET...
    public CommonResp save(@Valid @RequestBody DocSaveReq req){//请求保存参数
        //@RequestBody这个注解对应的就是json方式(POST提交)就行我们写这个book，这个是用content-type是appplication/sjon
        CommonResp resp = new CommonResp<>();
        docService.save(req);
        return resp;
    }

    //删除文章
    @DeleteMapping("/delete/{idsStr}")
    public CommonResp delete(@PathVariable String idsStr) {
        CommonResp resp = new CommonResp<>();
        List<String> list = Arrays.asList(idsStr.split(","));
        docService.delete(list);
        return resp;
    }

    @GetMapping("/find-content/{id}") //定义URL路径 表示这个接口支持所以请求方式 POST GET...
    public CommonResp findContent(@PathVariable Long id){//req-DocReq 请求 设置统一请求这样可以写很多不同类型参数
        CommonResp<String> resp = new CommonResp<>();
        String content = docService.findContent(id);
        resp.setContent(content);
        return resp;
    }
}
