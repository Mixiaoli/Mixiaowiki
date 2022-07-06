package com.mixiao.service;

import com.mixiao.domain.Ebook;
import com.mixiao.domain.EbookExample;
import com.mixiao.mapper.EbookMapper;
import com.mixiao.req.EbookReq;
import com.mixiao.resp.EbookResp;
import com.mixiao.util.CopyUtil;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import javax.annotation.Resource;
import java.util.List;

@Service//需要声明服务层才会扫描到
public class EbookService {

    @Resource //jdk自带的注入 @Autowired spring自带的
    private EbookMapper ebookMapper;

    public List<EbookResp> list(EbookReq req){
        EbookExample ebookExample = new EbookExample();
        EbookExample.Criteria criteria = ebookExample.createCriteria();
        if (!ObjectUtils.isEmpty(req.getName())) {
            criteria.andNameLike("%" + req.getName() + "%");
        }
        //改返回值-实体类转换用for循环
        List<Ebook> ebookList = ebookMapper.selectByExample(ebookExample);
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
        return list;
    }
}
