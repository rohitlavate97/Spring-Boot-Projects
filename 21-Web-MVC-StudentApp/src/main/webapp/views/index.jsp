<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<!DOCTYPE html>
  <html>
   <head>
    <meta charset="UTF-8">
	<title>Course Details</title>
   </head>
   <body>
	<h2>Student Enquiry Page</h2>
	<p>
		<font color="green">${msg}</font>
	</p>
	<form:form action="save" modelAttribute="student" method="POST">
		<table>
			<tr>
				<td>Name:</td>
				<td><form:input path="name"/></td>
			</tr>
			<tr>
				<td>Email:</td>
				<td><form:input path="email"/></td>
			</tr>
			<tr>
				<td>Gender:</td>
				<td><form:radiobutton path="gender" value="M"/>Male
					<td><form:radiobutton path="gender" value="F"/>Female
				</td>
			</tr>
			<tr>
				<td>Courses</td>
				<td>
					<form:select path="course">
						<form:option value="">-Select-</form:option>
						<form:options items="${courses}"/>
<!--					<form:option value="PYTHON">PYTHON</form:option>
						<form:option value="DEVOPS">DEVOPS</form:option>-->
					</form:select>
				</td>
			</tr>
			<tr>
				<td>Timings</td>
				<td>
						<form:checkboxes path="timings" items="${prefTimings}"/>
						<!--<form:checkbox path="timings" value="noon"/>Afternoon
						<form:checkbox path="timings" value="evening"/>Evening-->
				</td>
			</tr>
			<tr>
				<td></td>
				<td><input type="submit" value="save"/></td>
			</tr>
		</table>
	 </form:form>
	</body>
</html>