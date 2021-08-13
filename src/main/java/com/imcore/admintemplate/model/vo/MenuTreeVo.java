package com.imcore.admintemplate.model.vo;

import lombok.Data;

import java.util.List;

@Data
public class MenuTreeVo {
    private String id;
    private String parentId;
    private String title;
    private String value;
    private List<MenuTreeVo> children;
}
