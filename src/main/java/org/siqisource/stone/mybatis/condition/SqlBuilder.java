package org.siqisource.stone.mybatis.condition;

import java.util.Arrays;
import java.util.List;

import org.siqisource.stone.mybatis.condition.expression.BetweenExpression;
import org.siqisource.stone.mybatis.condition.expression.CompareExpression;
import org.siqisource.stone.mybatis.condition.expression.EnumExpression;
import org.siqisource.stone.mybatis.condition.expression.NoValueExpression;
import org.siqisource.stone.mybatis.condition.expression.SingleExpression;
import org.siqisource.stone.mybatis.utils.NameConvertStyle;
import org.siqisource.stone.mybatis.utils.NameConverter;



public class SqlBuilder {

	private static String p2c(String propertyName) {
		return NameConverter.convertWithNamespcae(propertyName, NameConvertStyle.camelToUnderlineLower);
	}

	public static CompareExpression singleValue(String prefix, String propertyName, String compareSymbol, Object value,
			String suffix) {
		String column = p2c(propertyName);
		return new SingleExpression(prefix, column, compareSymbol, value, suffix);
	}

	public static CompareExpression noValue(String prefix, String propertyName, String compareSymbol, String suffix) {
		String column = p2c(propertyName);
		return new NoValueExpression(prefix, column, compareSymbol, suffix);
	}

	public static CompareExpression betweenValue(String prefix, String propertyName, Object begin, Object end,
			String suffix) {
		String column = p2c(propertyName);
		return new BetweenExpression(prefix, column, begin, end, suffix);
	}

	public static CompareExpression listValue(String prefix, String propertyName, String compareSymbol, List<?> value,
			String suffix) {
		String column = p2c(propertyName);
		return new EnumExpression(prefix, column, compareSymbol, value, suffix);
	}

	public static CompareExpression listValue(String prefix, String propertyName, String compareSymbol, String values,
			String suffix) {
		String column = p2c(propertyName);
		return new EnumExpression(prefix, column, compareSymbol, Arrays.asList((Object[]) values.split(",")), suffix);
	}

}
