package com.hust;
import com.hust.server.pojo.MailConstants;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.amqp.core.Queue;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * 启动类
 * @EnableScheduling 开启定时任务
 * @author zhoubin
 * @since 1.0.0
 */
@SpringBootApplication
@MapperScan("com.hust.server.mapper")
@EnableScheduling
public class YebServerApplication {

    public static void main(String[] args) {
        SpringApplication.run(YebServerApplication.class,args);
    }
}