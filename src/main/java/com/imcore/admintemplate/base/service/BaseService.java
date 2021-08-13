package com.imcore.admintemplate.base.service;

import com.baomidou.mybatisplus.extension.service.IService;
import org.springframework.stereotype.Service;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

/**
 * @Author: admin
 * @Date: 2019/3/21 23:55
 * @Description:
 */
@Service
public interface BaseService<T> extends IService<T> {

	/**
	 * 保存一个实体，null的属性也会保存，不会使用数据库默认值
	 * @param record
	 * @return
	 */
	int insert(T record);


	/**
	 * 根据主键字段进行删除，方法参数必须包含完整的主键属性
	 * @param key
	 * @return
	 */
	int deleteById(Serializable key);

	/**
	 * 根据主键字段批量删除
	 * @param ids 要删除的主键列表，以逗号分隔
	 * @return
	 */
	int deleteByIds(String ids);

	/**
	 * 根据实体属性作为条件进行删除，查询条件使用等号
	 * @param record
	 * @return
	 */
	int delete(T record);

	/**
	 * 根据主键更新实体全部字段，null值也会被更新
	 * @param   columnPrimaryKey     数据库主键 如数据库库表中 user表的主键为id
	 * @param   primaryKeyValue      数据库主键的值:如数据库库表中 user表的主键为id ，其中值为3  表示 针对 id=3的数据 所有字段进行更新
	 * @param   record               实体对象
	 * @return
	 */
	int updateAllByPrimaryKey(String columnPrimaryKey, Object primaryKeyValue, T record);

	/**
	 * 根据主键更新属性不为null的值,局部更新
	 * @param record
	 * @return
	 */
	int updateNotNullByPrimaryKey(T record);

	/**
	 * 查询表中所有数据
	 * @return
	 */
	List<T> selectAll();

	/**
	 * 根据主键查询一条数据
	 * @param key
	 * @return
	 */
	T selectById(Serializable key);

	/**
	 * 根据实体中的属性值进行查询，查询条件使用等号
	 *
	 * @param record
	 * @return
	 */
	List<T> selectList(T record);


	Map<String,Object> entityToTableCloumn(Object obj) ;

	T selectOne(T t);
}
