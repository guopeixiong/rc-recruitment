package com.ruanchuang.handler;

import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import com.ruanchuang.domain.SysUser;
import com.ruanchuang.utils.LoginUtils;
import org.apache.ibatis.reflection.MetaObject;

import java.time.LocalDateTime;

/**
 * 处理mybatis plus自动填充
 * @Author guopeixiong
 * @Date 2023/8/2
 * @Email peixiongguo@163.com
 */
public class CreateAndUpdateMetaObjectHandler implements MetaObjectHandler {
    @Override
    public void insertFill(MetaObject metaObject) {
        String token = RequestContextHandler.getUserToken();
        this.strictInsertFill(metaObject, "createTime", LocalDateTime.class, LocalDateTime.now());
        if (token != null) {
            SysUser user = LoginUtils.getLoginUser(token);
            if (user == null) {
                return;
            }
            this.strictInsertFill(metaObject, "createBy", String.class, user.getFullName());
        }
    }

    @Override
    public void updateFill(MetaObject metaObject) {
        String token = RequestContextHandler.getUserToken();
        this.strictUpdateFill(metaObject, "updateTime", LocalDateTime.class, LocalDateTime.now());
        if (token != null) {
            SysUser user = LoginUtils.getLoginUser(token);
            if (user == null) {
                return;
            }
            this.strictUpdateFill(metaObject, "updateBy", String.class, user.getFullName());
        }
    }

}
