package com.xuecheng.content.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.xuecheng.base.model.PageParams;
import com.xuecheng.base.model.PageResult;
import com.xuecheng.content.model.dto.AddCourseDto;
import com.xuecheng.content.model.dto.CourseBaseInfoDto;
import com.xuecheng.content.model.dto.EditCourseDto;
import com.xuecheng.content.model.dto.QueryCourseParamsDto;
import com.xuecheng.content.model.po.CourseBase;

/**
 * 课程基本信息
 */
public interface CourseBaseInfoService extends IService<CourseBase> {
    /**
     * 课程查询
     */
    PageResult<CourseBase> queryCourseBaseList(Long companyId,PageParams pageParams,QueryCourseParamsDto queryCourseParamsDto);

    /**
     * 新增课程基础信息
     */
    CourseBaseInfoDto createCourseBase(Long companyId, AddCourseDto addCourseDto);

    /**
     * 根据id查询课程基本信息
     */
    CourseBaseInfoDto getCourseBaseInfo(Long courseId);


    /**
     * 修改课程信息
     */
    CourseBaseInfoDto updateCourseBase(Long companyId, EditCourseDto dto);

    /**
     * 删除课程信息
     */
    void deleteBaseInfo(Long courseId);
}
