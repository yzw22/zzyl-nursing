package com.zzyl.nursing.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;
import java.util.List;

/**
 * 护理计划返回对象（含关联的护理项目）
 */
@Data
@ApiModel(description = "护理计划")
public class NursingPlanVo {

    /**
     * 护理计划id
     */
    @ApiModelProperty(value = "护理计划id")
    private Long id;

    /**
     * 排序号
     */
    @ApiModelProperty(value = "护理计划排序号")
    private Integer sortNo;

    @ApiModelProperty(value = "护理计划名称")
    private String planName;

    @ApiModelProperty(value = "状态（0：禁用，1：启用）")
    private Integer status;

    @ApiModelProperty(value = "备注")
    private String remark;

    @ApiModelProperty(value = "创建时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;

    @ApiModelProperty(value = "护理计划项目列表")
    private List<NursingProjectPlanVo> projectPlans;

}
