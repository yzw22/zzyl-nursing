package com.zzyl.nursing.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import com.zzyl.common.annotation.Excel;
import com.zzyl.common.core.domain.BaseEntity;

/**
 * 护理计划对象 nursing_plan
 *
 * @author alexis
 * @date 2025-06-02
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@ApiModel(value="NursingPlan对象", description="护理计划")
@TableName("nursing_plan")
public class NursingPlan extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 编号 */
    @TableId(value = "id", type = IdType.AUTO)
    @ApiModelProperty("编号")
    private Long id;

    /** 排序号 */
    @Excel(name = "排序号")
    @ApiModelProperty("排序号")
    private Integer sortNo;

    /** 名称 */
    @Excel(name = "名称")
    @ApiModelProperty("名称")
    private String planName;

    /** 状态 0禁用 1启用 */
    @Excel(name = "状态 0禁用 1启用")
    @ApiModelProperty("状态 0禁用 1启用")
    private Integer status;

    /** 被护理等级引用的次数（非数据库字段，仅列表查询时回填，用于前端禁用删除按钮） */
    @TableField(exist = false)
    @ApiModelProperty("被护理等级引用的次数")
    private Long count;

}
