//
// Diese Datei wurde mit der JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.8-b130911.1802 generiert 
// Siehe <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// �nderungen an dieser Datei gehen bei einer Neukompilierung des Quellschemas verloren. 
// Generiert: 2016.06.12 um 04:15:16 PM CEST 
//


package org.sunfish.icsp.common.xacml.generated;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;

import oasis.names.tc.xacml._3_0.core.schema.wd_17.PolicySetType;


/**
 * <p>Java-Klasse f�r PolicySetListType complex type.
 * 
 * <p>Das folgende Schemafragment gibt den erwarteten Content an, der in dieser Klasse enthalten ist.
 * 
 * <pre>
 * &lt;complexType name="PolicySetListType">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element ref="{urn:oasis:names:tc:xacml:3.0:core:schema:wd-17}PolicySet" maxOccurs="unbounded"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "PolicySetListType", namespace = "urn:sunfish:pap", propOrder = {
    "policySet"
})
public class PolicySetListType {

    @XmlElement(name = "PolicySet", namespace = "urn:oasis:names:tc:xacml:3.0:core:schema:wd-17", required = true)
    protected List<PolicySetType> policySet;

    /**
     * Gets the value of the policySet property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the policySet property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getPolicySet().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link PolicySetType }
     * 
     * 
     */
    public List<PolicySetType> getPolicySet() {
        if (policySet == null) {
            policySet = new ArrayList<PolicySetType>();
        }
        return this.policySet;
    }

}
