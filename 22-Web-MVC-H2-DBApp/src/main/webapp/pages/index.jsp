<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<!DOCTYPE html>
<html>
	<head>
		<meta charset="UTF-8">
		<title>Home Page</title>
	</head>
	<body>
		<h2>Product Info</h2>
		<p><font color="green">${msg}</font></p>
		<form:form action="product" modelAttribute="p" method="POST">
			<table>
				<tr>
					<td>Name:</td>
					<td><form:input path="name"/></td>
				</tr>
				<tr>
					<td>Price:</td>
					<td><form:input path="price"/></td>
				</tr>
				<tr>
					<td>Quantity:</td>
					<td><form:input path="qty"/></td>
				</tr>
				<tr>
					<td></td>
					<td><input type="submit" value="save"></td>
				</tr>
			</table>
		</form:form>
		<a href="products">View Products</a>
	</body>
</html>