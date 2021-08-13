package com.imcore.admintemplate.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.imcore.admintemplate.base.model.BasePage;
import com.imcore.admintemplate.exception.model.ImcoreBusinessException;
import com.imcore.admintemplate.model.bo.SysMenu;
import com.baomidou.mybatisplus.extension.service.IService;
import com.imcore.admintemplate.model.dto.SysMenuDto;
import com.imcore.admintemplate.model.vo.MenuTreeVo;
import com.imcore.admintemplate.model.vo.SysMenuVo;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author liang chao
 * @since 2021-07-10
 */
public interface ISysMenuService extends IService<SysMenu> {


    Page<SysMenuVo> menuListAll(BasePage basePage);

    List<MenuTreeVo> getMenuTree();

    void menuAdd(SysMenuDto dto) throws ImcoreBusinessException;

    void menuEdit(SysMenuDto dto) throws ImcoreBusinessException;

    SysMenuVo getById(String id);

    void deleteById(String id);

    List<MenuTreeVo> treeForGrant(String roleId);

    /**
     * 查询所有系统权重的菜单(只查菜单数据,不查按钮权限数据)
     * @return
     */
    List<SysMenu> selectSystemWeightMenus();

}
