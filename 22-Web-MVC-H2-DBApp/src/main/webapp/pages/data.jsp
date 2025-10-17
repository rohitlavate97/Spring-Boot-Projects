<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<!DOCTYPE html>
<html>
	<head>
		<meta charset="UTF-8">
		<title>Products Page</title>
	</head>
	<body>
		<table>
			<thead>
				<tr>
					<th>S.NO</th>
					<th>Name</th>
					<th>Price</th>
					<th>Quantity</th>
				</tr>
			</thead>
			<tbody>
				<c:forEach items="${products}" var="p" varStatus="status">
					<tr>
						<td>${status.count}</td>
						<td>${p.name}</td>
						<td>${p.price}</td>
						<td>${p.qty}</td>
					</tr>
				</c:forEach>
			</tbody>
	 </body>
</html>