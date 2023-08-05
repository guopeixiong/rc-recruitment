package com.ruanchuang.service.impl;

import cn.dev33.satoken.secure.SaSecureUtil;
import cn.hutool.core.util.RandomUtil;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ruanchuang.constant.CacheConstants;
import com.ruanchuang.domain.SysLog;
import com.ruanchuang.domain.SysUser;
import com.ruanchuang.domain.dto.LoginDto;
import com.ruanchuang.domain.dto.RegisterDto;
import com.ruanchuang.enums.BusinessStatus;
import com.ruanchuang.enums.UserType;
import com.ruanchuang.exception.ServiceException;
import com.ruanchuang.mapper.SysUserMapper;
import com.ruanchuang.service.SysLogService;
import com.ruanchuang.service.SysUserService;
import com.ruanchuang.utils.IpUtils;
import com.ruanchuang.utils.JSONUtils;
import com.ruanchuang.utils.LoginUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;

/**
 * <p>
 * 用户信息表 服务实现类
 * </p>
 *
 * @author guopeixiong
 * @since 2023-07-30
 */
@Slf4j
@Service
public class SysUserServiceImpl extends ServiceImpl<SysUserMapper, SysUser> implements SysUserService {

    @Resource(name = "systemThreadPool")
    private ThreadPoolTaskExecutor systemThreadPool;

    @Autowired
    private SysLogService sysLogService;

    @Autowired
    private RedisTemplate redisTemplate;

    /**
     * 手机号或者学号密码登录方式
     *
     * @param loginDto
     * @param request
     * @return
     */
    @Override
    public String loginByPhoneAndPassword(LoginDto loginDto, HttpServletRequest request) {
        // TODO 测试阶段 关闭前端解密
//        loginDto.setPassword(RSAUtils.decryptByRsa(loginDto.getPassword()));
        SysUser user = this.baseMapper.selectOne(Wrappers.<SysUser>lambdaQuery().eq(SysUser::getPhone, loginDto.getAccount()));
        if (user == null) {
            user = this.baseMapper.selectOne(Wrappers.<SysUser>lambdaQuery().eq(SysUser::getStuNum, loginDto.getAccount()));
        }
        if (user == null) {
            throw new ServiceException("账号不存在");
        }
        String password = SaSecureUtil.md5BySalt(loginDto.getPassword(), user.getSalt());
        if (!password.equals(loginDto.getPassword())) {
            saveLoginLog(loginDto, null, request, false);
            throw new ServiceException("密码错误");
        }
        String token = LoginUtils.login(user);
        final Long userId = user.getId();
        this.saveLoginLog(loginDto, userId, request, true);
        return token;
    }

    /**
     * 邮箱验证码登录
     *
     * @param loginDto
     * @param request
     * @return
     */
    @Override
    public String loginByEmailCode(LoginDto loginDto, HttpServletRequest request) {
        String code = (String) redisTemplate.opsForValue().get(CacheConstants.CAPTCHA_CODE_KEY_LOGIN + loginDto.getEmail());
        if (!loginDto.getCode().equals(code)) {
            throw new ServiceException("验证码无效");
        }
        redisTemplate.delete(CacheConstants.CAPTCHA_CODE_KEY_LOGIN + loginDto.getEmail());
        SysUser user = this.baseMapper.selectOne(Wrappers.<SysUser>lambdaQuery().eq(SysUser::getEmail, loginDto.getEmail()));
        String token = LoginUtils.login(user);
        this.saveLoginLog(loginDto, user.getId(), request, true);
        return token;
    }

    /**
     * 用户注册
     * @param registerDto
     */
    @Override
    public void userRegister(RegisterDto registerDto) {
        // TODO 此处还需处理前端密码解密
        String code = (String) redisTemplate.opsForValue().get(CacheConstants.CAPTCHA_CODE_KEY_REGISTER + registerDto.getEmail());
        if (!registerDto.getCode().equals(code)) {
            throw new ServiceException("验证码无效");
        }
        redisTemplate.delete(CacheConstants.CAPTCHA_CODE_KEY_REGISTER + registerDto.getEmail());
        Long count = this.lambdaQuery().eq(SysUser::getStuNum, registerDto.getStuNum()).count();
        if (count.longValue() > 0) {
            throw new ServiceException("该学号已经被注册");
        }
        count = this.lambdaQuery().eq(SysUser::getEmail, registerDto.getEmail()).count();
        if (count.longValue() > 0) {
            throw new ServiceException("该邮箱已被注册");
        }
        String salt = RandomUtil.randomString(6);
        String password = SaSecureUtil.md5BySalt(registerDto.getPassword(), salt);
        SysUser user = new SysUser();
        user.setEmail(registerDto.getEmail())
                .setStuNum(registerDto.getStuNum())
                .setType(UserType.AVERAGE_USER)
                .setStatus(0)
                .setPassword(password)
                .setSalt(salt);
        this.save(user);
    }

    /**
     * 日志记录
     *
     * @param param
     * @param userId
     * @param request
     * @param loginSuccess
     */
    private void saveLoginLog(LoginDto param, Long userId, HttpServletRequest request, boolean loginSuccess) {
        systemThreadPool.execute(() -> {
            if (userId != null) {
                this.lambdaUpdate()
                        .eq(SysUser::getId, userId)
                        .set(SysUser::getLastLogin, LocalDateTime.now())
                        .set(SysUser::getLoginIp, IpUtils.getIpAddr(request))
                        .update();
            }
            SysLog sysLog = new SysLog();
            sysLog.setTitle("用户登录")
                    .setRequestIp(IpUtils.getIpAddr(request))
                    .setRequestParam(JSONUtils.toJsonString(param))
                    .setStatus(loginSuccess ? BusinessStatus.SUCCESS : BusinessStatus.FAIL);
            sysLogService.save(sysLog);
            if (!loginSuccess) {
                log.info("账号: '{}', 尝试登录失败", param.getAccount() == null ? param.getEmail() : param.getAccount());
            }
        });
    }



}
