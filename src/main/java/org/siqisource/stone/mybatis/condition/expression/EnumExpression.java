package org.siqisource.stone.mybatis.condition.expression;

import java.util.List;

public class EnumExpression implements CompareExpression {

	public EnumExpression(String prefixCode, String columnCode,
			String compareSymbol, List<?> valueList, String suffixCode) {
		this.prefixCode = prefixCode;
		this.columnCode = columnCode;
		this.compareSymbol = compareSymbol;
		this.suffixCode = suffixCode;
		this.valueList = valueList;
	}

	private String prefixCode;

	private String columnCode;

	private String compareSymbol;

	private String suffixCode;

	private List<?> valueList;

	public List<?> getValueList() {
		return valueList;
	}

	public void setValueList(List<?> valueList) {
		this.valueList = valueList;
	}

	public String getMybatisSql(int index) {
		StringBuffer sql = new StringBuffer(128);
		sql.append(this.prefixCode);
		sql.append(this.columnCode);
		sql.append(" ");
		sql.append(this.compareSymbol);
		sql.append(" ( ");
		for (int i = 0; i < this.valueList.size(); i++) {
			if (i != 0) {
				sql.append(" , ");
			}
			sql.append("#{condition.expressions[");
			sql.append(index);
			sql.append("].valueList[");
			sql.append(i);
			sql.append("]}");
		}
		sql.append(" ) ");
		sql.append(this.suffixCode);
		return sql.toString();
	}

}
