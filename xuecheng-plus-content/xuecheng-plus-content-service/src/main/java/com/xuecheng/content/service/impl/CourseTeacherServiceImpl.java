package com.xuecheng.content.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xuecheng.content.mapper.CourseTeacherMapper;
import com.xuecheng.content.model.po.CourseTeacher;
import com.xuecheng.content.service.CourseTeacherService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 课程-教师关系表
 */
@Slf4j
@Service
public class CourseTeacherServiceImpl extends ServiceImpl<CourseTeacherMapper, CourseTeacher> implements CourseTeacherService {

    /**
     * 获取该课程教师列表
     */
    @Override
    public List<CourseTeacher> list(Long courseId) {
        LambdaQueryWrapper<CourseTeacher> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(CourseTeacher::getCourseId,courseId);
        return this.list(queryWrapper);
    }

    /**
     * 新增/更新教师
     */
    @Override
    public CourseTeacher createTeacher(CourseTeacher courseTeacher) {
        if (courseTeacher.getId()==null){
            courseTeacher.setCreateDate(LocalDateTime.now());
            this.save(courseTeacher);
            return courseTeacher;
        }
        LambdaUpdateWrapper<CourseTeacher> updateWrapper = new LambdaUpdateWrapper<>();
        updateWrapper.eq(CourseTeacher::getCourseId,courseTeacher.getCourseId()).eq(CourseTeacher::getId,courseTeacher.getId());
        this.update(courseTeacher,updateWrapper);
        return courseTeacher;
    }

    /**
     * 删除教师
     */
    @Override
    public void deleteTeacher(Long courseId, Long id) {
        LambdaQueryWrapper<CourseTeacher> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(CourseTeacher::getCourseId,courseId).eq(CourseTeacher::getId,id);
        this.remove(queryWrapper);
    }

}
