package org.siqisource.stone.autoservice;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.session.RowBounds;
import org.siqisource.stone.mybatis.condition.Condition;
import org.siqisource.stone.mybatis.condition.PartitiveFields;

/**
 * base mapper to be inherited
 *
 * @author yulei
 *
 */
public interface GeneralMapper<M> extends AutoMapper<M> {

	/**
	 * insert object to database
	 *
	 * @param model
	 *            model
	 */
	public void insert(M model);

	/**
	 * insert some fields of the table
	 *
	 * @param fields
	 *            fields
	 */
	public void insertPartitive(@Param("fields") PartitiveFields fields);

	/**
	 * count the querying record
	 *
	 * @param condition
	 *            condition
	 * @return count
	 */
	int count(@Param("condition") Condition condition);

	/**
	 * list the pagination querying result
	 *
	 * @param condition
	 *            condition
	 * @param rowBounds
	 *            rowBounds
	 * @return list
	 */
	List<M> list(@Param("condition") Condition condition, @Param("rowBounds") RowBounds rowBounds);

	/**
	 * list the querying result
	 *
	 * @param condition
	 *            condition
	 * @return list
	 */
	List<M> list(@Param("condition") Condition condition);

	/**
	 * update batch records
	 *
	 * @param fields
	 *            fields
	 * @param condition
	 *            condition
	 */
	public void updateBatch(@Param("fields") PartitiveFields fields, @Param("condition") Condition condition);

	/**
	 * delete batch record from database
	 *
	 * @param condition condition
	 */
	public void deleteBatch(@Param("condition") Condition condition);
}
