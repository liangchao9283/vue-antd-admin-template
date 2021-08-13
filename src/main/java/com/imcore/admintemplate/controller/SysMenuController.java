package com.imcore.admintemplate.controller;


import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.imcore.admintemplate.base.annotion.BusinessLog;
import com.imcore.admintemplate.base.enums.LogAnnotionOpTypeEnum;
import com.imcore.admintemplate.base.model.BasePage;
import com.imcore.admintemplate.base.model.BaseResponse;
import com.imcore.admintemplate.exception.model.ImcoreBusinessException;
import com.imcore.admintemplate.model.dto.SysMenuDto;
import com.imcore.admintemplate.model.vo.MenuTreeVo;
import com.imcore.admintemplate.model.vo.SysMenuVo;
import com.imcore.admintemplate.service.ISysMenuService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author liang chao
 * @since 2021-07-10
 */
@RestController
@RequestMapping("/sys-menu")
public class SysMenuController {

    @Autowired
    private ISysMenuService sysMenuService;

    @RequestMapping("/")
    @PreAuthorize("hasRole('super_admin')")
    @BusinessLog(title = "系统菜单_列表", opType = LogAnnotionOpTypeEnum.QUERY)
    public BaseResponse<Page<SysMenuVo>> menuListAll(@RequestBody BasePage basePage){
        return BaseResponse.success(sysMenuService.menuListAll(basePage));
    }

    /**
     * 获取系统菜单树，用于新增，编辑时选择上级节点
     *
     */
    @GetMapping("/getMenuTree")
    @PreAuthorize("hasRole('super_admin')")
    @BusinessLog(title = "系统菜单_树", opType = LogAnnotionOpTypeEnum.TREE)
    public BaseResponse<List<MenuTreeVo>> getMenuTree(){
        return BaseResponse.success(sysMenuService.getMenuTree());
    }

    /**
     * 获取系统菜单树，用于给角色授权时选择
     */
    @GetMapping("/treeForGrant")
    @PreAuthorize("hasRole('super_admin') or hasAuthority('sysRole:grantMenu')")
    @BusinessLog(title = "系统菜单_授权树", opType = LogAnnotionOpTypeEnum.TREE)
    public BaseResponse<List<MenuTreeVo>> treeForGrant(@RequestParam("roleId") String roleId){
        return BaseResponse.success(sysMenuService.treeForGrant(roleId));
    }

    @PostMapping("/menuAdd")
    @PreAuthorize("hasRole('super_admin')")
    @BusinessLog(title = "系统菜单_增加", opType = LogAnnotionOpTypeEnum.ADD)
    public BaseResponse sysMenuAdd(@RequestBody @Validated SysMenuDto dto) throws ImcoreBusinessException {
        sysMenuService.menuAdd(dto);
        return BaseResponse.success();
    }

    @PostMapping("/menuEdit")
    @PreAuthorize("hasRole('super_admin')")
    @BusinessLog(title = "系统菜单_编辑", opType = LogAnnotionOpTypeEnum.EDIT)
    public BaseResponse sysMenuEdit(@RequestBody @Validated SysMenuDto dto) throws ImcoreBusinessException{
        sysMenuService.menuEdit(dto);
        return BaseResponse.success();
    }

    @DeleteMapping("/")
    @PreAuthorize("hasRole('super_admin')")
    @BusinessLog(title = "系统菜单_删除", opType = LogAnnotionOpTypeEnum.DELETE)
    public BaseResponse deleteById(@RequestParam("id") String id){
        sysMenuService.deleteById(id);
        return BaseResponse.success();
    }

    @GetMapping("/")
    @PreAuthorize("hasRole('super_admin')")
    @BusinessLog(title = "系统菜单_查看", opType = LogAnnotionOpTypeEnum.DETAIL)
    public BaseResponse<SysMenuVo> getById(@RequestParam("id") String id){
        return BaseResponse.success(sysMenuService.getById(id));
    }


}

