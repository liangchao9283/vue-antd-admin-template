package com.imcore.admintemplate.model.dto;

import com.imcore.admintemplate.base.model.BasePage;
import lombok.Data;

@Data
public class SysOpLogDto extends BasePage {
    private String name;
    private Integer opType;
    private String success;
    private String searchBeginTime;
    private String searchEndTime;
}
