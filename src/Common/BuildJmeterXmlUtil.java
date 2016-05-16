package Common;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Properties;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.apache.commons.lang3.StringUtils;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

public class BuildJmeterXmlUtil {
	
	private static final String STRINGPROP= "stringProp";
	private static final String BOOLPROP= "boolProp";
	private static final String NAME = "name";
	private static final String HASHTREE = "hashTree";
	private static final String ELEMENTPROP = "elementProp";
	private static final String COLLECTIONPROP = "collectionProp";
	private static final String THREADGROUP = "ThreadGroup";
	private static final String LONGPROP = "longProp";
	
	private static BuildJmeterXmlUtil buildJmeterXmlUtil = new BuildJmeterXmlUtil();
	
	private BuildJmeterXmlUtil() {
		
	}

	public static BuildJmeterXmlUtil getInstance() {
		return buildJmeterXmlUtil;
	}
	
	public void buildXml(Properties prop, String xmlFileName) {
		String fileName = prop.getProperty("buildXmlFolder") + xmlFileName + ".jmx";
		DocumentBuilderFactory icFactory = DocumentBuilderFactory.newInstance();
		DocumentBuilder icBuilder;
		try {
			icBuilder = icFactory.newDocumentBuilder();
			Document doc = icBuilder.newDocument();
			Element mainRootElement = this.getRootElement(doc, prop);
			doc.appendChild(mainRootElement);
			mainRootElement.appendChild(this.getFirstHashTree(doc));

			TransformerFactory transformerFactory = TransformerFactory.newInstance();
			Transformer transformer = transformerFactory.newTransformer();
			DOMSource source = new DOMSource(doc);
			StreamResult result = new StreamResult(new File(fileName));
			transformer.transform(source, result);

		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	private Element getRootElement(Document doc, Properties prop) {
		Element jmeterTestPlan = doc.createElement("jmeterTestPlan");
		jmeterTestPlan.setAttribute("version", prop.getProperty("version"));
		jmeterTestPlan.setAttribute("properties", prop.getProperty("properties"));
		jmeterTestPlan.setAttribute("jmeter", prop.getProperty("jmeter"));
		return jmeterTestPlan;
	}
	
	private Node getFirstHashTree(Document doc) {
		Element hashTree = doc.createElement(BuildJmeterXmlUtil.HASHTREE);
		hashTree.appendChild(this.getTestPlan(doc));
		hashTree.appendChild(this.getTestPlanHashTree(doc));
		return hashTree;
	}
	
	private Node getTestPlan(Document doc) {
		Element testPlan = doc.createElement("TestPlan");
		testPlan.setAttribute("guiclass", "TestPlanGui");
		testPlan.setAttribute("testclass", "TestPlan");
		testPlan.setAttribute("testname", "測試計畫");
		testPlan.setAttribute("enabled", "true");
		testPlan.appendChild(this.createElement(doc, BuildJmeterXmlUtil.STRINGPROP, BuildJmeterXmlUtil.NAME, "TestPlan.comments", ""));
		testPlan.appendChild(this.createElement(doc, BuildJmeterXmlUtil.BOOLPROP, BuildJmeterXmlUtil.NAME, "TestPlan.functional_mode", "false"));
		testPlan.appendChild(this.createElement(doc, BuildJmeterXmlUtil.BOOLPROP, BuildJmeterXmlUtil.NAME, "TestPlan.serialize_threadgroups", "false"));
		Map<String, String> propMap = new HashMap<String, String>();
		propMap.put("name", "TestPlan.user_defined_variables");
		propMap.put("elementType", "Arguments");
		propMap.put("guiclass", "ArgumentsPanel");
		propMap.put("testclass", "Arguments");
		propMap.put("testname", "使用者自訂變數");
		propMap.put("enabled", "true");
		List<Node> nodeList = new ArrayList<Node>();
		nodeList.add(this.createElement(doc, BuildJmeterXmlUtil.COLLECTIONPROP, BuildJmeterXmlUtil.NAME, "Arguments.arguments", ""));
		testPlan.appendChild(this.getElementProp(doc, propMap, nodeList));
		testPlan.appendChild(this.createElement(doc, BuildJmeterXmlUtil.STRINGPROP, BuildJmeterXmlUtil.NAME, "TestPlan.user_define_classpath", ""));
		return testPlan;
	}
	
	private Node getTestPlanHashTree(Document doc) {
		Element testPlanHashTree = doc.createElement(BuildJmeterXmlUtil.HASHTREE);
		testPlanHashTree.appendChild(this.getThreadGroup(doc));
		return testPlanHashTree;
	}
	
	private Node getThreadGroup(Document doc) {
		Element threadGroup = doc.createElement(BuildJmeterXmlUtil.THREADGROUP);
		threadGroup.setAttribute("guiclass", "ThreadGroupGui");
		threadGroup.setAttribute("testclass", "ThreadGroup");
		threadGroup.setAttribute("testname", "執行緒群組");
		threadGroup.setAttribute("enabled", "true");
		threadGroup.appendChild(this.createElement(doc, BuildJmeterXmlUtil.STRINGPROP, BuildJmeterXmlUtil.NAME, "ThreadGroup.on_sample_error", "continue"));
		Map<String, String> propMap = new HashMap<String, String>();
		propMap.put("name", "ThreadGroup.main_controller");
		propMap.put("elementType", "LoopController");
		propMap.put("guiclass", "LoopControlPanel");
		propMap.put("testclass", "LoopController");
		propMap.put("testname", "迴圈控制器");
		propMap.put("enabled", "true");
		List<Node> nodeList = new ArrayList<Node>();
		nodeList.add(this.createElement(doc, BuildJmeterXmlUtil.BOOLPROP, BuildJmeterXmlUtil.NAME, "LoopController.continue_forever", "false"));
		nodeList.add(this.createElement(doc, BuildJmeterXmlUtil.STRINGPROP, BuildJmeterXmlUtil.NAME, "LoopController.loops", "1"));
		threadGroup.appendChild(this.getElementProp(doc, propMap, nodeList));
		threadGroup.appendChild(this.createElement(doc, BuildJmeterXmlUtil.STRINGPROP, BuildJmeterXmlUtil.NAME, "ThreadGroup.num_threads", "3"));
		threadGroup.appendChild(this.createElement(doc, BuildJmeterXmlUtil.STRINGPROP, BuildJmeterXmlUtil.NAME, "ThreadGroup.ramp_time", "1"));
		threadGroup.appendChild(this.createElement(doc, BuildJmeterXmlUtil.LONGPROP, BuildJmeterXmlUtil.NAME, "ThreadGroup.start_time", "1462865589000"));
		threadGroup.appendChild(this.createElement(doc, BuildJmeterXmlUtil.LONGPROP, BuildJmeterXmlUtil.NAME, "ThreadGroup.end_time", "1462865589000"));
		threadGroup.appendChild(this.createElement(doc, BuildJmeterXmlUtil.BOOLPROP, BuildJmeterXmlUtil.NAME, "ThreadGroup.scheduler", "false"));
		threadGroup.appendChild(this.createElement(doc, BuildJmeterXmlUtil.STRINGPROP, BuildJmeterXmlUtil.NAME, "ThreadGroup.duration", ""));
		threadGroup.appendChild(this.createElement(doc, BuildJmeterXmlUtil.STRINGPROP, BuildJmeterXmlUtil.NAME, "ThreadGroup.delay", ""));
		return threadGroup;
	}
	
	private Node createElement(Document doc, String elementName,String name, String nameValue, String nodeValue) {
		Element stringProp = doc.createElement(elementName);
		stringProp.setAttribute(name, nameValue);
		if (StringUtils.isNotEmpty(nodeValue)) {
			stringProp.setTextContent(nodeValue);
		} else {
			stringProp.setTextContent("");
		}
		return stringProp;
	}
	
	private Node getElementProp(Document doc, Map<String,String> propMap, List<Node> nodeList) {
		Element elementProp = doc.createElement(BuildJmeterXmlUtil.ELEMENTPROP);
		for (Entry<String, String> entry : propMap.entrySet()) {
			elementProp.setAttribute(entry.getKey(), entry.getValue());
		}
		for (Node node : nodeList) {
			elementProp.appendChild(node);
		}
		return elementProp;
	}
}
