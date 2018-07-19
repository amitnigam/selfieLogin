<!DOCTYPE html>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<html lang="en">

<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1">
<title>Login with your selfie</title>
<link href="css/form.css" rel="stylesheet">
<script src="https://ajax.googleapis.com/ajax/libs/jquery/1.11.1/jquery.min.js"></script>
  <style>
   #pageloader
{
  background: rgba( 255, 255, 255, 0.8 );
  display: none;
  height: 100%;
  position: fixed;
  width: 100%;
  z-index: 9999;
}

#pageloader img
{
  left: 50%;
  margin-left: -32px;
  margin-top: -32px;
  position: absolute;
  top: 50%;
}
</style>
    </head>
   
    <body>
  
    <div id="pageloader">
<img src="/lg/images/loader-large.gif" alt="Loading" /><br/>
Loading--
</div>
       <div>    
        <c:if test="${param.error}">
            Invalid username and password.
            </c:if>
        </div>
        <div>
        <c:if test="${param.logout }" >
        you have been logged out.
        </c:if>
        </div>
        <div class="container">
        <form name="loginForm" method="POST" action="login" enctype="multipart/form-data">
            <div><label> Email Id : </label>
            <input name="email" type="text" autofocus="true"/>
            </div>
            <div>
		<label>Please take your selfie to login</label>
		<input type="file" name="mainimage" accept="image/*" capture="user" />
			</div>
            <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>
            <div><input type="submit" value="Sign In"/></div>
        </form>
        </div>
    <script type="text/javascript"> 
    $(document).ready(function(){
    	  $("#loginForm").on("submit", function(){
    	    $("#pageloader").fadeIn();
    	  });//submit
    	});//document ready
   </script>
    </body>
</html>