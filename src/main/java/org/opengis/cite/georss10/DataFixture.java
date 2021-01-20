package org.opengis.cite.georss10;

import org.testng.ITestContext;
import org.testng.annotations.BeforeClass;
import org.w3c.dom.Document;

/**
 * Includes various tests of capability 1.
 */
public class DataFixture {

    protected Document testSubject;

    /**
     * Obtains the test subject from the ISuite context. The suite attribute
     * {@link org.opengis.cite.georss10.SuiteAttribute#TEST_SUBJECT} should
     * evaluate to a DOM Document node.
     * 
     * @param testContext
     *            The test (group) context.
     */
    @BeforeClass
    public void obtainTestSubject(ITestContext testContext) {
        Object obj = testContext.getSuite().getAttribute(SuiteAttribute.TEST_SUBJECT.getName());
        if ((null != obj) && Document.class.isAssignableFrom(obj.getClass())) {
            this.testSubject = Document.class.cast(obj);
        }
    }

    /**
     * Sets the test subject. This method is intended to facilitate unit
     * testing.
     *
     * @param testSubject
     *            A Document node representing the test subject or metadata
     *            about it.
     */
    public void setTestSubject(Document testSubject) {
        this.testSubject = testSubject;
    }
}
