package com.xuecheng.content.service;

import com.xuecheng.content.model.dto.CoursePreviewDto;
import com.xuecheng.content.model.po.CoursePublish;

import java.io.File;

/**
 * 课程预览、发布接口
 */
public interface CoursePublishService {


    /**
     * 获取课程预览信息
     */
    CoursePreviewDto getCoursePreviewInfo(Long courseId);


    /**
     * 提交审核
     */
    void commitAudit(Long companyId, Long courseId);

    /**
     * 课程发布接口
     */
    void publish(Long companyId,Long courseId);

    /**
     * 课程静态化
     */
    File generateCourseHtml(Long courseId);
    /**
     * 上传课程静态化页面
     */
    void  uploadCourseHtml(Long courseId,File file);

    /**
     * 查询课程发布信息
     */
    CoursePublish getCoursePublish(Long courseId);


    /**
     * @description 查询缓存中的课程信息
     * @param courseId
     * @return com.xuecheng.content.model.po.CoursePublish
     * @author Mr.M
     * @date 2022/10/22 16:15
     */
    public CoursePublish getCoursePublishCache(Long courseId);

    CoursePreviewDto getCoursePublishInfo(Long courseId);
}