package com.mixiao.service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.mixiao.domain.Ebook;
import com.mixiao.domain.EbookExample;
import com.mixiao.mapper.EbookMapper;
import com.mixiao.req.EbookReq;
import com.mixiao.resp.EbookResp;
import com.mixiao.resp.PageResp;
import com.mixiao.util.CopyUtil;
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

    public PageResp<EbookResp> list(EbookReq req){
        EbookExample ebookExample = new EbookExample();
        EbookExample.Criteria criteria = ebookExample.createCriteria();
        if (!ObjectUtils.isEmpty(req.getName())) {
            criteria.andNameLike("%" + req.getName() + "%");
        }
        //改返回值-实体类转换用for循环
        PageHelper.startPage(req.getPage(),req.getSize());//使用PageHelper分页插件 1页3条 改成动态方式获取 需要查多少 请求参数
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
        List<EbookResp> list = CopyUtil.copyList(ebookList, EbookResp.class);
        PageResp<EbookResp> pageResp = new PageResp(); //返回参数
        pageResp.setTotal(pageInfo.getTotal());
        pageResp.setList(list);
        return pageResp;
    }
}
