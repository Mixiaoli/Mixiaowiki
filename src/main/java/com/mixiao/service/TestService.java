package com.mixiao.service;

import com.mixiao.domain.Test;
import com.mixiao.mapper.TestMapper;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service//需要声明服务层才会扫描到
public class TestService {

    @Resource //jdk自带的注入 @Autowired spring自带的
    private TestMapper testMapper;

    public List<Test> list(){
        return testMapper.list();
    }
}
