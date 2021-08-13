package com.imcore.admintemplate.base.model;

import lombok.Data;

import java.util.Date;
@Data
public class BaseToken {
    private String token;
    private Long expireAt;
}
