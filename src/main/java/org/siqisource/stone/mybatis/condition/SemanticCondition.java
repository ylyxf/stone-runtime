package org.siqisource.stone.mybatis.condition;

import java.util.Arrays;
import java.util.List;

import org.siqisource.stone.mybatis.condition.expression.BetweenExpression;
import org.siqisource.stone.mybatis.condition.expression.CompareExpression;
import org.siqisource.stone.mybatis.condition.expression.EnumExpression;
import org.siqisource.stone.mybatis.condition.expression.LiteralExpression;
import org.siqisource.stone.mybatis.condition.expression.NoValueExpression;
import org.siqisource.stone.mybatis.condition.expression.ParenthesisExpression;
import org.siqisource.stone.mybatis.condition.expression.SingleExpression;
import org.siqisource.stone.mybatis.utils.NameConvertStyle;
import org.siqisource.stone.mybatis.utils.NameConverter;


public class SemanticCondition extends Condition {

	private HalfExpress _halfExpress = null;

	private String p2c(String propertyName) {
		return NameConverter.convertWithNamespcae(propertyName, NameConvertStyle.camelToUnderlineLower);
	}

	public SemanticCondition prop(String property) {
		if (_halfExpress != null) {
			throw new RuntimeException("unclosed expression");
		}
		String columnCode = p2c(property);
		_halfExpress = new HalfExpress();
		_halfExpress.setColumnCode(columnCode);
		return this;
	}

	public SemanticCondition column(String columnCode) {
		if (_halfExpress != null) {
			throw new RuntimeException("unclosed expression");
		}
		_halfExpress = new HalfExpress();
		_halfExpress.setColumnCode(columnCode);
		return this;
	}

	public SemanticCondition func(String func) {
		if (_halfExpress != null) {
			throw new RuntimeException("unclosed expression");
		}
		_halfExpress = new HalfExpress();
		_halfExpress.setColumnCode(func);
		return this;
	}

	public SemanticCondition _C() {
		CompareExpression expression = new ParenthesisExpression(SqlKey.C);
		this.addExpression(expression);
		return this;
	}

	public SemanticCondition _J() {
		CompareExpression expression = new ParenthesisExpression(SqlKey.J);
		this.addExpression(expression);
		return this;
	}

	public SemanticCondition and() {
		CompareExpression expression = new LiteralExpression(SqlKey.AND);
		this.addExpression(expression);
		return this;
	}

	public SemanticCondition or() {
		CompareExpression expression = new LiteralExpression(SqlKey.OR);
		this.addExpression(expression);
		return this;
	}

	public SemanticCondition eq(Object value) {
		if (_halfExpress == null) {
			throw new RuntimeException("need special column/property first");
		}
		CompareExpression expression = new SingleExpression(_halfExpress.getPrefixCode(), _halfExpress.getColumnCode(),
				SqlKey.EQUAL, value, "");
		this.addExpression(expression);
		_halfExpress = null;
		return this;
	}

	public SemanticCondition neq(Object value) {
		if (_halfExpress == null) {
			throw new RuntimeException("need special column/property first");
		}
		CompareExpression expression = new SingleExpression(_halfExpress.getPrefixCode(), _halfExpress.getColumnCode(),
				SqlKey.NOT_EQUAL, value, "");
		this.addExpression(expression);
		_halfExpress = null;
		return this;
	}

	public SemanticCondition gt(Object value) {
		if (_halfExpress == null) {
			throw new RuntimeException("need special column/property first");
		}
		CompareExpression expression = new SingleExpression(_halfExpress.getPrefixCode(), _halfExpress.getColumnCode(),
				SqlKey.GREATER, value, "");
		this.addExpression(expression);
		_halfExpress = null;
		return this;
	}

	public SemanticCondition geqt(Object value) {
		if (_halfExpress == null) {
			throw new RuntimeException("need special column/property first");
		}
		CompareExpression expression = new SingleExpression(_halfExpress.getPrefixCode(), _halfExpress.getColumnCode(),
				SqlKey.GREATER_OR_EQUAL, value, "");
		this.addExpression(expression);
		_halfExpress = null;
		return this;
	}

	public SemanticCondition lt(Object value) {
		if (_halfExpress == null) {
			throw new RuntimeException("need special column/property first");
		}
		CompareExpression expression = new SingleExpression(_halfExpress.getPrefixCode(), _halfExpress.getColumnCode(),
				SqlKey.LESS, value, "");
		this.addExpression(expression);
		_halfExpress = null;
		return this;
	}

	public SemanticCondition leqt(Object value) {
		if (_halfExpress == null) {
			throw new RuntimeException("need special column/property first");
		}
		CompareExpression expression = new SingleExpression(_halfExpress.getPrefixCode(), _halfExpress.getColumnCode(),
				SqlKey.LESS_OR_EQUAL, value, "");
		this.addExpression(expression);
		_halfExpress = null;
		return this;
	}

	public SemanticCondition isNull() {
		if (_halfExpress == null) {
			throw new RuntimeException("need special column/property first");
		}
		CompareExpression expression = new NoValueExpression(_halfExpress.getPrefixCode(), _halfExpress.getColumnCode(),
				SqlKey.IS_NULL, "");
		this.addExpression(expression);
		_halfExpress = null;
		return this;
	}

