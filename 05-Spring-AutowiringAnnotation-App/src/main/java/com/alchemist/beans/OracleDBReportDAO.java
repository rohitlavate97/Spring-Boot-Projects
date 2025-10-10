package com.alchemist.beans;

import org.springframework.stereotype.Repository;

@Repository("reportDAO")    //to do autowiring by name
public class OracleDBReportDAO implements ReportDAO {

	@Override
	public void getData() {
		System.out.println("OracleDBReportDAO - Getting Data from Oracle DB");
	}

}
