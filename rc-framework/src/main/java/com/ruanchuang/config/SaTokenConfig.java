package com.ruanchuang.config;

import cn.dev33.satoken.jwt.StpLogicJwtForSimple;
import cn.dev33.satoken.stp.StpInterface;
import cn.dev33.satoken.stp.StpLogic;
import com.ruanchuang.domain.SysUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.RedisTemplate;
import com.ruanchuang.enums.UserType;

import java.util.ArrayList;
import java.util.List;

/**
 * @Author guopeixiong
 * @Date 2023/7/29
 * @Email peixiongguo@163.com
 */
@Configuration
public class SaTokenConfig implements StpInterface {

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    @Bean
    public StpLogic getStpLogicJwt() {
        return new StpLogicJwtForSimple();
    }

    @Override
    public List<String> getPermissionList(Object o, String s) {
        // 系统暂未开启权限功能 不做配置
        return null;
    }

    @Override
    public List<String> getRoleList(Object loginId, String loginType) {
        Long userId = Long.valueOf((String) loginId);
        SysUser userInfo = (SysUser) redisTemplate.opsForValue().get(userId.toString());
        ArrayList<String> roleId = new ArrayList<>();
        String role = null;
        /**
         * 0.普通用户
         * 1.系统管理员
         */
        switch (userInfo.getType().ordinal()) {
            case 0 -> role = UserType.AVERAGE_USER.getValue().toString();
            case 1 -> role = UserType.ADMIN.getValue().toString();
        }
        roleId.add(role);
        return roleId;
    }
}
