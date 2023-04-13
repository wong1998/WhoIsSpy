package com.wangzf;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;

//必须要有包名
//如果不需要数据库，需要排除数据源设置
@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class})
public class WhoIsSpyApplication {

    public static void main(String[] args) {
        SpringApplication.run(WhoIsSpyApplication .class, args);
    }

}