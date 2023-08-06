package com.ruanchuang.listener;

import com.ruanchuang.utils.SpringUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.SmartLifecycle;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Component;

/**
 * @Author guopeixiong
 * @Date 2023/8/6
 * @Email peixiongguo@163.com
 */
@Slf4j
@Component
public class AppListener implements SmartLifecycle {

    private boolean isRunning = false;

    @Override
    public void start() {
        log.info("======项目启动======");
        this.isRunning = true;
    }

    @Override
    public void stop() {
        log.info("======项目关闭开始======");
        log.info("线程池拒新任务, 准备关闭");
        ThreadPoolTaskExecutor businessThreadPool = SpringUtils.getBean("businessThreadPool");
        ThreadPoolTaskExecutor systemThreadPool = SpringUtils.getBean("systemThreadPool");
        businessThreadPool.shutdown();
        systemThreadPool.shutdown();
        log.info("======项目关闭======");
    }

    @Override
    public boolean isRunning() {
        return this.isRunning;
    }
}
