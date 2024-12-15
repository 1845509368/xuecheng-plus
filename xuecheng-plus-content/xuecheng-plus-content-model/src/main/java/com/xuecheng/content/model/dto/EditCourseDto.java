package com.xuecheng.content.model.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotEmpty;

/**
 * 添加课程dto
 */
@Data
public class EditCourseDto extends AddCourseDto {

    @NotEmpty(message = "课程id不能为空")
    @ApiModelProperty(value = "课程id", required = true)
    private Long id;

}
