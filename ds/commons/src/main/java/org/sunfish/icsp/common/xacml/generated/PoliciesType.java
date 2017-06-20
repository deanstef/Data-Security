//
// Diese Datei wurde mit der JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.8-b130911.1802 generiert 
// Siehe <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// �nderungen an dieser Datei gehen bei einer Neukompilierung des Quellschemas verloren. 
// Generiert: 2016.06.10 um 03:22:11 PM CEST 
//


package org.sunfish.icsp.common.xacml.generated;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;

import oasis.names.tc.xacml._3_0.core.schema.wd_17.PolicyType;


/**
 * <p>Java-Klasse f�r PoliciesType complex type.
 * 
 * <p>Das folgende Schemafragment gibt den erwarteten Content an, der in dieser Klasse enthalten ist.
 * 
 * <pre>
 * &lt;complexType name="PoliciesType">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="RootPolicy" type="{http://www.w3.org/2001/XMLSchema}boolean"/>
 *         &lt;element ref="{urn:oasis:names:tc:xacml:3.0:core:schema:wd-17}Policy"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "PoliciesType", namespace = "urn:sunfish:pap", propOrder = {
    "rootPolicy",
    "policy"
})
public class PoliciesType {

    @XmlElement(name = "RootPolicy")
    protected boolean rootPolicy;
    @XmlElement(name = "Policy", namespace = "urn:oasis:names:tc:xacml:3.0:core:schema:wd-17", required = true)
    protected PolicyType policy;

    /**
     * Ruft den Wert der rootPolicy-Eigenschaft ab.
     * 
     */
    public boolean isRootPolicy() {
        return rootPolicy;
    }

    /**
     * Legt den Wert der rootPolicy-Eigenschaft fest.
     * 
     */
    public void setRootPolicy(boolean value) {
        this.rootPolicy = value;
    }

    /**
     * Ruft den Wert der policy-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link PolicyType }
     *     
     */
    public PolicyType getPolicy() {
        return policy;
    }

    /**
     * Legt den Wert der policy-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link PolicyType }
     *     
     */
    public void setPolicy(PolicyType value) {
        this.policy = value;
    }

}
