package com.xuecheng.content.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.xuecheng.content.model.dto.TeachplanDto;
import com.xuecheng.content.model.po.Teachplan;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;

/**
 * 课程计划
 */
public interface TeachplanMapper extends BaseMapper<Teachplan> {
    /**
     * 查询某课程的课程计划，组成树型结构
     */
    List<TeachplanDto> selectTreeNodes(long courseId);

    /**
     * 查询某课程计划最大排序序号
     */
    @Select("select max(orderby) from teachplan where course_id=#{courseId} and parentid=#{parentId}")
    int findMaxOrderBy(@Param("courseId") long courseId, @Param("parentId") long parentId);

    @Select("select min(orderby) from teachplan where course_id=#{courseId} and parentid=#{parentId} and orderby > #{orderby}")
    int selectMinOrder(@Param("courseId") Long courseId, @Param("parentId") Long parentId, @Param("orderby") Integer orderby);

    @Update("update teachplan set orderby = #{orderby} where course_id=#{courseId} and parentid=#{parentId} and orderby = #{order}")
    int updateMinOrder(@Param("courseId") Long courseId, @Param("parentId") Long parentId, @Param("orderby") Integer orderby, @Param("order") int order);

    @Select("select max(orderby) from teachplan where course_id=#{courseId} and parentid=#{parentId} and orderby < #{orderby} ")
    int selectMaxOrderBy(@Param("courseId") Long courseId, @Param("parentId") Long parentId, @Param("orderby") Integer orderby);

    @Update("update teachplan set orderby = #{orderby} where course_id=#{courseId} and parentid=#{parentId} and orderby = #{order}")
    int updateMaxOrder(@Param("courseId") Long courseId, @Param("parentId") Long parentId, @Param("orderby") Integer orderby, @Param("order") int order);

    @Select("select count(1) from teachplan where course_id=#{courseId} and parentid=#{parentId} and orderby > #{orderby}")
    int isMaxOrder(@Param("courseId") Long courseId, @Param("parentId") Long parentId, @Param("orderby") Integer orderby);

    @Select("select count(1) from teachplan where course_id=#{courseId} and parentid=#{parentId} and orderby < #{orderby}")
    int isMinOrder(@Param("courseId") Long courseId, @Param("parentId") Long parentId, @Param("orderby") Integer orderby);
}
