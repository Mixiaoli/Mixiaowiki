package com.mixiao.service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.mixiao.domain.Ebook;
import com.mixiao.domain.EbookExample;
import com.mixiao.mapper.EbookMapper;
import com.mixiao.req.EbookQueryReq;
import com.mixiao.req.EbookSaveReq;
import com.mixiao.resp.EbookQueryResp;
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
public class EbookService {
    private static final Logger LOG = LoggerFactory.getLogger(EbookService.class);//把代码模板导入进来
    @Resource //jdk自带的注入 @Autowired spring自带的
    private EbookMapper ebookMapper;

    @Resource //jdk自带的注入 @Autowired spring自带的
    private SnowFlake snowFlake;//实例化

    public PageResp<EbookQueryResp> list(EbookQueryReq req) {
        EbookExample ebookExample = new EbookExample();
        EbookExample.Criteria criteria = ebookExample.createCriteria();
        if (!ObjectUtils.isEmpty(req.getName())) {
            criteria.andNameLike("%" + req.getName() + "%");
        }
        if (!ObjectUtils.isEmpty(req.getCategoryId2())) {
            criteria.andCategory2IdEqualTo(req.getCategoryId2());
        }
        //改返回值-实体类转换用for循环
        PageHelper.startPage(req.getPage(), req.getSize());//使用PageHelper分页插件 1页3条 改成动态方式获取 需要查多少 请求参数
        List<Ebook> ebookList = ebookMapper.selectByExample(ebookExample);

        PageInfo<Ebook> pageInfo = new PageInfo<>(ebookList);
        LOG.info("总行数：{}", pageInfo.getTotal());
        LOG.info("总页数：{}", pageInfo.getPages());
        //单体
        /**List<EbookResp> respList = new ArrayList<>();
         for (Ebook ebook : ebookList){
         //EbookResp ebookResp = new EbookResp();
         //BeanUtils.copyProperties(ebook,ebookResp);
         //对象复制
         EbookResp ebookResp = CopyUtil.copy(ebook, EbookResp.class);
         respList.add(ebookResp);
         }**/
        //列表
        List<EbookQueryResp> list = CopyUtil.copyList(ebookList, EbookQueryResp.class);
        PageResp<EbookQueryResp> pageResp = new PageResp(); //返回参数
        pageResp.setTotal(pageInfo.getTotal());
        pageResp.setList(list);
        return pageResp;
    }

    /**
     * 保存方法
     */
    public void save(EbookSaveReq req) {
        Ebook ebook = CopyUtil.copy(req, Ebook.class);//将请求参数变成实体传进来
        if (ObjectUtils.isEmpty(req.getId())) {
            //新增
            ebook.setId(snowFlake.nextId());
            ebookMapper.insert(ebook);//使用Mybatis,并且使用代码生成器后，就不需要去写sql语句了,都给你写好了
        } else {
            //更新
            ebookMapper.updateByPrimaryKey(ebook);//根据主建来更新
        }
    }

    /**
     * 删除方法
     */
    //删除文章
    public void delete(Long id) {
        ebookMapper.deleteByPrimaryKey(id);//根据Id
    }
}
