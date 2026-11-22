package com.zzyl.nursing.controller;

import java.util.List;
import com.zzyl.common.annotation.Log;
import com.zzyl.common.core.controller.BaseController;
import com.zzyl.common.core.domain.AjaxResult;
import com.zzyl.common.core.page.TableDataInfo;
import com.zzyl.common.enums.BusinessType;
import com.zzyl.nursing.domain.NursingPlan;
import com.zzyl.nursing.dto.NursingPlanDto;
import com.zzyl.nursing.service.INursingPlanService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * 护理计划Controller
 *
 * @author alexis
 * @date 2025-06-02
 */
@RestController
@RequestMapping("/nursing/plan")
@Api(tags = "护理计划相关接口")
public class NursingPlanController extends BaseController
{
    @Autowired
    private INursingPlanService nursingPlanService;

    /**
     * 查询护理计划列表
     */
    @GetMapping("/list")
    @ApiOperation("查询护理计划列表")
    public TableDataInfo list(NursingPlan nursingPlan)
    {
        startPage();
        List<NursingPlan> list = nursingPlanService.selectNursingPlanList(nursingPlan);
        return getDataTable(list);
    }

    /**
     * 查询所有护理计划（下拉选项）
     */
    @GetMapping("/all")
    @ApiOperation("查询所有护理计划")
    public AjaxResult listAll()
    {
        return success(nursingPlanService.selectAllNursingPlan());
    }

    /**
     * 获取护理计划详细信息
     */
    @GetMapping(value = "/{id}")
    @ApiOperation("获取护理计划详细信息")
    public AjaxResult getInfo(@PathVariable("id") Long id)
    {
        return success(nursingPlanService.selectNursingPlanVoById(id));
    }

    /**
     * 新增护理计划
     */
    @Log(title = "护理计划", businessType = BusinessType.INSERT)
    @PostMapping
    @ApiOperation("新增护理计划")
    public AjaxResult add(@RequestBody NursingPlanDto dto)
    {
        return toAjax(nursingPlanService.insertNursingPlan(dto));
    }

    /**
     * 修改护理计划
     */
    @Log(title = "护理计划", businessType = BusinessType.UPDATE)
    @PutMapping
    @ApiOperation("修改护理计划")
    public AjaxResult edit(@RequestBody NursingPlanDto dto)
    {
        return toAjax(nursingPlanService.updateNursingPlan(dto));
    }

    /**
     * 删除护理计划
     */
    @Log(title = "护理计划", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    @ApiOperation("删除护理计划")
    public AjaxResult remove(@PathVariable Long[] ids)
    {
        return toAjax(nursingPlanService.deleteNursingPlanByIds(ids));
    }
}
