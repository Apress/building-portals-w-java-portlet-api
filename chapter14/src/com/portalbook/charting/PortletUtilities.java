/* 
 *
 * This library is free software; you can redistribute it and/or modify it under the terms
 * of the GNU Lesser General Public License as published by the Free Software Foundation;
 * either version 2.1 of the License, or (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY;
 * without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 * See the GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License along with this
 * library; if not, write to the Free Software Foundation, Inc., 59 Temple Place, Suite 330,
 * Boston, MA 02111-1307, USA.
 *
 * [Java is a trademark or registered trademark of Sun Microsystems, Inc. 
 * in the United States and other countries.]
 * 
 * Based on the org/jfree/chart/servlet/ServletUtilities class, which is copyright 2002-2004, 
 * by Richard Atkinson and Contributors.
 * 
 * Changed April 11, 2004 by Jeff Linwood 
 */

package com.portalbook.charting;

import java.io.File;
import java.io.IOException;

import javax.portlet.PortletSession;

import org.jfree.chart.ChartRenderingInfo;
import org.jfree.chart.ChartUtilities;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.servlet.ChartDeleter;
import org.jfree.chart.servlet.ServletUtilities;


public class PortletUtilities extends ServletUtilities
{
	/**
	 * Saves the chart as a PNG format file in the temporary directory.
	 *
	 * @param chart  the JFreeChart to be saved.
	 * @param width  the width of the chart.
	 * @param height  the height of the chart.
	 * @param session  the HttpSession of the client.
	 *
	 * @return the filename of the chart saved in the temporary directory.
	 *
	 * @throws IOException if there is a problem saving the file.
	 */	
	public static String saveChartAsPNG(JFreeChart chart, int width, int height,
										PortletSession session) throws IOException {

		return PortletUtilities.saveChartAsPNG(chart, width, height, null, session);
        
	}

	/**
	 * Saves the chart as a PNG format file in the temporary directory and
	 * populates the ChartRenderingInfo object which can be used to generate
	 * an HTML image map.
	 *
	 * @param chart  the chart to be saved (<code>null</code> not permitted).
	 * @param width  the width of the chart.
	 * @param height  the height of the chart.
	 * @param info  the ChartRenderingInfo object to be populated (<code>null</code> permitted).
	 * @param session  the PortletSession of the client.
	 *
	 * @return the filename of the chart saved in the temporary directory.
	 *
	 * @throws IOException if there is a problem saving the file.
	 */	
	public static String saveChartAsPNG(JFreeChart chart, int width, int height,
										ChartRenderingInfo info, PortletSession session)
			throws IOException 
	{
		if (chart == null) {
			throw new IllegalArgumentException("Null 'chart' argument.");   
		}
		ServletUtilities.createTempDir();
		File tempFile = File.createTempFile(
			PortletUtilities.getTempFilePrefix(), ".png", 
			new File(System.getProperty("java.io.tmpdir"))
		);
		ChartUtilities.saveChartAsPNG(tempFile, chart, width, height, info);
		PortletUtilities.registerChartForDeletion(tempFile, session);
		return tempFile.getName();
	}
	
	
	/**
	 * Adds a ChartDeleter object to the session object with the name JFreeChart_Deleter
	 * if there is not already one bound to the session and adds the filename to the
	 * list of charts to be deleted.
	 *
	 * @param tempFile  the file to be deleted.
	 * @param session  the portlet session of the client.
	 */	
	protected static void registerChartForDeletion(File tempFile, PortletSession session) {

		//  Add chart to deletion list in session
		if (session != null) {
			ChartDeleter chartDeleter = (ChartDeleter) session.getAttribute("JFreeChart_Deleter", PortletSession.APPLICATION_SCOPE);
			if (chartDeleter == null) {
				chartDeleter = new ChartDeleter();
				session.setAttribute("JFreeChart_Deleter", chartDeleter, PortletSession.APPLICATION_SCOPE);
			}
			chartDeleter.addChart(tempFile.getName());
			System.out.println(tempFile.getName());
		}
		else {
			System.out.println("Session is null - chart will not be deleted");
		}
	}
}
