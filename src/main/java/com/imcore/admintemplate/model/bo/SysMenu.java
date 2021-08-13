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
 * @since 2021-07-10
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@TableName("sys_menu")
public class SysMenu extends BaseModel {

private static final long serialVersionUID=1L;

    /**
     * 菜单名称
     */
    private String name;

    /**
     * 父级ID
     */
    @TableField("parent_id")
    private String parentId;

    /**
     * 类型: 0菜单目录,1菜单页面,2权限
     */
    private Integer type;

    /**
     * 菜单地址/前端组件路径(type=2时无值)
     */
    private String component;

    /**
     * 路由地址(type=2时无值)
     */
    private String path;


    /**
     * 权限标识(type=2时有值)
     */
    @TableField("permission_code")
    private String permissionCode;

    /**
     * 图标(type=2时无值)
     */
    private String icon;

    /**
     * 排序
     */
    private Integer sort;

    /**
     * 权重: 1业务权重(菜单对超级管理员不可见), 2系统权重(菜单可分配给任何角色)
     */
    private Integer weight;

    /**
     * 是否可见(0不可见,1可见)
     */
    private Boolean visible;

    /**
     * 备注
     */
    private String remark;


}
