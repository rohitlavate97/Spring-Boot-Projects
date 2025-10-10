package com.alchemist.repository;

import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Repository;

@Repository("oracleDBDAO")
@Primary
public class OracleDBDAO implements ReportDAO {

	@Override
	public void getData() {
		System.out.println("Getting data from Oracle DB");
	}

}
