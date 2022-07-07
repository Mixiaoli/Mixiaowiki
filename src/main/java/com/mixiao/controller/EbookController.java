package com.mixiao.controller;

import com.mixiao.req.EbookReq;
import com.mixiao.resp.CommonResp;
import com.mixiao.resp.EbookResp;
import com.mixiao.resp.PageResp;
import com.mixiao.service.EbookService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@RestController //返回字符串 或JSON对象(更多是返回JSON对象)
@Controller //返回一个页面
@RequestMapping("/ebook")//ebook/list
public class EbookController {

    @Resource //注入进来
    private EbookService ebookService;

    @GetMapping("/list") //定义URL路径 表示这个接口支持所以请求方式 POST GET...
    public CommonResp list(EbookReq req){//req-EbookReq 请求 设置统一请求这样可以写很多不同类型参数
        //要返回CommonResp的统一类型 new他的对象
        /**接口定义的泛型所以他里面也要是list<Ebook>(自定义类型),注入的控制层将掉到的数据返回回来给List
            resp.存储list数据 返回resp
         **/
        CommonResp<PageResp<EbookResp>> resp = new CommonResp<>();
        PageResp<EbookResp> list = ebookService.list(req);
        resp.setContent(list);
        return resp;
    }
}
