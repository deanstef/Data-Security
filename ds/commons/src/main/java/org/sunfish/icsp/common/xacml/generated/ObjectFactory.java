//
// Diese Datei wurde mit der JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.8-b130911.1802 generiert
// Siehe <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a>
// ?nderungen an dieser Datei gehen bei einer Neukompilierung des Quellschemas verloren.
// Generiert: 2016.06.09 um 03:40:57 PM CEST
//


package org.sunfish.icsp.common.xacml.generated;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlElementDecl;
import javax.xml.bind.annotation.XmlRegistry;
import javax.xml.namespace.QName;


/**
 * This object contains factory methods for each
 * Java content interface and Java element interface
 * generated in the org.sunfish.icsp.common.xacml.generated package.
 * <p>An ObjectFactory allows you to programatically
 * construct new instances of the Java representation
 * for XML content. The Java representation of XML
 * content can consist of schema derived interfaces
 * and classes representing the binding of schema
 * type definitions, element declarations and model
 * groups.  Factory methods for each of these are
 * provided in this class.
 *
 */
@XmlRegistry
public class ObjectFactory {

	private final static QName _PolicySets_QNAME = new QName("urn:sunfish:pap", "PolicySets");
	private final static QName _Policies_QNAME = new QName("urn:sunfish:pap", "Policies");
	private final static QName _PolicyList_QNAME = new QName("urn:sunfish:pap", "PolicyList");
	private final static QName _PolicySetList_QNAME = new QName("urn:sunfish:pap", "PolicySetList");
	private final static QName _IdReferenceList_QNAME = new QName("urn:sunfish:prp", "IdReferenceList");
	private final static QName _PolicyListPolicySetList_QNAME = new QName("urn:sunfish:prp", "PolicyListPolicySetList");

    private final static QName _SunfishMaskingObligationExpression_QNAME = new QName("urn:sunfish", "SunfishMaskingObligationExpression");



	/**
	 * Create a new ObjectFactory that can be used to create new instances of schema derived classes for package: org.sunfish.icsp.common.xacml.generated
	 *
	 */
	public ObjectFactory() {
	}

	/**
	 * Create an instance of {@link PolicySetsType }
	 *
	 */
	public PolicySetsType createPolicySetsType() {
		return new PolicySetsType();
	}

	/**
	 * Create an instance of {@link JAXBElement }{@code <}{@link PolicySetsType }{@code >}}
	 *
	 */
	@XmlElementDecl(namespace = "urn:sunfish:pap", name = "PolicySets")
	public JAXBElement<PolicySetsType> createPolicySets(final PolicySetsType value) {
		return new JAXBElement<>(_PolicySets_QNAME, PolicySetsType.class, null, value);
	}

	/**
	 * Create an instance of {@link PoliciesType }
	 *
	 */
	public PoliciesType createPoliciesType() {
		return new PoliciesType();
	}

	/**
	 * Create an instance of {@link JAXBElement }{@code <}{@link PoliciesType }{@code >}}
	 *
	 */
	@XmlElementDecl(namespace = "urn:sunfish:pap", name = "Policies")
	public JAXBElement<PoliciesType> createPolicies(final PoliciesType value) {
		return new JAXBElement<>(_Policies_QNAME, PoliciesType.class, null, value);
	}

	/**
	 * Create an instance of {@link JAXBElement }{@code <}{@link PolicyListType }{@code >}}
	 *
	 */
	@XmlElementDecl(namespace = "urn:sunfish:pap", name = "PolicyList")
	public JAXBElement<PolicyListType> createPolicyList(final PolicyListType value) {
		return new JAXBElement<>(_PolicyList_QNAME, PolicyListType.class, null, value);
	}

	/**
	 * Create an instance of {@link JAXBElement }{@code <}{@link PolicySetListType }{@code >}}
	 *
	 */
	@XmlElementDecl(namespace = "urn:sunfish:pap", name = "PolicySetList")
	public JAXBElement<PolicySetListType> createPolicySetList(final PolicySetListType value) {
		return new JAXBElement<>(_PolicySetList_QNAME, PolicySetListType.class, null, value);
	}

	 /**
     * Create an instance of {@link JAXBElement }{@code <}{@link IdReferenceListType }{@code >}}
     *
     */
    @XmlElementDecl(namespace = "urn:sunfish:prp", name = "IdReferenceList")
    public JAXBElement<IdReferenceListType> createIdReferenceList(final IdReferenceListType value) {
        return new JAXBElement<>(_IdReferenceList_QNAME, IdReferenceListType.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link PolicyListPolicySetListType }{@code >}}
     *
     */
    @XmlElementDecl(namespace = "urn:sunfish:prp", name = "PolicyListPolicySetList")
    public JAXBElement<PolicyListPolicySetListType> createPolicyListPolicySetList(final PolicyListPolicySetListType value) {
        return new JAXBElement<>(_PolicyListPolicySetList_QNAME, PolicyListPolicySetListType.class, null, value);
    }



}
