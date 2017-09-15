package com.anishsneh.microweaver.service.core.util;

import java.io.StringReader;
import java.io.StringWriter;
import java.io.Writer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.xml.parsers.DocumentBuilderFactory;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.DOMConfiguration;
import org.w3c.dom.Node;
import org.w3c.dom.bootstrap.DOMImplementationRegistry;
import org.w3c.dom.ls.DOMImplementationLS;
import org.w3c.dom.ls.LSOutput;
import org.w3c.dom.ls.LSSerializer;
import org.xml.sax.InputSource;

import com.anishsneh.microweaver.service.core.vo.Task;

import freemarker.template.Version;
import freemarker.template.Configuration;
import freemarker.template.Template;

/**
 * The Class WorkflowContextUtil.
 * 
 * @author Anish Sneh
 * 
 */
public class WorkflowContextUtil {
	
	/** The Constant log. */
	private static final Logger log = LoggerFactory.getLogger(WorkflowContextUtil.class);
	
	/**
	 * Gets the context xml.
	 *
	 * @param taskList the task list
	 * @return the context xml
	 */
	public static String getContextXml(final List<Task> taskList) {
		final Configuration fmConf = new Configuration(new Version(2, 3, 26));
		fmConf.setClassForTemplateLoading(WorkflowContextUtil.class, "/");
		try {
			final Template fmTemplate = fmConf.getTemplate("templates/workflowContext.xml.ftl");
			fmConf.setDefaultEncoding("UTF-8");
			fmConf.setLocale(Locale.US);
			final Map<String, Object> input = new HashMap<String, Object>();
			input.put("tasks", taskList);
			final Writer outString = new StringWriter();
			fmTemplate.process(input, outString);
			final String contextXmlAsString = formatXml(outString.toString());
			return contextXmlAsString;
		} 
		catch (final Exception ex) {
			log.error("Failed to parse context xml template", ex);
		}
		return null;
	}

	/**
	 * Format xml.
	 *
	 * @param xmlAsString the xml as string
	 * @return the string
	 */
	public static String formatXml(final String xmlAsString) {
		try {
			final InputSource src = new InputSource(new StringReader(xmlAsString));
			final Node document = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(src).getDocumentElement();
			final DOMImplementationRegistry registry = DOMImplementationRegistry.newInstance();
			final DOMImplementationLS impl = (DOMImplementationLS) registry.getDOMImplementation("LS");
			final LSSerializer serializer = impl.createLSSerializer();
			final DOMConfiguration config = serializer.getDomConfig();
			config.setParameter("format-pretty-print", true);
			config.setParameter("xml-declaration", true);
			final LSOutput output = impl.createLSOutput();
			output.setEncoding("UTF-8");
			final Writer writer = new StringWriter();
			output.setCharacterStream(writer);
			serializer.write(document, output);
			return writer.toString();
		} 
		catch (final Exception e) {
			e.printStackTrace();
			return xmlAsString;
		}
	}
}
