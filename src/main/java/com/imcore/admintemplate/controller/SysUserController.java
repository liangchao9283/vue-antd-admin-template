package com.imcore.admintemplate.controller;


import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.imcore.admintemplate.base.annotion.BusinessLog;
import com.imcore.admintemplate.base.enums.LogAnnotionOpTypeEnum;
import com.imcore.admintemplate.base.model.BasePage;
import com.imcore.admintemplate.base.model.BaseResponse;
import com.imcore.admintemplate.exception.model.ImcoreBusinessException;
import com.imcore.admintemplate.model.bo.SysUser;
import com.imcore.admintemplate.model.dto.SysUserPasswordDto;
import com.imcore.admintemplate.model.vo.SysUserVo;
import com.imcore.admintemplate.service.ISysUserRoleService;
import com.imcore.admintemplate.service.ISysUserService;
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
 * @since 2021-07-08
 */
@RestController
@RequestMapping("/sys-user")
public class SysUserController {

    @Autowired
    private ISysUserService sysUserService;
    @Autowired
    private ISysUserRoleService sysUserRoleService;

    @PostMapping("/")
    @PreAuthorize("hasAnyAuthority('ROLE_super_admin','sysUser')") //ROLE_super_admin是表示要有super_admin角色,/sysUser即是前端"用户管理"页面的准入权限,也是此接口的调用权限,'ROLE_super_admin'和'/sysUser'满足任何一个即可访问本接口
    @BusinessLog(title = "系统用户_查询", opType = LogAnnotionOpTypeEnum.QUERY)
    public BaseResponse<Page<SysUserVo>> userListAll(@RequestBody BasePage basePage){
        return BaseResponse.success(sysUserService.userListAll(basePage));
    }

    @PostMapping("/userAdd")
    @PreAuthorize("hasRole('super_admin') or hasAuthority('sysUser:add')")
    @BusinessLog(title = "系统用户_新增", opType = LogAnnotionOpTypeEnum.ADD)
    public BaseResponse userAdd(@RequestBody  @Validated SysUser sysUser) throws ImcoreBusinessException {
        sysUserService.userAdd(sysUser);
        return BaseResponse.success();
    }

    @PostMapping("/userEdit")
    @PreAuthorize("hasRole('super_admin') or hasAuthority('sysUser:edit')")
    @BusinessLog(title = "系统用户_编辑", opType = LogAnnotionOpTypeEnum.EDIT)
    public BaseResponse userEdit(@RequestBody  @Validated SysUser sysUser) throws ImcoreBusinessException {
        sysUserService.userEdit(sysUser);
        return BaseResponse.success();
    }

    @DeleteMapping("/")
    @PreAuthorize("hasRole('super_admin') or hasAuthority('sysUser:delete')")
    @BusinessLog(title = "系统用户_删除", opType = LogAnnotionOpTypeEnum.DELETE)
    public BaseResponse deleteById(@RequestParam("id") String id) throws ImcoreBusinessException{
        sysUserService.deleteById(id);
        return BaseResponse.success();
    }

    @GetMapping("/userOwnRole")
    @PreAuthorize("hasRole('super_admin') or hasAuthority('sysUser:grantRole')")
    @BusinessLog(title = "系统用户_拥有角色", opType = LogAnnotionOpTypeEnum.DETAIL)
    public BaseResponse sysUserOwnRole(@RequestParam("userId") String userId){
        List<String> roleIds = sysUserRoleService.selectRoleIdsByUserId(userId);
        return BaseResponse.success(roleIds);
    }

    @PostMapping("/userGrantRole/{userId}")
    @PreAuthorize("hasRole('super_admin') or hasAuthority('sysUser:grantRole')")
    @BusinessLog(title = "系统用户_授权角色", opType = LogAnnotionOpTypeEnum.GRANT)
    public BaseResponse userGrantRole(@PathVariable String userId,@RequestBody List<String> grantRoleIdList) throws ImcoreBusinessException{
        sysUserRoleService.userGrantRole(userId,grantRoleIdList);
        return BaseResponse.success();
    }

    @GetMapping("/userRestPassword")
    @PreAuthorize("hasRole('super_admin') or hasAuthority('sysUser:restPassword')")
    @BusinessLog(title = "系统用户_重置密码", opType = LogAnnotionOpTypeEnum.UPDATE)
    public BaseResponse userRestPassword(@RequestParam("id") String id) throws ImcoreBusinessException{
        sysUserService.userRestPassword(id);
        return BaseResponse.success();
    }

    @PostMapping("/editUserStatus")
    @PreAuthorize("hasRole('super_admin') or hasAuthority('sysUser:editUserStatus')")
    @BusinessLog(title = "系统用户_修改状态", opType = LogAnnotionOpTypeEnum.CHANGE_STATUS)
    public BaseResponse editUserStatus(@RequestBody SysUser sysUser) throws ImcoreBusinessException{
        sysUserService.editUserStatus(sysUser);
        return BaseResponse.success();
    }

    @PostMapping("/userEditPassword")
    @BusinessLog(title = "系统用户_修改密码", opType = LogAnnotionOpTypeEnum.UPDATE)
    public BaseResponse userEditPassword(@RequestBody @Validated SysUserPasswordDto passwordDto) throws ImcoreBusinessException{
        sysUserService.userEditPassword(passwordDto);
        return BaseResponse.success();
    }
}

