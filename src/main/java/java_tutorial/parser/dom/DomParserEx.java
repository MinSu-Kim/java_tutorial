package java_tutorial.parser.dom;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

/**
 * DomParser
 * @author mskim
 * https://www.tutorialspoint.com/java_xml/java_dom_create_document.htm
 */
/*	
<class>
	<student rollno = "393">
		<firstname>dinkar</firstname>
		<lastname>kad</lastname>
		<nickname>dinkar</nickname>
		<marks>85</marks>
   </student>
	<student rollno = "493">
		<firstname>Vaneet</firstname>
		<lastname>Gupta</lastname>
		<nickname>vinni</nickname>
	<marks>95</marks>
	</student>

	<student rollno = "593">
		<firstname>jasvir</firstname>
		<lastname>singn</lastname>
		<nickname>jazz</nickname>
		<marks>90</marks>
	</student>
</class>
*/
public class DomParserEx {

	public DomParserEx() {
		File inputFile = new File(System.getProperty("user.dir") + "//Data//input.xml");
		File outFile = new File(System.getProperty("user.dir") + "//Data//write.xml");
		List<Student> stdList;
		try {
			stdList = domParsing(inputFile);
			System.out.println(stdList);
			writeXml(outFile);
		} catch (ParserConfigurationException e) {
			e.printStackTrace();
		} catch (SAXException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (TransformerException e) {
			e.printStackTrace();
		}

	}

	private List<Student> domParsing(File inputFile) throws ParserConfigurationException, SAXException, IOException {

		DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
		DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
		Document doc = dBuilder.parse(inputFile);

		doc.getDocumentElement().normalize();

		System.out.println("Root element :" + doc.getDocumentElement().getNodeName());
		NodeList nList = doc.getElementsByTagName("student");
		if (nList.getLength() <= 0)
			return null;

		List<Student> stdList = new ArrayList<>();
		System.out.println("----------------------------");

		for (int temp = 0; temp < nList.getLength(); temp++) {
			Node nNode = nList.item(temp);

			if (nNode.getNodeName().equals("student")) {
				Student std = new Student();
				if (nNode.getNodeType() == Node.ELEMENT_NODE) {
					Element eElement = (Element) nNode;
					std.setRollno(Integer.parseInt(eElement.getAttribute("rollno")));
					std.setFirstName(eElement.getElementsByTagName("firstname").item(0).getTextContent());
					std.setLastName(eElement.getElementsByTagName("lastname").item(0).getTextContent());
					std.setNickName(eElement.getElementsByTagName("nickname").item(0).getTextContent());
					std.setGrade(Integer.parseInt(eElement.getElementsByTagName("marks").item(0).getTextContent()));
				}
				stdList.add(std);
			}
		}

		return stdList;
	}

	private void writeXml(File outFile) throws ParserConfigurationException, TransformerException {
		DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
		DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
		Document doc = dBuilder.newDocument();
		
		// root element
        Element rootElement = doc.createElement("class");
        doc.appendChild(rootElement);
        
        // student element
        Element student = doc.createElement("student");
        rootElement.appendChild(student);
        
        // setting attribute to element
        Attr attr = doc.createAttribute("rollno");
        attr.setValue(500+"");
        student.setAttributeNode(attr);
        
        // student element
        Element firstName = doc.createElement("firstname");
        firstName.appendChild(doc.createTextNode("Kim"));
        student.appendChild(firstName);
        
        Element lastName = doc.createElement("lastname");
        lastName.appendChild(doc.createTextNode("minsu"));
        student.appendChild(lastName);
        
        Element nickName = doc.createElement("nickname");
        nickName.appendChild(doc.createTextNode("teacher"));
        student.appendChild(nickName);
        
        Element grade = doc.createElement("marks");
        grade.appendChild(doc.createTextNode("99"));
        student.appendChild(grade);
        
        // write the content into xml file
        TransformerFactory transformerFactory = TransformerFactory.newInstance();
        Transformer transformer = transformerFactory.newTransformer();
        DOMSource source = new DOMSource(doc);
        StreamResult result = new StreamResult(outFile);
        transformer.transform(source, result);
        
        // Output to console for testing
        StreamResult consoleResult = new StreamResult(System.out);
        transformer.transform(source, consoleResult);
	}

	public static void main(String[] args) {
		new DomParserEx();
	}

}
