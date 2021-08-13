package com.imcore.admintemplate.model.bo;

import com.baomidou.mybatisplus.annotation.TableName;
import com.imcore.admintemplate.base.model.BaseModel;
import com.baomidou.mybatisplus.annotation.TableField;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 
 * </p>
 *
 * @author liang chao
 * @since 2021-07-20
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@TableName("sys_role_menu")
public class SysRoleMenu extends BaseModel {

private static final long serialVersionUID=1L;

    @TableField("role_id")
    private String roleId;

    @TableField("menu_id")
    private String menuId;


}
