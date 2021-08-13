package com.imcore.admintemplate.model.bo;

import com.baomidou.mybatisplus.annotation.TableName;
import com.imcore.admintemplate.base.model.BaseModel;
import com.baomidou.mybatisplus.annotation.TableField;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import org.hibernate.validator.constraints.NotBlank;


/**
 * <p>
 * 角色表
 * </p>
 *
 * @author liang chao
 * @since 2021-07-19
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@TableName("sys_role")
public class SysRole extends BaseModel {

private static final long serialVersionUID=1L;

    @TableField("role_name")
    @NotBlank
    private String roleName;

    @NotBlank
    private String code;

    private String remark;
}
