package com.px.controller.system;

import com.px.common.result.Result;
import com.px.system.entity.SysType;
import com.px.system.service.SysTypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * 类型表管理
 *
 * @author 品讯科技
 * @since 2023-10-27
 */
@RestController
@RequestMapping("/sys/type")
public class SysTypeController {

    @Autowired
    private SysTypeService typeService;

    /**
     * 根据code查询配置信息
     *
     * @param codeList
     * @return
     */
    @PostMapping("/getByCodes")
    public Result getByCodes(@RequestBody List<String> codeList) {
        Map<String, String> resMap = new LinkedHashMap<>();
        List<SysType> typeList = typeService.listByCodes(codeList);
        for (SysType sysType : typeList) {
            resMap.put(sysType.getCode(), sysType.getValue());
        }
        return new Result(resMap);
    }

    /**
     * 根据code设置配置信息
     *
     * @param map
     * @return
     */
    @PostMapping("/editByCodes")
    public Result editByCode(@RequestBody Map<String, String> map) {
        List<SysType> list = new ArrayList<>();
        for (Map.Entry<String, String> entry : map.entrySet()) {
            String key = entry.getKey();
            String value = entry.getValue();
            SysType type = typeService.getByCode(key);
            if (type != null) {
                type.setValue(value);
                list.add(type);
            }
        }
        typeService.updateBatchById(list);
        return Result.ok();
    }


    @GetMapping("/getValByCode")
    @Transactional
    public Result getValByCode(String code) {
        SysType type = typeService.getByCode(code);
        return new Result(type != null ? type.getValue() : null);
    }

}