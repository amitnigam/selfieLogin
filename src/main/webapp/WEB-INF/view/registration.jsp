<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<meta name="viewport" content="width=device-width, initial-scale=1">
<title>Welcome</title>
<link href="css/form.css" rel="stylesheet">
</head>
<body>

<div>
	
	<c:if test="${!empty successMessage}">
	<p>${successMessage } <a href="login">Login now </a></p>
	</c:if>
	
	<c:if test="${!empty message}">
	<p>${message } Try <a href="login">login again </a> or Register below</p> 
	</c:if>
	<div class="container">
	<form:form method="post" modelAttribute="user" action="registration" enctype="multipart/form-data" >
		
		
			
			<form:label path="email">Your Email id</form:label>
				<form:input path="email"/>
		
			
		
			<form:label path="password">Create a Password</form:label>
				<form:input path="password"/>
		
			<form:label path="name">First Name</form:label>
				<form:input path="name" />
			
			<form:label path="lastName"  >Last Name</form:label>
				<form:input path="lastName" />
						
				
			
			<form:label path="cellphone">Cell Phone</form:label>
			
				<form:input  path="cellphone" />
			
			
			<form:label path="mainimage">Take your photo</form:label>
				
				<form:input type="file" path="mainimage" accept="image/*" capture="user" />
		
		
			<input type="submit" value="Create"/>
	
					
					
	</form:form>
	</div>







</div>

</body>
</html>