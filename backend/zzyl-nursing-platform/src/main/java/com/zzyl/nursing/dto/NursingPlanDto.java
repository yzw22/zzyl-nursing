package com.zzyl.nursing.dto;

import com.zzyl.nursing.domain.NursingProjectPlan;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * 护理计划新增/修改参数对象
 */
@Data
@ApiModel(description = "护理计划新增/修改参数")
public class NursingPlanDto {

    /**
     * 编号（修改时必填，新增时为空）
     */
    @ApiModelProperty(value = "编号")
    private Long id;

    /**
     * 排序号
     */
    @ApiModelProperty(value = "排序号")
    private Integer sortNo;

    /**
     * 计划名称
     */
    @ApiModelProperty(value = "计划名称")
    private String planName;

    /**
     * 状态（0：禁用，1：启用）
     */
    @ApiModelProperty(value = "状态（0：禁用，1：启用）")
    private Integer status;

    /**
     * 备注
     */
    @ApiModelProperty(value = "备注")
    private String remark;

    /**
     * 护理计划关联项目列表
     */
    @ApiModelProperty(value = "护理计划关联项目列表")
    private List<NursingProjectPlan> projectPlans;
}
