package com.xuecheng.content.api;

import com.xuecheng.content.model.po.CourseTeacher;
import com.xuecheng.content.service.CourseTeacherService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 课程-教师关系表
 */
@Slf4j
@Api(tags = "课程教师管理接口")
@RestController
@RequestMapping("courseTeacher")
public class CourseTeacherController {

    @Autowired
    private CourseTeacherService courseTeacherService;

    @ApiOperation("获取该课程教师列表")
    @GetMapping("list/{courseId}")
    public List<CourseTeacher> list(@PathVariable Long courseId) {
        return courseTeacherService.list(courseId);
    }


    @ApiOperation("新增/修改教师")
    @PostMapping
    public CourseTeacher createTeacher(@RequestBody CourseTeacher courseTeacher) {
        return courseTeacherService.createTeacher(courseTeacher);
    }

    @ApiOperation("删除教师")
    @DeleteMapping("/course/{courseId}/{id}")
    public void deleteTeacher(@PathVariable Long courseId, @PathVariable Long id) {
        courseTeacherService.deleteTeacher(courseId, id);
    }


}
