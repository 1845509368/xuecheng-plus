package com.xuecheng.content.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.xuecheng.content.model.po.CourseTeacher;

import java.util.List;

/**
 * 课程-教师关系表
 */
public interface CourseTeacherService extends IService<CourseTeacher> {

    /**
     * 获取该课程教师列表
     */
    List<CourseTeacher> list(Long courseId);

    /**
     * 新增教师
     */
    CourseTeacher createTeacher(CourseTeacher courseTeacher);


    /**
     * 删除教师
     */
    void deleteTeacher(Long courseId, Long id);
}
