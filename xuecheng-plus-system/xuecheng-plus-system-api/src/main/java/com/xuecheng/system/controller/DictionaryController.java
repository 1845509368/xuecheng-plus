package com.xuecheng.system.controller;

import com.xuecheng.system.model.po.Dictionary;
import com.xuecheng.system.service.DictionaryService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 数据字典
 */
@Slf4j
@Api(tags = "数据字典")
@RestController
@RequestMapping("/dictionary")
public class DictionaryController  {

    @Autowired
    private DictionaryService  dictionaryService;

    @ApiOperation("查询所有数据字典内容")
    @GetMapping("/all")
    public List<Dictionary> queryAll() {
        return dictionaryService.queryAll();
    }

    @ApiOperation("根据code查询数据字典")
    @GetMapping("/code/{code}")
    public Dictionary getByCode(@PathVariable String code) {
        return dictionaryService.getByCode(code);
    }

}
