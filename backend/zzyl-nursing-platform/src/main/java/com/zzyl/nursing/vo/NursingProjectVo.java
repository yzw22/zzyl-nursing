package com.zzyl.nursing.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 护理项目下拉选项（对应前端 el-option 的 label 与 value）
 */
@Data
@ApiModel(value = "NursingProjectVo对象", description = "护理项目下拉选项")
public class NursingProjectVo {
    /**
     * 项目名称
     */
    @ApiModelProperty("项目名称")
    private String label;

    /**
     * 项目ID
     */
    @ApiModelProperty("项目ID")
    private String value;
}