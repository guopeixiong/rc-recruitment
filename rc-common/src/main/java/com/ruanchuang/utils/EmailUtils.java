package com.ruanchuang.utils;

import com.ruanchuang.exception.ServiceException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * @Author guopeixiong
 * @Date 2023/8/6
 * @Email peixiongguo@163.com
 */
@Slf4j
@Component
public class EmailUtils {

    @Resource
    private JavaMailSenderImpl javaMailSender;

    @Value("${spring.mail.username}")
    private String userName;

    @Resource(name = "businessThreadPool")
    private ThreadPoolTaskExecutor threadPoolTaskExecutor;

    /**
     * 发送验证码邮件
     * @param targetEmail
     * @param code
     * @param codeType
     */
    public void sendCode(String targetEmail, String code, String codeType) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setSubject(codeType);
        message.setText("【软件创新实验室】\n您的验证码是" + code + "，有效期5分钟，如非本人操作请勿略");
        message.setFrom(userName);
        message.setTo(targetEmail);
        threadPoolTaskExecutor.execute(() -> {
            try {
                javaMailSender.send(message);
            } catch (MailException e) {
                log.error("email send error, target email: \"{}\", error details: {}", targetEmail, e.getMessage());
            }
        });
    }

    /**
     * 发送管理员账号密码
     * @param targetEmail
     * @param password
     */
    public void sendAdminAccountPassword(String targetEmail, String password, String account) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setSubject("管理员账号开通通知");
        message.setText("【软件创新实验室】\n您的管理员账号开通，账号：" + account + "，初始密码为：" + password);
        message.setFrom(userName);
        message.setTo(targetEmail);
        threadPoolTaskExecutor.execute(() -> {
            try {
                javaMailSender.send(message);
            } catch (MailException e) {
                log.error("email send error, target email: \"{}\", error details: {}", targetEmail, e.getMessage());
            }
        });
    }

    /**
     * 测试邮件发送
     * @param targetEmail
     */
    public void testEmailCanSend(String targetEmail) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setSubject("管理员账号开通通知");
        message.setText("【软件创新实验室】\n你好，你的管理员账户正在开通中，稍后账号密码将会发送到此邮箱");
        message.setFrom(userName);
        message.setTo(targetEmail);
        try {
            javaMailSender.send(message);
        } catch (MailException e) {
            throw new ServiceException("开通失败：通知邮件发送失败，请稍后再试");
        }
    }

}
