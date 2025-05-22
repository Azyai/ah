package com.itay.mapper;

import ch.qos.logback.core.net.SMTPAppenderBase;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.itay.dto.response.ParticipationResp;
import com.itay.entity.Participation;
import com.itay.request.IdRequest;
import jakarta.validation.constraints.NotNull;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface ParticipationMapper extends BaseMapper<Participation> {


    String selectUserName(@NotNull @Param("userId") Long userId);
}
