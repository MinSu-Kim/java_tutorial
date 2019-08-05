package java_tutorial.parser.dom;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dom4j.Node;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.SAXReader;
import org.dom4j.io.XMLWriter;

/**
 * Dom4J Parser
 * 
 * @author mskim
 *         https://www.tutorialspoint.com/java_xml/java_dom4j_parse_document.htm
 */

public class Dom4JParserEx {

	public Dom4JParserEx() {
		File inputFile = new File(System.getProperty("user.dir") + "//Data//input.xml");
		File outFile = new File(System.getProperty("user.dir") + "//Data//output.xml");
		List<Student> stdList;

		try {
			stdList = domParsing(inputFile);
			System.out.println(stdList);
			writeXml(outFile);
		} catch (DocumentException e) {
			e.printStackTrace();
		}

	}

	private List<Student> domParsing(File inputFile) throws DocumentException {
		List<Student> stdList;

		SAXReader reader = new SAXReader();
		Document document = reader.read(inputFile);

		System.out.println("Root element :" + document.getRootElement().getName());

		@SuppressWarnings("unchecked")
		List<Node> nodes = document.selectNodes("/class/student");

		if (nodes.size() <= 0)
			return null;
		stdList = new ArrayList<Student>();
		for (Node node : nodes) {
			System.out.println("\nCurrent Element :" + node.getName());
			Student std = new Student();
			std.setRollno(Integer.parseInt(node.valueOf("@rollno")));
			std.setFirstName(node.selectSingleNode("firstname").getText());
			std.setLastName(node.selectSingleNode("lastname").getText());
			std.setNickName(node.selectSingleNode("nickname").getText());
			std.setGrade(Integer.parseInt(node.selectSingleNode("marks").getText()));
			stdList.add(std);
		}
		return stdList;
	}

	private void writeXml(File outFile) {
		try {
			Document document = DocumentHelper.createDocument();
			Element root = document.addElement("class");
			Element studentElement = root.addElement("student").addAttribute("rollno", "500");

			studentElement.addElement("firstname").addText("Kim");
			studentElement.addElement("lastname").addText("minsu");
			studentElement.addElement("nickname").addText("teacher");
			studentElement.addElement("marks").addText("99");

			// Pretty print the document to System.out
			OutputFormat format = OutputFormat.createPrettyPrint();
			XMLWriter writer = new XMLWriter(System.out, format);
			writer.write(document);
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}
	
	public static void main(String[] args) {
		new Dom4JParserEx();
	}

}
