package org.siqisource.stone.autoservice;

import org.apache.ibatis.annotations.Param;
import org.siqisource.stone.mybatis.condition.PartitiveFields;


/**
 * base mapper to be inherited
 * 
 * @author yulei
 * 
 * @param <M,K>
 */
public interface SingleKeyMapper<M,K> extends GeneralMapper<M> {

	/**
	 * read object from database
	 * 
	 * @param id
	 *            primary key of the table
	 * @return object
	 */
	public M read(@Param("id") K id);

	/**
	 * update database
	 * 
	 * @param model
	 */
	public void update(M model);

	/**
	 * update some fields of the table
	 * 
	 * @param fields
	 * @param id
	 */
	public void updatePartitive(@Param("fields") PartitiveFields fields, @Param("id") K id);

	/**
	 * delete from database
	 * 
	 * @param id
	 *            primary key of the table
	 */
	public void delete(@Param("id") K id);

}
