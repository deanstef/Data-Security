/**
 *
 */
package org.sunfish.icsp.common.rest;

import java.text.SimpleDateFormat;
import java.util.TimeZone;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.core.Configurable;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.openaz.xacml.pdp.util.OpenAZPDPProperties;
import org.glassfish.jersey.client.ClientConfig;
import org.glassfish.jersey.media.multipart.MultiPartFeature;
import org.glassfish.jersey.server.ResourceConfig;
import org.sunfish.icsp.common.factories.ThreadSafeCombiningAlgorithmFactory;
import org.sunfish.icsp.common.factories.ThreadSafeDataTypeFactory;
import org.sunfish.icsp.common.factories.ThreadSafeFunctionDefinitionFactory;
import org.sunfish.icsp.common.rest.mappers.*;

/**
 * @author <a href="mailto:bernd.pruenster@a-sit.at">Bernd Pr&uuml;nster</a>
 *
 */
public class CommonSetup extends ResourceConfig {
  public static final int POOL_SZ=4;

  public static final TimeZone         TIME_ZONE   = TimeZone.getTimeZone("UTC");
  public static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
  private static final Logger          log         = LogManager.getLogger(CommonSetup.class);

  private static final Client CLIENT = ClientBuilder.newClient(createClientConfig());;

  private static <C extends Configurable<?>> void registerMappers(final C c) {

    log.info("Replacing broken Factories with thread-safe ones");
    log.info("POOL_SZ="+POOL_SZ);
    System.setProperty(OpenAZPDPProperties.PROP_COMBININGALGORITHMFACTORY,
        ThreadSafeCombiningAlgorithmFactory.class.getCanonicalName());
    System.setProperty(OpenAZPDPProperties.PROP_FUNCTIONDEFINITIONFACTORY,
        ThreadSafeFunctionDefinitionFactory.class.getCanonicalName());
    System.setProperty("xacml.dataTypeFactory", ThreadSafeDataTypeFactory.class.getCanonicalName());

    doRegister(c, XMLProvider.class, "XML Provider");
    doRegister(c, JSONProvider.class, "JSON Provider");

    doRegister(c, XACMLRequestProvider.class, "XACML Request Provider");
    doRegister(c, XACMLResponseProvider.class, "XACML Response Writer");
    doRegister(c, XACMLPolicyTypeReader.class, "XACML PolicyType Reader");
    doRegister(c, XACMLPolicyTypeWriter.class, "XACML PolicyType Writer");
    doRegister(c, XACMLPolicySetTypeReader.class, "XACML PolicySetType Reader");
    doRegister(c, XACMLPolicySetTypeWriter.class, "XACML PolicySetType Writer");
    doRegister(c, XACMLPolicySetsTypeReader.class, "XACML PolicySetsType Reader");
    doRegister(c, XACMLPolicySetsTypeWriter.class, "XACML PolicySetsType Writer");
    doRegister(c, XACMLPoliciesTypeWriter.class, "XACML PoliciesType Writer");
    doRegister(c, XACMLPoliciesTypeReader.class, "XACML PoliciesType Reader");
    doRegister(c, XACMLPolicyListTypeReader.class, "XACML PolicyListType Reader");
    doRegister(c, XACMLPolicyListTypeWriter.class, "XACML PolicyListType Writer");
    doRegister(c, XACMLPolicySetListTypeWriter.class, "XACML PolicySetListType Writer");
    doRegister(c, XACMLPolicySetListTypeReader.class, "XACML PolicySetListType Reader");
    doRegister(c, XACMLPolicyListPolicySetListTypeWriter.class, "XACML PolicyListPolicySetListType Writer");
    doRegister(c, XACMLPolicyListPolicySetListTypeReader.class, "XACML PolicyListPolicySetListType Reader");
    doRegister(c, XACMLPolicyDefReader.class, "XACML PolicyDef Reader");
    doRegister(c, XACMLSunfishPIPRequestProvider.class, "XACML Sunfish PIP Request Provider");
    doRegister(c, XACMLSunfishAttributeDesignatorSetProvider.class, "XACML AttributeSesigantorSet Provider");
    doRegister(c, XMLHomeWriter.class, "application/home+xml writer");
    doRegister(c, XACMLSunfishObligationExpressionTypeWriter.class, "XACML Sunfish ObligationType Writer");


    doRegister(c, JacksonJsonParamConverterProvider.class, "JacksonJsonParamConverter");
    doRegister(c, ObjectMapperContextResolver.class, "JacksonJsonParamConverter");

    doRegister(c, DefaultExceptionMapper.class, "Exception Mapper");
    doRegister(c, StatusFilter.class, "Status Code Filter");
    doRegister(c, HeaderFilter.class, "Header Filter");
    doRegister(c, DebuggingFilter.class, "Transparent Filter");
    doRegister(c, MultiPartFeature.class, "Multipart/Formdata Support");


    DATE_FORMAT.setTimeZone(TIME_ZONE);
  }

  private static <C extends Configurable<?>> void doRegister(final C c, final Class<?> provider,
      final String description) {
    log.debug("{}: Registering {} {}", c.getClass().getSimpleName(), description,
        provider.getCanonicalName());
    c.register(provider);
  }

  public CommonSetup() {
    registerMappers(this);
  }

  public static ClientConfig createClientConfig() {
    final ClientConfig config = new ClientConfig();
    registerMappers(config);
    return config;
  }

  public static Client createClient() {
    return CLIENT;
  }
}
