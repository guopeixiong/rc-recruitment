package com.ruanchuang.service.impl;

import com.ruanchuang.domain.IndexRollingImage;
import com.ruanchuang.enums.Constants;
import com.ruanchuang.exception.ServiceException;
import com.ruanchuang.exception.SystemException;
import com.ruanchuang.mapper.IndexRollingImageMapper;
import com.ruanchuang.service.IndexRollingImageService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Set;

/**
 * <p>
 * 首页轮播图管理表 服务实现类
 * </p>
 *
 * @author guopeixiong
 * @since 2023-08-01
 */
@Service
@Slf4j
public class IndexRollingImageServiceImpl extends ServiceImpl<IndexRollingImageMapper, IndexRollingImage> implements IndexRollingImageService {

    @Value("${file.store-address}")
    private String rootPath;

    private Set<String> fileType = Set.of("jpg", "jpeg", "png", "gif");

    /**
     * 上传图片
     * @param file
     */
    @Override
    public IndexRollingImage uploadImage(MultipartFile file) {
        File path = new File(rootPath + File.separator + "indexImage");
        if (!path.exists()) {
            path.mkdirs();
        }
        String fileName = file.getOriginalFilename();
        int dotIndex = fileName.lastIndexOf(".");
        if (dotIndex > -1 && dotIndex < fileName.length() - 1) {
            fileName = fileName.substring(dotIndex + 1).toLowerCase();
        }
        if (!fileType.contains(fileName)) {
            throw new ServiceException("图片类型错误");
        }
        fileName = System.currentTimeMillis() + "." + fileName;
        File targetFile = new File(path.getPath() + File.separator + fileName);
        try {
            FileCopyUtils.copy(file.getBytes(), targetFile);
        } catch (IOException e) {
            log.error("上传首页轮播图异常, 异常信息: '{}'", e.getMessage());
            throw new SystemException("上传首页轮播图异常");
        }
        String imgUrl = "/indexImage/" + fileName;
        String imagePath = targetFile.getPath();
        IndexRollingImage indexRollingImage = new IndexRollingImage()
                .setImagePath(imagePath)
                .setUrl(imgUrl);
        this.save(indexRollingImage);
        indexRollingImage.setImagePath(null)
                .setCreateBy(null)
                .setIsEnabled(Constants.INDEX_IMAGE_DISABLE);
        return indexRollingImage;
    }

    /**
     * 删除图片
     * @param id
     */
    @Override
    public void deleteImage(Long id) {
        boolean success = this.removeById(id);
        if (!success) {
            throw new SystemException("删除失败");
        }
    }

    /**
     * 修改启用状态
     * @param id
     * @param disable
     */
    @Override
    public void changeStatus(Long id, Integer disable) {
        IndexRollingImage indexRollingImage = new IndexRollingImage()
                .setId(id)
                .setIsEnabled(disable.equals(Constants.INDEX_IMAGE_DISABLE) ? Constants.INDEX_IMAGE_DISABLE : Constants.INDEX_IMAGE_ENABLE);
        boolean success = this.updateById(indexRollingImage);
        if (!success) {
            throw new SystemException("修改启用状态失败");
        }
    }

    /**
     * 获取图片列表
     * @return
     */
    @Override
    public List<IndexRollingImage> listImage() {
        return this.lambdaQuery()
                .orderByDesc(IndexRollingImage::getIsEnabled, IndexRollingImage::getCreateTime)
                .select(IndexRollingImage::getId,
                        IndexRollingImage::getCreateTime,
                        IndexRollingImage::getUrl,
                        IndexRollingImage::getIsEnabled,
                        IndexRollingImage::getRemark)
                .list();
    }
}
