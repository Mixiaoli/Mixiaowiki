package com.mixiao.service;

import com.mixiao.domain.Ebook;
import com.mixiao.domain.EbookExample;
import com.mixiao.mapper.EbookMapper;
import com.mixiao.req.EbookReq;
import com.mixiao.resp.EbookResp;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

@Service//需要声明服务层才会扫描到
public class EbookService {

    @Resource //jdk自带的注入 @Autowired spring自带的
    private EbookMapper ebookMapper;

    public List<EbookResp> list(EbookReq req){
        EbookExample ebookExample = new EbookExample();
        EbookExample.Criteria criteria = ebookExample.createCriteria();
        criteria.andNameLike("%" +req.getName()+ "%");
        //改返回值
        List<Ebook> ebookList = ebookMapper.selectByExample(ebookExample);

        List<EbookResp> respList = new ArrayList<>();
        for (Ebook ebook : ebookList){
            EbookResp ebookResp = new EbookResp();
            ebookResp.setId(ebook.getId());
            BeanUtils.copyProperties(ebook,ebookResp);
            respList.add(ebookResp);
        }
        return respList;
    }
}
