
<%
/**
 *	$RCSfile: error.jsp,v $
 *	$Revision: 1.1.1.1 $
 *	$Date: 2002/04/26 23:50:04 $
 */
%>

<%@ page isErrorPage="true" %>

<%@ page import="java.util.*,
                 java.net.*,
                 com.Yasna.forum.*,
                 com.Yasna.forum.util.*,
				 com.Yasna.forum.util.admin.*"
%>

<%	//////////////////
	// get parameters
	String errorMessage = ParamUtils.getParameter(request,"msg");
%>

<html>
<head>
	<title></title>
	<link rel="stylesheet" href="style/global.css">
</head>

<body background="images/shadowBack.gif" bgcolor="#ffffff" text="#000000" link="#0000ff" vlink="#800080" alink="#ff0000">

<%	///////////////////////
	// pageTitleInfo variable (used by include/pageTitle.jsp)
	String[] pageTitleInfo = { "Error" };
%>
<%	///////////////////
	// pageTitle include
%><%@ include file="include/pageTitle.jsp" %>

<p>

<%	if( errorMessage != null ) { %>

	error message: <%= errorMessage %>

<%	} else { %>

	<%	String message = exception.getMessage(); %>
	<%	if( message == null ) { %>
	<%		message = ""; %>
	<%	} %>

	<%	if( exception instanceof UnauthorizedException ) { %>
	
		You are not authorized to take that action.
	
	<%	} else { %>
	
		A general error occured. (exception: <%= exception %>)
	
	<%	} %>
	
<%	} %>

<%	exception.printStackTrace(); %>

</body>
</html>
