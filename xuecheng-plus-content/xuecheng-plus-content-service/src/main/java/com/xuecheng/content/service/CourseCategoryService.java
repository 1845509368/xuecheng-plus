package com.xuecheng.content.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.xuecheng.content.model.dto.CourseCategoryTreeDto;
import com.xuecheng.content.model.po.CourseCategory;

import java.util.List;

/**
 * 课程分类 服务类
 */
public interface CourseCategoryService extends IService<CourseCategory> {
    /**
     * 课程分类查询
     */
    List<CourseCategoryTreeDto> queryTreeNodes(String id);
}
