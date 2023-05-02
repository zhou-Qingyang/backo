package com.xiaozhou.backo.admin;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

/**
 * @Description: TODO(这里用一句话描述这个类的作用)
 * @date: 2023年1月19日 下午11:00:50
 */
@SpringBootApplication
@ComponentScan(basePackages = "com.xiaozhou.backo.common.*,com.xiaozhou.backo.admin.*,com.xiaozhou.backo.framework.*,com.xiaozhou.backo.system.*")
@MapperScan("com.xiaozhou.backo.system.mapper")
public class BackoAdminApplication {

	public static void main(String[] args) {
		SpringApplication.run(BackoAdminApplication.class, args);
		System.out.println("Xiaozhou管理系统启动成功~~~~");
	}

}
