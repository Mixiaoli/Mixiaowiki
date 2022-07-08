package com.mixiao.controller;

import com.mixiao.req.CategoryQueryReq;
import com.mixiao.req.CategorySaveReq;
import com.mixiao.resp.CommonResp;
import com.mixiao.resp.CategoryQueryResp;
import com.mixiao.resp.PageResp;
import com.mixiao.service.CategoryService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.Valid;

@RestController //返回字符串 或JSON对象(更多是返回JSON对象)
@Controller //返回一个页面
@RequestMapping("/category")//category/list
public class CategoryController {

    @Resource //注入进来
    private CategoryService categoryService;
    //@Valid //控制层接收参数在进行绑定注解校验规则的时候出现的异常后抛出
    @GetMapping("/list") //定义URL路径 表示这个接口支持所以请求方式 POST GET...
    public CommonResp list(@Valid CategoryQueryReq req){//req-CategoryReq 请求 设置统一请求这样可以写很多不同类型参数
        //要返回CommonResp的统一类型 new他的对象
        /**接口定义的泛型所以他里面也要是list<Category>(自定义类型),注入的控制层将掉到的数据返回回来给List
            resp.存储list数据 返回resp
         **/
        CommonResp<PageResp<CategoryQueryResp>> resp = new CommonResp<>();
        PageResp<CategoryQueryResp> list = categoryService.list(req);
        resp.setContent(list);
        return resp;
    }
    //保存编辑数据
    @PostMapping("/save") //定义URL路径 表示这个接口支持所以请求方式 POST GET...
    public CommonResp save(@Valid @RequestBody CategorySaveReq req){//请求保存参数
        //@RequestBody这个注解对应的就是json方式(POST提交)就行我们写这个book，这个是用content-type是appplication/sjon
        CommonResp resp = new CommonResp<>();
        categoryService.save(req);
        return resp;
    }

    //删除文章
    @DeleteMapping("/delete/{id}") //id跟下面注解映射
    public CommonResp delete(@PathVariable Long id){//请求保存参数 @path可以将 URL 中占位符参数绑定到控制器处理方法的入参
        //@RequestBody这个注解对应的就是json方式(POST提交)就行我们写这个book，这个是用content-type是appplication/sjon
        CommonResp resp = new CommonResp<>();
        categoryService.delete(id);
        return resp;
    }
}
