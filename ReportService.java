/**
 * 
 */
package com.exilant.itap.spagobi.service;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.Map;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.eclipse.birt.report.engine.api.EngineException;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.bind.annotation.RequestMethod;

import com.exilant.itap.spagobi.controller.SaveReportSpago;

/**
 * @author gupteshwari.sahu
 *
 */
@Service
public class ReportService {

	static FileOutputStream output = null;

	@GET
	@RequestMapping(value = "/reports", method = RequestMethod.GET)
	@Path("/getReports/{reportName}/{ID}")
	@Produces(MediaType.APPLICATION_XML)
	public static Object main(@PathParam("reportName") String reportName,
			@PathParam("ID") long id) throws EngineException {

		Object str = null;
		SaveReportSpago spago = new SaveReportSpago();
		ModelAndView data = new ModelAndView("Reports", "spago",
				spago.saveReport("html", reportName, id));
		Map<String, Object> modelMap = data.getModelMap();
		str = "<HTML>" + data.getModelMap().get("spago") + "</HTML>";

		/**
		 * For saving the response in file ,before running code add the file in
		 * the path where you want to save the response.
		 * 
		 * 
		 * BufferedWriter bufferedWriter = null; try { System.out.println("1");
		 * String strContent = str.toString(); File myFile = new File(
		 * "/absolutepath name for file "
		 * ); System.out.println("2");
		 * 
		 * Writer writer = new FileWriter(myFile); System.out.println("5");
		 * bufferedWriter = new BufferedWriter(writer); System.out.println("6");
		 * bufferedWriter.write(strContent); System.out.println("7 status :" +
		 * strContent); } catch (IOException e) {
		 * System.out.println("Error occured while creating the file"); }
		 * finally{ try{ if(bufferedWriter != null) bufferedWriter.close(); }
		 * catch(Exception ex){
		 * System.out.println("Error occured while creating the file"); }
		 * 
		 * }
		 **/
		System.out.println(data.getModelMap().get("spago"));
		return (str);
	}

}
