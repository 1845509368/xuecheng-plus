package com.xuecheng.content.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xuecheng.content.mapper.CourseCategoryMapper;
import com.xuecheng.content.model.dto.CourseCategoryTreeDto;
import com.xuecheng.content.model.po.CourseCategory;
import com.xuecheng.content.service.CourseCategoryService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * <p>
 * 课程分类 服务实现类
 * </p>
 *
 * @author 张星宇
 */
@Slf4j
@Service
public class CourseCategoryServiceImpl extends ServiceImpl<CourseCategoryMapper, CourseCategory> implements CourseCategoryService {


    @Autowired
    CourseCategoryMapper courseCategoryMapper;


    /**
     * 课程分类查询
     */
    @Override
    public List<CourseCategoryTreeDto> queryTreeNodes(String id) {

        List<CourseCategoryTreeDto> courseCategoryTreeDtos = courseCategoryMapper.selectTreeNodes(id);

        // 将list转成map，排除根节点
        Map<String, CourseCategoryTreeDto> map = courseCategoryTreeDtos.stream().filter(item -> !id.equals(item.getId()))
                .collect(Collectors.toMap(CourseCategory::getId, value -> value, (key1, key2) -> key2));

        // 最终返回的list
        List<CourseCategoryTreeDto> courseCategoryList = new ArrayList<>();

        // 依次遍历每个元素,排除根节点
        courseCategoryTreeDtos.stream().filter(item -> !id.equals(item.getId()))
                .forEach(item->{

                    if (item.getParentid().equals(id)){ // 父亲id等于当前id
                        courseCategoryList.add(item); // 把当前节点存入list
                    }
                    //找到当前节点的父节点
                    CourseCategoryTreeDto courseCategoryParent = map.get(item.getParentid());

                    if (courseCategoryParent!=null){ // 当前父节点不为空
                        if (courseCategoryParent.getChildrenTreeNodes() == null){
                            // 父节点的ChildrenTreeNodes属性为空，则new一个集合给它
                            courseCategoryParent.setChildrenTreeNodes(new ArrayList<CourseCategoryTreeDto>());
                        }
                        // 下边开始往ChildrenTreeNodes属性放子节点
                        courseCategoryParent.getChildrenTreeNodes().add(item);
                    }

                });
        /*
                   1,      根结点,        根结点,        0,      1,1,0
                   1-1,    前端开发,      前端开发,       1,      1,1,0
                   1-1-1,  HTML/CSS,     HTML/CSS,      1-1,    1,1,1
          流程梳理：
          1.将查询出的List集合转成map集合备用，过滤排除根节点（传入的节点）
          2.new一个最终返回的list集合
          3.遍历原始List中的每个元素，过滤排除根节点（传入的节点）
          4.拿到每个item后，判断item的父节点id是否等于当前节点id
           等于：把当前节点存入最终结果的list
          5.找到当前item的父节点，判断父节点是否在map中
           存在：
          6.判断当前item的父节点的子孩子是否为空
           为空：new一个集合给子孩子
           不为空：
          7.将item添加到（当前item）父亲的子孩子属性上
         */

        return courseCategoryList;
    }
}