	public SemanticCondition isNotNull() {
		if (_halfExpress == null) {
			throw new RuntimeException("need special column/property first");
		}
		CompareExpression expression = new NoValueExpression(_halfExpress.getPrefixCode(), _halfExpress.getColumnCode(),
				SqlKey.IS_NOT_NULL, "");
		this.addExpression(expression);
		_halfExpress = null;
		return this;
	}

	public SemanticCondition in(String values, String separator) {

		if (_halfExpress == null) {
			throw new RuntimeException("need special column/property first");
		}
		CompareExpression expression = new EnumExpression(_halfExpress.getPrefixCode(), _halfExpress.getColumnCode(),
				SqlKey.IN, Arrays.asList((Object[]) values.split(separator)), "");
		this.addExpression(expression);
		_halfExpress = null;
		return this;
	}

	public SemanticCondition in(List<Object> values) {
		if (_halfExpress == null) {
			throw new RuntimeException("need special column/property first");
		}
		CompareExpression expression = new EnumExpression(_halfExpress.getPrefixCode(), _halfExpress.getColumnCode(),
				SqlKey.IN, values, "");
		this.addExpression(expression);
		_halfExpress = null;
		return this;
	}

	public SemanticCondition in(Object[] values) {
		if (_halfExpress == null) {
			throw new RuntimeException("need special column/property first");
		}
		CompareExpression expression = new EnumExpression(_halfExpress.getPrefixCode(), _halfExpress.getColumnCode(),
				SqlKey.IN, Arrays.asList(values), "");
		this.addExpression(expression);
		_halfExpress = null;
		return this;
	}

	public SemanticCondition notIn(String values, String separator) {
		if (_halfExpress == null) {
			throw new RuntimeException("need special column/property first");
		}
		CompareExpression expression = new EnumExpression(_halfExpress.getPrefixCode(), _halfExpress.getColumnCode(),
				SqlKey.NOT_IN, Arrays.asList((Object[]) values.split(separator)), "");
		this.addExpression(expression);
		_halfExpress = null;
		return this;
	}

	public SemanticCondition notIn(List<Object> values) {
		if (_halfExpress == null) {
			throw new RuntimeException("need special column/property first");
		}
		CompareExpression expression = new EnumExpression(_halfExpress.getPrefixCode(), _halfExpress.getColumnCode(),
				SqlKey.NOT_IN, values, "");
		this.addExpression(expression);
		_halfExpress = null;
		return this;
	}

	public SemanticCondition notIn(Object[] values) {
		if (_halfExpress == null) {
			throw new RuntimeException("need special column/property first");
		}
		CompareExpression expression = new EnumExpression(_halfExpress.getPrefixCode(), _halfExpress.getColumnCode(),
				SqlKey.NOT_IN, Arrays.asList(values), "");
		this.addExpression(expression);
		_halfExpress = null;
		return this;
	}

	public SemanticCondition like(String value) {
		if (_halfExpress == null) {
			throw new RuntimeException("need special column/property first");
		}
		CompareExpression expression = new SingleExpression(_halfExpress.getPrefixCode(), _halfExpress.getColumnCode(),
				SqlKey.LIKE, "%" + value + "%", "");
		this.addExpression(expression);
		_halfExpress = null;
		return this;
	}

	public SemanticCondition notLike(String value) {
		if (_halfExpress == null) {
			throw new RuntimeException("need special column/property first");
		}
		CompareExpression expression = new SingleExpression(_halfExpress.getPrefixCode(), _halfExpress.getColumnCode(),
				SqlKey.NOT_LIKE, "%" + value + "%", "");
		this.addExpression(expression);
		_halfExpress = null;
		return this;
	}

	public SemanticCondition beginWith(String value) {
		if (_halfExpress == null) {
			throw new RuntimeException("need special column/property first");
		}
		CompareExpression expression = new SingleExpression(_halfExpress.getPrefixCode(), _halfExpress.getColumnCode(),
				SqlKey.LIKE, value + "%", "");
		this.addExpression(expression);
		_halfExpress = null;
		return this;
	}

	public SemanticCondition endWith(String value) {
		if (_halfExpress == null) {
			throw new RuntimeException("need special column/property first");
		}
		CompareExpression expression = new SingleExpression(_halfExpress.getPrefixCode(), _halfExpress.getColumnCode(),
				SqlKey.LIKE, "%" + value, "");
		this.addExpression(expression);
		_halfExpress = null;
		return this;
	}

	public SemanticCondition between(Object beginValue, Object endValue) {
		if (_halfExpress == null) {
			throw new RuntimeException("need special column/property first");
		}
		CompareExpression expression = new BetweenExpression(_halfExpress.getPrefixCode(), _halfExpress.getColumnCode(),
				beginValue, endValue, "");
		this.addExpression(expression);
		_halfExpress = null;
		return this;
	}
	
	public SemanticCondition raw(String value) {
		CompareExpression expression = new LiteralExpression(value);
		this.addExpression(expression);
		_halfExpress = null;
		return this;
	}

	public class HalfExpress {

		private String prefixCode = "";

		private String columnCode;

		public String getPrefixCode() {
			return prefixCode;
		}

		public void setPrefixCode(String prefixCode) {
			this.prefixCode = prefixCode;
		}

		public String getColumnCode() {
			return columnCode;
		}

		public void setColumnCode(String columnCode) {
			this.columnCode = columnCode;
		}

	}
}
