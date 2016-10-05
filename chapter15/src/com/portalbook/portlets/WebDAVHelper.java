package com.portalbook.portlets;

import java.io.*;

import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.HttpURL;

import org.apache.webdav.lib.WebdavResource;

public class WebDAVHelper
{
    private WebdavResource resource = null;

    protected void openURL(String uri, String username, String password)
        throws HttpException, IOException
    {
        HttpURL url = new HttpURL(uri);

        if (resource == null)
        {
            url.setUserinfo(username, password);
            resource = new WebdavResource(url);
        }
        else
        {
            resource.close();
            resource.setHttpURL(url);
        }
    }

    protected void setPath(String path) throws WebDAVException
    {
        try
        {
            String collPath = fixPath(path);
            resource.setPath(collPath);
            if (!resource.exists())
            {
                throw new WebDAVException("Path does not exist.");
            }
        }
        catch (Exception e)
        {
            throw new WebDAVException(e.getMessage(), e);
        }
    }

    protected String getParentPath(String path)
    {
        path = fixPath(path);

        //for the root and any paths right beneath it.
        if (path.lastIndexOf('/') == path.indexOf('/', 1))
        {
            return path;
        }

        //our paths have a trailing slash		
        int trailingSlashIndex = path.lastIndexOf('/');
        String cleanedPath = path.substring(0, trailingSlashIndex);

        int lastSlashIndex = cleanedPath.lastIndexOf('/');
        String parentPath = cleanedPath.substring(0, lastSlashIndex + 1);

        System.out.println("parent path: " + parentPath);
        return parentPath;

    }

    protected String fixPath(String path)
    {
        if (!path.startsWith("/"))
        {
            path = resource.getPath() + "/" + path;
        }

        //clean up any double slashes
        path = path.replaceAll("//", "/");

        return path;
    }

    public WebdavResource getResource()
    {
        return resource;
    }

}
