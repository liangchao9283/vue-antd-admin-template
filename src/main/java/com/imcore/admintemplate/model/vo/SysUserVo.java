package com.imcore.admintemplate.model.vo;

import lombok.Data;

@Data
public class SysUserVo {

    private String id;
    private String username;
    private String name;
    private String phone;
    private Integer sex;
    private String email;
    private Integer status;
    private String roles;
}
