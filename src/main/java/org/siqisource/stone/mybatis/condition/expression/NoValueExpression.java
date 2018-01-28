package org.siqisource.stone.mybatis.condition.expression;

public class NoValueExpression implements CompareExpression {

	public NoValueExpression(String prefixCode , String columnCode,
			String compareSymbol, String suffixCode) {
		this.prefixCode = prefixCode;
		this.columnCode = columnCode;
		this.compareSymbol = compareSymbol;
		this.suffixCode = suffixCode;
	}

	private String prefixCode;
	
	private String columnCode;
	
	private String compareSymbol;
	
	private String suffixCode;

	public String getMybatisSql(int index) {
		StringBuffer sql = new StringBuffer();
		sql.append(this.prefixCode);
		sql.append(this.columnCode);
		sql.append(" ");
		sql.append(this.compareSymbol);
		sql.append(this.suffixCode);
		return sql.toString();
	}

	public String getPrefixCode() {
		return prefixCode;
	}

	public void setPrefixCode(String prefixCode) {
		this.prefixCode = prefixCode;
	}

	public String getCompareSymbol() {
		return compareSymbol;
	}

	public void setCompareSymbol(String compareSymbol) {
		this.compareSymbol = compareSymbol;
	}

	public String getSuffixCode() {
		return suffixCode;
	}

	public void setSuffixCode(String suffixCode) {
		this.suffixCode = suffixCode;
	}
	
}
