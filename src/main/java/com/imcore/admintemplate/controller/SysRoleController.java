package com.imcore.admintemplate.controller;


import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.imcore.admintemplate.base.annotion.BusinessLog;
import com.imcore.admintemplate.base.enums.LogAnnotionOpTypeEnum;
import com.imcore.admintemplate.base.model.BasePage;
import com.imcore.admintemplate.base.model.BaseResponse;
import com.imcore.admintemplate.exception.model.ImcoreBusinessException;
import com.imcore.admintemplate.model.bo.SysRole;
import com.imcore.admintemplate.service.ISysRoleMenuService;
import com.imcore.admintemplate.service.ISysRoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * <p>
 * 角色表 前端控制器
 * </p>
 *
 * @author liang chao
 * @since 2021-07-19
 */
@RestController
@RequestMapping("/sys-role")
public class SysRoleController {

    @Autowired
    private ISysRoleService sysRoleService;

    @Autowired
    private ISysRoleMenuService sysRoleMenuService;

    @PostMapping("/")
    @PreAuthorize("hasRole('super_admin') or hasAuthority('sysRole')")
    @BusinessLog(title = "系统角色_查询", opType = LogAnnotionOpTypeEnum.QUERY)
    public BaseResponse<Page<SysRole>> roleListAll(@RequestBody BasePage basePage){
        return BaseResponse.success(sysRoleService.roleListAll(basePage));
    }

    /**
     * 获取所有角色，用于给用户授权时选择
     */
    @GetMapping("/findAll")
    @PreAuthorize("hasRole('super_admin') or hasAuthority('sysUser:grantRole')")
    @BusinessLog(title = "系统角色_查询所有", opType = LogAnnotionOpTypeEnum.QUERY)
    public BaseResponse <List<SysRole>> findAll(){
        return BaseResponse.success(sysRoleService.findAll());
    }

    @PostMapping("/roleAdd")
    @PreAuthorize("hasRole('super_admin') or hasAuthority('sysRole:add')")
    @BusinessLog(title = "系统角色_增加", opType = LogAnnotionOpTypeEnum.ADD)
    public BaseResponse roleAdd(@RequestBody @Validated SysRole sysRole) throws ImcoreBusinessException{
        sysRoleService.roleAdd(sysRole);
        return BaseResponse.success();
    }

    @PostMapping("/roleEdit")
    @PreAuthorize("hasRole('super_admin') or hasAuthority('sysRole:edit')")
    @BusinessLog(title = "系统角色_编辑", opType = LogAnnotionOpTypeEnum.EDIT)
    public BaseResponse roleEdit(@RequestBody @Validated SysRole sysRole) throws ImcoreBusinessException {
        sysRoleService.roleEdit(sysRole);
        return BaseResponse.success();
    }

    @DeleteMapping("/")
    @PreAuthorize("hasRole('super_admin') or hasAuthority('sysRole:delete')")
    @BusinessLog(title = "系统角色_删除", opType = LogAnnotionOpTypeEnum.DELETE)
    public BaseResponse roleDeleteById(@RequestParam(value = "id")String id) throws ImcoreBusinessException{
        sysRoleService.roleDeleteById(id);
        return BaseResponse.success();
    }

    @GetMapping("/roleOwnMenu")
    @PreAuthorize("hasRole('super_admin') or hasAuthority('sysRole:grantMenu')")
    @BusinessLog(title = "系统角色_拥有菜单", opType = LogAnnotionOpTypeEnum.DETAIL)
    public BaseResponse roleOwnMenu(@RequestParam("id")String id){
        List<String> menuIds = sysRoleMenuService.roleOwnMenu(id);
        return BaseResponse.success(menuIds);
    }

    @PostMapping("/roleGrantMenu/{roleId}")
    @PreAuthorize("hasRole('super_admin') or hasAuthority('sysRole:grantMenu')")
    @BusinessLog(title = "系统角色_授权菜单", opType = LogAnnotionOpTypeEnum.GRANT)
    public BaseResponse roleGrantMenu(@PathVariable String roleId,@RequestBody List<String> grantMenuIdList) throws ImcoreBusinessException{
        sysRoleMenuService.roleGrantMenu(roleId,grantMenuIdList);
        return BaseResponse.success();
    }
}

