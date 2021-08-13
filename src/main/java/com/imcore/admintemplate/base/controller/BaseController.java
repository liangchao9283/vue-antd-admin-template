package com.imcore.admintemplate.base.controller;

import com.imcore.admintemplate.base.model.BaseModel;
import com.imcore.admintemplate.base.model.BaseResponse;
import com.imcore.admintemplate.base.enums.ResultCodeEnum;
import com.imcore.admintemplate.base.service.BaseService;
import com.imcore.admintemplate.exception.model.ImcoreBusinessException;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;


public class BaseController<T extends BaseModel> {

    @Autowired
    private BaseService<T> baseService;


    /**
     * 保存一个实体，null的属性也会保存，会使用数据库默认值
     *
     */
    @RequestMapping(value = "insert", method = RequestMethod.POST)
    public BaseResponse insert(T t) {
        baseService.insert(t);
        return BaseResponse.success();
    }

    /**
     * 根据实体属性作为条件进行删除，查询条件使用等号
     *
     * @param record 对象
     * @return
     */
    @RequestMapping(value = "delete", method = RequestMethod.POST)
    public BaseResponse delete(T record) {
        return BaseResponse.success(baseService.delete(record));
    }

    /**
     * 根据主键字段进行删除，方法参数必须包含完整的主键属性
     *
     * @param id
     * @return
     */
    @RequestMapping(value = "deleteById", method = RequestMethod.POST)
    public BaseResponse deleteById(@RequestParam("id")String id, HttpServletRequest request) throws ImcoreBusinessException {
        if(StringUtils.isBlank(id)){
            return BaseResponse.error(ResultCodeEnum.PARAMS_MISS);
        }
        return BaseResponse.success(baseService.deleteById(id));
    }


    @RequestMapping(value = "/deleteByIds", method = RequestMethod.POST)
    public BaseResponse deleteByIds(@RequestParam("ids") String ids) {
        return BaseResponse.success(baseService.deleteByIds(ids));
    }

    /**
     * 查询表中所有数据
     *
     * @mbggenerated
     */
    @RequestMapping(value = "selectAll", method = RequestMethod.GET)
    public BaseResponse selectAll() {
        return BaseResponse.success(baseService.selectAll());
    }

    /**
     * 根据主键查询一条数据
     *
     * @mbggenerated
     */
    @RequestMapping(value = "selectById", method = RequestMethod.GET)
    public BaseResponse selectById(@RequestParam("id") String id) {
        return BaseResponse.success(baseService.selectById( id));
    }

    /**
     * 据实体中的属性值进行查询，查询条件使用等号
     *
     * @mbggenerated
     */
    @RequestMapping(value = "selectList", method = RequestMethod.GET)
    public BaseResponse selectList(T t) {
        return BaseResponse.success(baseService.selectList(t));
    }

    /**
     * <p>
     * 根据 entity 条件，查询一条记录
     * </p>
     */
    @RequestMapping(value = "selectOne", method = RequestMethod.GET)
    public BaseResponse selectOne(T t) {
        return BaseResponse.success(baseService.selectOne(t));
    }

}
