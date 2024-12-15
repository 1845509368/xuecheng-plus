package com.xuecheng.content.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xuecheng.base.execption.XueChengPlusException;
import com.xuecheng.base.model.PageParams;
import com.xuecheng.base.model.PageResult;
import com.xuecheng.content.mapper.*;
import com.xuecheng.content.model.dto.AddCourseDto;
import com.xuecheng.content.model.dto.CourseBaseInfoDto;
import com.xuecheng.content.model.dto.EditCourseDto;
import com.xuecheng.content.model.dto.QueryCourseParamsDto;
import com.xuecheng.content.model.po.*;
import com.xuecheng.content.service.CourseBaseInfoService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

/**
 * 课程基本信息
 */
@Slf4j
@Service
public class CourseBaseInfoServiceImpl extends ServiceImpl<CourseBaseMapper, CourseBase> implements CourseBaseInfoService {

    @Autowired
    CourseBaseMapper courseBaseMapper;

    @Autowired
    CourseMarketMapper courseMarketMapper;

    @Autowired
    CourseCategoryMapper courseCategoryMapper;

    @Autowired
    CourseTeacherMapper courseTeacherMapper;

    @Autowired
    TeachplanMapper teachplanMapper;

    /**
     * 课程查询
     */
    @Override
    public PageResult<CourseBase> queryCourseBaseList(Long companyId,PageParams pageParams, QueryCourseParamsDto queryCourseParamsDto) {

        //测试查询接口
        LambdaQueryWrapper<CourseBase> queryWrapper = new LambdaQueryWrapper<>();

        //拼接查询条件（条件，属性名，比较值）
        queryWrapper.like(StringUtils.isNotBlank(queryCourseParamsDto.getCourseName()),CourseBase::getName,queryCourseParamsDto.getCourseName());
        queryWrapper.eq(StringUtils.isNotBlank(queryCourseParamsDto.getAuditStatus()),CourseBase::getAuditStatus,queryCourseParamsDto.getAuditStatus());
        queryWrapper.eq(StringUtils.isNotBlank(queryCourseParamsDto.getPublishStatus()),CourseBase::getStatus,queryCourseParamsDto.getPublishStatus());
        queryWrapper.eq(StringUtils.isNotBlank(companyId.toString()),CourseBase::getCompanyId,companyId);

        //封装Mybatis中的page对象
        Page<CourseBase> page = new Page<>();
        page.setCurrent(pageParams.getPageNo());
        page.setSize(pageParams.getPageSize());

        //根据条件进行查询该页的记录
        Page<CourseBase> courseBasePage = this.page(page, queryWrapper);

        //封装我们的Page对象
        PageResult<CourseBase> courseBasePageResult = new PageResult<>();
        courseBasePageResult.setCounts(courseBasePage.getTotal());//总记录数：counts = total
        courseBasePageResult.setPage(courseBasePage.getCurrent());//当前页码：page = current
        courseBasePageResult.setPageSize(courseBasePage.getSize());//每页记录数：pageSize = size
        courseBasePageResult.setItems(courseBasePage.getRecords());//数据：items = records
        return courseBasePageResult;
    }


    /**
     * 新增课程基础信息
     */
    @Transactional
    @Override
    public CourseBaseInfoDto createCourseBase(Long companyId, AddCourseDto dto) {

        //新增对象
        CourseBase courseBaseNew = new CourseBase();
        //拷贝对象
        BeanUtils.copyProperties(dto, courseBaseNew);
        courseBaseNew.setCreateDate(LocalDateTime.now());
        courseBaseNew.setAuditStatus("202002");
        courseBaseNew.setStatus("203001");
        courseBaseNew.setCompanyId(companyId);
        int insert = courseBaseMapper.insert(courseBaseNew);
        if (insert<=0) {
            XueChengPlusException.cast("新增课程基本信息失败");
        }

        //向课程营销表保持课程营销信息
        CourseMarket courseMarketNew = new CourseMarket();
        BeanUtils.copyProperties(dto,courseMarketNew);
        Long courseId = courseBaseNew.getId();

        //课程id和课程营销id是一对一
        courseMarketNew.setId(courseId);

        insert = saveCourseMarket(courseMarketNew);
        if (insert<=0) {
            XueChengPlusException.cast("保存课程营销信息失败");
        }
        //查询课程基本信息及营销信息并返回
        return getCourseBaseInfo(courseId);
    }


