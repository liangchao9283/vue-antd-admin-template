package com.imcore.admintemplate.model.dto;

import com.imcore.admintemplate.base.model.BasePage;
import lombok.Data;

@Data
public class SysVisLogDto extends BasePage {
    private String name;
    private Integer visType;
    private String success;
    private String searchBeginTime;
    private String searchEndTime;
}
