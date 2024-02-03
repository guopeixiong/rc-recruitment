package com.ruanchuang.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.ruanchuang.domain.IndexRollingImage;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * <p>
 * 首页轮播图管理表 服务类
 * </p>
 *
 * @author guopeixiong
 * @since 2023-08-01
 */
public interface IndexRollingImageService extends IService<IndexRollingImage> {

    /**
     * 上传图片
     * @param file
     */
    IndexRollingImage uploadImage(MultipartFile file);

    /**
     * 获取图片列表
     * @return
     */
    List<IndexRollingImage> listImage();

    /**
     * 修改启用状态
     * @param id
     * @param disable
     */
    void changeStatus(Long id, Integer disable);

    /**
     * 删除图片
     * @param id
     */
    void deleteImage(Long id);

    /**
     * 获取首页轮播图
     * @return
     */
    List<String> getIndexImage();
}
