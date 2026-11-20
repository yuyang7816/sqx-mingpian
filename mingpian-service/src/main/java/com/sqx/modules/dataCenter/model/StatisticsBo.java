package com.sqx.modules.dataCenter.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.sqx.common.exception.SqxException;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Year;
import java.time.YearMonth;

/**
 * @author chengming
 * @since 2025/9/4
 */
@Data
public class StatisticsBo {

    @ApiModelProperty(value = "year=按年查询 month=按月查询 day=按天查询")
    @NotBlank(message = "请输入查询类型")
    private String findType;

    @ApiModelProperty(value = "查询日期 yyyy-MM-dd")
    @NotNull(message = "请输入查询日期")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate findDate;

    @ApiModelProperty(value = "提现统计类型 1=用户 2=商户")
    private Integer type;

    @ApiModelProperty(hidden = true)
    private LocalDateTime startTime;
    @ApiModelProperty(hidden = true)
    private LocalDateTime endTime;

    @ApiModelProperty(hidden = true)
    public void initFormattedDateRange() {
        LocalDateTime start;
        LocalDateTime end;

        switch (findType.toLowerCase()) {
            case "day":
                start = findDate.atStartOfDay();
                end = findDate.atTime(23, 59, 59);
                break;

            case "month":
                YearMonth yearMonth = YearMonth.from(findDate);
                start = yearMonth.atDay(1).atStartOfDay();
                end = yearMonth.atEndOfMonth().atTime(23, 59, 59);
                break;

            case "year":
                Year year = Year.from(findDate);
                start = year.atDay(1).atStartOfDay(); // 1月1日
                LocalDate lastDay = LocalDate.of(year.getValue(), 12, 31);
                end = lastDay.atTime(23, 59, 59);
                break;

            default:
                throw new SqxException("不支持的 findType: " + findType);
        }

        this.startTime = start;
        this.endTime = end;
    }
}
