package com.ruanchuang;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @Author guopeixiong
 * @Date 2023/7/29
 * @Email peixiongguo@163.com
 */
@MapperScan("com.ruanchuang.**.mapper")
@SpringBootApplication
public class Main {

    public static void main(String[] args) {
        SpringApplication.run(Main.class, args);
    }

}