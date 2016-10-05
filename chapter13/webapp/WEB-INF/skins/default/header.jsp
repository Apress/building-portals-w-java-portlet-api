
<%
/**
 *	$RCSfile: header.jsp,v $
 *	$Revision: 1.1.1.1 $
 *	$Date: 2002/04/26 23:50:08 $
 */
%>

<%@ page import="java.util.Date,
                 java.text.SimpleDateFormat" %>

<%!	////////////////////////
	// global page variables
	
	// date formatter for today's date
	private final SimpleDateFormat todayDateFormatter = 
		new SimpleDateFormat("MMMM d");
%>

<html>
<head>
	<title><%= title %></title>
	<link rel="stylesheet" href="style/global.css">
</head>

<%-- customize the background colors, links, etc for the entire skin here: --%>
<body bgcolor="#FFFFFF" text="#000000" link="#0000FF" vlink="#800080" alink="#FF0000">

<%	/////////////////////
	// header variables
	
	// change these values to customize the look of your header
	
	// Colors
	String headerBgcolor = "#000000";
	String headerFgcolor = "#ffffff";
	
	// Header text
	String headerText = "PaperStack demonstration discussion forums";
%>

<table bgcolor="<%= headerBgcolor %>" cellspacing="0" cellpadding="1" width="100%" border="0">
   <td>
      <table bgcolor="<%= headerFgcolor %>" cellspacing="0" cellpadding="0" width="100%" border="0">
         <td width="1%" class="banner">
            PaperStack
         </td>
         <td width="98%" align="center">
            <span class="headerText"><%= headerText %></span>
         </td>
         <td width="1%" nowrap>
            <span class="headerDate"><%= todayDateFormatter.format( new Date() ) %></span>&nbsp;
         </td>
      </table>
   </td>
</table>
<p>