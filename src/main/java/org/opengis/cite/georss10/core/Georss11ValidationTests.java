package org.opengis.cite.georss10.core;

import static org.testng.Assert.assertTrue;

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
import org.apache.xerces.dom.DeferredElementNSImpl;
import org.opengis.cite.georss10.DataFixture;
import org.opengis.cite.georss10.ETSAssert;
import org.opengis.cite.georss10.ErrorMessage;
import org.opengis.cite.georss10.ErrorMessageKeys;
import org.opengis.cite.validation.RelaxNGValidator;
import org.opengis.cite.validation.ValidationErrorHandler;
import org.testng.Assert;
import org.testng.SkipException;
import org.testng.annotations.Test;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

/**
 * <p>
 * Georss11ValidationTests class.
 * </p>
 *
 */
public class Georss11ValidationTests extends DataFixture {

	/**
	 * Clause 5.1: Use version 1.1 of the GeoRSS schema. The 1.0 version of the schema is
	 * still available for backwards compatibility. Clause 7.3: GeoRSS Simple
	 * Serialization Test Method: Verify the test subject uses valid where or geometry
	 * elements
	 * @throws org.xml.sax.SAXException If the resource cannot be parsed.
	 * @throws java.io.IOException If the resource is not accessible.
	 */
	@Test(description = "Implements Clause 5.1 and Clause 7.3 of OGC 17-002r1")
	public void docHasValidSimpleGeoRSSElements() throws SAXException, IOException {

		URL schemaUrl = this.getClass().getResource("/org/opengis/cite/georss10/xsd/opengis/georss/1.1/georss.xsd");

		Schema schema = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI).newSchema(schemaUrl);
		Validator validator = schema.newValidator();

		String[] geometryTypes = { "point", "line", "polygon", "box", "elev", "floor", "radius" };

		// check if there are any geometries

		boolean geometriesFound = false;

		for (String geometryType : geometryTypes) {
			NodeList geomList = testSubject.getElementsByTagNameNS("http://www.georss.org/georss", geometryType);
			int numGeoms = geomList.getLength();
			if (numGeoms > 0)
				geometriesFound = true;
		}

		if (geometriesFound) { // Simple GeoRSS Conformance
			for (String geometryType : geometryTypes) {
				NodeList geomList = testSubject.getElementsByTagNameNS("http://www.georss.org/georss", geometryType);
				int numGeoms = geomList.getLength();
				for (int i = 0; i < numGeoms; i++) {

					Source source = new DOMSource(geomList.item(i));
					ETSAssert.assertSchemaValid(validator, source);

				}

			}

		}
		else if (testSubject.getElementsByTagNameNS("http://www.georss.org/georss", "where").getLength() > 0) { // GeoRSS
																												// GML
																												// Conformance

			throw new SkipException(
					"Detected a GeoRSS GML document. See the results for the docHasValidGMLGeoRSSElements() test.");

		}
		else {

			assertTrue(geometriesFound,
					"There were no georss geometry elements nor where elements found in the document. Fails Clause 7.3 and Clause 7.4");

		}

	}

	/**
	 * Clause 5.1: Use version 1.1 of the GeoRSS schema. The 1.0 version of the schema is
	 * still available for backwards compatibility. Clause 7.4: Encoding of GeoRSS in a
	 * GML Profile
	 * @throws org.xml.sax.SAXException If the resource cannot be parsed.
	 * @throws java.io.IOException If the resource is not accessible.
	 */
	@Test(description = "Implements Clause 5.1 and Clause 7.4 of OGC 17-002r1")
	public void docHasValidGMLGeoRSSElements() throws SAXException, IOException {

		URL schemaUrl = this.getClass().getResource("/org/opengis/cite/georss10/xsd/opengis/georss/1.1/georss.xsd");

		Schema schema = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI).newSchema(schemaUrl);
		Validator validator = schema.newValidator();

		String[] geometryTypes = { "point", "line", "polygon", "box", "elev", "floor", "radius" };

		// check if there are any geometries

		boolean geometriesFound = false;

		for (String geometryType : geometryTypes) {
			NodeList geomList = testSubject.getElementsByTagNameNS("http://www.georss.org/georss", geometryType);
			int numGeoms = geomList.getLength();
			if (numGeoms > 0)
				geometriesFound = true;
		}

		if (geometriesFound) { // Simple GeoRSS Conformance
			System.out.println("CHK 1");
			throw new SkipException(
					"Detected a Simple GeoRSS document. See the results for the docHasValidSimpleGeoRSSElements() test.");

		}
		else if (testSubject.getElementsByTagNameNS("http://www.georss.org/georss", "where").getLength() > 0) { // GeoRSS
																												// GML
																												// Conformance

			URL schemaUrl2 = this.getClass()
				.getResource("/org/opengis/cite/georss10/xsd/opengis/gml/3.1.1/gml-3.1.1.xsd");

			Schema schema2 = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI).newSchema(schemaUrl2);
			Validator validator2 = schema2.newValidator();

			NodeList whereList = testSubject.getElementsByTagNameNS("http://www.georss.org/georss", "where");
			int numWheres = whereList.getLength();

			for (int i = 0; i < numWheres; i++) {

				if (whereList.item(i).getClass().equals(DeferredElementNSImpl.class)) {

					DeferredElementNSImpl whereElement = (DeferredElementNSImpl) whereList.item(i);

					String[] geometryTypes2 = { "Point", "LineString", "Polygon", "Envelope" };
					for (String geometryType2 : geometryTypes2) {
						NodeList geomList2 = whereElement.getElementsByTagNameNS("http://www.opengis.net/gml",
								geometryType2);

						for (int j = 0; j < geomList2.getLength(); j++) {
							Source source2 = new DOMSource(geomList2.item(j));
							ETSAssert.assertSchemaValid(validator2, source2);

						}

					}

				}

			}

		}
		else {

			assertTrue(geometriesFound,
					"There were no georss geometry elements nor where elements found in the document. Fails Clause 7.3 and Clause 7.4");

		}

	}

	/**
	 * Clause 6: There are currently two encodings of GeoRSS: Simple and GML...Both
	 * formats are designed for use with Atom 1.0, RSS 2.0 and RSS 1.0 Test Method: Verify
	 * the test subject is a valid Atom feed.
	 * @throws org.xml.sax.SAXException If the resource cannot be parsed.
	 * @throws java.io.IOException If the resource is not accessible.
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
