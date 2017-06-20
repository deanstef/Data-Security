package eu.sunfishproject.icsp.pep.attributes;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import eu.sunfishproject.icsp.pep.config.PEPConfig;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.openaz.xacml.api.*;
import org.apache.openaz.xacml.pdp.policy.expressions.AttributeDesignator;
import org.sunfish.icsp.common.api.pip.SunfishPIPRequest;
import org.sunfish.icsp.common.exceptions.ICSPException;
import org.sunfish.icsp.common.exceptions.InvalidRequestException;
import org.sunfish.icsp.common.rest.model.SunfishHttpObject;
import org.sunfish.icsp.common.rest.model.SunfishRequestData;
import org.sunfish.icsp.common.rest.pip.PIPAdapter;
import org.sunfish.icsp.common.util.CommonUtil;
import org.sunfish.icsp.common.xacml.XACMLConstants;
import org.sunfish.icsp.common.xacml.XACMLRequestBuilder;
import org.sunfish.icsp.common.xacml.XACMLUtils;
import org.sunfish.icsp.common.xacml.XMLDataTypes;
import org.sunfish.icsp.common.xacml.exceptions.XACMLBuilderException;

import javax.ws.rs.core.MultivaluedMap;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;


/**
 * @author <a href="mailto:dominik.ziegler@a-sit.at">Dominik Ziegler</a>
 */
public class RequestAttributeEnhancer {

    private static Logger log = LogManager.getLogger(RequestAttributeEnhancer.class);


    public static Request enhanceRequest(Request requestToEnhance, SunfishHttpObject sunfishHttpObject, String[] requestedAttributes) throws ICSPException {

        Request enhancedRequest = requestToEnhance;


        try {
            XACMLRequestBuilder xacml = new XACMLRequestBuilder(requestToEnhance);

            ObjectMapper mapper = new ObjectMapper();

            xacml = xacml.setCategory(XACMLConstants.CATEGORY_TARGET);


            if(sunfishHttpObject.getBody() != null) {
                xacml = xacml.addBase64Attribute(XACMLConstants.ATTRIBUTE_TARGET_BODY, sunfishHttpObject.getBody());
            }

            if(sunfishHttpObject.getHeaders() != null) {
                String headerJson = mapper.writeValueAsString(sunfishHttpObject.getHeaders());
                xacml = xacml.addStringAttribute(XACMLConstants.ATTRIBUTE_TARGET_HEADER, headerJson);
            }

            if(sunfishHttpObject.getQueryString() != null) {
                xacml = xacml.addStringAttribute(XACMLConstants.ATTRIBUTE_TARGET_QUERY_STRING, sunfishHttpObject.getQueryString());
            }


            requestToEnhance = xacml.build();

            List<AttributeDesignator> missingAttributes = getMissingAttributes(requestedAttributes);

            SunfishPIPRequest pipRequest = new SunfishPIPRequest(requestToEnhance, missingAttributes);


            for(PIPAdapter pipAdapter : PEPConfig.getInstance().getPIPs()) {
                if(pipAdapter.matches(pipRequest)) {
                    Request response = pipAdapter.request(pipRequest);
                    if(response != null) {
                        enhancedRequest = response;
                    } else {
                        log.error("PIP did not reply with correct request.");
                    }
                }
            }


            xacml = new XACMLRequestBuilder(enhancedRequest);
            xacml = xacml.removeCategory(XACMLConstants.CATEGORY_TARGET);
            enhancedRequest = xacml.build();


        } catch (XACMLBuilderException e) {
            log.error("Could not create XACML Request", e);
        } catch (ICSPException e) {
            log.error("Could not connect to pip", e);
        } catch (JsonProcessingException e) {
            log.error("Could not process json", e);
        } catch(Exception e) {
            log.error("Error occured: ", e);
            throw new InvalidRequestException();
        }


        return enhancedRequest;


    }


    private static List<AttributeDesignator> getMissingAttributes(String[] requestedAttributes) {


        List<AttributeDesignator> missingAttributes = new ArrayList<>();
        for(String attributeId : requestedAttributes) {
            missingAttributes.add(XACMLUtils.createAttributeDesignator(attributeId, XACMLConstants.CATEGORY_REQUEST, XMLDataTypes.XML_TYPE_STRING));
        }

        return missingAttributes;


    }




}
