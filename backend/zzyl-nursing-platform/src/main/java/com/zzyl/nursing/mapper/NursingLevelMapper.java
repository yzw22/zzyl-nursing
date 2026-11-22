package com.zzyl.nursing.mapper;

import java.util.List;
import com.zzyl.nursing.domain.NursingLevel;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.zzyl.nursing.vo.NursingLevelVo;
import org.apache.ibatis.annotations.Mapper;

/**
 * 护理等级Mapper接口
 */
@Mapper
public interface NursingLevelMapper extends BaseMapper<NursingLevel> {

}
