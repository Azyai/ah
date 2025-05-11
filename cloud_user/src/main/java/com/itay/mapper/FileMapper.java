package com.itay.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.itay.entity.Menu;
import com.itay.entity.SysFile;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface FileMapper extends BaseMapper<SysFile> {

    int insertFile(SysFile sysFile);
}
