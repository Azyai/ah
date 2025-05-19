package com.itay.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.itay.entity.Activity;
import com.itay.entity.ActivityRestriction;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface ActivityRestrictionMapper extends BaseMapper<ActivityRestriction> {
}
