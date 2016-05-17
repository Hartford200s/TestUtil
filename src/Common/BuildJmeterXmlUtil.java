package Common;

import java.io.File;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.math.BigDecimal;
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
import org.springframework.web.bind.annotation.RequestMethod;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

public class BuildJmeterXmlUtil {
	
	private static BuildJmeterXmlUtil buildJmeterXmlUtil = new BuildJmeterXmlUtil();
	
	private Properties prop = null;
	private String actionURL;
	private Parameter[] paramArr = null;
	private String requestMethod = null;
	
	private BuildJmeterXmlUtil() {
		
	}

	public static BuildJmeterXmlUtil getInstance() {
		return buildJmeterXmlUtil;
	}
	
	public void buildXml(Properties prop, String xmlFileName, String actionURL, Parameter[] paramArr, String requestMethod) {
		this.prop = prop;
		this.actionURL = actionURL;
		this.paramArr = paramArr;
		this.requestMethod = requestMethod;
		String fileName = this.prop.getProperty("buildXmlFolder") + xmlFileName + ".jmx";
		DocumentBuilderFactory icFactory = DocumentBuilderFactory.newInstance();
		DocumentBuilder icBuilder;
		try {
			icBuilder = icFactory.newDocumentBuilder();
			Document doc = icBuilder.newDocument();
			doc.appendChild(this.getRootElement(doc));
			

			TransformerFactory transformerFactory = TransformerFactory.newInstance();
			Transformer transformer = transformerFactory.newTransformer();
			DOMSource source = new DOMSource(doc);
			StreamResult result = new StreamResult(new File(fileName));
			transformer.transform(source, result);

		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	private Element getRootElement(Document doc) {
		Element jmeterTestPlan = doc.createElement("jmeterTestPlan");
		jmeterTestPlan.setAttribute("version", this.prop.getProperty("version"));
		jmeterTestPlan.setAttribute("properties", this.prop.getProperty("properties"));
		jmeterTestPlan.setAttribute("jmeter", this.prop.getProperty("jmeter"));
		jmeterTestPlan.appendChild(this.getFirstHashTree(doc));
		return jmeterTestPlan;
	}
	
	private Node getFirstHashTree(Document doc) {
		Element hashTree = doc.createElement(JmeterConstant.HASHTREE);
		hashTree.appendChild(this.getTestPlan(doc));
		hashTree.appendChild(this.getTestPlanHashTree(doc));
		return hashTree;
	}
	
	private Node getTestPlan(Document doc) {
		Element testPlan = doc.createElement("TestPlan");
		testPlan.setAttribute(JmeterConstant.GUICLASS, "TestPlanGui");
		testPlan.setAttribute(JmeterConstant.TESTCLASS, "TestPlan");
		testPlan.setAttribute(JmeterConstant.TESTNAME, "測試計畫");
		testPlan.setAttribute(JmeterConstant.ENABLED, JmeterConstant.TRUE);
		testPlan.appendChild(this.createElement(doc, JmeterConstant.STRINGPROP, JmeterConstant.NAME, "TestPlan.comments", ""));
		testPlan.appendChild(this.createElement(doc, JmeterConstant.BOOLPROP, JmeterConstant.NAME, "TestPlan.functional_mode", JmeterConstant.FALSE));
		testPlan.appendChild(this.createElement(doc, JmeterConstant.BOOLPROP, JmeterConstant.NAME, "TestPlan.serialize_threadgroups", JmeterConstant.FALSE));
		Map<String, String> propMap = new HashMap<String, String>();
		propMap.put(JmeterConstant.NAME, "TestPlan.user_defined_variables");
		propMap.put(JmeterConstant.ELEMENTTYPE, "Arguments");
		propMap.put(JmeterConstant.GUICLASS, "ArgumentsPanel");
		propMap.put(JmeterConstant.TESTCLASS, "Arguments");
		propMap.put(JmeterConstant.TESTNAME, "使用者自訂變數");
		propMap.put(JmeterConstant.ENABLED, JmeterConstant.TRUE);
		List<Node> nodeList = new ArrayList<Node>();
		nodeList.add(this.createElement(doc, JmeterConstant.COLLECTIONPROP, JmeterConstant.NAME, "Arguments.arguments", ""));
		testPlan.appendChild(this.getElementProp(doc, propMap, nodeList));
		testPlan.appendChild(this.createElement(doc, JmeterConstant.STRINGPROP, JmeterConstant.NAME, "TestPlan.user_define_classpath", ""));
		return testPlan;
	}
	
	private Node getTestPlanHashTree(Document doc) {
		Element testPlanHashTree = doc.createElement(JmeterConstant.HASHTREE);
		testPlanHashTree.appendChild(this.getThreadGroup(doc));
		testPlanHashTree.appendChild(this.getThreadGroupHashTree(doc));
		testPlanHashTree.appendChild(this.getConfigTestElement(doc));
		testPlanHashTree.appendChild(this.createElement(doc, JmeterConstant.HASHTREE, "", "", ""));
		testPlanHashTree.appendChild(this.getResultCollector(doc));
		testPlanHashTree.appendChild(this.createElement(doc, JmeterConstant.HASHTREE, "", "", ""));
		return testPlanHashTree;
	}
	
	private Node getThreadGroup(Document doc) {
		Element threadGroup = doc.createElement(JmeterConstant.THREADGROUP);
		threadGroup.setAttribute(JmeterConstant.GUICLASS, "ThreadGroupGui");
		threadGroup.setAttribute(JmeterConstant.TESTCLASS, "ThreadGroup");
		threadGroup.setAttribute(JmeterConstant.TESTNAME, "執行緒群組");
		threadGroup.setAttribute(JmeterConstant.ENABLED, JmeterConstant.TRUE);
		threadGroup.appendChild(this.createElement(doc, JmeterConstant.STRINGPROP, JmeterConstant.NAME, "ThreadGroup.on_sample_error", "continue"));
		Map<String, String> propMap = new HashMap<String, String>();
		propMap.put(JmeterConstant.NAME, "ThreadGroup.main_controller");
		propMap.put(JmeterConstant.ELEMENTTYPE, "LoopController");
		propMap.put(JmeterConstant.GUICLASS, "LoopControlPanel");
		propMap.put(JmeterConstant.TESTCLASS, "LoopController");
		propMap.put(JmeterConstant.TESTNAME, "迴圈控制器");
		propMap.put(JmeterConstant.ENABLED, JmeterConstant.TRUE);
		List<Node> nodeList = new ArrayList<Node>();
		nodeList.add(this.createElement(doc, JmeterConstant.BOOLPROP, JmeterConstant.NAME, "LoopController.continue_forever", JmeterConstant.FALSE));
		nodeList.add(this.createElement(doc, JmeterConstant.STRINGPROP, JmeterConstant.NAME, "LoopController.loops", this.prop.getProperty("loops")));
		threadGroup.appendChild(this.getElementProp(doc, propMap, nodeList));
		threadGroup.appendChild(this.createElement(doc, JmeterConstant.STRINGPROP, JmeterConstant.NAME, "ThreadGroup.num_threads", this.prop.getProperty("num_threads")));
		threadGroup.appendChild(this.createElement(doc, JmeterConstant.STRINGPROP, JmeterConstant.NAME, "ThreadGroup.ramp_time", this.prop.getProperty("ramp_time")));
		threadGroup.appendChild(this.createElement(doc, JmeterConstant.LONGPROP, JmeterConstant.NAME, "ThreadGroup.start_time", "1462865589000"));
		threadGroup.appendChild(this.createElement(doc, JmeterConstant.LONGPROP, JmeterConstant.NAME, "ThreadGroup.end_time", "1462865589000"));
		threadGroup.appendChild(this.createElement(doc, JmeterConstant.BOOLPROP, JmeterConstant.NAME, "ThreadGroup.scheduler", JmeterConstant.FALSE));
		threadGroup.appendChild(this.createElement(doc, JmeterConstant.STRINGPROP, JmeterConstant.NAME, "ThreadGroup.duration", ""));
		threadGroup.appendChild(this.createElement(doc, JmeterConstant.STRINGPROP, JmeterConstant.NAME, "ThreadGroup.delay", ""));
		return threadGroup;
	}
	
	private Node getThreadGroupHashTree(Document doc) {
		Element hashTree = doc.createElement(JmeterConstant.HASHTREE);
		hashTree.appendChild(this.getHTTPSamplerProxy(doc));
		hashTree.appendChild(this.createElement(doc, JmeterConstant.HASHTREE, "", "", ""));
		return hashTree;
	}
	
	private Node getHTTPSamplerProxy(Document doc) {
		Element HTTPSamplerProxy = doc.createElement(JmeterConstant.HTTPSAMPLERPROXY);
		HTTPSamplerProxy.setAttribute(JmeterConstant.GUICLASS, "HttpTestSampleGui");
		HTTPSamplerProxy.setAttribute(JmeterConstant.TESTCLASS, "HTTPSamplerProxy");
		HTTPSamplerProxy.setAttribute(JmeterConstant.TESTNAME, "HTTP 要求");
		HTTPSamplerProxy.setAttribute(JmeterConstant.ENABLED, JmeterConstant.TRUE);
		Map<String, String> map = new HashMap<String, String>();
		map.put(JmeterConstant.NAME, "HTTPsampler.Arguments");
		map.put(JmeterConstant.ELEMENTTYPE, "Arguments");
		map.put(JmeterConstant.GUICLASS, "HTTPArgumentsPanel");
		map.put(JmeterConstant.TESTCLASS, "Arguments");
		map.put(JmeterConstant.TESTNAME, "使用者自訂變數");
		map.put(JmeterConstant.ENABLED, JmeterConstant.TRUE);
		List<Node> nodeList = new ArrayList<Node>();
		nodeList.add(this.getHTTPSamplerProxy_ElementProp_CollectionProp(doc));
		HTTPSamplerProxy.appendChild(this.getElementProp(doc, map, nodeList));
		HTTPSamplerProxy.appendChild(this.createElement(doc, JmeterConstant.STRINGPROP, JmeterConstant.NAME, "HTTPSampler.domain", this.prop.getProperty("domain")));
		HTTPSamplerProxy.appendChild(this.createElement(doc, JmeterConstant.STRINGPROP, JmeterConstant.NAME, "HTTPSampler.port", this.prop.getProperty("port")));
		HTTPSamplerProxy.appendChild(this.createElement(doc, JmeterConstant.STRINGPROP, JmeterConstant.NAME, "HTTPSampler.connect_timeout", ""));
		HTTPSamplerProxy.appendChild(this.createElement(doc, JmeterConstant.STRINGPROP, JmeterConstant.NAME, "HTTPSampler.response_timeout", ""));
		HTTPSamplerProxy.appendChild(this.createElement(doc, JmeterConstant.STRINGPROP, JmeterConstant.NAME, "HTTPSampler.protocol", ""));
		HTTPSamplerProxy.appendChild(this.createElement(doc, JmeterConstant.STRINGPROP, JmeterConstant.NAME, "HTTPSampler.contentEncoding", "utf8"));
		String path = this.prop.getProperty("path") + actionURL;
		HTTPSamplerProxy.appendChild(this.createElement(doc, JmeterConstant.STRINGPROP, JmeterConstant.NAME, "HTTPSampler.path", path));
		HTTPSamplerProxy.appendChild(this.createElement(doc, JmeterConstant.STRINGPROP, JmeterConstant.NAME, "HTTPSampler.method", isPost() ? "POST" :"GET"));
		HTTPSamplerProxy.appendChild(this.createElement(doc, JmeterConstant.BOOLPROP, JmeterConstant.NAME, "HTTPSampler.follow_redirects", JmeterConstant.TRUE));
		HTTPSamplerProxy.appendChild(this.createElement(doc, JmeterConstant.BOOLPROP, JmeterConstant.NAME, "HTTPSampler.auto_redirects", JmeterConstant.FALSE));
		HTTPSamplerProxy.appendChild(this.createElement(doc, JmeterConstant.BOOLPROP, JmeterConstant.NAME, "HTTPSampler.use_keepalive", JmeterConstant.TRUE));
		HTTPSamplerProxy.appendChild(this.createElement(doc, JmeterConstant.BOOLPROP, JmeterConstant.NAME, "HTTPSampler.DO_MULTIPART_POST", String.valueOf(isPost())));
		HTTPSamplerProxy.appendChild(this.createElement(doc, JmeterConstant.BOOLPROP, JmeterConstant.NAME, "HTTPSampler.monitor", JmeterConstant.FALSE));
		HTTPSamplerProxy.appendChild(this.createElement(doc, JmeterConstant.STRINGPROP, JmeterConstant.NAME, "HTTPSampler.embedded_url_re", ""));
		return HTTPSamplerProxy; 
	}
	
	private Node getConfigTestElement(Document doc) {
		Element configTestElement = doc.createElement(JmeterConstant.CONFIG_TEST_ELEMENT);
		configTestElement.setAttribute(JmeterConstant.GUICLASS, "HttpDefaultsGui");
		configTestElement.setAttribute(JmeterConstant.TESTCLASS, "ConfigTestElement");
		configTestElement.setAttribute(JmeterConstant.TESTNAME, "HTTP 要求預設值");
		configTestElement.setAttribute(JmeterConstant.ENABLED, JmeterConstant.TRUE);
		
		Map<String,String> map = new HashMap<String, String>();
		map.put(JmeterConstant.NAME, "HTTPsampler.Arguments");
		map.put(JmeterConstant.ELEMENTTYPE, "Arguments");
		map.put(JmeterConstant.GUICLASS, "HTTPArgumentsPanel");
		map.put(JmeterConstant.TESTCLASS, "Arguments");
		map.put(JmeterConstant.TESTNAME, "使用者自訂變數");
		map.put(JmeterConstant.ENABLED, JmeterConstant.TRUE);
		List<Node> nodeList = new ArrayList<>();
		nodeList.add(this.createElement(doc, JmeterConstant.COLLECTIONPROP, JmeterConstant.NAME, "Arguments.arguments", ""));
		configTestElement.appendChild(getElementProp(doc, map, nodeList));
		configTestElement.appendChild(this.createElement(doc, JmeterConstant.STRINGPROP, JmeterConstant.NAME, "HTTPSampler.domain", this.prop.getProperty("domain")));
		configTestElement.appendChild(this.createElement(doc, JmeterConstant.STRINGPROP, JmeterConstant.NAME, "HTTPSampler.port", this.prop.getProperty("port")));
		configTestElement.appendChild(this.createElement(doc, JmeterConstant.STRINGPROP, JmeterConstant.NAME, "HTTPSampler.connect_timeout", ""));
		configTestElement.appendChild(this.createElement(doc, JmeterConstant.STRINGPROP, JmeterConstant.NAME, "HTTPSampler.response_timeout", ""));
		configTestElement.appendChild(this.createElement(doc, JmeterConstant.STRINGPROP, JmeterConstant.NAME, "HTTPSampler.protocol", "http"));
		configTestElement.appendChild(this.createElement(doc, JmeterConstant.STRINGPROP, JmeterConstant.NAME, "HTTPSampler.contentEncoding", ""));
		configTestElement.appendChild(this.createElement(doc, JmeterConstant.STRINGPROP, JmeterConstant.NAME, "HTTPSampler.path", ""));
		configTestElement.appendChild(this.createElement(doc, JmeterConstant.STRINGPROP, JmeterConstant.NAME, "HTTPSampler.concurrentPool", "4"));
		return configTestElement;
	}
	
	private Node getResultCollector(Document doc) {
		Element resultCollector = doc.createElement(JmeterConstant.RESULTCOLLECTOR);
		resultCollector.setAttribute(JmeterConstant.GUICLASS, "TableVisualizer");
		resultCollector.setAttribute(JmeterConstant.TESTCLASS, "ResultCollector");
		resultCollector.setAttribute(JmeterConstant.TESTNAME, "檢視表格式結果");
		resultCollector.setAttribute(JmeterConstant.ENABLED, JmeterConstant.TRUE);
		resultCollector.appendChild(this.createElement(doc, JmeterConstant.BOOLPROP, JmeterConstant.NAME, "ResultCollector.error_logging", JmeterConstant.FALSE));
		resultCollector.appendChild(this.getObjProp(doc));
		resultCollector.appendChild(this.createElement(doc, JmeterConstant.STRINGPROP, JmeterConstant.NAME, "filename", ""));
		return resultCollector;
	}
	
	private Node getObjProp(Document doc) {
		Element objProp = doc.createElement("objProp");
		objProp.appendChild(this.createElement(doc, "name", "", "", "saveConfig"));
		objProp.appendChild(this.getValueElement(doc));
		return objProp;
	}
	
	private Node getValueElement(Document doc){
		Element valueElement = doc.createElement(JmeterConstant.VALUE);
		valueElement.setAttribute(JmeterConstant.CLASS, "SampleSaveConfiguration");
		valueElement.appendChild(this.createElement(doc, JmeterConstant.TIME, "", "", JmeterConstant.TRUE));
		valueElement.appendChild(this.createElement(doc, JmeterConstant.LATENCY, "", "", JmeterConstant.TRUE));
		valueElement.appendChild(this.createElement(doc, JmeterConstant.TIMESTAMP, "", "", JmeterConstant.TRUE));
		valueElement.appendChild(this.createElement(doc, JmeterConstant.SUCCESS, "", "", JmeterConstant.TRUE));
		valueElement.appendChild(this.createElement(doc, JmeterConstant.LABEL, "", "", JmeterConstant.TRUE));
		valueElement.appendChild(this.createElement(doc, JmeterConstant.CODE, "", "", JmeterConstant.TRUE));
		valueElement.appendChild(this.createElement(doc, JmeterConstant.MESSAGE, "", "", JmeterConstant.TRUE));
		valueElement.appendChild(this.createElement(doc, JmeterConstant.THREADNAME, "", "", JmeterConstant.TRUE));
		valueElement.appendChild(this.createElement(doc, JmeterConstant.DATATYPE, "", "", JmeterConstant.TRUE));
		valueElement.appendChild(this.createElement(doc, JmeterConstant.ENCODING, "", "", JmeterConstant.FALSE));
		valueElement.appendChild(this.createElement(doc, JmeterConstant.ASSERTIONS, "", "", JmeterConstant.TRUE));
		valueElement.appendChild(this.createElement(doc, JmeterConstant.SUBRESULTS, "", "", JmeterConstant.TRUE));
		valueElement.appendChild(this.createElement(doc, JmeterConstant.RESPONSEDATA, "", "", JmeterConstant.FALSE));
		valueElement.appendChild(this.createElement(doc, JmeterConstant.SAMPLERDATA, "", "", JmeterConstant.FALSE));
		valueElement.appendChild(this.createElement(doc, JmeterConstant.XML, "", "", JmeterConstant.FALSE));
		valueElement.appendChild(this.createElement(doc, JmeterConstant.FIELDNAMES, "", "", JmeterConstant.FALSE));
		valueElement.appendChild(this.createElement(doc, JmeterConstant.RESPONSEHEADERS, "", "", JmeterConstant.FALSE));
		valueElement.appendChild(this.createElement(doc, JmeterConstant.REQUESTHEADERS, "", "", JmeterConstant.FALSE));
		valueElement.appendChild(this.createElement(doc, JmeterConstant.RESPONSEDATAONERROR, "", "", JmeterConstant.FALSE));
		valueElement.appendChild(this.createElement(doc, JmeterConstant.SAVE_ASSERTION_RESULTS_FAILURE_MESSAGE, "", "", JmeterConstant.FALSE));
		valueElement.appendChild(this.createElement(doc, JmeterConstant.ASSERTIONS_RESULTS_TO_SAVE, "", "", "0"));
		valueElement.appendChild(this.createElement(doc, JmeterConstant.BYTES, "", "", JmeterConstant.TRUE));
		valueElement.appendChild(this.createElement(doc, JmeterConstant.THREADCOUNTS, "", "", JmeterConstant.TRUE));
		return valueElement;
	}
	
	private Element createElement(Document doc, String elementName,String attributeName, String attributeValue, String nodeValue) {
		Element stringProp = doc.createElement(elementName);
		if (StringUtils.isNotEmpty(attributeName) && StringUtils.isNotEmpty(attributeValue)) {
			stringProp.setAttribute(attributeName, attributeValue);
		}
		if (StringUtils.isNotEmpty(nodeValue)) {
			stringProp.setTextContent(nodeValue);
		} else {
			stringProp.setTextContent("");
		}
		return stringProp;
	}
	
	private Node getElementProp(Document doc, Map<String,String> propMap, List<Node> nodeList) {
		Element elementProp = doc.createElement(JmeterConstant.ELEMENTPROP);
		for (Entry<String, String> entry : propMap.entrySet()) {
			elementProp.setAttribute(entry.getKey(), entry.getValue());
		}
		for (Node node : nodeList) {
			elementProp.appendChild(node);
		}
		return elementProp;
	}
	
	private Node getHTTPSamplerProxy_ElementProp_CollectionProp(Document doc) {
		Element collectionProp = this.createElement(doc, JmeterConstant.COLLECTIONPROP, JmeterConstant.NAME, "Arguments.arguments", "");
		if (this.paramArr != null && this.paramArr.length >0) {
			for (Parameter parameter : paramArr){
				String parmName = parameter.getName();
				String parmValue = "";
				
				Class typeClass = parameter.getType();
				if (String.class.isAssignableFrom(typeClass)) {
					parmValue = "test";
				} else if (Long.class.isAssignableFrom(typeClass) || long.class.isAssignableFrom(typeClass) || Integer.class.isAssignableFrom(typeClass) || int.class.isAssignableFrom(typeClass)|| Double.class.isAssignableFrom(typeClass) || double.class.isAssignableFrom(typeClass) || Float.class.isAssignableFrom(typeClass) || float.class.isAssignableFrom(typeClass) || BigDecimal.class.isAssignableFrom(typeClass)) {
					parmValue = "0";
				} else {
					parmValue = "";
				}

				Map<String, String> propMap = new HashMap<>();
				propMap.put(JmeterConstant.NAME, parmName);
				propMap.put(JmeterConstant.ELEMENTTYPE, "HTTPArgument");
				List<Node> nodeList = new ArrayList<>();
				nodeList.add(this.createElement(doc, JmeterConstant.BOOLPROP, JmeterConstant.NAME, "HTTPArgument.always_encode", JmeterConstant.FALSE));
				nodeList.add(this.createElement(doc, JmeterConstant.STRINGPROP, JmeterConstant.NAME, "Argument.value", parmValue));
				nodeList.add(this.createElement(doc, JmeterConstant.STRINGPROP, JmeterConstant.NAME, "Argument.metadata", "="));
				nodeList.add(this.createElement(doc, JmeterConstant.BOOLPROP, JmeterConstant.NAME, "HTTPArgument.use_equals", JmeterConstant.TRUE));
				nodeList.add(this.createElement(doc, JmeterConstant.STRINGPROP, JmeterConstant.NAME, "Argument.name", parmName));
				collectionProp.appendChild(this.getElementProp(doc, propMap, nodeList));
			}
		}
		return collectionProp;
	}
	
	private boolean isPost() {
		return RequestMethod.POST.toString().equals(requestMethod);
	}
	
}
