package com.portalbook.portlets;

import java.util.*;

import javax.portlet.PortletPreferences;
import javax.portlet.PreferencesValidator;
import javax.portlet.ValidatorException;

public class ExamplePreferencesValidator implements PreferencesValidator
{
    public void validate(PortletPreferences prefs) throws ValidatorException
    {
        LinkedList failedKeys = new LinkedList();
        Enumeration names = prefs.getNames();
        while (names.hasMoreElements())
        {
            String name = (String) names.nextElement();
            String value = prefs.getValue(name, "");
            if (value.length() < 4)
            {
                failedKeys.add(name);
            }
        }
        if (failedKeys.size() > 0)
        {
            String errMsg =
                "Preference values must be at least 4 characters long";
            throw new ValidatorException(errMsg, failedKeys);
        }

    }
}
