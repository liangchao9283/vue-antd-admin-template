package com.imcore.admintemplate.model.dto;

import lombok.Data;
import org.hibernate.validator.constraints.NotBlank;

@Data
public class SysUserPasswordDto {
    @NotBlank
    private String oldPassword;
    @NotBlank
    private String newPassword;
}
