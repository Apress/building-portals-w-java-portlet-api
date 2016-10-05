
<%@ page errorPage="error.jsp" %>

<html>
<head>
	<title>Untitled</title>
</head>

<body>

Error Test Page

<%	String error = request.getParameter("error");
	if( error != null && error.equals("true") ) {
		throw new Exception("blah");
	}
%>


</body>
</html>
