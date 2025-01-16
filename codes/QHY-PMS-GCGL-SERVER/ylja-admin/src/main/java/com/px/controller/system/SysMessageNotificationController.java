package com.px.controller.system;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.px.bean.vo.MessageVo;
import com.px.common.consts.DetailsPathConst;
import com.px.common.result.Result;
import com.px.common.util.CollUtils;
import com.px.common.util.security.SecurityUtils;
import com.px.marketmanagement.entity.MmEarnestMoney;
import com.px.marketmanagement.entity.MmEarnestMoneyRetrieve;
import com.px.marketmanagement.service.MmEarnestMoneyRetrieveService;
import com.px.marketmanagement.service.MmEarnestMoneyService;
import com.px.system.bean.dto.SysMessageNotificationQueryDto;
import com.px.system.bean.vo.SysMessageNotificationVo;
import com.px.system.entity.SysMessageNotification;
import com.px.system.querycondition.SysMessageNotificationQueryCondition;
import com.px.system.service.SysDepartmentService;
import com.px.system.service.SysMessageNotificationService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 消息中心管理
 *
 * @author 品讯科技
 * @since 2023-12-10
 */
@RestController
@RequestMapping("/sys/messageNotification")
public class SysMessageNotificationController {

    @Autowired
    private SysMessageNotificationService sysMessageNotificationService;
    @Autowired
    private SysDepartmentService departmentService;
    @Autowired
    private MmEarnestMoneyService earnestMoneyService;
    @Autowired
    private MmEarnestMoneyRetrieveService earnestMoneyRetrieveService;

    /**
     * 分页查询
     *
     * @param dto
     * @return
     */
    @GetMapping("/getPage")
    public Result<MessageVo> getPage(SysMessageNotificationQueryDto dto) {
        MessageVo vo = new MessageVo();
        dto.setUserId(SecurityUtils.getUserCode());
        Page<SysMessageNotificationVo> voPage = sysMessageNotificationService.getPage(dto);
        vo.setPageVo(voPage);
        List<SysMessageNotificationVo> records = voPage.getRecords();
        if (CollUtils.isNotEmpty(records)) {
            for (SysMessageNotificationVo record : records) {
                List<SysMessageNotification> smnList = record.getMessageNotifications();
                if (CollUtils.isNotEmpty(smnList)) {
                    for (SysMessageNotification smn : smnList) {
                        String content = smn.getContent();
                        if (StringUtils.isBlank(content)) {
                            MmEarnestMoneyRetrieve moneyRetrieve = earnestMoneyRetrieveService.getById(smn.getDataId());
                            if (content.startsWith("保证金收回") || content.startsWith("保证金转履约金")) {
                                String skipUrl = smn.getSkipUrl();
                                if (content.startsWith("保证金收回")) {
                                    skipUrl = DetailsPathConst.getSkipUri("保证金收回");
                                }
                                if (content.startsWith("保证金转履约金")) {
                                    skipUrl = DetailsPathConst.getSkipUri("保证金转履约金");
                                }
                                if (moneyRetrieve != null) {
                                    MmEarnestMoney earnestMoney = earnestMoneyService.getByCode(moneyRetrieve.getEarnestMoneyCode());
                                    if (earnestMoney != null) {
                                        skipUrl += skipUrl + "?id=" + earnestMoney.getId() + "&code=" + earnestMoney.getEarnestMoneyCode();
                                    }
                                }
                                smn.setSkipUrl(skipUrl);
                            }
                        }
                    }
                }
            }
        }
        SysMessageNotificationQueryDto queryDto = new SysMessageNotificationQueryDto();
        queryDto.setUserId(SecurityUtils.getUserCode());
        queryDto.setIsRead(0);
        Integer num = sysMessageNotificationService.count(SysMessageNotificationQueryCondition.build(queryDto));
        vo.setNum(num);
        return new Result(vo);
    }

    /**
     * 分页查询-项目竣工验收快通知
     *
     * @param dto
     * @return
     */
    @GetMapping("/getProjectMsgPage")
    public Result<Page<SysMessageNotification>> getProjectMsgPage(SysMessageNotificationQueryDto dto) {
        dto.setType(4);
        LambdaQueryWrapper<SysMessageNotification> queryWrapper = SysMessageNotificationQueryCondition.build(dto);
        if (StringUtils.isNotBlank(dto.getDepCode())) {
            queryWrapper.in(SysMessageNotification::getDepCode, departmentService.getNextLevel(dto.getDepCode()));
        }
        Page<SysMessageNotification> page = sysMessageNotificationService.page(new Page<>(dto.getPageNum(), dto.getPageSize()), queryWrapper);
        return new Result(page);
    }

    /**
     * 查询当前用户消息数量
     *
     * @return
     */
    @GetMapping("/countUserMsg")
    public Result<Integer> getPage() {
        SysMessageNotificationQueryDto dto = new SysMessageNotificationQueryDto();
        dto.setUserId(SecurityUtils.getUserCode());
        Integer num = sysMessageNotificationService.count(SysMessageNotificationQueryCondition.build(dto));
        return new Result(num);
    }

    /**
     * 更新消息为已读-单个
     *
     * @param id
     * @return
     */
    @GetMapping("/readMsg")
    public Result readMsg(Long id) {
        SysMessageNotification messageNotification = sysMessageNotificationService.getById(id);
        if (messageNotification != null) {
            messageNotification.setIsRead(1);
            sysMessageNotificationService.updateById(messageNotification);
        }
        return Result.ok();
    }

    /**
     * 更新消息为已读-批量
     *
     * @param ids
     * @return
     */
    @PostMapping("/readBathMsg")
    public Result readBathMsg(@RequestBody List<Long> ids) {
        sysMessageNotificationService.update(new LambdaUpdateWrapper<SysMessageNotification>()
                .in(SysMessageNotification::getId, ids).set(SysMessageNotification::getIsRead, 1));
        return Result.ok();
    }

}