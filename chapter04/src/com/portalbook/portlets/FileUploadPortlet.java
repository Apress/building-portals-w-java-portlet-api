package com.portalbook.portlets;
import org.apache.commons.fileupload.PortletDiskFileUpload;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.FileItem;
import javax.portlet.ActionRequest;
import javax.portlet.ActionResponse;
import javax.portlet.GenericPortlet;
import javax.portlet.PortletException;
import javax.portlet.PortletURL;
import javax.portlet.RenderRequest;
import javax.portlet.RenderResponse;
import java.io.IOException;
import java.io.Writer;
import java.io.File;
import java.util.*;
public class FileUploadPortlet extends GenericPortlet
{
    public static final String ERROR_NO_FILE = "ERROR_NO_FILE";
    
    public void doView(RenderRequest request, RenderResponse response)
        throws PortletException, IOException
    {
        response.setContentType("text/html");
        
        Writer writer = response.getWriter();
        
        String error = request.getParameter("error");
        String size = request.getParameter("size");
        String contentType = request.getParameter("contentType");
        String serverFileName = request.getParameter("serverFileName");
        String param1 = request.getParameter("param1");
        
        if (ERROR_NO_FILE.equals(error))
        {
            writer.write("Expected to process an uploaded file.<P>");
        }
        else if (error != null)
        {
            writer.write(error + "<P>");
        }
        if (serverFileName != null)
        {
            //portlet upload was a success if serverName is set
            writer.write("File Size: " + size + "<BR>");
            writer.write("Content Type: " + contentType + "<BR>");
            writer.write("File Name on Server: " + serverFileName + "<BR>");
        }
        if (param1 != null)
        {
            writer.write("Parameter 1: " + param1);
        }
        
        PortletURL actionURL = response.createActionURL();
        
        writer.write(
            "<form method='post' enctype='multipart/form-data'");
        writer.write(" action=' " + actionURL.toString() + "'>");
        writer.write("Upload File: <input type='file' name='fileupload'>");
        writer.write(
            "<br>Parameter 1: <input type='text' name='param1' size='30'>");
        writer.write("<br><input type='submit'>");
        writer.write("</form>");
    }
    public void processAction(ActionRequest request, ActionResponse response)
        throws PortletException, IOException
    {
        // Check the request content type to see if it starts with multipart/
        if (!PortletDiskFileUpload.isMultipartContent(request))
        {
            //set an error message
            response.setRenderParameter("error", ERROR_NO_FILE);
            return;
        }
        
        PortletDiskFileUpload dfu = new PortletDiskFileUpload();
        
        //maximum allowed file upload size (10 MB)
        dfu.setSizeMax(10 * 1000 * 1000);
        
        //maximum size in memory (vs disk) (100 KB)
        dfu.setSizeThreshold(100 * 1000);
        
        try
        {
            //get the FileItems
            List fileItems = dfu.parseRequest(request);
            Iterator iter = fileItems.iterator();
            while (iter.hasNext())
            {
                FileItem item = (FileItem) iter.next();
                if (item.isFormField())
                {
                    //pass along to render request
                    String fieldName = item.getFieldName();
                    String value = item.getString();
                    response.setRenderParameter(fieldName, value);
                }
                else
                {
                    //write the uploaded file to a new location
                    String fieldName = item.getFieldName();
                    String fileName = item.getName();
                    String contentType = item.getContentType();
                    long size = item.getSize();
                    response.setRenderParameter("size", Long.toString(size));
                    response.setRenderParameter("contentType", contentType);
                    String tempDir = System.getProperty("java.io.tmpdir");
                    String serverFileName = fieldName + "-portlet.tmp";
                    File serverFile = new File(tempDir, serverFileName);
                    item.write(serverFile);
                    response.setRenderParameter("serverFileName",
                        serverFileName);
                    getPortletContext().log(
                        "serverFileName : " + tempDir + "/" + serverFileName);
                }
            }
        }
        catch (FileUploadException fue)
        {
            String msg = "File Upload Exception: " + fue.getMessage();
            response.setRenderParameter("error", msg);
            getPortletContext().log(msg, fue);
        }
        catch (Exception e)
        {
            String msg = "Exception: " + e.getMessage();
            response.setRenderParameter("error", msg);
            getPortletContext().log(msg, e);
        }
    }
}
