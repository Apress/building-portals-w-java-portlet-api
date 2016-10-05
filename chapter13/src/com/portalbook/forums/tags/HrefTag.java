package com.portalbook.forums.tags;

import java.io.IOException;

import javax.portlet.PortletRequest;
import javax.portlet.PortletResponse;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.TagSupport;

public class HrefTag extends TagSupport
{

	/**
	 * Getter for the attribute used to dictate
	 * the path to be rewritten
	 * 
	 * @param path The path to be rewritten as an absolute URL
	 */
	public void setPath(String path)
	{
		this.path = path;
	}

	/**
	 * Retrieves the attribute used to dictate
	 * the path to be rewritten
	 * 
	 * @return The path to be rewritten
	 */
	public String getPath()
	{
		return this.path;
	}

	/**
	 * Ignores the body of the tag (there shouldn't be one) 
	 * and generates an absolute URL for the provided path
	 * attribute relative to the context in which this
	 * portlet is running.
	 * 
	 * @return SKIP_BODY
	 * @throws JspException if the output stream cannot be written
	 */
	public int doStartTag() 
	   throws JspException
	{
		try
		{
			String contextPath = ((PortletRequest) pageContext
					.getRequest()).getContextPath();
			String absolutePath = ((PortletResponse) pageContext
					.getResponse()).encodeURL(contextPath + "/" + getPath());
			pageContext.getServletContext().log("Path: " + path);
			pageContext.getServletContext().log(
					"Context path: " + contextPath);
			pageContext.getServletContext().log(
					"Absolute path: " + absolutePath);
			pageContext.getOut().print(absolutePath);
			return SKIP_BODY;
		}
		catch (IOException e)
		{
			throw new JspException(
					"Could not write to the page buffer while expanding an href custom tag",
					e);
		}
	}

	// The field to store the path attribute
	private String path;
}