    /**
     * 保存课程营销信息
     */
    private int saveCourseMarket(CourseMarket courseMarketNew){
        //收费规则
        String charge = courseMarketNew.getCharge();

        if (charge.equals("201001")){
            if (courseMarketNew.getPrice() == null || courseMarketNew.getPrice() <=0){
                XueChengPlusException.cast("课程收费价格不能为空且必须大于0");
            }
        }

        //根据id从课程营销表查询
        CourseMarket courseMarket = courseMarketMapper.selectById(courseMarketNew.getId());
        if (courseMarket == null) {
            return courseMarketMapper.insert(courseMarketNew);
        }else {
            BeanUtils.copyProperties(courseMarketNew,courseMarket);
            return courseMarketMapper.updateById(courseMarket);
        }
    }


    /**
     * 根据课程id查询课程基本信息，包括基本信息和营销信息
     */
    @Override
    public CourseBaseInfoDto getCourseBaseInfo(Long courseId){

        CourseBase courseBase = courseBaseMapper.selectById(courseId);
        if(courseBase == null){
            return null;
        }
        CourseMarket courseMarket = courseMarketMapper.selectById(courseId);
        CourseBaseInfoDto courseBaseInfoDto = new CourseBaseInfoDto();
        BeanUtils.copyProperties(courseBase,courseBaseInfoDto);
        if(courseMarket != null){
            BeanUtils.copyProperties(courseMarket,courseBaseInfoDto);
        }

        //查询分类名称
        CourseCategory courseCategoryBySt = courseCategoryMapper.selectById(courseBase.getSt());
        courseBaseInfoDto.setStName(courseCategoryBySt.getName());
        CourseCategory courseCategoryByMt = courseCategoryMapper.selectById(courseBase.getMt());
        courseBaseInfoDto.setMtName(courseCategoryByMt.getName());

        return courseBaseInfoDto;

    }


    /**
     * 修改课程信息
     */
    @Transactional
    @Override
    public CourseBaseInfoDto updateCourseBase(Long companyId, EditCourseDto dto) {

        //课程id
        Long courseId = dto.getId();
        CourseBase courseBase = courseBaseMapper.selectById(courseId);
        if(courseBase==null){
            XueChengPlusException.cast("课程不存在");
        }

        //校验本机构只能修改本机构的课程
        if(!courseBase.getCompanyId().equals(companyId)){
            XueChengPlusException.cast("本机构只能修改本机构的课程");
        }

        //封装基本信息的数据
        BeanUtils.copyProperties(dto,courseBase);
        courseBase.setChangeDate(LocalDateTime.now());

        //更新课程基本信息
        int i = courseBaseMapper.updateById(courseBase);

        //封装营销信息的数据
        CourseMarket courseMarket = new CourseMarket();
        BeanUtils.copyProperties(dto,courseMarket);
        saveCourseMarket(courseMarket);
        //查询课程信息
        return this.getCourseBaseInfo(courseId);

    }

    /**
     * 删除课程信息：需要删除课程相关的基本信息、营销信息、课程计划、课程教师信息。
     */
    @Override
    @Transactional
    public void deleteBaseInfo(Long courseId) {
        //删除课程教师信息
        courseTeacherMapper.delete(new LambdaQueryWrapper<CourseTeacher>().eq(CourseTeacher::getCourseId,courseId));
        //删除课程计划
        teachplanMapper.delete(new LambdaQueryWrapper<Teachplan>().eq(Teachplan::getCourseId,courseId));
        //删除营销信息
        courseMarketMapper.deleteById(courseId);
        //删除课程基本信息
        this.removeById(courseId);
    }
}
