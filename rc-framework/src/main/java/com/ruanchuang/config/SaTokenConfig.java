package com.ruanchuang.config;

import cn.dev33.satoken.jwt.StpLogicJwtForSimple;
import cn.dev33.satoken.stp.StpInterface;
import cn.dev33.satoken.stp.StpLogic;
import com.ruanchuang.constant.Constants;
import com.ruanchuang.domain.SysUser;
import com.ruanchuang.utils.LoginUtils;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.ArrayList;
import java.util.List;

/**
 * @Author guopeixiong
 * @Date 2023/7/29
 * @Email peixiongguo@163.com
 */
@Configuration
public class SaTokenConfig implements StpInterface {

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
        SysUser userInfo = LoginUtils.getLoginUser();
        ArrayList<String> roleId = new ArrayList<>();
        String role = null;
        /**
         * 0.普通用户
         * 1.系统管理员
         */
        switch (userInfo.getType().intValue()) {
            case 0 -> role = Constants.USER_TYPE_NORMAL;
            case 1 -> role = Constants.USER_TYPE_ADMIN;
        }
        roleId.add(role);
        return roleId;
    }
}
