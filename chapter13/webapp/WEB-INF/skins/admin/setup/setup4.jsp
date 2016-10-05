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
		
<% try { %>
				 
<%	boolean setupError = false;
	String errorMessage = "";
	//Make sure the install has not already been completed.
	String setup = PropertyManager.getProperty("setup");
	if( setup != null && setup.equals("true") ) {
%>
	<html>
<head>
	<title>Yazd Setup - Step 4</title>
		<link rel="stylesheet" href="style/global.css">
</head>

<body bgcolor="#FFFFFF" text="#000000" link="#0000FF" vlink="#800080" alink="#FF0000">

<img src="images/setup.gif" width="210" height="38" alt="Yazd Setup" border="0">
<hr size="0"><p>

	<font color="Red">Error!</font>
	<p><font size=2>
	
	Yazd setup appears to have already been completed. If you'd like to re-run 
	this tool, delete the 'setup=true' property from your yazd.properties file.
	
	</font>
		
<%	
	}
	else {
	
		boolean error = false;
		String yazdHome = ParamUtils.getParameter(request,"yazdHome");
		if (yazdHome == null) {
		yazdHome = PropertyManager.getProperty("yazdHome");
		}
		boolean setYazdHome = ParamUtils.getBooleanParameter(request,"setYazdHome");
		//Look for error case, but only give a new error message if there isn't
		//already an error.
		if(setYazdHome && yazdHome == null ) {
			error = true;
			errorMessage = "No value was entered for Yazd Home. Please enter a path.";
		}
	%>
	<%	if( !error && setYazdHome ) {
			// chomp a trailing "/" or "\\"
			while( yazdHome.length() > 0 
					&& yazdHome.charAt(yazdHome.length()-1) == '/'
					|| yazdHome.charAt(yazdHome.length()-1) == '\\' )
			{
				yazdHome = yazdHome.substring(0,yazdHome.length()-1);
			}
			// check if the app server can write to that file
			File yazdHomeDir = new File(yazdHome);
			error = !yazdHomeDir.exists();
			if( error ) {
				errorMessage = "The directory you entered doesn't exist. Be sure to " +
					"create the Yazd Home directory on your filesystem, and then try again.";
			} else {
				error = !yazdHomeDir.canRead();
				if( error ) {
					errorMessage = "The directory you entered exists, but you don't "+
						"have read access for it. Please fix the problem and try again.";
				} else {
					error = !yazdHomeDir.canWrite();
					if( error ) {
						errorMessage = "The directory you entered exists, buy you " +
							"don't have have write access for it. Please fix the " +
							"problem and try again.";
					} else {
						error = !yazdHomeDir.isAbsolute();
						errorMessage = "You didn't enter an absolute path for the Yazd Home " +
						 	"directory (e.g., starting the path with '/' in Unix, or 'c:\' " +
							"in Windows). Please fix the problem and try again.";
					}
				}
			}
			
			// at this point, the path exists and we can read & write to it
			// so create the file
			if( !error ) {
				// create the search directory
				File searchDir = new File( yazdHome + File.separator + "search" );
				if (!searchDir.exists()) {
					searchDir.mkdir();
				}
				// set the yazdHome property in the yazd.properties file
				PropertyManager.setProperty("yazdHome",yazdHome);
				// redirect
				response.sendRedirect("setup5.jsp");
				return;
			}
		}
%>


<html>
<head>
	<title>Yazd Setup - Step 4</title>
		<link rel="stylesheet" href="style/global.css">
</head>

<body bgcolor="#FFFFFF" text="#000000" link="#0000FF" vlink="#800080" alink="#FF0000">

<img src="images/setup.gif" width="210" height="38" alt="Yazd Setup" border="0">
<hr size="0"><p>

<b>Yazd Home Directory</b>

<ul>

<font size="2">
	Yazd needs a place to store data on your filesystem. The directory where this
	data is stored is called "Yazd Home". This step in the setup tool will help
	you create this directory.
	<p>
	First decide where you'd like Yazd Home to exist. This could be "/usr/local/yazdHome"
	in Unix, or "c:\yazdHome" in Windows. Naming the directory "yazdHome" is not
	required, but is recommended. After creating the directory, make sure that
	your application server has read and write access to it. Now, enter the full
	path to the directory you created below.<p>

<%
	if (error) {
%>

	<font color="Red">Error:</font></font>	<i><%= errorMessage %></i>

	<p>

<%	} %>
	
	
	<form action="setup4.jsp" method="post">
	<input type="hidden" name="setYazdHome" value="true">
		
	<table cellpadding="3" cellspacing="0" border="0">
	<tr>
		<td><font size="-1">Yazd Home:</font></td>
		<td><input type="text" size="50" name="yazdHome" value="<%= (yazdHome!=null)?yazdHome:"" %>"></td>
	</tr>
	</table>

</ul>

<center>
<input type="submit" value="Continue">
</center>
</form>

<% } //end else of setupError %>


<p>
<hr size="0">
<center><font size="-1"><i>www.Yasna.com/yazd</i></font></center>
</font>
</body>
</html>

<%	} catch (Exception e ) {
		e.printStackTrace();
	}
%>
