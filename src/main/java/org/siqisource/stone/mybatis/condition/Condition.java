package org.siqisource.stone.mybatis.condition;

import java.util.ArrayList;
import java.util.List;

import org.siqisource.stone.mybatis.condition.expression.CompareExpression;
import org.siqisource.stone.mybatis.condition.expression.Order;
import org.siqisource.stone.mybatis.condition.expression.OrderByExpression;


public class Condition {

	/** expressions */
	private List<CompareExpression> expressions = null;

	/** orderBy */
	private OrderByExpression orderBy = null;

	public Condition() {

	}

	public Condition addCondition(Condition condition) {
		expressions.addAll(condition.getExpressions());
		return this;
	}

	public Condition addExpression(CompareExpression expression) {
		if (expressions == null) {
			expressions = new ArrayList<CompareExpression>();
		}
		expressions.add(expression);
		return this;
	}

	public String getComboedExpressions() {
		StringBuffer sql = new StringBuffer();
		if (expressions != null) {
			if (expressions.size() > 0) {
				for (int i = 0, iSize = expressions.size(); i < iSize; i++) {
					CompareExpression expression = expressions.get(i);
					sql.append(expression.getMybatisSql(i));
				}
			}
		}

		return sql.toString();
	}

	public Condition copy() {
		Condition conditionList = new Condition();
		conditionList.orderBy = this.orderBy.copy();
		conditionList.expressions = new ArrayList<CompareExpression>();
		for (CompareExpression expression : this.expressions) {
			conditionList.expressions.add(expression);// fleet copy
		}
		return conditionList;
	}

	public Condition order(Order order) {
		if (order != null) {
			this.order(order.getField(), order.getDirection());
		}
		return this;
	}

	public Condition orderAsc(String propertiesNames) {
		if (this.orderBy == null) {
			this.orderBy = new OrderByExpression();
		}
		orderBy.orderAsc(propertiesNames, true);
		return this;
	}

	public Condition orderDesc(String propertiesNames) {
		if (this.orderBy == null) {
			this.orderBy = new OrderByExpression();
		}
		orderBy.orderDesc(propertiesNames, true);
		return this;
	}

	public Condition orderAsc(String propertiesNames, boolean needP2c) {
		if (this.orderBy == null) {
			this.orderBy = new OrderByExpression();
		}
		orderBy.orderAsc(propertiesNames, needP2c);
		return this;
	}

	public Condition orderDesc(String propertiesNames, boolean needP2c) {
		if (this.orderBy == null) {
			this.orderBy = new OrderByExpression();
		}
		orderBy.orderDesc(propertiesNames, needP2c);
		return this;
	}

	public Condition order(String propertiesNames, String direction) {
		if (SqlKey.ASC.equals(direction)) {
			this.orderAsc(propertiesNames);
		}
		if (SqlKey.DESC.equals(direction)) {
			this.orderDesc(propertiesNames);
		}
		return this;
	}

	public OrderByExpression getOrderBy() {
		return orderBy;
	}

	public List<CompareExpression> getExpressions() {
		return expressions;
	}
}
