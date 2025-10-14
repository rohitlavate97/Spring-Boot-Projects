package com.alchemist;

import java.util.List;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;

import com.alchemist.entity.Employee;
import com.alchemist.repository.EmpRepository;

@SpringBootApplication
public class Application {

    private final EmpRepository empRepository;

    Application(EmpRepository empRepository) {
        this.empRepository = empRepository;
    }

	public static void main(String[] args) {
		ConfigurableApplicationContext context = SpringApplication.run(Application.class, args);
		EmpRepository empRepo = context.getBean(EmpRepository.class);
		System.out.println("EmpRepository Bean is: " + empRepo.getClass().getName());

		/*
		 * Employee e1 = new Employee();
		 * 
		 * e1.setEmpName("John Doe"); e1.setEmpSalary(50000.0); e1.setEmpGender("Male");
		 * e1.setEmpDept("IT"); empRepo.save(e1);
		 * 
		 * e1.setEmpName("Jane Smith"); e1.setEmpSalary(30000.0);
		 * e1.setEmpGender("Female"); e1.setEmpDept("HR");
		 * 
		 * Employee e2 = new Employee(); e2.setEmpName("Alice Johnson");
		 * e2.setEmpSalary(35000.0); e2.setEmpGender("Female"); e2.setEmpDept("HR");
		 * 
		 * Employee e3 = new Employee(); e3.setEmpName("Bob Brown");
		 * e3.setEmpSalary(40000.0); e3.setEmpGender("Male"); e3.setEmpDept("IT");
		 * 
		 * empRepo.saveAll( java.util.List.of(e1, e2, e3) );
		 * System.out.println("Employees saved successfully!");
		 */

		List<Employee> listbySalaryDesc = empRepo.getEmployeeBySalaryDesc();
		listbySalaryDesc.forEach(System.out::println);
		System.out.println("-----------------------------------------------------");
		
		// ascending order by bookName
		Sort sortByNameAsc = Sort.by("empName").ascending();
		//List<Employee> empAsc = empRepo.findAll(Sort.by("empName").ascending());
		List<Employee> empAsc = empRepo.findAll(sortByNameAsc);
		empAsc.forEach(System.out::println);
		System.out.println("------------------------------------------------------");
		
		//Sort sortByDeptDesc = Sort.by("empDept").descending();
		Sort sortByDeptDesc = Sort.by(Sort.Direction.DESC, "empDept");
		List<Employee> empDeptDesc = empRepo.findAll(sortByDeptDesc);
		empDeptDesc.forEach(System.out::println);
		System.out.println("------------------------------------------------------");
		
		//sorting based on multiple fields
		//Sort sortByDeptAscAndSalaryDesc = Sort.by("empDept","empSalary").ascending();
		Sort sortByDeptAscAndSalaryDesc = Sort.by("empDept").ascending()
                .and(Sort.by("empSalary").descending());
		List<Employee> empDeptAscAndSalaryDesc = empRepo.findAll(sortByDeptAscAndSalaryDesc);
		empDeptAscAndSalaryDesc.forEach(System.out::println);
		System.out.println("------------------------------------------------------");
		
		int pageNumber = 0; // first page
		PageRequest page = PageRequest.of(pageNumber, 2);
		Page<Employee> empPage = empRepo.findAll(page);
		List<Employee> empList = empPage.getContent();
		empList.forEach(System.out::println);
		System.out.println("------------------------------------------------------");
		
		int peNum = 0; // first page
		PageRequest pg = PageRequest.of(peNum, 3, Sort.by("empName").ascending());
		Page<Employee> empPg = empRepo.findAll(pg);
		List<Employee> empLst = empPg.getContent();
		empLst.forEach(System.out::println);
		System.out.println("------------------------------------------------------");
		
		//to filter according to the column name--->i.e where clause
		 Employee emp = new Employee();
		 emp.setEmpDept("IT");   // We want to find all employees from IT dept
		 Example<Employee> example = Example.of(emp);
		 List<Employee> result = empRepo.findAll(example);
		 result.forEach(System.out::println);
		 System.out.println("------------------------------------------------------");
		 
		 Employee e =new Employee();
		 e.setEmpDept("HR");
		 e.setEmpGender("Male");
		 Example ex = Example.of(e);
		 List<Employee> res = empRepo.findAll(ex);
		 res.forEach(System.out::println);
		 System.out.println("------------------------------------------------------");
		 
		 ExampleMatcher matcher = ExampleMatcher.matching()
			        .withIgnoreCase("empName")
			        .withStringMatcher(ExampleMatcher.StringMatcher.CONTAINING);
			Employee e2 = new Employee();
			e2.setEmpName("swapnil"); // will match "Swaobuk", "Swapnila", "Mr. Swapnil" etc.
			Example<Employee> exemp = Example.of(e2, matcher);
			List<Employee> output = empRepo.findAll(exemp);
			output.forEach(System.out::println);
	
	}

}
