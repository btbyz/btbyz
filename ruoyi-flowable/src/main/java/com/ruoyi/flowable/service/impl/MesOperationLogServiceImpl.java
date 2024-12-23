package com.ruoyi.flowable.service.impl;

import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.ExcelWriter;
import com.alibaba.excel.write.metadata.WriteSheet;
import com.alibaba.excel.write.metadata.style.WriteCellStyle;
import com.alibaba.excel.write.style.HorizontalCellStyleStrategy;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ruoyi.flowable.domain.dto.MesOperationLog;
import com.ruoyi.flowable.mapper.MesOperationLogMapper;
import com.ruoyi.flowable.service.IMesOperationLogService;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.VerticalAlignment;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDate;
import java.util.List;


/**
 * 系统运维日志Service业务层处理
 * 
 * @author ruoyi
 * @date 2024-10-24
 */
@Service
public class MesOperationLogServiceImpl extends ServiceImpl<MesOperationLogMapper, MesOperationLog>  implements IMesOperationLogService
{
    @Autowired
    private MesOperationLogMapper mesOperationLogMapper;

    @Autowired
    private IMesOperationLogService mesOperationLogService;

    /**
     * 查询系统运维日志
     * 
     * @param id 系统运维日志主键
     * @return 系统运维日志
     */
    @Override
    public MesOperationLog selectMesOperationLogById(Long id)
    {
        return mesOperationLogMapper.selectMesOperationLogById(id);
    }

    /**
     * 查询系统运维日志列表
     * 
     * @param mesOperationLog 系统运维日志
     * @return 系统运维日志
     */
    @Override
    public List<MesOperationLog> selectMesOperationLogList(MesOperationLog mesOperationLog)
    {
        return mesOperationLogMapper.selectMesOperationLogList(mesOperationLog);
    }

    /**
     * 新增系统运维日志
     *
     * @param mesOperationLog 系统运维日志
     * @return 结果
     */
    @Override
    public boolean insertMesOperationLog(MesOperationLog mesOperationLog)
    {
        mesOperationLog.updateFromFullDate();
        return mesOperationLogService.save(mesOperationLog);
    }

    /**
     * 修改系统运维日志
     *
     * @param mesOperationLog 系统运维日志
     * @return
     */
    @Override
    public boolean updateMesOperationLog(MesOperationLog mesOperationLog)
    {
        mesOperationLog.updateFromFullDate();
        return mesOperationLogService.updateById(mesOperationLog);
    }

    /**
     * 批量删除系统运维日志
     * 
     * @param ids 需要删除的系统运维日志主键
     * @return 结果
     */
    @Override
    public int deleteMesOperationLogByIds(Long[] ids)
    {
        return mesOperationLogMapper.deleteMesOperationLogByIds(ids);
    }

    /**
     *批量新增运维日志
     *
     * @param mesOperationLogs 系统运维日志
     * @return 结果
     */
    @Override
    public boolean batchInsertMesOperationLog(List<MesOperationLog> mesOperationLogs){
        for (MesOperationLog mesOperationLog : mesOperationLogs) {
            mesOperationLog.updateFromFullDate();
        }
        return mesOperationLogService.saveBatch(mesOperationLogs);
    }

    @Override
    public void exportMesOperationLog(MesOperationLog mesOperationLog, HttpServletResponse response) {
        response.reset();
        String fileName = "log_" + LocalDate.now() + ".xlsx";
        response.setHeader("Content-Disposition", "attachment;filename=" + fileName + "");
        response.setContentType("application/octet-stream; charset=UTF-8");
        try {
            List<MesOperationLog> dataList = mesOperationLogService.selectMesOperationLogList(mesOperationLog);
            // 设置样式
            WriteCellStyle contentWriteCellStyle = new WriteCellStyle();
            contentWriteCellStyle.setWrapped(true);
            contentWriteCellStyle.setVerticalAlignment(VerticalAlignment.CENTER);
            contentWriteCellStyle.setHorizontalAlignment(HorizontalAlignment.LEFT);
            HorizontalCellStyleStrategy horizontalCellStyleStrategy =
                    new HorizontalCellStyleStrategy(null, contentWriteCellStyle);
            ExcelWriter excelWriter = EasyExcel.write(response.getOutputStream()).build();
            WriteSheet sheet = EasyExcel.writerSheet().registerWriteHandler(new HorizontalCellStyleStrategy(new WriteCellStyle(), contentWriteCellStyle)).head(MesOperationLog.class).build();
            excelWriter.write(dataList, sheet);
            excelWriter.finish();
            response.flushBuffer();

        } catch (IOException e) {
            log.error("导出异常。", e);
        }
    }

    /**
     * 根据分组名查询数量
     * @param mesOperationLog
     * @return
     */
    @Override
    public List<MesOperationLog> selectCountByDimension(MesOperationLog mesOperationLog) {
        return mesOperationLogMapper.selectCountByDimension(mesOperationLog);
    }

}
