package com.imcore.admintemplate.model.vo;

import com.imcore.admintemplate.model.bo.MenuPermission;
import lombok.Data;

import java.util.List;

@Data
public class SysMenuVo {

    private String id;
    private String name;
    private Integer type;
    private String parentId;
    private String icon;
    private Integer sort;
    private String remark;
    private String component;
    private String path;
    private Integer weight;
    private Boolean visible;
    private String permissionCode;
    private  List<MenuPermission> permissionsData;
    private List<SysMenuVo> children;
}
