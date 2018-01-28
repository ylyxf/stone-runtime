package org.siqisource.stone.mybatis.condition;

import java.util.Arrays;
import java.util.List;

import org.siqisource.stone.mybatis.condition.expression.BetweenExpression;
import org.siqisource.stone.mybatis.condition.expression.CompareExpression;
import org.siqisource.stone.mybatis.condition.expression.EnumExpression;
import org.siqisource.stone.mybatis.condition.expression.NoValueExpression;
import org.siqisource.stone.mybatis.condition.expression.ParenthesisExpression;
import org.siqisource.stone.mybatis.condition.expression.SingleExpression;
import org.siqisource.stone.mybatis.utils.NameConvertStyle;
import org.siqisource.stone.mybatis.utils.NameConverter;
 


public class SimpleCondition extends Condition {

	private String p2c(String propertyName) {
		return NameConverter.convertWithNamespcae(propertyName, NameConvertStyle.camelToUnderlineLower);
	}

	public void andIsNotNull(String propertyName) {
		String column = p2c(propertyName);
		CompareExpression expression = new NoValueExpression(SqlKey.AND, column, SqlKey.IS_NOT_NULL, "");
		this.addExpression(expression);
	}

	public void andIsNull(String propertyName) {
		String column = p2c(propertyName);
		CompareExpression expression = new NoValueExpression(SqlKey.AND, column, SqlKey.IS_NULL, "");
		this.addExpression(expression);
	}

	public void andEqual(String propertyName, Object value) {
		String column = p2c(propertyName);
		CompareExpression expression = new SingleExpression(SqlKey.AND, column, SqlKey.EQUAL, value, "");
		this.addExpression(expression);
	}

	public void andLike(String propertyName, String value) {
		String column = p2c(propertyName);
		CompareExpression expression = new SingleExpression(SqlKey.AND, column, SqlKey.LIKE, "%" + value + "%", "");
		this.addExpression(expression);
	}

	public void andNotLike(String propertyName, String value) {
		String column = p2c(propertyName);
		CompareExpression expression = new SingleExpression(SqlKey.AND, column, SqlKey.NOT_LIKE, "%" + value + "%", "");
		this.addExpression(expression);
	}

	public void andBeginWith(String propertyName, String value) {
		String column = p2c(propertyName);
		CompareExpression expression = new SingleExpression(SqlKey.AND, column, SqlKey.LIKE, value + "%", "");
		this.addExpression(expression);
	}

	public void andEndWith(String propertyName, String value) {
		String column = p2c(propertyName);
		CompareExpression expression = new SingleExpression(SqlKey.AND, column, SqlKey.LIKE, "%" + value, "");
		this.addExpression(expression);
	}

	public void andNotEqual(String propertyName, Object value) {
		String column = p2c(propertyName);
		CompareExpression expression = new SingleExpression(SqlKey.AND, column, SqlKey.NOT_EQUAL, value, "");
		this.addExpression(expression);
	}

	public void andGreater(String propertyName, Object value) {
		String column = p2c(propertyName);
		CompareExpression expression = new SingleExpression(SqlKey.AND, column, SqlKey.GREATER, value, "");
		this.addExpression(expression);
	}

	public void andNoLess(String propertyName, Object value) {
		String column = p2c(propertyName);
		CompareExpression expression = new SingleExpression(SqlKey.AND, column, SqlKey.GREATER_OR_EQUAL, value, "");
		this.addExpression(expression);
	}

	public void andLess(String propertyName, Object value) {
		String column = p2c(propertyName);
		CompareExpression expression = new SingleExpression(SqlKey.AND, column, SqlKey.LESS, value, "");
		this.addExpression(expression);
	}

	public void andNoGreater(String propertyName, Object value) {
		String column = p2c(propertyName);
		CompareExpression expression = new SingleExpression(SqlKey.AND, column, SqlKey.LESS_OR_EQUAL, value, "");
		this.addExpression(expression);
	}

	public void andBetween(String propertyName, Object beginValue, Object endValue) {
		String column = p2c(propertyName);
		CompareExpression expression = new BetweenExpression(SqlKey.AND, column, beginValue, endValue, "");
		this.addExpression(expression);
	}

	public void andIn(String propertyName, List<Object> value) {
		String column = p2c(propertyName);
		CompareExpression expression = new EnumExpression(SqlKey.AND, column, SqlKey.IN, value, "");
		this.addExpression(expression);
	}

	public void andIn(String propertyName, Object[] values) {
		String column = p2c(propertyName);
		CompareExpression expression = new EnumExpression(SqlKey.AND, column, SqlKey.IN, Arrays.asList(values), "");
		this.addExpression(expression);
	}

	public void andIn(String propertyName, String values, String separator) {
		String column = p2c(propertyName);
		CompareExpression expression = new EnumExpression(SqlKey.AND, column, SqlKey.IN,
				Arrays.asList((Object[]) values.split(separator)), "");
		this.addExpression(expression);
	}

