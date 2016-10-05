package com.portalbook.portlets;

import java.io.IOException;
import java.io.PrintWriter;

import javax.portlet.GenericPortlet;
import javax.portlet.PortletException;
import javax.portlet.PortletSession;
import javax.portlet.RenderRequest;
import javax.portlet.RenderResponse;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.JFreeChart;
import org.jfree.data.DatasetUtilities;
import org.jfree.data.DefaultPieDataset;
import org.jfree.data.PieDataset;

import com.portalbook.charting.PortletUtilities;

public class ChartingPortlet extends GenericPortlet
{
    public void doView(RenderRequest request, RenderResponse response)
        throws PortletException, IOException
    {

        response.setContentType("text/html");
        PrintWriter writer = response.getWriter();

        PortletSession session = request.getPortletSession();

        PieDataset dataset = createDataset();

        //Lower the number of entries in the pie table by combining
        //the ones that fall under a 2% threshold into "Other"
        dataset = DatasetUtilities.limitPieDataset(dataset, 0.02);

        JFreeChart chart = createChart(dataset);

        String filename =
            PortletUtilities.saveChartAsPNG(chart, 400, 300, session);

        writer.write("<H1>IT Expenditures Chart</H1>");
        String chartServlet =
            request.getContextPath() + "/servlet/DisplayChart";
        writer.write(
            "<IMG SRC='" + chartServlet + "?filename=" + filename + "'");

    }

    protected PieDataset createDataset()
    {
        DefaultPieDataset dataset = new DefaultPieDataset();
        dataset.setValue("Software", 65.2);
        dataset.setValue("Services", 20.1);
        dataset.setValue("Hardware", 17.3);
        dataset.setValue("Network", 18.9);
        dataset.setValue("Recruiting", 1.3);
        dataset.setValue("Training", 2.8);
        dataset.setValue("Turnover", 1.2);
        dataset.setValue("Corporate Initiatives", 0.5);

        return dataset;
    }

    protected JFreeChart createChart(PieDataset dataset)
    {
        JFreeChart chart = null;
        String title = "IT Expenditures 2003";
        chart =
            ChartFactory.createPieChart3D(title, dataset, true, false, false);
        return chart;
    }

}
