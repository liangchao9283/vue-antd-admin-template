package com.imcore.admintemplate.base.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.imcore.admintemplate.base.model.BaseModel;
import com.imcore.admintemplate.base.service.BaseService;
import com.imcore.admintemplate.utils.UuidUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.*;

/**
 * @Author: liangchao
 * @Date: 2019/3/21 23:55
 * @Description:
 */
@Slf4j
public class BaseServiceImpl<T extends BaseModel> extends ServiceImpl<BaseMapper<T>,T > implements BaseService<T> {

    @Autowired
    private BaseMapper<T> myMapper;
    /**
     * 根据主键字段进行删除，方法参数必须包含完整的主键属性
     * @param key
     * @return
     */
    @Override
    @Transactional
    public int deleteById(Serializable key) {
        showLog(true,"deleteById",key);
        return myMapper.deleteById(key);
    }

    /**
     * 根据主键字段批量删除
     * @param ids 要删除的主键列表，以逗号分隔
     * @return
     */
    @Override
    @Transactional
    public int deleteByIds(String ids) {
        String[] newIds = ids.split(",");
        List<String> idList = Arrays.asList(newIds);
        return myMapper.deleteBatchIds(idList);
    }

    /**
     *根据实体属性作为条件进行删除，查询条件使用等号
     * @param record
     * @return
     */
    @Override
    @Transactional
    public int delete(T record) {
        QueryWrapper<T> wrapperEntity = new QueryWrapper<T>();
        wrapperEntity.setEntity(record);
        showLog(true,"delete",record);
        return myMapper.delete(wrapperEntity);
    }

    /**
     * 保存一个实体，null的属性也会保存，不会使用数据库默认值
     *
     * @param record
     * @return
     */
    @Override
    @Transactional
    public int insert(T record) {
        if(org.apache.commons.lang.StringUtils.isBlank(record.getId())){
            record.setId(UuidUtil.getUUID());
        }
        record.setCreatedOn(new Date());
        record.setUpdatedOn(new Date());

        showLog(true,"insert",record);
        return myMapper.insert(record);
    }



    @Override
    public List<T> selectList(T record) {
        QueryWrapper<T> queryWrapper=new QueryWrapper<T>();
        queryWrapper.setEntity(record);
        showLog(true,"select",record);
        return myMapper.selectList(queryWrapper);
    }

    /**
     *
     * @param key
     * @return
     */

    @Override
    public T selectById(Serializable key) {
        showLog(true, "selectByPrimaryKey:", key);
        return myMapper.selectById(key);
    }

    @Override
    public List<T> selectAll() {
        showLog(true, "selectAll", null);
        QueryWrapper<T> queryWrapper = new QueryWrapper<T>();
        return myMapper.selectList(queryWrapper);
    }

    @Override
    public int updateAllByPrimaryKey(String columnPrimaryKey, Object primaryKeyValue, T record) {
        showLog(true, "updateAllByPrimaryKey columnPrimaryKey:"+columnPrimaryKey+",primaryKeyValue:"+primaryKeyValue, record);
        if (null == record|| org.apache.commons.lang.StringUtils.isBlank(columnPrimaryKey)||null==primaryKeyValue){
            return 0;
        }
        record.setUpdatedOn(new Date());
        UpdateWrapper<T> uw = new UpdateWrapper<T>();
        Map<String,Object> columnAndTableMap=entityToTableCloumn(record);
        String json = JSON.toJSONString(record, SerializerFeature.WriteMapNullValue);
        log.info("---columnAndTableMap :-----"+json);
        if(columnAndTableMap!=null){
            columnAndTableMap.remove("tableName");
            columnAndTableMap.forEach((key,value)->{
                uw.set(key, value);
            });
        }
        uw.eq(columnPrimaryKey,primaryKeyValue);
        return myMapper.update(record, uw);

    }
    /**
     * 根据主键更新实体全部字段，null值也会被更新
     * @param record
     * @return
     */
    @Transactional
    public int updateAllByPrimaryKey(T record) {
        showLog(true,"updateByPrimaryKey",record);
        if(null==record){
            return 0;
        }
        record.setUpdatedOn(new Date());
        String json= JSON.toJSONString(record, SerializerFeature.WriteMapNullValue);
        JSONObject jsonObject = JSONObject.parseObject(json);
        UpdateWrapper<T> uw = new UpdateWrapper<T>();
        if(null!=jsonObject) {
            jsonObject.forEach((key, value) -> {
                uw.set(key,value);
            });
        }

        return myMapper.update(record,uw);
    }

    /***
     * mybatis plus 注解转换成table
     * @param obj    实体
     * @return
     */
    @Override
    public Map<String,Object> entityToTableCloumn(Object obj) {
        Map<String ,Object> map=new HashMap<String,Object>();
        StringBuilder sb = new StringBuilder();
        Class c = obj.getClass();
        //如果类不包含@Table注解 结束
        if (!c.isAnnotationPresent(TableName.class)) {
            return null;
        }
        //获得表名
        TableName table = (TableName) c.getAnnotation(TableName.class);
        map.put("tableName",table.value());
        Field fields[] = c.getDeclaredFields();
        for (Field field : fields) {
            //如果该字段不包含@Column注解  遍历下一个
            if (!field.isAnnotationPresent(TableField.class)||!field.isAnnotationPresent(TableId.class)) {
                continue;
            }
            String columnName =null;
            String fieldName =null;
            if(field.isAnnotationPresent(TableId.class)){
                TableId id = field.getAnnotation(TableId.class);
                columnName = id.value();
                fieldName = field.getName();
            }else if(field.isAnnotationPresent(TableField.class)){
                TableField column = field.getAnnotation(TableField.class);
                columnName = column.value();
                fieldName = field.getName();
            }else{
                continue;
            }
            if(org.apache.commons.lang.StringUtils.isNotBlank(columnName)){
                //获取该字段的get方法
                String methodName = "get" + fieldName.substring(0, 1).toUpperCase() + fieldName.substring(1);
                try {
                    Method method = c.getDeclaredMethod(methodName);
                    Object o = method.invoke(obj);
                    map.put(columnName,o);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        return map;
    }

    /**
     * 根据 entity 条件，查询一条记录
     */
    @Override
    public T selectOne(T record) {
        QueryWrapper<T> queryWrapper=new QueryWrapper<T>();
        queryWrapper.setEntity(record);
        showLog(true,"select",record);
        return  myMapper.selectOne(queryWrapper);
    }

    /**
     * 根据主键更新属性不为null的值,局部更新
     * @param record
     * @return
     */
    @Override
    @Transactional
    public int updateNotNullByPrimaryKey(T record) {
        showLog(true,"updateByPrimaryKeySelective",record);
        record.setUpdatedOn(new Date());
        return myMapper.updateById(record);
    }

    /***
     * 是否打印輸出日志
     * @param flag
     * @param methodName
     * @param entity
     */
    private void showLog(boolean flag,String methodName,Object entity){
        if(flag){
            String json="";
            if(null!=entity){
                json=JSON.toJSONString(entity, SerializerFeature.WriteMapNullValue);
            }
            StringBuffer buf=new StringBuffer("-----").append(methodName).append("----- 入參:");
            log.info(buf.toString(),json);
        }

    }

}
