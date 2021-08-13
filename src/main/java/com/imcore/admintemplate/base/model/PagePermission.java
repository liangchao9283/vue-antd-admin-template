package com.imcore.admintemplate.base.model;

import lombok.Data;

import java.util.List;

@Data
public class PagePermission {
    private String id;                  //用于判断用户是否能够进入页面
    private List<String> operation;     //用于判断用户是否有权限操作页面的按钮
}
