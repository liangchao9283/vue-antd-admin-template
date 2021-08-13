package com.imcore.admintemplate.service;

import com.imcore.admintemplate.base.model.PagePermission;
import com.imcore.admintemplate.model.vo.RouteVo;

import java.util.List;

public interface LoginService {

    List<RouteVo> selectUserRoutes();

    List<PagePermission> selectUserPagePermission();
}
