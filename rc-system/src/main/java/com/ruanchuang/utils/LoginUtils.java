package com.ruanchuang.utils;

import cn.dev33.satoken.context.SaHolder;
import cn.dev33.satoken.context.model.SaStorage;
import cn.dev33.satoken.session.SaSession;
import cn.dev33.satoken.stp.SaLoginModel;
import cn.dev33.satoken.stp.StpUtil;
import com.ruanchuang.domain.SysUser;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

/**
 * @Author guopeixiong
 * @Date 2023/8/1
 * @Email peixiongguo@163.com
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class LoginUtils {

    public static final String LOGIN_USER_KEY = "loginUser";

    public static final String USER_KEY = "userId";

    /**
     * 用户登录
     * @param user
     */
    public static String login(SysUser user) {
        SaStorage storage = SaHolder.getStorage();
        storage.set(LOGIN_USER_KEY, user);
        storage.set(USER_KEY, user.getId());
        SaLoginModel model = new SaLoginModel();
        StpUtil.login(user.getId(), model.setExtra(USER_KEY, user.getId()));
        StpUtil.getTokenSession().set(LOGIN_USER_KEY, user);
        return StpUtil.getTokenValue();
    }

    /**
     * 基于多级缓存获取登录用户
     * @return
     */
    public static SysUser getLoginUser() {
        SysUser user = (SysUser) SaHolder.getStorage().get(LOGIN_USER_KEY);
        if (user != null) {
            return user;
        }
        SaSession session = StpUtil.getTokenSession();
        if (session == null) {
            return null;
        }
        user = (SysUser) session.get(LOGIN_USER_KEY);
        SaHolder.getStorage().set(LOGIN_USER_KEY, user);
        return user;
    }

    /**
     * 基于token获取登录用户信息
     * @param token
     * @return
     */
    public static SysUser getLoginUser(String token) {
        SaSession session = StpUtil.getTokenSessionByToken(token);
        if (session == null) {
            return null;
        }
        return (SysUser) session.get(LOGIN_USER_KEY);
    }

    /**
     * 退出登录
     */
    public static void logout() {
        SaStorage storage = SaHolder.getStorage();
        storage.delete(LOGIN_USER_KEY);
        storage.delete(USER_KEY);
        SaSession session = StpUtil.getTokenSession();
        if (session != null) {
            session.delete(LOGIN_USER_KEY);
        }
        StpUtil.logout();
    }

    /**
     * 忽略敏感信息
     * @param user
     */
    public static void ignoreSensitiveInformation(SysUser user) {
        user.setPassword(null)
                .setSalt(null)
                .setType(null)
                .setStatus(null)
                .setCreateBy(null)
                .setCreateTime(null)
                .setUpdateBy(null)
                .setUpdateTime(null)
                .setIsDelete(null)
                .setVersion(null);
    }

}
