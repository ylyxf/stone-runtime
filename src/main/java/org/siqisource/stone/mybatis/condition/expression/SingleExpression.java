package org.siqisource.stone.mybatis.condition.expression;

public class SingleExpression implements CompareExpression {

	public SingleExpression(String prefixCode, String columnCode,
			String compareSymbol, Object value, String suffixCode) {
		this.prefixCode = prefixCode;
		this.columnCode = columnCode;
		this.compareSymbol = compareSymbol;
		this.suffixCode = suffixCode;
		this.value = value;
	}

	private String prefixCode;

	private String suffixCode;

	private String columnCode;

	private String compareSymbol;

	private Object value;

	public String getMybatisSql(int index) {
		StringBuffer sql = new StringBuffer();
		sql.append(this.prefixCode);
		sql.append(this.columnCode);
		sql.append(" ");
		sql.append(this.compareSymbol);
		sql.append(" #{condition.expressions[");
		sql.append(index);
		sql.append("].value} ");
		sql.append(this.suffixCode);
		return sql.toString();
	}

	public String getPrefixCode() {
		return prefixCode;
	}

	public void setPrefixCode(String prefixCode) {
		this.prefixCode = prefixCode;
	}

	public String getSuffixCode() {
		return suffixCode;
	}

	public void setSuffixCode(String suffixCode) {
		this.suffixCode = suffixCode;
	}

	public String getColumnCode() {
		return columnCode;
	}

	public void setColumnCode(String columnCode) {
		this.columnCode = columnCode;
	}

	public String getCompareSymbol() {
		return compareSymbol;
	}

	public void setCompareSymbol(String compareSymbol) {
		this.compareSymbol = compareSymbol;
	}

	public Object getValue() {
		return value;
	}

	public void setValue(Object value) {
		this.value = value;
	}

}
