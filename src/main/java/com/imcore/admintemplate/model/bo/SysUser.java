package com.imcore.admintemplate.model.bo;

import com.baomidou.mybatisplus.annotation.TableName;
import com.imcore.admintemplate.base.model.BaseModel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import org.hibernate.validator.constraints.NotBlank;

import javax.validation.constraints.NotNull;


/**
 * <p>
 *
 * </p>
 *
 * @author liang chao
 * @since 2021-07-08
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@TableName("sys_user")
public class SysUser extends BaseModel {

private static final long serialVersionUID=1L;

    @NotBlank
    private String username;

    private String password;

    @NotBlank
    private String phone;

    @NotBlank
    private String name;

    private String salt;

    private String avatar;

    private String address;

    private String position;

    @NotNull
    private Integer sex;

    private String email;

    private Integer status;

}
