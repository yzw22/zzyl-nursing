package com.zzyl.nursing.service.impl;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import cn.hutool.core.util.ObjectUtil;
import com.alibaba.fastjson2.JSON;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.zzyl.common.exception.base.BaseException;
import com.zzyl.common.utils.CodeGenerator;
import com.zzyl.common.utils.DateUtils;
import com.zzyl.nursing.domain.*;
import com.zzyl.nursing.dto.CheckInApplyDto;
import com.zzyl.nursing.dto.CheckInElderDto;
import com.zzyl.nursing.mapper.*;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.zzyl.nursing.service.ICheckInService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.transaction.annotation.Transactional;

/**
 * 入住Service业务层处理
 *
 * @author mcy
 * @date 2026-03-05
 */
@Service
public class CheckInServiceImpl extends ServiceImpl<CheckInMapper, CheckIn> implements ICheckInService {
    @Autowired
    private CheckInMapper checkInMapper;

    @Autowired
    private ElderMapper elderMapper;

    @Autowired
    private BedMapper bedMapper;

    @Autowired
    private ContractMapper contractMapper;

    @Autowired
    private CheckInConfigMapper checkInConfigMapper;

    /**
     * 查询入住
     *
     * @param id 入住主键
     * @return 入住
     */
    @Override
    public CheckIn selectCheckInById(Long id) {
        return getById(id);
    }

    /**
     * 查询入住列表
     *
     * @param checkIn 入住
     * @return 入住
     */
    @Override
    public List<CheckIn> selectCheckInList(CheckIn checkIn) {
        return checkInMapper.selectCheckInList(checkIn);
    }

    /**
     * 新增入住
     *
     * @param checkIn 入住
     * @return 结果
     */
    @Override
    public int insertCheckIn(CheckIn checkIn) {
        return save(checkIn) ? 1 : 0;
    }

    /**
     * 修改入住
     *
     * @param checkIn 入住
     * @return 结果
     */
    @Override
    public int updateCheckIn(CheckIn checkIn) {
        return updateById(checkIn) ? 1 : 0;
    }

    /**
     * 批量删除入住
     *
     * @param ids 需要删除的入住主键
     * @return 结果
     */
    @Override
    public int deleteCheckInByIds(Long[] ids) {
        return removeByIds(Arrays.asList(ids)) ? 1 : 0;
    }

    /**
     * 删除入住信息
     *
     * @param id 入住主键
     * @return 结果
     */
    @Override
    public int deleteCheckInById(Long id) {
        return removeById(id) ? 1 : 0;
    }

    /**
     * 入住申请
     * @param checkInApplyDto
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void apply(CheckInApplyDto checkInApplyDto) {
        //判断老人是否已经入住
        LambdaQueryWrapper<Elder> elderQueryWrapper = new LambdaQueryWrapper<>();
        elderQueryWrapper.eq(Elder::getIdCardNo, checkInApplyDto.getCheckInElderDto().getIdCardNo());
        elderQueryWrapper.in(Elder::getStatus, 1, 4); // 1 在院 4 入住中
        Elder elder = elderMapper.selectOne(elderQueryWrapper);
        if (ObjectUtil.isNotEmpty(elder)) {
            throw new BaseException("该老人已入住");
        }

        // 更新床位状态为已入住
        // 根据页面传递的入住配置信息中的床位id查询床位信息
        Bed bed = bedMapper.selectById(checkInApplyDto.getCheckInConfigDto().getBedId());
        bed.setBedStatus(1); // 1 已入住
        bedMapper.updateById(bed);

        // 新增或更新老人基本信息
        elder = insertOrUpdateElder(bed, checkInApplyDto.getCheckInElderDto());

        // 新增签约办理
        String contractNo = "HT" + CodeGenerator.generateContractNumber();
        insertContract(elder, contractNo, checkInApplyDto);

        // 新增入住信息
        CheckIn checkIn = insertCheckInInfo(elder,checkInApplyDto);

        // 新增入住配置
        insertCheckInConfig(checkIn.getId(),checkInApplyDto);

    }

    /**
     * 新增入住信息
     * @param id
     * @param checkInApplyDto
     */
    private void insertCheckInConfig(Long id, CheckInApplyDto checkInApplyDto) {
        CheckInConfig checkInConfig = new CheckInConfig();
        checkInConfig.setCheckInId(id);
        // 拷贝信息
        BeanUtils.copyProperties(checkInApplyDto.getCheckInConfigDto(),checkInConfig);
        checkInConfigMapper.insert(checkInConfig);
    }

    /**
     * 新增入住信息
     * @param elder
     * @param checkInApplyDto
     */
    private CheckIn insertCheckInInfo(Elder elder, CheckInApplyDto checkInApplyDto) {
        CheckIn checkIn = new CheckIn();
        checkIn.setElderId(elder.getId());
        checkIn.setElderName(elder.getName());
        checkIn.setIdCardNo(elder.getIdCardNo());
        checkIn.setStartDate(checkInApplyDto.getCheckInConfigDto().getStartDate());
        checkIn.setEndDate(checkInApplyDto.getCheckInConfigDto().getEndDate());
        checkIn.setNursingLevelName(checkInApplyDto.getCheckInConfigDto().getNursingLevelName());
        checkIn.setBedNumber(elder.getBedNumber());
        checkIn.setStatus(0);
        checkIn.setRemark(JSON.toJSONString(checkInApplyDto.getElderFamilyDtoList()));
        checkInMapper.insert(checkIn);
        return checkIn;
    }

    /**
     * 新增签约办理
     *
     * @param elder           老人信息
     * @param contractNo      合同编号
     * @param checkInApplyDto 入住申请信息
     */
    private void insertContract(Elder elder, String contractNo, CheckInApplyDto checkInApplyDto) {
        // 签约对象
        Contract contract = new Contract();
        BeanUtils.copyProperties(checkInApplyDto.getCheckInContractDto(), contract);
        contract.setContractNumber(contractNo);
        contract.setElderId(elder.getId());
        contract.setElderName(elder.getName());

        //获取入住开始时间和入住结束时间
        LocalDateTime startDate = checkInApplyDto.getCheckInConfigDto().getStartDate();
        LocalDateTime endDate = checkInApplyDto.getCheckInConfigDto().getEndDate();
        contract.setStartDate(startDate);
        contract.setEndDate(endDate);
        int status = startDate.isAfter(LocalDateTime.now()) ? 0 : 1;
        contract.setStatus(status);
        contractMapper.insert(contract);
    }

    /**
     * 新增或更新老人信息
     *
     * @param bed
     * @param checkInElderDto
     */
    private Elder insertOrUpdateElder(Bed bed, CheckInElderDto checkInElderDto) {
        // 准备一个老人对象（保存要新增或更新的数据）
        Elder elder = new Elder();
        BeanUtils.copyProperties(checkInElderDto, elder);
        elder.setBedId(bed.getId());
        elder.setBedNumber(bed.getBedNumber());
        elder.setStatus(1);
        // 查询老人信息(根据老人的身份证号及没入住的状态)
        LambdaQueryWrapper<Elder> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Elder::getIdCardNo, elder.getIdCardNo());
        queryWrapper.notIn(Elder::getStatus, 1, 4);
        Elder elderDb = elderMapper.selectOne(queryWrapper);
        if (ObjectUtil.isNotEmpty(elderDb)) {
            // 更新
            elder.setId(elderDb.getId());
            elderMapper.updateById(elder);
        } else {
            // 插入
            elderMapper.insert(elder);
        }
        return elder;
    }


}
