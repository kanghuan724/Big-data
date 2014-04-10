package serviceStatus;

import static serviceStatus.serviceConstants.*;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.DocumentBuilder;

import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.w3c.dom.Node;
import org.w3c.dom.Element;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.HashSet;


 
public class XMLParsing {
	private static void writeToTilderFormat(String prefix,
			List<String> attributeList, String fileName) {
		File targetDir = new File(TRANSFERRED_SERVICE);
		File targetFile = new File(targetDir, fileName);
		try {
			FileOutputStream fos = new FileOutputStream(targetFile,true);
			Writer writer = new BufferedWriter(new OutputStreamWriter(
				          fos, "utf-8"));
			StringBuffer tempBuffer = new StringBuffer();
			tempBuffer.append(prefix);
			for (int i = 0; i < attributeList.size(); i++) {
				tempBuffer.append("~");
				tempBuffer.append(attributeList.get(i));
			}
			tempBuffer.append("\n");
			writer.write(tempBuffer.toString());
			writer.close();
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
	}
	private static void appendAttributes(List<String> attributeList,Element lineElement)
	{
		attributeList.add(lineElement.getElementsByTagName(NAME).item(0).getTextContent());
		attributeList.add(lineElement.getElementsByTagName(STATUS).item(0).getTextContent());
		attributeList.add(lineElement.getElementsByTagName(DATE).item(0).getTextContent());
		attributeList.add(lineElement.getElementsByTagName(TIME).item(0).getTextContent());
	}
	private static String getNodeValue (NodeList cList,String nodeName)
	{
		for (int i=0;i<cList.getLength();i++)
		{
			Node currentNode = cList.item(i);
			if (currentNode.getNodeName().equals(nodeName))
			{
				Element currentElement = (Element) currentNode;
				return (currentElement.getChildNodes().item(0).getTextContent());
			}
		}
		return null;
	}
	private static void translateNodeWithPrefix(String prefix,Set<String> transportationSet,Node cnode,String fileName)
	{
		String nodeName = cnode.getNodeName();
		if (transportationSet.contains(nodeName)==false)
			return;
		NodeList lineList = cnode.getChildNodes();
		for (int lineIndex = 0 ;lineIndex<lineList.getLength();lineIndex ++)
		{
			Node lineNode = lineList.item(lineIndex);
			String category = lineNode.getNodeName();
			
			if (category.equals(LINE))
			{
				List<String> attributeList = new ArrayList<String> ();
				attributeList.add(nodeName);
				Element lineElement = (Element) lineNode;
				appendAttributes(attributeList,lineElement);
				writeToTilderFormat(prefix,attributeList,fileName);
				
			}
		}
	}
	public static void main(String argv[]) {
		
		File directory = new File("SERVICE_DATA");
		for (final File fileEntry : directory.listFiles()) {
			String fileName = fileEntry.getName();
			try {
				Set transportationSet = new HashSet(
						Arrays.asList(TRANSPORTATION_LIST));
				File fXmlFile = new File(directory,fileName);
				DocumentBuilderFactory dbFactory = DocumentBuilderFactory
						.newInstance();
				DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
				Document doc = dBuilder.parse(fXmlFile);
				doc.getDocumentElement().normalize();
				String timeStamp = null;
				NodeList cList = doc.getDocumentElement().getChildNodes();
				timeStamp = getNodeValue(cList, TIME_STAMP);
				for (int i = 0; i < cList.getLength(); i++) {
					Node cNode = cList.item(i);
					translateNodeWithPrefix(timeStamp, transportationSet, cNode,fileName);

				}

			} catch (Exception e) {
				System.out.println(e.getMessage());

			}
		}
	}

}
