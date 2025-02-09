package com.xuecheng.system.model.po;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * 数据字典
 */
@Data
@ApiModel("数据字典")
@TableName("dictionary")
public class Dictionary implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(type = IdType.AUTO)
    @ApiModelProperty("id标识")
    private Long id;

    @ApiModelProperty("数据字典名称")
    private String name;

    @ApiModelProperty("数据字典代码")
    private String code;

    @ApiModelProperty("数据字典项")
    private String itemValues;

}
