package com.ruanchuang.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.ruanchuang.domain.SysUser;
import com.ruanchuang.domain.dto.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;

/**
 * <p>
 * 用户信息表 服务类
 * </p>
 *
 * @author guopeixiong
 * @since 2023-07-30
 */
public interface SysUserService extends IService<SysUser> {

    /**
     * 手机号密码登录方式
     *
     * @param loginDto
     * @param request
     * @return
     */
    String loginByPhoneAndPassword(LoginDto loginDto, HttpServletRequest request);

    /**
     * 邮箱验证码登录
     * @param loginDto
     * @param request
     * @return
     */
    String loginByEmailCode(LoginDto loginDto, HttpServletRequest request);

    /**
     * 用户注册
     * @param registerDto
     */
    void userRegister(RegisterDto registerDto);

    /**
     * 用户修改个人信息
     * @param user
     * @return
     */
    boolean updateUserInfo(UpdateUserInfoDto user);

    /**
     * 用户重置密码
     * @param forgetPasswordDto
     */
    void resetPwd(ForgetPasswordDto forgetPasswordDto);

    /**
     * 用户修改密码
     * @param updatePwdDto
     */
    void updatePwd(UpdatePwdDto updatePwdDto);

    /**
     * 用户上传头像
     * @param file
     */
    String uploadAvatar(MultipartFile file);

    /**
     * 管理员登录
     * @param loginDto
     * @param request
     * @return
     */
    String adminLogin(LoginDto loginDto, HttpServletRequest request);

    /**
     * 分页查询普通用户
     * @param userQueryDto
     * @return
     */
    IPage<SysUser> normalList(UserQueryDto userQueryDto);

    /**
     * 修改用户状态
     * @param userstatusDto
     */
    void updateUserStatus(UserStatusDto userstatusDto);

    /**
     * 分页查询管理员用户
     * @param userQueryDto
     * @return
     */
    IPage<SysUser> adminList(UserQueryDto userQueryDto);

    /**
     * 添加管理员
     * @param addAdminDto
     */
    void addAdmin(AddAdminDto addAdminDto);
}
