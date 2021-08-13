package com.imcore.admintemplate.mapper;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.imcore.admintemplate.model.bo.MenuPermission;
import com.imcore.admintemplate.model.bo.SysMenu;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.imcore.admintemplate.model.vo.SysMenuVo;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author liang chao
 * @since 2021-07-10
 */
public interface SysMenuMapper extends BaseMapper<SysMenu> {

    /**
     * 根据父id查询,带分页
     * @param page
     * @return
     */
    List<SysMenuVo> menuListByParentIdWithPage(String parentId, Page<SysMenuVo> page);

    List<SysMenuVo> menuListByParentId(String parentId,@Param("types") List<Integer> types);

    List<MenuPermission> permissionListByParentId(String parentId);

    SysMenu selectPermissionCode(String permissionCode);

    List<String> selectIdByParentId(String parentId);

    List<SysMenu> selectSystemWeightMenus();

}
