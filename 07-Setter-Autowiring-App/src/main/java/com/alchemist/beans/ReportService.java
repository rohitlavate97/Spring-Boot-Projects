package com.alchemist.beans;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import com.alchemist.repository.ReportDAO;

@Service
public class ReportService {
	private ReportDAO reportDAO;
	@Autowired
	public void setReportDAO(@Qualifier("oracleDBDAO") ReportDAO reportDAO) {
		this.reportDAO = reportDAO;
	}

	public void generateReport() {
		System.out.println("ReportService - Generating Report");
		reportDAO.getData();
		System.out.println("ReportService - Report Generated");
	}

}
