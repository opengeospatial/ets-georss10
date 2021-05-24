package org.opengis.cite.georss10.level1;

import java.net.URL;

import javax.xml.XMLConstants;
import javax.xml.transform.Source;
import javax.xml.transform.dom.DOMSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import javax.xml.validation.Validator;

import org.opengis.cite.georss10.DataFixture;
import org.opengis.cite.georss10.ETSAssert;
import org.testng.annotations.Test;
import org.xml.sax.SAXException;

public class Georss11ValidationTests extends DataFixture{

    @Test
    public void Georss11Validation() {
        URL schemaUrl = this.getClass().getResource("/org/opengis/cite/georss10/xsd/opengis/georss/1.1/georss.xsd");
        Source source = new DOMSource(this.testSubject);
        Validator validator;
        try {
            Schema schema = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI).newSchema(schemaUrl);
            validator = schema.newValidator();
            ETSAssert.assertSchemaValid(validator, source);
        } catch (SAXException e) {
            e.printStackTrace();
        }
    }

}
