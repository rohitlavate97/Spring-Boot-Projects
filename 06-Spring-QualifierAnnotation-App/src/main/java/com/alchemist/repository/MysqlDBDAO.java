package com.alchemist.repository;

import org.springframework.stereotype.Repository;

@Repository("mysqlDBDAO")
public class MysqlDBDAO implements ReportDAO {

	@Override
	public void getData() {
		System.out.println("Getting data from MySQL DB");
	}

}
