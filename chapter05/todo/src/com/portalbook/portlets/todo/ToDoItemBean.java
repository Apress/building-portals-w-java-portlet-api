package com.portalbook.portlets.todo;

import java.util.Date;

public class ToDoItemBean
{
    private String description;
    private int priority;
    private Date submittedDate;
    private boolean status;

    public String getDescription()
    {
        return description;
    }

    public int getPriority()
    {
        return priority;
    }

    public void setDescription(String string)
    {
        description = string;
    }

    public void setPriority(int i)
    {
        priority = i;
    }

    public boolean getStatus()
    {
        return status;
    }

    public void setStatus(boolean b)
    {
        status = b;
    }

    public Date getSubmittedDate()
    {
        return submittedDate;
    }

    public void setSubmittedDate(Date date)
    {
        submittedDate = date;
    }

}