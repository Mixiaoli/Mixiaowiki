package com.mixiao.service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.mixiao.domain.Content;
import com.mixiao.domain.Doc;
import com.mixiao.domain.DocExample;
import com.mixiao.mapper.ContentMapper;
import com.mixiao.mapper.DocMapper;
import com.mixiao.req.DocQueryReq;
import com.mixiao.req.DocSaveReq;
import com.mixiao.resp.DocQueryResp;
import com.mixiao.resp.PageResp;
import com.mixiao.util.CopyUtil;
import com.mixiao.util.SnowFlake;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import javax.annotation.Resource;
import java.util.List;

@Service//需要声明服务层才会扫描到
public class DocService {
    private static final Logger LOG = LoggerFactory.getLogger(DocService.class);//把代码模板导入进来
    @Resource //jdk自带的注入 @Autowired spring自带的
    private DocMapper docMapper;

    @Resource //jdk自带的注入 @Autowired spring自带的
    private ContentMapper contentMapper;

    @Resource //jdk自带的注入 @Autowired spring自带的
    private SnowFlake snowFlake;//实例化

    public List<DocQueryResp> all(){
        DocExample docExample = new DocExample();
        docExample.setOrderByClause("sort asc");
        List<Doc> docList = docMapper.selectByExample(docExample);
        //列表复制
        List<DocQueryResp> list = CopyUtil.copyList(docList, DocQueryResp.class);
        return list;
    }

    //查询方法
    public PageResp<DocQueryResp> list(DocQueryReq req){
        DocExample docExample = new DocExample();
        DocExample.Criteria criteria = docExample.createCriteria();
        if (!ObjectUtils.isEmpty(req.getName())) {
            criteria.andNameLike("%" + req.getName() + "%");
        }
        //改返回值-实体类转换用for循环
        PageHelper.startPage(req.getPage(),req.getSize());//使用PageHelper分页插件 1页3条 改成动态方式获取 需要查多少 请求参数
        List<Doc> docList = docMapper.selectByExample(docExample);

        PageInfo<Doc> pageInfo = new PageInfo<>(docList);
        LOG.info("总行数：{}", pageInfo.getTotal());
        LOG.info("总页数：{}", pageInfo.getPages());
        //单体
        /**List<DocResp> respList = new ArrayList<>();
         for (Doc doc : docList){
         //DocResp docResp = new DocResp();
         //BeanUtils.copyProperties(doc,docResp);
         //对象复制
         DocResp docResp = CopyUtil.copy(doc, DocResp.class);
         respList.add(docResp);
         }**/
        //列表
        List<DocQueryResp> list = CopyUtil.copyList(docList, DocQueryResp.class);
        PageResp<DocQueryResp> pageResp = new PageResp(); //返回参数
        pageResp.setTotal(pageInfo.getTotal());
        pageResp.setList(list);
        return pageResp;
    }

    /**保存方法
     *
     */
    public void save(DocSaveReq req){
        Doc doc = CopyUtil.copy(req,Doc.class);//将请求参数变成实体传进来
        Content content = CopyUtil.copy(req,Content.class);//将请求参数变成实体传进来
        if (ObjectUtils.isEmpty(req.getId())){
            //新增
            doc.setId(snowFlake.nextId());
            docMapper.insert(doc);//使用Mybatis,并且使用代码生成器后，就不需要去写sql语句了,都给你写好了

            content.setId(doc.getId());
            contentMapper.insert(content);
        }else {
            //更新
            docMapper.updateByPrimaryKey(doc);//根据主建来更新
            int count=contentMapper.updateByPrimaryKeyWithBLOBs(content);//全部字段 大字段
            if (count == 0){
                contentMapper.insert(content);
            }
        }
    }
    /**删除方法
     *
     */
    //删除文章
    public void delete(List<String> ids){
        DocExample docExample = new DocExample();
        DocExample.Criteria criteria = docExample.createCriteria();
        criteria.andIdIn(ids);
        docMapper.deleteByExample( docExample);//根据Id
    }
}
