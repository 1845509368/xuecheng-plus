package com.xuecheng.system.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xuecheng.system.mapper.DictionaryMapper;
import com.xuecheng.system.model.po.Dictionary;
import com.xuecheng.system.service.DictionaryService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 数据字典
 */
@Slf4j
@Service
public class DictionaryServiceImpl extends ServiceImpl<DictionaryMapper, Dictionary> implements DictionaryService {

    /**
     * 查询所有数据字典内容
     */
    @Override
    public List<Dictionary> queryAll() {
        return this.list();
    }


    /**
     * 根据code查询数据字典
     */
    @Override
    public Dictionary getByCode(String code) {
        LambdaQueryWrapper<Dictionary> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Dictionary::getCode, code);
        return this.getOne(queryWrapper);
    }
}
