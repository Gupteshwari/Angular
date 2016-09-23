/**
 * 
 */
package com.exilant.itap.spagobi.controller;

import java.io.File;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.springframework.stereotype.Controller;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.w3c.dom.Node;
import org.w3c.dom.Element;

/**
 * @author gupteshwari.sahu
 *
 */
@Controller
@SuppressWarnings("unused")
public class SetParamsInputControl {


	public File main(String ReportName, Long AppID) {

		// Finding the path of the Report folder
		String path = SetParamsInputControl.class.getClassLoader().getResource("")
		.getPath();
		path = path.replace("classes", "Reports");
		File fXmlFile = new File(path + ReportName);
		File NewXML = null;
		
		try {

			// Reading the XML File
			DocumentBuilderFactory dbFactory = DocumentBuilderFactory
					.newInstance();
			DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
			Document doc = dBuilder.parse(fXmlFile);

			doc.getDocumentElement().normalize();
			
			// Changing the value in XML File

			NodeList nList = doc.getElementsByTagName("scalar-parameter");

			for (int temp = 0; temp < nList.getLength(); temp++) {

				Node nNode = nList.item(temp);
				String str = "";

				if (nNode.getNodeType() == Node.ELEMENT_NODE) {
					Element eElement = (Element) nNode;
					  if(ReportName == "Test_Case_Details.rptdesign") {
						str="appID_for_TestCase_param";
					} else if(ReportName=="Test_Bed_Report.rptdesign") {
						str= "appID";
					}else if(ReportName== "Automation_Test_Report.rptdesign") {
						 str="appID";
					}
					

					if (eElement.getAttribute("name").equals(str)) {
						String textChanged="";
						System.out.println(" old expression : "
								+ eElement.getElementsByTagName("value")
										.item(0).getTextContent());
						Node x = eElement.getElementsByTagName("value").item(0)
								.getChildNodes().item(0);
						textChanged += x.getNodeValue() + "<br>";
						x.setNodeValue(String.valueOf(AppID));
						System.out.println(" new expression : "
								+ eElement.getElementsByTagName("value")
										.item(0).getTextContent());

					}

				}
			}

			// Saving the xml file with new name

			TransformerFactory transformerFactory = TransformerFactory
					.newInstance();
			Transformer transformer = transformerFactory.newTransformer();
			DOMSource source = new DOMSource(doc);
			NewXML = new File(path+ "New" + ReportName);
			StreamResult streamResult = new StreamResult(NewXML);
			transformer.transform(source, streamResult);
			System.out.println("Done with value changes");
			/**
			 * END
			 */
		} catch (Exception e) {
			e.printStackTrace();
		}
		return NewXML;
	}
}
