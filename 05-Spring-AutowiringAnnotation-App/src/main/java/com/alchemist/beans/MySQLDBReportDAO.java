package com.alchemist.beans;

public class MySQLDBReportDAO implements ReportDAO {

	@Override
	public void getData() {
		System.out.println("MySQLDBReportDAO - Getting Data from MySQL DB");
	}

}
