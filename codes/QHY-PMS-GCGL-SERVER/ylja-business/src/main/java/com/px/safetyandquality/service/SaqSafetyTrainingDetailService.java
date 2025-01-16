package com.px.safetyandquality.service;

import com.px.safetyandquality.entity.SaqSafetyTrainingDetail;
import com.baomidou.mybatisplus.extension.service.IService;
import com.px.safetyandquality.bean.dto.SaqSafetyTrainingDetailQueryDto;
import com.px.safetyandquality.bean.vo.SaqSafetyTrainingDetailVo;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;

import java.util.List;

/**
 * <p>
 * 培训明细 服务类
 * </p>
 *
 * @author 品讯科技
 * @since 2023-12-24
 */
public interface SaqSafetyTrainingDetailService extends IService<SaqSafetyTrainingDetail> {

   /**
   * 分页查询
   * @param dto
   * @return
   */
   Page<SaqSafetyTrainingDetailVo> getPage(SaqSafetyTrainingDetailQueryDto dto);

   List<SaqSafetyTrainingDetailVo> buildSaqSafetyTrainingDetailVoList(List<SaqSafetyTrainingDetail> list);

}

