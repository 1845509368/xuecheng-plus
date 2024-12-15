package com.xuecheng.content.api;

import com.xuecheng.base.model.RestResponse;
import com.xuecheng.content.model.dto.BindTeachplanMediaDto;
import com.xuecheng.content.model.dto.SaveTeachplanDto;
import com.xuecheng.content.model.dto.TeachplanDto;
import com.xuecheng.content.service.TeachplanService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 课程计划编辑
 */
@Api(tags = "课程计划编辑接口")
@RestController
@RequestMapping("/teachplan")
public class TeachplanController {

    @Autowired
    TeachplanService teachplanService;

    @ApiOperation("查询课程计划树形结构")
    @GetMapping("/{courseId}/tree-nodes")
    public List<TeachplanDto> teachplan(@PathVariable("courseId") Long courseId) {
        return teachplanService.findTeachplanTree(courseId);
    }

    @ApiOperation("课程计划创建或修改")
    @PostMapping
    public void saveTeachplan(@RequestBody SaveTeachplanDto teachplan) {
        teachplanService.saveTeachplan(teachplan);
    }

    @ApiOperation("课程计划和媒资信息绑定")
    @PostMapping("/association/media")
    public void associationMedia(@RequestBody BindTeachplanMediaDto bindTeachplanMediaDto) {
        teachplanService.associationMedia(bindTeachplanMediaDto);
    }


    @ApiOperation("删除课程计划")
    @DeleteMapping("{id}")
    public RestResponse<Boolean> deleteTeachplan(@PathVariable Long id) {
        return teachplanService.deleteTeachplan(id);
    }


    @ApiOperation("向下移动")
    @PostMapping("/movedown/{id}")
    public void movedown() {
        // TODO 向下移动
    }

    @ApiOperation("向上移动")
    @PostMapping("/moveup/{id}")
    public void moveup() {
        // TODO 向上移动
    }


}