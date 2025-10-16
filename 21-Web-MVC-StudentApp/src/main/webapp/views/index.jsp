<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Student Enquiry Form</title>
    <style>
        /* Modern CSS Reset and Base Styles */
        * {
            margin: 0;
            padding: 0;
            box-sizing: border-box;
        }
        
        body {
            font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif;
            background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
            min-height: 100vh;
            display: flex;
            justify-content: center;
            align-items: center;
            padding: 20px;
        }
        
        .container {
            background: white;
            border-radius: 15px;
            box-shadow: 0 20px 40px rgba(0,0,0,0.1);
            padding: 40px;
            max-width: 600px;
            width: 100%;
            transition: transform 0.3s ease;
        }
        
        .container:hover {
            transform: translateY(-5px);
        }
        
        .header {
            text-align: center;
            margin-bottom: 30px;
        }
        
        .header h2 {
            color: #333;
            font-size: 2.2em;
            margin-bottom: 10px;
            background: linear-gradient(135deg, #667eea, #764ba2);
            -webkit-background-clip: text;
            -webkit-text-fill-color: transparent;
            background-clip: text;
        }
        
        .header p {
            color: #666;
            font-size: 1.1em;
        }
        
        .success-message {
            background: #d4edda;
            border: 1px solid #c3e6cb;
            color: #155724;
            padding: 12px 20px;
            border-radius: 8px;
            margin-bottom: 25px;
            text-align: center;
            font-weight: 500;
        }
        
        .form-table {
            width: 100%;
            border-collapse: collapse;
        }
        
        .form-table tr {
            transition: background-color 0.3s ease;
        }
        
        .form-table tr:hover {
            background-color: #f8f9fa;
        }
        
        .form-table td {
            padding: 15px 10px;
            vertical-align: top;
        }
        
        .form-table td:first-child {
            font-weight: 600;
            color: #495057;
            width: 120px;
            padding-right: 20px;
        }
        
        .form-label {
            display: block;
            margin-bottom: 5px;
            font-weight: 600;
            color: #495057;
        }
        
        /* Input Fields Styling */
        .form-input {
            width: 100%;
            padding: 12px 15px;
            border: 2px solid #e9ecef;
            border-radius: 8px;
            font-size: 16px;
            transition: all 0.3s ease;
            background: #f8f9fa;
        }
        
        .form-input:focus {
            outline: none;
            border-color: #667eea;
            background: white;
            box-shadow: 0 0 0 3px rgba(102, 126, 234, 0.1);
        }
        
        /* Radio Buttons Styling */
        .radio-group {
            display: flex;
            gap: 20px;
            align-items: center;
        }
        
        .radio-label {
            display: flex;
            align-items: center;
            gap: 8px;
            cursor: pointer;
            padding: 8px 12px;
            border-radius: 6px;
            transition: background-color 0.3s ease;
        }
        
        .radio-label:hover {
            background-color: #f1f3f4;
        }
        
        .radio-label input[type="radio"] {
            transform: scale(1.2);
            accent-color: #667eea;
        }
        
        /* Select Dropdown Styling */
        .form-select {
            width: 100%;
            padding: 12px 15px;
            border: 2px solid #e9ecef;
            border-radius: 8px;
            font-size: 16px;
            background: #f8f9fa;
            cursor: pointer;
            transition: all 0.3s ease;
        }
        
        .form-select:focus {
            outline: none;
            border-color: #667eea;
            background: white;
            box-shadow: 0 0 0 3px rgba(102, 126, 234, 0.1);
        }
        
        /* Checkboxes Styling */
        .checkbox-group {
            display: flex;
            flex-wrap: wrap;
            gap: 15px;
        }
        
        .checkbox-label {
            display: flex;
            align-items: center;
            gap: 8px;
            cursor: pointer;
            padding: 8px 15px;
            background: #f8f9fa;
            border-radius: 6px;
            transition: all 0.3s ease;
            border: 2px solid transparent;
        }
        
        .checkbox-label:hover {
            background: #e9ecef;
            border-color: #667eea;
        }
        
        .checkbox-label input[type="checkbox"] {
            transform: scale(1.2);
            accent-color: #667eea;
        }
        
        /* Submit Button Styling */
        .submit-section {
            text-align: center;
            margin-top: 30px;
            padding-top: 20px;
            border-top: 1px solid #e9ecef;
        }
        
        .submit-btn {
            background: linear-gradient(135deg, #667eea, #764ba2);
            color: white;
            border: none;
            padding: 15px 40px;
            font-size: 18px;
            font-weight: 600;
            border-radius: 50px;
            cursor: pointer;
            transition: all 0.3s ease;
            box-shadow: 0 4px 15px rgba(102, 126, 234, 0.3);
        }
        
        .submit-btn:hover {
            transform: translateY(-2px);
            box-shadow: 0 6px 20px rgba(102, 126, 234, 0.4);
        }
        
        .submit-btn:active {
            transform: translateY(0);
        }
        
        /* Responsive Design */
        @media (max-width: 768px) {
            .container {
                padding: 25px;
                margin: 10px;
            }
            
            .form-table td:first-child {
                width: 100px;
            }
            
            .radio-group {
                flex-direction: column;
                gap: 10px;
                align-items: flex-start;
            }
            
            .checkbox-group {
                flex-direction: column;
                gap: 10px;
            }
        }
        
        /* Animation for form elements */
        @keyframes fadeIn {
            from { opacity: 0; transform: translateY(20px); }
            to { opacity: 1; transform: translateY(0); }
        }
        
        .form-table tr {
            animation: fadeIn 0.5s ease forwards;
        }
        
        .form-table tr:nth-child(1) { animation-delay: 0.1s; }
        .form-table tr:nth-child(2) { animation-delay: 0.2s; }
        .form-table tr:nth-child(3) { animation-delay: 0.3s; }
        .form-table tr:nth-child(4) { animation-delay: 0.4s; }
        .form-table tr:nth-child(5) { animation-delay: 0.5s; }
        .form-table tr:nth-child(6) { animation-delay: 0.6s; }
    </style>
</head>
<body>
    <div class="container">
        <div class="header">
            <h2>🎓 Student Enquiry Form</h2>
            <p>Complete your course registration details</p>
        </div>
        
        <!-- Success Message -->
        <c:if test="${not empty msg}">
            <div class="success-message">
                ✅ ${msg}
            </div>
        </c:if>
        
        <form:form action="save" modelAttribute="student" method="POST">
            <table class="form-table">
                <tr>
                    <td><label class="form-label">Name:</label></td>
                    <td>
                        <form:input path="name" cssClass="form-input" placeholder="Enter your full name"/>
                    </td>
                </tr>
                <tr>
                    <td><label class="form-label">Email:</label></td>
                    <td>
                        <form:input path="email" cssClass="form-input" placeholder="Enter your email address"/>
                    </td>
                </tr>
                <tr>
                    <td><label class="form-label">Gender:</label></td>
                    <td>
                        <div class="radio-group">
                            <label class="radio-label">
                                <form:radiobutton path="gender" value="M"/> Male
                            </label>
                            <label class="radio-label">
                                <form:radiobutton path="gender" value="F"/> Female
                            </label>
                        </div>
                    </td>
                </tr>
                <tr>
                    <td><label class="form-label">Courses:</label></td>
                    <td>
                        <form:select path="course" cssClass="form-select">
                            <form:option value="">- Select Course -</form:option>
                            <form:options items="${courses}"/>
                        </form:select>
                    </td>
                </tr>
                <tr>
                    <td><label class="form-label">Timings:</label></td>
                    <td>
                        <div class="checkbox-group">
                            <form:checkboxes path="timings" items="${prefTimings}" element="div class='checkbox-label'"/>
                        </div>
                    </td>
                </tr>
                <tr>
                    <td colspan="2" class="submit-section">
                        <button type="submit" class="submit-btn">
                            📝 Submit Enquiry
                        </button>
                    </td>
                </tr>
            </table>
        </form:form>
    </div>
</body>
</html>