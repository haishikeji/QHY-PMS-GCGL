package com.px.bean.vo;

import com.px.common.bean.vo.CommonStatisticsVo;
import lombok.Data;

import java.util.List;

@Data
public class EmployeeStatementVo {

    /**
     * 各部门在职人数
     */
    private List<CommonStatisticsVo> deptStatistics;
    /**
     * 职能人数
     */
    private List<CommonStatisticsVo> roleStatistics;
    /**
     * 员工证书统计
     */
    private List<CommonStatisticsVo> certificateStatistics;
    /**
     * 员工类型分布
     */
    private List<CommonStatisticsVo> empTypeStatistics;
    /**
     * 司龄分布
     */
    private List<CommonStatisticsVo> stayAgeStatistics;

}
