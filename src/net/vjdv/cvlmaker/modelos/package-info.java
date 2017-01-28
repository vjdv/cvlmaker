@XmlSchema(
  namespace = "",
  elementFormDefault = XmlNsForm.QUALIFIED,
  xmlns = {
    @XmlNs(namespaceURI = "http://www.omg.org/XMI", prefix = "xmi"),
    @XmlNs(namespaceURI = "http://www.w3.org/2001/XMLSchema-instance", prefix = "xsi"),
    @XmlNs(namespaceURI = "http:///cvl.ecore", prefix = "cvl"),
  }
)
package net.vjdv.cvlmaker.modelos;

import javax.xml.bind.annotation.XmlNs;
import javax.xml.bind.annotation.XmlNsForm;
import javax.xml.bind.annotation.XmlSchema;