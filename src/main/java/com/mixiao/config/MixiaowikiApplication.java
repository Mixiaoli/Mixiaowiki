package com.mixiao.config;

import org.mybatis.spring.annotation.MapperScan;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.core.env.Environment;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * @SpringBootApplication注解执行
 *
 * SpringBootConfiguration 配置类
 *
 * EnableAutoConfiguration 开启自动配置功能
 *
 * ComponentScan() 扫码包
 *
 * **/

@SpringBootApplication
//@ComponentScan({"com.mixiao","com.test"}) 扫描多个包
@ComponentScan("com.mixiao")//去寻找执行com.mixiao下的各种包
@MapperScan("com.mixiao.mapper")//让系统知道Mapper文件夹是持久层
@EnableScheduling
@EnableAsync //启动而异步化 另外起一个线程执行后面的内容
public class MixiaowikiApplication {
    private static final Logger LOG = LoggerFactory.getLogger(MixiaowikiApplication.class);
    public static void main(String[] args) {
        //new app 这个主文件.class 然后run主文件 启动后去初始化找Bean 下都有什么 执行什么
        SpringApplication app = new SpringApplication(MixiaowikiApplication.class);
        //getProperty 获取 properties 属性文件值的方法 然后赋值给dev然后才能获取打印端口号
        Environment env = app.run(args).getEnvironment();
        LOG.info("启动成功！！");
        LOG.info("地址: \thttp://127.0.0.1:{}", env.getProperty("server.port"));
    }

}
