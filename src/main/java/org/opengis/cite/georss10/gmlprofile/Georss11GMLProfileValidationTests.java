package org.opengis.cite.georss10.gmlprofile;

import java.io.IOException;
import java.io.StringWriter;
import java.net.URL;

import javax.xml.XMLConstants;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.TransformerFactoryConfigurationError;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import javax.xml.validation.Validator;
import static org.testng.Assert.assertTrue;
import org.apache.xerces.dom.DeferredElementNSImpl;
import org.opengis.cite.georss10.DataFixture;
import org.opengis.cite.georss10.ETSAssert;
import org.opengis.cite.georss10.ErrorMessage;
import org.opengis.cite.georss10.ErrorMessageKeys;
import org.opengis.cite.validation.RelaxNGValidator;
import org.opengis.cite.validation.ValidationErrorHandler;
import org.testng.Assert;
import org.testng.annotations.Test;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

public class Georss11GMLProfileValidationTests extends DataFixture {

	/**
	 * Clause 7.4: Encoding of GeoRSS in a GML Profile
	 *
	 * @throws SAXException If the resource cannot be parsed.
	 * @throws IOException  If the resource is not accessible.
	 */
	@Test(description = "Implements Clause 7.4 of OGC 17-002r1")
	public void docHasValidGeoRSSGMLElements() throws SAXException, IOException {
		URL schemaRef = getClass().getResource("/org/opengis/cite/georss10/rnc/atom.rnc");
		RelaxNGValidator rngValidator = new RelaxNGValidator(schemaRef);

		try {

			URL schemaUrl = this.getClass().getResource("/org/opengis/cite/georss10/xsd/opengis/gml/3.1.1/gml-3.1.1.xsd");

			Schema schema = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI).newSchema(schemaUrl);
			Validator validator = schema.newValidator();
			
			NodeList whereList = testSubject.getElementsByTagNameNS("http://www.georss.org/georss", "where");
			int numWheres = whereList.getLength();
			
		
			assertTrue((numWheres>0),  "There were no georss:where elements found in the document" );
		
			
			for (int i = 0; i < numWheres; i++) {
				
	
			
				if(whereList.item(i).getClass().equals(DeferredElementNSImpl.class))
				{
	
					
					DeferredElementNSImpl whereElement = (DeferredElementNSImpl) whereList.item(i);
					
					String[] geometryTypes = { "Point", "LineString", "Polygon", "Envelope" };
					for (String geometryType : geometryTypes) {					
					 NodeList geomList = whereElement.getElementsByTagNameNS("http://www.opengis.net/gml", geometryType);
					 
			
					 
					 for(int j=0; j < geomList.getLength(); j++) {
						Source source = new DOMSource(geomList.item(j));
		
						ETSAssert.assertSchemaValid(validator, source);
					 }
					 
					}
					
				}
				

				
			}			


		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	/**
	 * Clause 6: There are currently two encodings of GeoRSS: Simple and GML...Both
	 * formats are designed for use with Atom 1.0, RSS 2.0 and RSS 1.0 Test Method:
	 * Verify the test subject is a valid Atom feed.
	 *
	 * @throws SAXException If the resource cannot be parsed.
	 * @throws IOException  If the resource is not accessible.
	 */
	@Test(description = "Implements Clause 6 of OGC 17-002r1")
	public void docIsValidAtomFeed() throws SAXException, IOException {
		URL schemaRef = getClass().getResource("/org/opengis/cite/georss10/rnc/atom.rnc");
		RelaxNGValidator rngValidator = new RelaxNGValidator(schemaRef);

		Source xmlSource = (null != testSubject) ? new DOMSource(testSubject) : null;

		rngValidator.validate(xmlSource);
		ValidationErrorHandler err = rngValidator.getErrorHandler();

		Assert.assertFalse(err.errorsDetected(),
				ErrorMessage.format(ErrorMessageKeys.NOT_SCHEMA_VALID, err.getErrorCount(), err.toString()));

	}

}
