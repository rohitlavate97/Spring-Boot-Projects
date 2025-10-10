package com.alchemist.repository;

import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Repository;

@Repository("mysqlDBDAO")
//@Primary
public class MysqlDBDAO implements ReportDAO {

	@Override
	public void getData() {
		System.out.println("Getting data from MySQL DB");
	}

}
