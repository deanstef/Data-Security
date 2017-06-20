package org.sunfish.icsp.common.rest.mappers;

import org.sunfish.icsp.common.rest.CommonSetup;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import com.fasterxml.jackson.jaxrs.xml.JacksonXMLProvider;

public class XMLProvider extends JacksonXMLProvider {
  public XMLProvider() {
    final XmlMapper xmlMapper = new XmlMapper();
    xmlMapper
        .setVisibility(xmlMapper.getVisibilityChecker().withFieldVisibility(JsonAutoDetect.Visibility.ANY)
            .withGetterVisibility(JsonAutoDetect.Visibility.NONE)
            .withSetterVisibility(JsonAutoDetect.Visibility.NONE)
            .withCreatorVisibility(JsonAutoDetect.Visibility.NONE));
    xmlMapper.setDateFormat(CommonSetup.DATE_FORMAT);
    setMapper(xmlMapper);
  }
}