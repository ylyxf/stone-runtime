package org.siqisource.stone.mybatis.condition.expression;

import org.siqisource.stone.mybatis.condition.SqlKey;

public class BetweenExpression  implements CompareExpression {

	public BetweenExpression(String prefixCode, String columnCode,
			Object beginValue, Object endValue, String suffixCode) {

		this.prefixCode = prefixCode;
		this.columnCode = columnCode;
		this.compareSymbol = SqlKey.BETWEEN;
		this.suffixCode = suffixCode;
		
		this.beginValue = beginValue;
		this.endValue = endValue;
	}

	private String prefixCode;
	
	private String columnCode;
	
	private String compareSymbol;
	
	private String suffixCode;
	
	private Object beginValue;

	private Object endValue;

	public Object getBeginValue() {
		return beginValue;
	}

	public void setBeginValue(Object beginValue) {
		this.beginValue = beginValue;
	}

	public Object getEndValue() {
		return endValue;
	}

	public void setEndValue(Object endValue) {
		this.endValue = endValue;
	}

	public String getMybatisSql(int index) {
		StringBuffer sql = new StringBuffer(128);
		sql.append(this.prefixCode);
		sql.append(this.columnCode);
		sql.append(" ");
		sql.append(this.compareSymbol);
		sql.append(" #{condition.expressions[");
		sql.append(index);
		sql.append("].beginValue} AND #{condition.expressions[");
		sql.append(index);
		sql.append("].endValue} ");
		sql.append(this.suffixCode);
		return sql.toString();
	}

}
