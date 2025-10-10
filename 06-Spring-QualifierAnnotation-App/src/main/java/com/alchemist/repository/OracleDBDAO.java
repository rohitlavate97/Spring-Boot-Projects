package com.alchemist.repository;

import org.springframework.stereotype.Repository;

@Repository("oracleDBDAO")
public class OracleDBDAO implements ReportDAO {

	@Override
	public void getData() {
		System.out.println("Getting data from Oracle DB");
	}

}
