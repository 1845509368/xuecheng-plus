package com.xuecheng.content.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.xuecheng.base.execption.XueChengPlusException;
import com.xuecheng.base.model.RestResponse;
import com.xuecheng.content.mapper.TeachplanMapper;
import com.xuecheng.content.mapper.TeachplanMediaMapper;
import com.xuecheng.content.model.dto.BindTeachplanMediaDto;
import com.xuecheng.content.model.dto.SaveTeachplanDto;
import com.xuecheng.content.model.dto.TeachplanDto;
import com.xuecheng.content.model.po.Teachplan;
import com.xuecheng.content.model.po.TeachplanMedia;
import com.xuecheng.content.service.TeachplanService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 课程计划service接口实现类
 */
@Service
public class TeachplanServiceImpl implements TeachplanService {

    @Autowired
    TeachplanMapper teachplanMapper;

    @Autowired
    TeachplanMediaMapper teachplanMediaMapper;


    /**
     * 查询课程计划树型结构
     */
    @Override
    public List<TeachplanDto> findTeachplanTree(long courseId) {
        List<TeachplanDto> teachplanDtos = teachplanMapper.selectTreeNodes(courseId);
        return teachplanDtos;
    }


    /**
     * 保存课程计划
     */
    @Override
    public void saveTeachplan(SaveTeachplanDto saveTeachplanDto) {
        //通过课程计划id判断是新增和修改
        Long teachplanId = saveTeachplanDto.getId();
        if(teachplanId ==null){
            //新增
            Teachplan teachplan = new Teachplan();
            BeanUtils.copyProperties(saveTeachplanDto,teachplan);
            //确定排序字段，找到它的同级节点个数，排序字段就是个数加1  select count(1) from teachplan where course_id=117 and parentId=268
            Long parentId = saveTeachplanDto.getParentid();
            Long courseId = saveTeachplanDto.getCourseId();
            int order = getTeachplanOrder(courseId, parentId);
            teachplan.setOrderby(order);
            teachplanMapper.insert(teachplan);

        }else{
            //修改
            Teachplan teachplan = teachplanMapper.selectById(teachplanId);
            //将参数复制到teachplan
            BeanUtils.copyProperties(saveTeachplanDto,teachplan);
            teachplanMapper.updateById(teachplan);
        }

    }



    private int getTeachplanOrder(Long courseId,Long parentId){
        int max = teachplanMapper.findMaxOrderBy(courseId, parentId);
        return  max+1;
    }

    /**
     * 教学计划绑定媒资
     */
    @Transactional
    @Override
    public void associationMedia(BindTeachplanMediaDto bindTeachplanMediaDto) {
        //教学计划id
        Long teachplanId = bindTeachplanMediaDto.getTeachplanId();

        Teachplan teachplan = teachplanMapper.selectById(teachplanId);
        if(teachplan==null){
            XueChengPlusException.cast("教学计划不存在");
        }

        Integer grade = teachplan.getGrade();
        if(grade!=2){
            XueChengPlusException.cast("只允许第二级教学计划绑定媒资文件");
        }

        //课程id
        Long courseId = teachplan.getCourseId();

        //先删除原来该教学计划绑定的媒资
        teachplanMediaMapper.delete(new LambdaQueryWrapper<TeachplanMedia>().eq(TeachplanMedia::getTeachplanId,teachplanId));

        //再添加教学计划与媒资的绑定关系
        TeachplanMedia teachplanMedia = new TeachplanMedia();
        teachplanMedia.setCourseId(courseId);
        teachplanMedia.setTeachplanId(teachplanId);
        teachplanMedia.setMediaFilename(bindTeachplanMediaDto.getFileName());
        teachplanMedia.setMediaId(bindTeachplanMediaDto.getMediaId());
        teachplanMedia.setCreateDate(LocalDateTime.now());
        teachplanMediaMapper.insert(teachplanMedia);
    }

    /**
     * 删除教学计划
     *
     */
    @Override
    @Transactional
    public RestResponse<Boolean> deleteTeachplan(Long id) {
//        1、删除大章节，大章节下有小章节时不允许删除。
//        2、删除大章节，大单节下没有小章节时可以正常删除。
//        3、删除小章节，同时将关联的信息进行删除。
        LambdaQueryWrapper<Teachplan> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Teachplan::getParentid,id);
        Integer count = teachplanMapper.selectCount(queryWrapper);
        if (count>0){
            return RestResponse.validFail("大章节下有小章节时不允许删除");
        }
        teachplanMapper.deleteById(id);
        teachplanMediaMapper.delete(new LambdaQueryWrapper<TeachplanMedia>().eq(TeachplanMedia::getTeachplanId,id));

        return RestResponse.success();
    }

    @Override
    @Transactional
    public void moveDown(Long id) {
        Teachplan teachplan = teachplanMapper.selectById(id);
        Integer orderby = teachplan.getOrderby();
        //判断是否是最大排序
        int count = teachplanMapper.isMaxOrder(teachplan.getCourseId(), teachplan.getParentid(), orderby);
        if (count == 0){
            return;
        }
        int order = teachplanMapper.selectMinOrder(teachplan.getCourseId(), teachplan.getParentid(), orderby);
        teachplanMapper.updateMinOrder(teachplan.getCourseId(), teachplan.getParentid(), orderby, order);
        teachplan.setOrderby(order);
        teachplanMapper.updateById(teachplan);
    }

    @Override
    @Transactional
    public void moveUp(Long id) {
        Teachplan teachplan = teachplanMapper.selectById(id);
        Integer orderby = teachplan.getOrderby();
        //判断是否是最小排序
        int count = teachplanMapper.isMinOrder(teachplan.getCourseId(), teachplan.getParentid(), orderby);
        if (count == 0){
            return;
        }
        int order = teachplanMapper.selectMaxOrderBy(teachplan.getCourseId(), teachplan.getParentid(),orderby);
        teachplanMapper.updateMaxOrder(teachplan.getCourseId(), teachplan.getParentid(), orderby, order);
        teachplan.setOrderby(order);
        teachplanMapper.updateById(teachplan);
    }
}