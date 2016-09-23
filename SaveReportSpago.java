/**
 *  
 */
package *.spagobi.controller;

import java.io.*;

import org.eclipse.birt.core.framework.Platform;
import org.eclipse.birt.report.engine.api.EngineConfig;
import org.eclipse.birt.report.engine.api.EngineException;
import org.eclipse.birt.report.engine.api.HTMLRenderOption;
import org.eclipse.birt.report.engine.api.HTMLServerImageHandler;
import org.eclipse.birt.report.engine.api.IReportEngine;
import org.eclipse.birt.report.engine.api.IReportEngineFactory;
import org.eclipse.birt.report.engine.api.IReportRunnable;
import org.eclipse.birt.report.engine.api.IRunAndRenderTask;
import org.eclipse.core.internal.registry.RegistryProviderFactory;
import org.springframework.stereotype.Controller;

/**
 * @author gupteshwari.sahu
 *
 */
@Controller
public class SaveReportSpago {
	HTMLRenderOption options = new HTMLRenderOption();

	public String saveReport(String format, String ReportName, Long ID)
			throws EngineException {
		IReportEngine engine = null;
		EngineConfig config = null;

		/**
		 * Change the value in the XML file
		 */
		SetParamsInputControl pojo = new SetParamsInputControl();
		File fXmlFile = pojo.main(ReportName, ID);
		try {
			
			// start up Platform
			config = new EngineConfig();
			config.setLogConfig("/logs", java.util.logging.Level.FINEST);
			Platform.startup(config);

			// create new Report Engine
			IReportEngineFactory factory = (IReportEngineFactory) Platform
					.createFactoryObject(IReportEngineFactory.EXTENSION_REPORT_ENGINE_FACTORY);
			engine = factory.createReportEngine(config);

			// open the report design
			IReportRunnable design = null;
			design = engine.openReportDesign(fXmlFile.getAbsolutePath());

			// create RunandRender Task
			IRunAndRenderTask task = engine.createRunAndRenderTask(design);

			config = new EngineConfig();
			config.setLogConfig("/logs", java.util.logging.Level.FINEST);
			Platform.startup(config);

			task.setParameterValue("myParam", format);
			task.validateParameters();
			task.setParameterValue("value", ID);
			ByteArrayOutputStream outs = new ByteArrayOutputStream();

			// set render options including output type
			if (format.equals("html")) {
				System.out.println("for saving the file in folder");
				options.setOutputStream(outs);
				options.setImageHandler(new HTMLServerImageHandler());
				options.setBaseImageURL("images");
				options.setEmbeddable(true);
				options.setOutputFormat(format);
				task.setRenderOption(options);
			}

			// run task
			String output;
			task.run();
			output = outs.toString();
			System.out.println(" Report task Done");
			task.close();
			engine.destroy();
			Platform.shutdown();
			RegistryProviderFactory.releaseDefault();
			engine = null;
			return output;
		} catch (Exception ex) {
			ex.printStackTrace();
			String str = "Error Occured";
			return str;
		} finally {
			Platform.shutdown();
			RegistryProviderFactory.releaseDefault();
		}
	}

	


}
