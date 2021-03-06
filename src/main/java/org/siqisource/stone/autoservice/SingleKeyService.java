package org.siqisource.stone.autoservice;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.siqisource.stone.mybatis.condition.PartitiveFields;

public interface SingleKeyService<M, K> extends AutoService<M> {

	/**
	 * read object from database
	 *
	 * @param id
	 *            primary key(s) of the table
	 * @return object
	 */
	public M read(@Param("id") K id);

	/**
	 * update database
	 *
	 * @param model model
	 */
	public void update(M model);

	/**
	 * update database
	 *
	 * @param models models
	 */
	public void updateBatch(List<M> models);

	/**
	 * update some fields of the table
	 *
	 * @param fields fields
	 * @param id id
	 */
	public void updatePartitive(PartitiveFields fields, @Param("id") K id);

	/**
	 * delete from database
	 *
	 * @param id
	 *            primary key(s) of the table
	 */
	public void delete(K id);

	/**
	 * delete batch record from database
	 *
	 * @param idList idList
	 */
	public void deleteBatch(K[] idList);

}
