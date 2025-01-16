package com.px.system.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.px.system.entity.SysDepartment;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * 部门 Mapper 接口
 * </p>
 *
 * @author 品讯科技
 * @since 2023-11-08
 */
public interface SysDepartmentMapper extends BaseMapper<SysDepartment> {

    Long countData(@Param("nextDepCodeList") List<String> nextDepCodeList);
}
