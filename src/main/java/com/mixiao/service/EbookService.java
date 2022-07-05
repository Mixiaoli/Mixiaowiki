package com.mixiao.service;

import com.mixiao.domain.Ebook;
import com.mixiao.mapper.EbookMapper;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service//需要声明服务层才会扫描到
public class EbookService {

    @Resource //jdk自带的注入 @Autowired spring自带的
    private EbookMapper ebookMapper;

    public List<Ebook> list(){
        return ebookMapper.selectByExample(null);
    }
}
