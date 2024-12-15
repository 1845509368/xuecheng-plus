package com.xuecheng.content.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.xuecheng.content.model.dto.TeachplanDto;
import com.xuecheng.content.model.po.Teachplan;

import java.util.List;

/**
 * 课程计划
 */
public interface TeachplanMapper extends BaseMapper<Teachplan> {
    /**
     * 查询某课程的课程计划，组成树型结构
     */
    List<TeachplanDto> selectTreeNodes(long courseId);
}
