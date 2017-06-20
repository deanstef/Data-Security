//
// Diese Datei wurde mit der JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.8-b130911.1802 generiert 
// Siehe <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// �nderungen an dieser Datei gehen bei einer Neukompilierung des Quellschemas verloren. 
// Generiert: 2016.06.10 um 03:23:01 PM CEST 
//


package org.sunfish.icsp.common.xacml.generated;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;

import oasis.names.tc.xacml._3_0.core.schema.wd_17.PolicySetType;


/**
 * <p>Java-Klasse f�r PolicySetsType complex type.
 * 
 * <p>Das folgende Schemafragment gibt den erwarteten Content an, der in dieser Klasse enthalten ist.
 * 
 * <pre>
 * &lt;complexType name="PolicySetsType">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="RootPolicySet" type="{http://www.w3.org/2001/XMLSchema}boolean"/>
 *         &lt;element ref="{urn:oasis:names:tc:xacml:3.0:core:schema:wd-17}PolicySet"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "PolicySetsType", namespace = "urn:sunfish:pap", propOrder = {
    "rootPolicySet",
    "policySet"
})
public class PolicySetsType {

    @XmlElement(name = "RootPolicySet")
    protected boolean rootPolicySet;
    @XmlElement(name = "PolicySet", namespace = "urn:oasis:names:tc:xacml:3.0:core:schema:wd-17", required = true)
    protected PolicySetType policySet;

    /**
     * Ruft den Wert der rootPolicySet-Eigenschaft ab.
     * 
     */
    public boolean isRootPolicySet() {
        return rootPolicySet;
    }

    /**
     * Legt den Wert der rootPolicySet-Eigenschaft fest.
     * 
     */
    public void setRootPolicySet(boolean value) {
        this.rootPolicySet = value;
    }

    /**
     * Ruft den Wert der policySet-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link PolicySetType }
     *     
     */
    public PolicySetType getPolicySet() {
        return policySet;
    }

    /**
     * Legt den Wert der policySet-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link PolicySetType }
     *     
     */
    public void setPolicySet(PolicySetType value) {
        this.policySet = value;
    }

}
