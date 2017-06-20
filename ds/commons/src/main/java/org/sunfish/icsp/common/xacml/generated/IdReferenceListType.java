//
// Diese Datei wurde mit der JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.8-b130911.1802 generiert 
// Siehe <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Änderungen an dieser Datei gehen bei einer Neukompilierung des Quellschemas verloren. 
// Generiert: 2016.09.07 um 09:57:24 AM CEST 
//


package org.sunfish.icsp.common.xacml.generated;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;

import oasis.names.tc.xacml._3_0.core.schema.wd_17.IdReferenceType;


/**
 * <p>Java-Klasse f�r IdReferenceListType complex type.
 * 
 * <p>Das folgende Schemafragment gibt den erwarteten Content an, der in dieser Klasse enthalten ist.
 * 
 * <pre>
 * &lt;complexType name="IdReferenceListType">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="PolicyIdReference" type="{urn:oasis:names:tc:xacml:3.0:core:schema:wd-17}IdReferenceType" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element name="PolicySetIdReference" type="{urn:oasis:names:tc:xacml:3.0:core:schema:wd-17}IdReferenceType" maxOccurs="unbounded" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "IdReferenceListType", namespace = "urn:sunfish:prp", propOrder = {
    "policyIdReference",
    "policySetIdReference"
})
public class IdReferenceListType {

    @XmlElement(name = "PolicyIdReference")
    protected List<IdReferenceType> policyIdReference;
    @XmlElement(name = "PolicySetIdReference")
    protected List<IdReferenceType> policySetIdReference;

    /**
     * Gets the value of the policyIdReference property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the policyIdReference property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getPolicyIdReference().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link IdReferenceType }
     * 
     * 
     */
    public List<IdReferenceType> getPolicyIdReference() {
        if (policyIdReference == null) {
            policyIdReference = new ArrayList<IdReferenceType>();
        }
        return this.policyIdReference;
    }

    /**
     * Gets the value of the policySetIdReference property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the policySetIdReference property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getPolicySetIdReference().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link IdReferenceType }
     * 
     * 
     */
    public List<IdReferenceType> getPolicySetIdReference() {
        if (policySetIdReference == null) {
            policySetIdReference = new ArrayList<IdReferenceType>();
        }
        return this.policySetIdReference;
    }

}
