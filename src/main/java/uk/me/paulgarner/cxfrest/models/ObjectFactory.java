package uk.me.paulgarner.cxfrest.models;

import javax.xml.bind.annotation.XmlRegistry;

@XmlRegistry
public class ObjectFactory {
 
    public ObjectFactory() {
    }
 
    public MailMessage createMailMessage() {
        return new MailMessage();
    }
}
