<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Book Search - Alchemist</title>
    <style>
        body {
            font-family: Arial, sans-serif;
            margin: 40px;
            background: #f5f5f5;
        }
        .container {
            background: white;
            padding: 30px;
            border-radius: 10px;
            box-shadow: 0 2px 10px rgba(0,0,0,0.1);
            max-width: 500px;
            margin: 0 auto;
        }
        h1 {
            color: #333;
            text-align: center;
        }
        .search-form {
            margin-bottom: 30px;
            padding: 20px;
            background: #f8f9fa;
            border-radius: 5px;
        }
        .search-form input[type="text"] {
            padding: 10px;
            width: 200px;
            margin-right: 10px;
            border: 1px solid #ddd;
            border-radius: 4px;
        }
        .search-form input[type="submit"] {
            padding: 10px 20px;
            background: #007bff;
            color: white;
            border: none;
            border-radius: 4px;
            cursor: pointer;
        }
        .search-form input[type="submit"]:hover {
            background: #0056b3;
        }
        .book-details {
            margin-top: 20px;
            padding: 20px;
            background: #e8f5e8;
            border-radius: 5px;
            border-left: 5px solid #28a745;
        }
        .no-book {
            margin-top: 20px;
            padding: 20px;
            background: #f8d7da;
            border-radius: 5px;
            border-left: 5px solid #dc3545;
            color: #721c24;
        }
    </style>
</head>
<body>
    <div class="container">
        <h1>📚 Book Search</h1>
        
        <!-- FIXED: Added action="book" to form -->
        <div class="search-form">
            <form action="book" method="get">
                BookId: <input type="text" name="id"/>
                <input type="submit" value="Search"/>
            </form>
        </div>

        <!-- Display book details if book exists -->
        <% if (request.getAttribute("book") != null) { %>
            <div class="book-details">
                <h3>✅ Book Found!</h3>
                BookId: ${book.bookId}<br/>
                Book Name: ${book.bookName}<br/>
                Book Price: ${book.bookPrice}
            </div>
        <% } %>

        <!-- Display message if no book found but search was attempted -->
        <% 
            String idParam = request.getParameter("id");
            if (idParam != null && !idParam.isEmpty() && request.getAttribute("book") == null) { 
        %>
            <div class="no-book">
                <h3>❌ Book Not Found</h3>
                No book found with ID: <strong><%= idParam %></strong>
            </div>
        <% } %>
    </div>
</body>
</html>