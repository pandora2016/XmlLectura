package br.com.syspdv.synch.server.model.layout.importacao.xml;

import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.namespace.QName;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamReader;

import org.apache.log4j.Logger;

public class SysPDVStAXReader {

	private static Logger logger = Logger.getLogger(SysPDVStAXReader.class);
	
	private final String xmlFilePath;

	public SysPDVStAXReader(String xmlFilePath) {
		this.xmlFilePath = xmlFilePath;
	}

	public List<Map<String, Object>> getItems() {
		
		List<Map<String, Object>> rows = new ArrayList<Map<String,Object>>();
		
		try {
			
			XMLInputFactory factory = XMLInputFactory.newFactory();
			XMLStreamReader parser = factory.createXMLStreamReader(new FileInputStream(xmlFilePath), "Cp1252");
			
			String currentTag = null;
			Map<String, Object> currentRow = null;
			
			while (parser.hasNext()) {
				
				int currentEvent = parser.next();
				switch (currentEvent) {
					case XMLStreamReader.START_ELEMENT:
						
						currentTag = parser.getLocalName();
						
						if ("ROW".equals(currentTag)) {
							currentRow = new HashMap<String, Object>(65); // numero medio de atributos de produtos
							
							int count = parser.getAttributeCount();
							if (count > 0) {
								for (int i=0; i < count; i++) {
									
									QName name = parser.getAttributeName(i);
									String value = parser.getAttributeValue(i);
									
									currentRow.put(name.toString(), value);
								}
							}
							
						}
						
						break;
					case XMLStreamReader.END_ELEMENT:
						
						currentTag = parser.getLocalName();
						
						if ("ROW".equals(currentTag)) {
							rows.add(currentRow);
						}
						
						break;
				}
				
			}
			
			
		} catch (Exception e) {
			logger.error("Erro ao processar XML " + xmlFilePath + ": " + e.getMessage());
			throw new IllegalStateException(e);
		}
		
		return rows;
	}
}
