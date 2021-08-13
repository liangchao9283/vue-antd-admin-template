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
 * @since 2021-07-22
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@TableName("sys_user_role")
public class SysUserRole extends BaseModel {

private static final long serialVersionUID=1L;

    @TableField("user_id")
    private String userId;

    @TableField("role_id")
    private String roleId;


}
