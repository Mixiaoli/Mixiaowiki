package com.mixiao;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.core.env.Environment;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;



@SpringBootApplication
@EnableScheduling
@EnableAsync
public class MixiaowikiApplication {

    private static final Logger LOG = LoggerFactory.getLogger(MixiaowikiApplication.class);

    public static void main(String[] args) {
        //new app 这个主文件.class 然后run主文件
        SpringApplication app = new SpringApplication(MixiaowikiApplication.class);
        //getProperty 获取 properties 属性文件值的方法 然后赋值给dev然后才能获取打印端口号
        Environment env = app.run(args).getEnvironment();
        LOG.info("启动成功！！");
        LOG.info("地址: \thttp://127.0.0.1:{}", env.getProperty("server.port"));
    }

}