	public void andNotIn(String propertyName, List<Object> value) {
		String column = p2c(propertyName);
		CompareExpression expression = new EnumExpression(SqlKey.AND, column, SqlKey.NOT_IN, value, "");
		this.addExpression(expression);
	}

	public void andNotIn(String propertyName, Object[] values) {
		String column = p2c(propertyName);
		CompareExpression expression = new EnumExpression(SqlKey.AND, column, SqlKey.NOT_IN, Arrays.asList(values), "");
		this.addExpression(expression);
	}

	public void andNotIn(String propertyName, String values, String separator) {
		String column = p2c(propertyName);
		CompareExpression expression = new EnumExpression(SqlKey.AND, column, SqlKey.NOT_IN,
				Arrays.asList((Object[]) values.split(separator)), "");
		this.addExpression(expression);
	}

	public void orIsNotNull(String propertyName) {
		String column = p2c(propertyName);
		CompareExpression expression = new NoValueExpression(SqlKey.OR, column, SqlKey.IS_NOT_NULL, "");
		this.addExpression(expression);
	}

	public void orIsNull(String propertyName) {
		String column = p2c(propertyName);
		CompareExpression expression = new NoValueExpression(SqlKey.OR, column, SqlKey.IS_NULL, "");
		this.addExpression(expression);
	}

	public void orEqual(String propertyName, Object value) {
		String column = p2c(propertyName);
		CompareExpression expression = new SingleExpression(SqlKey.OR, column, SqlKey.EQUAL, value, "");
		this.addExpression(expression);
	}

	public void orLike(String propertyName, String value) {
		String column = p2c(propertyName);
		CompareExpression expression = new SingleExpression(SqlKey.OR, column, SqlKey.LIKE, "%" + value + "%", "");
		this.addExpression(expression);
	}

	public void orNotLike(String propertyName, String value) {
		String column = p2c(propertyName);
		CompareExpression expression = new SingleExpression(SqlKey.OR, column, SqlKey.NOT_LIKE, "%" + value + "%", "");
		this.addExpression(expression);
	}

	public void orBeginWith(String propertyName, String value) {
		String column = p2c(propertyName);
		CompareExpression expression = new SingleExpression(SqlKey.OR, column, SqlKey.LIKE, value + "%", "");
		this.addExpression(expression);
	}

	public void orEndWith(String propertyName, String value) {
		String column = p2c(propertyName);
		CompareExpression expression = new SingleExpression(SqlKey.OR, column, SqlKey.LIKE, "%" + value, "");
		this.addExpression(expression);
	}

	public void orNotEqual(String propertyName, Object value) {
		String column = p2c(propertyName);
		CompareExpression expression = new SingleExpression(SqlKey.OR, column, SqlKey.NOT_EQUAL, value, "");
		this.addExpression(expression);
	}

	public void orGreater(String propertyName, Object value) {
		String column = p2c(propertyName);
		CompareExpression expression = new SingleExpression(SqlKey.OR, column, SqlKey.GREATER, value, "");
		this.addExpression(expression);
	}

	public void orNoLess(String propertyName, Object value) {
		String column = p2c(propertyName);
		CompareExpression expression = new SingleExpression(SqlKey.OR, column, SqlKey.GREATER_OR_EQUAL, value, "");
		this.addExpression(expression);
	}

	public void orLess(String propertyName, Object value) {
		String column = p2c(propertyName);
		CompareExpression expression = new SingleExpression(SqlKey.OR, column, SqlKey.LESS, value, "");
		this.addExpression(expression);
	}

	public void orNoGreater(String propertyName, Object value) {
		String column = p2c(propertyName);
		CompareExpression expression = new SingleExpression(SqlKey.OR, column, SqlKey.LESS_OR_EQUAL, value, "");
		this.addExpression(expression);
	}

	public void orBetween(String propertyName, Object beginValue, Object endValue) {
		String column = p2c(propertyName);
		CompareExpression expression = new BetweenExpression(SqlKey.OR, column, beginValue, endValue, "");
		this.addExpression(expression);
	}

	public void orIn(String propertyName, List<Object> value) {
		String column = p2c(propertyName);
		CompareExpression expression = new EnumExpression(SqlKey.OR, column, SqlKey.IN, value, "");
		this.addExpression(expression);
	}

	public void orIn(String propertyName, Object[] values) {
		String column = p2c(propertyName);
		CompareExpression expression = new EnumExpression(SqlKey.OR, column, SqlKey.IN, Arrays.asList(values), "");
		this.addExpression(expression);
	}

	public void orIn(String propertyName, String values, String separator) {
		String column = p2c(propertyName);
		CompareExpression expression = new EnumExpression(SqlKey.OR, column, SqlKey.IN,
				Arrays.asList((Object[]) values.split(separator)), "");
		this.addExpression(expression);
	}

	public void orNotIn(String propertyName, List<Object> value) {
		String column = p2c(propertyName);
		CompareExpression expression = new EnumExpression(SqlKey.OR, column, SqlKey.NOT_IN, value, "");
		this.addExpression(expression);
	}

