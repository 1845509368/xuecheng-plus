package com.xuecheng.content.service;


import com.xuecheng.base.model.RestResponse;
import com.xuecheng.content.model.dto.*;

import java.util.List;

/**
 * 课程基本信息管理业务接口
 */
public interface TeachplanService {

    /**
     * 查询课程计划树型结构
     */
    List<TeachplanDto> findTeachplanTree(long courseId);

    /**
     * 保存课程计划
     */
    void saveTeachplan(SaveTeachplanDto teachplanDto);

    /**
     * 教学计划绑定媒资
     */
    void associationMedia(BindTeachplanMediaDto bindTeachplanMediaDto);

    /**
     * 删除教学计划
     */
    RestResponse<Boolean> deleteTeachplan(Long id);
}