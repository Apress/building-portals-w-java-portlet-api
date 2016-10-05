package com.portalbook.charting;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartUtilities;
import org.jfree.chart.JFreeChart;

import org.jfree.chart.plot.PlotOrientation;

import org.jfree.data.CategoryDataset;
import org.jfree.data.DefaultCategoryDataset;

import java.io.*;

public class SimpleChartDemo
{

    protected CategoryDataset createChartData()
    {
        DefaultCategoryDataset dataset = new DefaultCategoryDataset();

        dataset.addValue(1.3, "2004", "January");
        dataset.addValue(2.6, "2004", "February");
        dataset.addValue(4.6, "2004", "March");


        return dataset;
    }

    protected JFreeChart createBarChart3D(CategoryDataset dataset)
    {
        String title = "Weather in Austin, Texas";
        JFreeChart chart =
            ChartFactory.createBarChart3D(
                title,
                "Months in 2004",
                "Number of Sunny Days",
                dataset,
                PlotOrientation.VERTICAL,
                true,
                false,
                false);

        return chart;

    }

    protected void saveChartAsPNG(JFreeChart chart) throws IOException
    {
        File file = new File("barchart3d.png");
        ChartUtilities.saveChartAsPNG(file, chart, 800, 600);
    }

    public static void main(String[] args)
    {
        SimpleChartDemo demo = new SimpleChartDemo();
        CategoryDataset dataset = demo.createChartData();
        JFreeChart chart = demo.createBarChart3D(dataset);

        try
        {
            demo.saveChartAsPNG(chart);
        }
        catch (IOException e)
        {
            System.out.println(e.getMessage());
            e.printStackTrace();
        }

    }
}
