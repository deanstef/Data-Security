package org.sunfish.icsp.common.rest.mappers;

import org.sunfish.icsp.common.rest.CommonSetup;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.jaxrs.json.JacksonJsonProvider;

public class JSONProvider extends JacksonJsonProvider {

  public JSONProvider() {
    setMapper(new JSONMapper());
  }

  public static class JSONMapper extends ObjectMapper {
    private static final long serialVersionUID = 1L;

    public JSONMapper() {
      setSerializationInclusion(JsonInclude.Include.NON_EMPTY);
      setSerializationInclusion(JsonInclude.Include.NON_NULL);

      setVisibility(getVisibilityChecker().withFieldVisibility(JsonAutoDetect.Visibility.ANY)
          .withGetterVisibility(JsonAutoDetect.Visibility.NONE)
          .withSetterVisibility(JsonAutoDetect.Visibility.NONE)
          .withCreatorVisibility(JsonAutoDetect.Visibility.NONE));
      setDateFormat(CommonSetup.DATE_FORMAT);
    }

    public String stringify(final Object o) throws JsonProcessingException {
      return writer().writeValueAsString(o);
    }

  }
}