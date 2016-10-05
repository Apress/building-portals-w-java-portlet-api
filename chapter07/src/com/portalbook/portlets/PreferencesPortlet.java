package com.portalbook.portlets;

import java.io.IOException;
import java.io.Writer;
import java.util.Enumeration;

import javax.portlet.ActionRequest;
import javax.portlet.ActionResponse;
import javax.portlet.GenericPortlet;
import javax.portlet.PortletException;
import javax.portlet.PortletPreferences;
import javax.portlet.PortletURL;
import javax.portlet.RenderRequest;
import javax.portlet.RenderResponse;

public class PreferencesPortlet extends GenericPortlet
{
    protected void doView(RenderRequest request, RenderResponse response)
        throws PortletException, IOException
    {
        response.setContentType("text/html");

        Writer writer = response.getWriter();

        writer.write("<H2>Portlet Preferences</H2>");

        //get the user's preferences
        PortletPreferences prefs = request.getPreferences();

        //Write the preferences out
        writer.write("<P>The user's current preferences:");
        Enumeration names = prefs.getNames();

        if (!names.hasMoreElements())
        {
            writer.write("<P>There are no preferences defined for this portlet.");
        }

        while (names.hasMoreElements())
        {
            String name = (String) names.nextElement();
            String value = prefs.getValue(name, "defaultValue");
            writer.write("<P>Name: " + name);
            writer.write("<BR>Value:" + value);
        }

        //create a form so preferences can be created
        writer.write("<P><H2>Set Preferences</H2><P>");
        PortletURL url = response.createActionURL();
        writer.write("<FORM METHOD='POST' ACTION='" + url + "'>");
        writer.write(
            "Preference 1: <INPUT NAME='pref1' TYPE='text' SIZE='30'><P>");
        writer.write(
            "Preference 2: <INPUT NAME='pref2' TYPE='text' SIZE='30'><P>");
        writer.write("<INPUT TYPE='submit'>");
        writer.write("</FORM>");

    }

    public void processAction(ActionRequest request, ActionResponse response)
        throws PortletException, IOException
    {
        //get the preference values from the request
        String pref1 = request.getParameter("pref1");
        String pref2 = request.getParameter("pref2");

        PortletPreferences prefs = request.getPreferences();

        //set the preferences
        prefs.setValue("pref1", pref1);
        prefs.setValue("pref2", pref2);

        //store the preferences
        prefs.store();
    }
}
