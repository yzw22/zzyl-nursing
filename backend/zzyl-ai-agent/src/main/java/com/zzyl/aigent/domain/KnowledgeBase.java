package com.zzyl.aigent.domain;

import com.baomidou.mybatisplus.annotation.TableName;
import com.zzyl.common.annotation.Excel;
import com.zzyl.common.core.domain.BaseEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 知识库对象 knowledge_base
 *
 * @author zzyl
 * @date 2026-10-29
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@ApiModel(value = "KnowledgeBase对象", description = "知识库")
@TableName("knowledge_base")
public class KnowledgeBase extends BaseEntity {

    private static final long serialVersionUID = 1L;

    /** 主键ID */
    @ApiModelProperty("主键ID")
    private Long id;

    /** 知识标题 */
    @Excel(name = "知识标题")
    @ApiModelProperty("知识标题")
    private String title;

    /** 分类 */
    @Excel(name = "分类")
    @ApiModelProperty("分类")
    private Integer category;

    /** 标签数组 */
    @Excel(name = "标签数组")
    @ApiModelProperty("标签数组")
    private String tags;

    /** 状态 0：禁用 1：启用 */
    @Excel(name = "状态")
    @ApiModelProperty("状态 0：禁用 1：启用")
    private Integer status;

    /** 优先级 */
    @Excel(name = "优先级")
    @ApiModelProperty("优先级")
    private Integer priority;

    /** 文档地址 */
    @Excel(name = "文档地址")
    @ApiModelProperty("文档地址")
    private String documentUrl;

}
