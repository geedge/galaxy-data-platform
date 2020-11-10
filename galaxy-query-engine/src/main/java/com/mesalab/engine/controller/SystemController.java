package com.mesalab.engine.controller;

import com.mesalab.common.annotation.SysLog;
import com.mesalab.common.model.StorageDeletion;
import com.mesalab.common.dto.results.BaseResult;
import com.mesalab.common.enums.ResultStatusEnum;
import com.mesalab.common.exception.BusinessException;
import com.mesalab.common.util.BaseResultUtil;
import com.mesalab.engine.service.SystemService;
import com.zdjizhi.utils.StringUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping(value = "/sys")
public class SystemController {

    @Autowired
    private SystemService systemService;


    @RequestMapping("/storage/quota")
    public BaseResult storageQuota() {
        return systemService.getStorageQuota();
    }

    @SysLog
    @RequestMapping("storage/daily-trend")
    public BaseResult dailyTrendOfStorage(@RequestParam Map<String, Object> param) {
        String searchStartTime = StringUtil.stripToEmpty((String)param.get("start_time"));
        String searchEndTime =  StringUtil.stripToEmpty((String)param.get("end_time"));
        return systemService.dailyTrendOfStorage(searchStartTime, searchEndTime);
    }

    @SysLog
    @PostMapping(value = "storage/deletion")
    public BaseResult storageDeletion(@RequestBody List<StorageDeletion> list) {

        if (StringUtil.isEmpty(list)) {
            return BaseResultUtil.failure(ResultStatusEnum.FAIL.getCode(), "未获取到参数");
        }

        for (StorageDeletion info : list) {
            if (StringUtil.isBlank(info.getLogType()) || StringUtil.isBlank(String.valueOf(info.getMaxDays()))) {
                throw new BusinessException(ResultStatusEnum.FAIL.getCode(), "参数校验: logType 与 maxDays 需同时填写", null);
            }
            if ("ALL".equalsIgnoreCase(info.getLogType()) && list.size() != 1) {
                throw new BusinessException(ResultStatusEnum.FAIL.getCode(), "参数校验: logType存在ALL时, 不能与其它类型共存", null);
            }
            if (!(info.getMaxDays() >= 0 && info.getMaxDays() <= 2000)) {
                throw new BusinessException(ResultStatusEnum.FAIL.getCode(), "参数校验: logType非All: maxDays 必须是数值且[0-2000]", null);
            }
        }
        return systemService.deleteStorage(list);
    }

    @SysLog
    @GetMapping(value = "storage/deletion")
    public BaseResult storageDeletionStatus(StorageDeletion storageDeletionInfo) {
        return systemService.getDeleteStorageStatus(storageDeletionInfo.getLogType());
    }

    @SysLog
    @DeleteMapping(value = "engine/tasks/{queryId}")
    public BaseResult killQuery(@PathVariable String queryId) {
        log.info("停止查询, 参数: queryId is {}", queryId);
        if (StringUtil.isBlank(queryId)) {
            return BaseResultUtil.success4Message("ok");
        }
        return systemService.deleteQueryTask(queryId);
    }

}
