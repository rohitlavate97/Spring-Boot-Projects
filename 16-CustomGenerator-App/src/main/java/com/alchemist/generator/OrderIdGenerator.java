package com.alchemist.generator;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

import org.hibernate.engine.spi.SharedSessionContractImplementor;
import org.hibernate.id.IdentifierGenerator;

public class OrderIdGenerator implements IdentifierGenerator{

	@Override
	public Serializable generate(SharedSessionContractImplementor session, Object object) {
		
		String prefix = "OD";
		String suffix = "";
		try {
			Connection connection = session.getJdbcConnectionAccess().obtainConnection();
			Statement statement = connection.createStatement();
			String query = "SELECT ORDER_ID_SEQ.NEXTVAL as nextval FROM DUAL";
			ResultSet result = statement.executeQuery(query);
			if(result.next()) {
				int seqVal = result.getInt(1);
				suffix = String.valueOf(seqVal);
			}
		}catch (Exception e) {
			e.printStackTrace();
		}
		return prefix + suffix;
	}

}
