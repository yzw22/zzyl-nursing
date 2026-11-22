package com.zzyl.nursing.service.impl;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zzyl.nursing.domain.NursingPlan;
import com.zzyl.nursing.domain.NursingProjectPlan;
import com.zzyl.nursing.dto.NursingPlanDto;
import com.zzyl.nursing.mapper.NursingPlanMapper;
import com.zzyl.nursing.mapper.NursingProjectPlanMapper;
import com.zzyl.nursing.service.INursingPlanService;
import com.zzyl.nursing.vo.NursingPlanVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 护理计划Service业务层处理
 *
 * @author alexis
 * @date 2025-06-02
 */
@Service
public class NursingPlanServiceImpl extends ServiceImpl<NursingPlanMapper, NursingPlan> implements INursingPlanService
{
    @Autowired
    private NursingPlanMapper nursingPlanMapper;

    @Autowired
    private NursingProjectPlanMapper nursingProjectPlanMapper;

    /**
     * 查询护理计划列表
     *
     * @param nursingPlan 护理计划
     * @return 护理计划集合
     */
    @Override
    public List<NursingPlan> selectNursingPlanList(NursingPlan nursingPlan)
    {
        return nursingPlanMapper.selectNursingPlanList(nursingPlan);
    }

    /**
     * 查询护理计划详情（含关联的护理项目）
     *
     * @param id 护理计划主键
     * @return 护理计划详情
     */
    @Override
    public NursingPlanVo selectNursingPlanVoById(Long id)
    {
        NursingPlan plan = getById(id);
        if (plan == null)
        {
            return null;
        }
        NursingPlanVo vo = new NursingPlanVo();
        vo.setId(plan.getId());
        vo.setSortNo(plan.getSortNo());
        vo.setPlanName(plan.getPlanName());
        vo.setStatus(plan.getStatus());
        vo.setRemark(plan.getRemark());
        vo.setCreateTime(plan.getCreateTime());
        vo.setProjectPlans(nursingProjectPlanMapper.selectByNursingPlanId(id));
        return vo;
    }

    /**
     * 新增护理计划
     *
     * @param dto 护理计划参数
     * @return 结果
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public int insertNursingPlan(NursingPlanDto dto)
    {
        NursingPlan plan = new NursingPlan();
        plan.setSortNo(dto.getSortNo());
        plan.setPlanName(dto.getPlanName());
        plan.setStatus(dto.getStatus());
        plan.setRemark(dto.getRemark());
        int result = save(plan) ? 1 : 0;
        insertProjectPlans(dto.getProjectPlans(), plan.getId());
        return result;
    }

    /**
     * 修改护理计划
     *
     * @param dto 护理计划参数
     * @return 结果
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public int updateNursingPlan(NursingPlanDto dto)
    {
        NursingPlan plan = new NursingPlan();
        plan.setId(dto.getId());
        plan.setSortNo(dto.getSortNo());
        plan.setPlanName(dto.getPlanName());
        plan.setStatus(dto.getStatus());
        plan.setRemark(dto.getRemark());
        int result = updateById(plan) ? 1 : 0;
        // 重新维护关联的护理项目（启用/禁用等部分更新时不传 projectPlans）
        if (dto.getProjectPlans() != null)
        {
            nursingProjectPlanMapper.deleteByNursingPlanId(dto.getId());
            insertProjectPlans(dto.getProjectPlans(), dto.getId());
        }
        return result;
    }

    /**
     * 批量删除护理计划
     *
     * @param ids 需要删除的护理计划主键集合
     * @return 结果
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public int deleteNursingPlanByIds(Long[] ids)
    {
        for (Long id : ids)
        {
            nursingProjectPlanMapper.deleteByNursingPlanId(id);
        }
        return removeByIds(Arrays.asList(ids)) ? 1 : 0;
    }

    /**
     * 删除护理计划
     *
     * @param id 护理计划主键
     * @return 结果
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public int deleteNursingPlanById(Long id)
    {
        nursingProjectPlanMapper.deleteByNursingPlanId(id);
        return removeById(id) ? 1 : 0;
    }

    /**
     * 查询所有护理计划（下拉选项）
     *
     * @return 护理计划集合
     */
    @Override
    public List<NursingPlan> selectAllNursingPlan()
    {
        LambdaQueryWrapper<NursingPlan> wrapper = new LambdaQueryWrapper<>();
        wrapper.orderByAsc(NursingPlan::getSortNo).orderByAsc(NursingPlan::getId);
        return list(wrapper);
    }

    /**
     * 批量新增护理计划关联的护理项目（过滤未选择项目的空行）
     *
     * @param projectPlans 护理计划关联项目列表
     * @param planId 护理计划id
     */
    private void insertProjectPlans(List<NursingProjectPlan> projectPlans, Long planId)
    {
        if (projectPlans == null || projectPlans.isEmpty())
        {
            return;
        }
        List<NursingProjectPlan> validPlans = projectPlans.stream()
                .filter(item -> item != null && item.getProjectId() != null)
                .collect(Collectors.toList());
        if (!validPlans.isEmpty())
        {
            nursingProjectPlanMapper.batchInsert(validPlans, planId);
        }
    }
}
