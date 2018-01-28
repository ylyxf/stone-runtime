package org.siqisource.stone.mybatis.condition.expression;

import java.util.ArrayList;
import java.util.List;

import org.siqisource.stone.mybatis.condition.SqlKey;
import org.siqisource.stone.mybatis.utils.NameConvertStyle;
import org.siqisource.stone.mybatis.utils.NameConverter;


public class OrderByExpression {

	private List<Order> orders = new ArrayList<Order>(1);

	public void orderAsc(String propertyNames, Boolean needP2c) {
		order(propertyNames, SqlKey.ASC, needP2c);
	}

	public void orderDesc(String propertyNames, Boolean needP2c) {
		order(propertyNames, SqlKey.DESC, needP2c);
	}

	@Override
	public String toString() {
		StringBuffer sbSql = new StringBuffer(64);
		sbSql.append(SqlKey.ORDER_BY);
		for (int i = 0; i < orders.size(); i++) {
			Order order = orders.get(i);
			if (i != 0) {
				sbSql.append(" , ");
			}

			sbSql.append(order.getField());
			sbSql.append(" ");
			sbSql.append(order.getDirection());
			sbSql.append(" ");
		}
		return sbSql.toString();
	}

	private void order(String propertyNames, String direction, Boolean needP2c) {
		String[] propertyNameArray = propertyNames.split(",");
		for (String property : propertyNameArray) {
			Order order = new Order();
			if (needP2c) {
				property = NameConverter.convertWithNamespcae(property, NameConvertStyle.camelToUnderlineLower);
			}
			order.setField(property);
			order.setDirection(direction);
			orders.add(order);
		}
	}

	public OrderByExpression copy() {
		OrderByExpression orderBy = new OrderByExpression();
		for (Order order : orders) {
			orderBy.orders.add(order);
		}
		return orderBy;
	}

}
