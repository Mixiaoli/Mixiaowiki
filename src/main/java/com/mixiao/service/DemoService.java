package com.mixiao.service;

import com.mixiao.domain.Demo;
import com.mixiao.mapper.DemoMapper;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service//需要声明服务层才会扫描到
public class DemoService {

    @Resource //jdk自带的注入 @Autowired spring自带的
    private DemoMapper demoMapper;

    public List<Demo> list() {
        return demoMapper.selectByExample(null);
    }
}
