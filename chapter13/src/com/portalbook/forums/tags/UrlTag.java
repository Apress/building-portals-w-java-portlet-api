package com.portalbook.forums.tags;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.BodyContent;
import javax.servlet.jsp.tagext.BodyTagSupport;
import javax.portlet.PortletMode;
import javax.portlet.PortletModeException;
import javax.portlet.RenderResponse;
import javax.portlet.PortletURL;
import javax.portlet.WindowState;
import javax.portlet.WindowStateException;

public class UrlTag extends BodyTagSupport
{

	/**
	 * Getter for the scripting variable attribute
	 * @return The name of the scripting variable
	 */
	public String getVar()
	{
		return var;
	}

	/**
	 * The getter for the mode attribute
	 * @return The name of the mode to select
	 */
	public String getMode()
	{
		return mode;
	}

	/**
	 * The getter for the state attribute
	 * @return The name of the window state to select
	 */
	public String getState()
	{
		return state;
	}

	/**
	 * The setter for the scripting variable attribute
	 * @param var The name of the scripting variable
	 */
	public void setVar(String var)
	{
		this.var = var;
	}

	/**
	 * The setter for the mode attribute
	 * @param mode The name of the mode to select
	 */
	public void setMode(String mode)
	{
		this.mode = mode;
	}

	/**
	 * The setter for the state attribute
	 * @param state The name of the state to select
	 */
	public void setState(String state)
	{
		this.state = state;
	}

	/**
	 * Determines an initial PortletURL object to refer to
	 * the current portlet context. The mode and state
	 * attributes are then extracted and applied to the
	 * PortletURL.
	 * 
	 * EVAL_BODY_BUFFERED is returned so that the link to
	 * rewrite can be determined
	 * 
	 * @return EVAL_BODY_BUFFERED 
	 * @throws JspException Thrown if the desired mode or state cannot be set.
	 */
	public int doStartTag() throws JspException
	{
		// Get the URL representing the portlet
		PortletURL url = ((RenderResponse) pageContext.getResponse())
				.createRenderURL();

		// Get the desired mode and state (if not default) from
		// the attributes
		PortletMode mode = getModeFromModeName(getMode());
		WindowState state = getStateFromStateName(getState());

		// Set the mode and state
		try
		{
			if (mode != null)
				url.setPortletMode(mode);
			if (state != null)
				url.setWindowState(state);
		}
		catch (PortletModeException e)
		{
			throw new JspException("Could not set portlet mode in url: "
					+ mode);
		}
		catch (WindowStateException e)
		{
			throw new JspException("Could not set portlet state in url: "
					+ state);
		}

		// Make the URL available as a page variable if
		// the user has requested this.
		if (getVar() != null)
		{
			pageContext.setAttribute(getVar(), url);
		}

		// Evaluate the body (for the link)
		return EVAL_BODY_BUFFERED;
	}

	/**
	 * Retrieves the PortletURL created in the start tag
	 * and adds to it the HREF and QUERY parts of the 
	 * desired path. The path is split into two parts because
	 * the Pluto portlet container (erroneously ?) does not
	 * encode the '?' part of the parameter. This must therefore
	 * be removed from the parameter data - since otherwise it
	 * will be included in the URL when used to invoke the 
	 * ForumPortlet and the ForumPortlet will only receive 
	 * those parts of the invoking URL prior to the '?' 
	 *  
	 * Returns SKIP_BODY to indicate that the tag has 
	 * completed processing of the body.
	 * 
	 * @return SKIP_BODY 
	 * @throws JspException Thrown if the link cannot be retrieved from the body.
	 */
	public int doAfterBody() throws JspException
	{
		PortletURL url = (PortletURL) pageContext.getAttribute(getVar());

		// Retrieve the content of the tag
		BodyContent body = getBodyContent();
		if (body == null)
		{
			throw new JspException("No body (link) provided in tag");
		}

		// Check that we've got a path !
		String path = body.getString();
		if (path == null)
		{
			throw new JspException("No path (link) provided in tag");
		}

		pageContext.getServletContext().log("UrlTag path=" + path);

		// Determine the href part and the query part
		// of the given relative (hopefully) URL.
		int splitAt = path.indexOf('?');

		// The HREF part is from the beginning of the string upto, but
		// not including the '?' (or the whole string if there's no '?').
		String href = (splitAt > 0) ? path.substring(0, splitAt) : path;

		// The QUERY part is from immediately AFTER the '?' to the end
		// of the string (or an empty string if there's no '?')
		String query = (splitAt > 0) ? path.substring(splitAt + 1) : "";

		// Add the parameters to the PortletURL
		// ready for the user to obtain them.
		url.setParameter(HREF, href);
		url.setParameter(QUERY, query);

		// We're done processing the body
		return SKIP_BODY;
	}

	/**
	 * Converts the state name as a String into
	 * the WindowState object representing it
	 * 
	 * @param name The name of the state
	 * @return A WindowState representing the named state
	 * @throws JspException Thrown if a non-standard state name is provided
	 */
	private WindowState getStateFromStateName(String name)
			throws JspException
	{
		if (name.equalsIgnoreCase(WindowState.MAXIMIZED.toString()))
		{
			return WindowState.MAXIMIZED;
		}
		else if (name.equalsIgnoreCase(WindowState.MINIMIZED.toString()))
		{
			return WindowState.MINIMIZED;
		}
		else if (name.equalsIgnoreCase(WindowState.NORMAL.toString()))
		{
			return WindowState.NORMAL;
		}
		else
		{
			// Tag can't handle non-standard states
			throw new JspException("Can't handle non-standard state: "
					+ name);
		}
	}

	/**
	 * Converts the mode name as a String into
	 * the PortletMode object representing it
	 * 
	 * @param name The name of the mode
	 * @return A PortletMode representing the named mode
	 * @throws JspException Thrown if a non-standard mode name is provided
	 */
	private PortletMode getModeFromModeName(String name)
			throws JspException
	{
		if (name.equalsIgnoreCase(PortletMode.EDIT.toString()))
		{
			return PortletMode.EDIT;
		}
		else if (name.equalsIgnoreCase(PortletMode.VIEW.toString()))
		{
			return PortletMode.VIEW;
		}
		else if (name.equalsIgnoreCase(PortletMode.HELP.toString()))
		{
			return PortletMode.HELP;
		}
		else
		{
			// Tag can't handle non-standard modes
			throw new JspException("Can't handle non-standard mode: "
					+ name);
		}
	}

	// The field to contain the mode
	// attribute (defaults to VIEW)
	private String mode = "view";

	// The field to contain the state
	// attribute (defaults to NORMAL)
	private String state = "normal";

	// The field to contain the name
	// of the scripting variable - this
	// is a required field.
	private String var = null;

	/**
	 * Defines the parameter name for the HREF part
	 * of a URL
	 */
	public static final String HREF = "href";

	/**
	 * Defines the parameter name for the QUERY part
	 * of a URL
	 */
	public static final String QUERY = "query";
}