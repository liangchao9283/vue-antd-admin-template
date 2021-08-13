package com.imcore.admintemplate.base.model;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;

import java.util.Date;

public class BaseModel {
    /**
     * 主键
     */
    @TableId(value="id",type= IdType.UUID)
    private String id;

    /**
     * 创建时间
     */
    @TableField(value="created_on",fill = FieldFill.INSERT)
    private Date createdOn;


    /**
     * 修改时间
     */
    @TableField(value="updated_on",fill = FieldFill.INSERT_UPDATE)
    private Date updatedOn;


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Date getCreatedOn() {
        return createdOn;
    }

    public void setCreatedOn(Date createdOn) {
        this.createdOn = createdOn;
    }

    public Date getUpdatedOn() {
        return updatedOn;
    }

    public void setUpdatedOn(Date updatedOn) {
        this.updatedOn = updatedOn;
    }
}