	public void orNotIn(String propertyName, Object[] values) {
		String column = p2c(propertyName);
		CompareExpression expression = new EnumExpression(SqlKey.OR, column, SqlKey.NOT_IN, Arrays.asList(values), "");
		this.addExpression(expression);
	}

	public void orNotIn(String propertyName, String values, String separator) {
		String column = p2c(propertyName);
		CompareExpression expression = new EnumExpression(SqlKey.OR, column, SqlKey.NOT_IN,
				Arrays.asList((Object[]) values.split(separator)), "");
		this.addExpression(expression);
	}

	public void isNotNull(String propertyName) {
		String column = p2c(propertyName);
		CompareExpression expression = new NoValueExpression("", column, SqlKey.IS_NOT_NULL, "");
		this.addExpression(expression);
	}

	public void isNull(String propertyName) {
		String column = p2c(propertyName);
		CompareExpression expression = new NoValueExpression("", column, SqlKey.IS_NULL, "");
		this.addExpression(expression);
	}

	public void equal(String propertyName, Object value) {
		String column = p2c(propertyName);
		CompareExpression expression = new SingleExpression("", column, SqlKey.EQUAL, value, "");
		this.addExpression(expression);
	}

	public void like(String propertyName, String value) {
		String column = p2c(propertyName);
		CompareExpression expression = new SingleExpression("", column, SqlKey.LIKE, "%" + value + "%", "");
		this.addExpression(expression);
	}

	public void notLike(String propertyName, String value) {
		String column = p2c(propertyName);
		CompareExpression expression = new SingleExpression("", column, SqlKey.NOT_LIKE, "%" + value + "%", "");
		this.addExpression(expression);
	}

	public void beginWith(String propertyName, String value) {
		String column = p2c(propertyName);
		CompareExpression expression = new SingleExpression("", column, SqlKey.LIKE, value + "%", "");
		this.addExpression(expression);
	}

	public void endWith(String propertyName, String value) {
		String column = p2c(propertyName);
		CompareExpression expression = new SingleExpression("", column, SqlKey.LIKE, "%" + value, "");
		this.addExpression(expression);
	}

	public void notEqual(String propertyName, Object value) {
		String column = p2c(propertyName);
		CompareExpression expression = new SingleExpression("", column, SqlKey.NOT_EQUAL, value, "");
		this.addExpression(expression);
	}

	public void greater(String propertyName, Object value) {
		String column = p2c(propertyName);
		CompareExpression expression = new SingleExpression("", column, SqlKey.GREATER, value, "");
		this.addExpression(expression);
	}

	public void noLess(String propertyName, Object value) {
		String column = p2c(propertyName);
		CompareExpression expression = new SingleExpression("", column, SqlKey.GREATER_OR_EQUAL, value, "");
		this.addExpression(expression);
	}

	public void less(String propertyName, Object value) {
		String column = p2c(propertyName);
		CompareExpression expression = new SingleExpression("", column, SqlKey.LESS, value, "");
		this.addExpression(expression);
	}

	public void noGreater(String propertyName, Object value) {
		String column = p2c(propertyName);
		CompareExpression expression = new SingleExpression("", column, SqlKey.LESS_OR_EQUAL, value, "");
		this.addExpression(expression);
	}

	public void between(String propertyName, Object beginValue, Object endValue) {
		String column = p2c(propertyName);
		CompareExpression expression = new BetweenExpression("", column, beginValue, endValue, "");
		this.addExpression(expression);
	}

	public void in(String propertyName, List<Object> value) {
		String column = p2c(propertyName);
		CompareExpression expression = new EnumExpression("", column, SqlKey.IN, value, "");
		this.addExpression(expression);
	}

	public void in(String propertyName, Object[] values) {
		String column = p2c(propertyName);
		CompareExpression expression = new EnumExpression("", column, SqlKey.IN, Arrays.asList(values), "");
		this.addExpression(expression);
	}

	public void in(String propertyName, String values, String separator) {
		String column = p2c(propertyName);
		CompareExpression expression = new EnumExpression("", column, SqlKey.IN,
				Arrays.asList((Object[]) values.split(separator)), "");
		this.addExpression(expression);
	}

	public void notIn(String propertyName, List<Object> value) {
		String column = p2c(propertyName);
		CompareExpression expression = new EnumExpression("", column, SqlKey.NOT_IN, value, "");
		this.addExpression(expression);
	}

	public void notIn(String propertyName, Object[] values) {
		String column = p2c(propertyName);
		CompareExpression expression = new EnumExpression("", column, SqlKey.NOT_IN, Arrays.asList(values), "");
		this.addExpression(expression);
	}

	public void notIn(String propertyName, String values, String separator) {
		String column = p2c(propertyName);
		CompareExpression expression = new EnumExpression("", column, SqlKey.NOT_IN,
				Arrays.asList((Object[]) values.split(separator)), "");
		this.addExpression(expression);
	}

	public void andC() {
		CompareExpression expression = new ParenthesisExpression(SqlKey.AND_C);
		this.addExpression(expression);
	}

	public void J() {
		CompareExpression expression = new ParenthesisExpression(SqlKey.J);
		this.addExpression(expression);
	}
}
