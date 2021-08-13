package com.imcore.admintemplate.model.dto;

import com.imcore.admintemplate.model.bo.MenuPermission;
import lombok.Data;
import org.hibernate.validator.constraints.NotBlank;

import javax.validation.constraints.NotNull;
import java.util.List;

@Data
public class SysMenuDto {
    private String id;
    @NotBlank
    private String name;
    @NotNull
    private Integer type;
    @NotBlank
    private String parentId;
    private String icon;
    private Integer sort;
    private String remark;
    @NotBlank
    private String component;
    @NotBlank
    private String path;
    private Integer weight;
    @NotNull
    private Boolean visible;

    List<MenuPermission> permissionsData;
}
