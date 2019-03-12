package org.siqisource.stone.autoservice;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.session.RowBounds;
import org.siqisource.stone.communication.Page;
import org.siqisource.stone.mybatis.condition.Condition;
import org.siqisource.stone.mybatis.condition.PartitiveFields;

public interface GeneralService<M> extends AutoService<M> {

	/**
	 * insert object to database
	 *
	 * @param model
	 *            model
	 */
	public void insert(M model);

	/**
	 * insert object to database batch
	 *
	 * @param models
	 *            models
	 */
	public void insertBatch(List<M> models);

	/**
	 * insert some fields of the table
	 *
	 * @param fields
	 *            fields
	 */
	public void insertPartitive(@Param("fields") PartitiveFields fields);

	/**
	 * read object from database
	 *
	 * @param condition
	 *            condition
	 *
	 * @return first record of result set
	 */
	public M readOne(Condition condition);

	/**
	 * count the querying record
	 *
	 * @param condition
	 *            condition
	 * @return count
	 */
	int count(Condition condition);

	/**
	 * list the pagination querying result
	 *
	 * @param condition
	 *            condition
	 * @param rowBounds
	 *            rowBounds
	 * @return list
	 */
	List<M> list(Condition condition, RowBounds rowBounds);

	/**
	 * list the pagination querying result
	 *
	 * @param condition
	 * @param rowBounds
	 * @return
	 */
	Page<M> page(Condition condition, RowBounds rowBounds);

	/**
	 * list the querying result
	 *
	 * @param condition
	 *            condition
	 * @return list
	 */
	List<M> list(Condition condition);

	/**
	 * update batch records
	 *
	 * @param fields
	 *            fields
	 * @param condition
	 *            condition
	 */
	public void updatePartitive(PartitiveFields fields, @Param("condition") Condition condition);

}
