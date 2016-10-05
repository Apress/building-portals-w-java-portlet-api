package com.portalbook.portlets;


public class WebDAVException extends Exception
{
	public WebDAVException (String msg)
	{
		super(msg);
	}
	public WebDAVException (String msg, Throwable t)
	{
		super(msg,t);
	}	
}
