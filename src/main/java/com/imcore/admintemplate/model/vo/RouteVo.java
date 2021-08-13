package com.imcore.admintemplate.model.vo;

import lombok.Data;

import java.util.List;

@Data
public class RouteVo {
    private String id;
    private String router;
    private String path;
    private String name;
    private String icon;
    private String link;
    private String component;
    private Boolean visible;
    private String authority;
    private List<RouteVo> children;
}
