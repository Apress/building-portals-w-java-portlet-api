<%
/**
 * Yazd Setup Tool
 * November 28, 2000
 */
%>

<%@ page import="java.io.*,
                 java.util.*,
				 java.sql.*,
                 com.Yasna.forum.*,
				 com.Yasna.forum.util.*,
				 com.Yasna.forum.database.*"%>

<%!	
	private String[] getPropertyNames( DbConnectionProvider conProvider ) {
		Enumeration enum = conProvider.propertyNames();
		Vector v = new Vector();
		while( enum.hasMoreElements() ) {
			v.addElement( enum.nextElement() );
		}
		String[] propNames = new String[ v.size() ];
		v.copyInto(propNames);
		return propNames;
	}	
%>

<% 
	DbConnectionProvider conProvider =  new DbConnectionDefaultPool();
	boolean error = false;
	String errorMessage = null;
	String[] propNames = getPropertyNames(conProvider);		
	String[] paramPropVals = new String[propNames.length];

	//Make sure the install has not already been completed.
	String setup = PropertyManager.getProperty("setup");
	if( setup != null && setup.equals("true") ) {
		error = true;
		errorMessage = "Yazd setup appears to have already been completed. If you'd like " +
			"to re-run this tool, delete the 'setup=true' property from your yazd.properties " +
			"file.";
	}
	
	// get parameters:
	boolean setProps = ParamUtils.getBooleanParameter(request,"setProps");
		
	if( !error && setProps ) {
		
		for( int i=0; i<propNames.length; i++ ) {
			paramPropVals[i] = ParamUtils.getParameter(request,propNames[i]);
			if( paramPropVals[i] == null ) {
				paramPropVals[i] = "";
			}
		}
		
		for( int i=0; i < paramPropVals.length; i++ ) {
			conProvider.setProperty(propNames[i], paramPropVals[i]);
		}
		
		//Now test the connection. In this version of the install tool we test the driver name to make sure
		//it can be loaded. In the future, we might not want to do this in the generic case because the setup
		//tool needs to handle any possible connection provider setup. Perhaps we'll have specialized pages 
		//for the common connection providers.
		Connection con = null;
		String driver = null;
		try {
			driver = ParamUtils.getParameter(request, "driver");
			Class.forName(driver);
		}
		catch (ClassNotFoundException cnfe) {
			error = true;
			errorMessage = "Could not load JDBC driver: " + driver + ". Be sure that the driver is in the " +
				"classpath of your application server and then press the back button to try again.";
		}
		
		if (!error) {
			//Set the connection provider.
			DbConnectionManager.setDbConnectionProvider(conProvider);
			
			try {
				con = DbConnectionManager.getConnection();
				if (con == null) {
					error = true;
					errorMessage = "A connection to the database could not be made. Press the back button to " +
						"ensure that all fields were filled in properly.";
				}
			}
			catch (Exception e) {
				error = true;
			}
			finally {
				try {
					con.close();
				} catch( Exception ignored ) {}
			}
		}		
		if (!error) {
			// redirect
			response.sendRedirect("setup3.jsp");
			return;
		}
	}
%>

<html>
<head>
	<title>Yazd Setup - Step 2</title>
		<link rel="stylesheet" href="style/global.css">
</head>

<body bgcolor="#FFFFFF" text="#000000" link="#0000FF" vlink="#800080" alink="#FF0000">

<img src="images/setup.gif" width="210" height="38" alt="Yazd Setup" border="0">
<hr size="0"><p>

<%
	if (!error) {
%>

<b>Setup Your Database Connection</b>

<ul>
	<font size="2">
	Yazd needs to connect to a database in order to function properly. Fill in
	the following fields with the connection information for your database. Note: you should already
	have completed the import of the Yazd database schema as outlined in the installation guide.
	<p>
	
	<form action="setup2.jsp" method="post">
	<input type="hidden" name="setProps" value="true">
		
	<table cellpadding="3" cellspacing="0" border="0">
	<% for( int i=0; i<propNames.length; i++ ) { 
			String prop = conProvider.getProperty(propNames[i]);
			String val = paramPropVals[i];
	%>
	<tr>
		<%	if( setProps ) { %>
				<td><font size="-1" color="#ff0000"><%= propNames[i] %></font></td>
		<%	} 
			else { %>
				<td><font size="-1"><%= propNames[i] %></font></td>
		<%	} %>
		<%
			if( val == null ) {
				val = PropertyManager.getProperty("DbConnectionDefaultPool."+propNames[i]);
				if( val == null ) {
					val = "";
				}
			}
		%>
		<td><input type="text" size="30" name="<%= propNames[i] %>" value="<%= val %>"></td>
		<td><font size="-1"><i><%= conProvider.getPropertyDescription(propNames[i]) %></i></font></td>
	</tr>
	<% } %>
	</table>

</ul>

<center>
<input type="submit" value="Make Connection">
</center>
</form>

<%
	} //end no error
	else {
%>
	<font color="Red">Error!</font>
	<p><font size=2>
	
	<%= errorMessage %>
	
	</font>
<%
	} //end error
%>

<p>
<hr size="0">
<center><font size="-1"><i>www.Yasna.com/yazd</i></font></center>
</font>
</body>
</html>


