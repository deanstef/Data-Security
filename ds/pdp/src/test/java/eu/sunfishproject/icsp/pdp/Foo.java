package eu.sunfishproject.icsp.pdp;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;

import javax.ws.rs.WebApplicationException;

import org.apache.openaz.xacml.pdp.policy.PolicyDef;
import org.apache.openaz.xacml.pdp.policy.PolicySetIdReference;
import org.apache.openaz.xacml.std.StdIdReferenceMatch;
import org.apache.openaz.xacml.std.StdVersionMatch;
import org.sunfish.icsp.common.rest.ExtendedMediaType;
import org.sunfish.icsp.common.rest.mappers.XACMLPolicyDefReader;
import org.sunfish.icsp.common.rest.mappers.XACMLPolicyListPolicySetListTypeReader;
import org.sunfish.icsp.common.rest.mappers.XACMLPolicyListPolicySetListTypeWriter;
import org.sunfish.icsp.common.rest.mappers.XACMLPolicySetTypeReader;
import org.sunfish.icsp.common.xacml.generated.PolicyListPolicySetListType;

import eu.sunfishproject.icsp.pdp.PolicyCache.CachedPolicy;
import oasis.names.tc.xacml._3_0.core.schema.wd_17.PolicySetType;

public class Foo {
  public static void main(final String[] args)
      throws WebApplicationException, FileNotFoundException, IOException {
    final XACMLPolicyDefReader r = new XACMLPolicyDefReader();
    final PolicyDef pol = r.readFrom(PolicyDef.class, null, null,
        ExtendedMediaType.APPLICATION_XML_XACML_TYPE, null, new FileInputStream(
            "/home/bpruenster/Documents/2016-05 SUNFISH/playground/incubator-openaz/openaz-xacml-test/src/test/resources/testsets/spec/4.2.4.5-PolicySet.xml"));

    PolicyCache.cacheReferencedItem(new CachedPolicy(pol, new RestPRP("localhost:8080")));

    final PolicySetIdReference ref = new PolicySetIdReference();

    ref.setIdReferenceMatch(new StdIdReferenceMatch(pol.getIdentifier(), new StdVersionMatch(new int[] {
        1,
        -1 }), null, null));

    final PolicyDef x = PolicyCache.get(ref);

    System.out.println(x);
    final PolicyListPolicySetListType test = new PolicyListPolicySetListType();
    final PolicySetType tr = new XACMLPolicySetTypeReader().readFrom(PolicySetType.class, null, null,
        ExtendedMediaType.APPLICATION_XML_XACML_TYPE, null, new FileInputStream(
            "/home/bpruenster/Documents/2016-05 SUNFISH/playground/incubator-openaz/openaz-xacml-test/src/test/resources/testsets/spec/4.2.4.5-PolicySet.xml"));
    test.getPolicySet().add(tr);
    final ByteArrayOutputStream bOut = new ByteArrayOutputStream();
    final XACMLPolicyListPolicySetListTypeWriter pw = new XACMLPolicyListPolicySetListTypeWriter();
    pw.writeTo(test, List.class, null, null, ExtendedMediaType.APPLICATION_XML_XACML_TYPE, null, bOut);
    System.out.println(new String(bOut.toByteArray()));
    final ByteArrayInputStream bIn = new ByteArrayInputStream(bOut.toByteArray());
    final XACMLPolicyListPolicySetListTypeReader pr = new XACMLPolicyListPolicySetListTypeReader();

    final List<PolicyDef> readFrom = pr.readFrom(null, null, null,
        ExtendedMediaType.APPLICATION_XML_XACML_TYPE, null, bIn);

    System.out.println("done");

  }
}
