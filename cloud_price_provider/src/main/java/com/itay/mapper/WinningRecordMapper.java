package com.itay.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.itay.entity.Participation;
import com.itay.entity.WinningRecord;
import jakarta.validation.constraints.NotNull;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface WinningRecordMapper extends BaseMapper<WinningRecord> {

    String selectUserName(@Param("userId") @NotNull Long userId);
}
