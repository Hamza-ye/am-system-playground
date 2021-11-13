package org.nmcpye.activitiesmanagement.extended.schemamodule;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.junit.jupiter.api.Test;
import org.nmcpye.activitiesmanagement.AMTest;
import org.nmcpye.activitiesmanagement.IntegrationTest;
import org.nmcpye.activitiesmanagement.extended.feedback.ErrorCode;
import org.nmcpye.activitiesmanagement.extended.feedback.ErrorReport;
import org.nmcpye.activitiesmanagement.extended.schema.annotation.PropertyRange;
import org.nmcpye.activitiesmanagement.extended.schemamodule.validation.SchemaValidator;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@IntegrationTest
public class SchemaValidatorTest
    extends AMTest {
    @Autowired
    private SchemaValidator schemaValidator;

    @Test
    public void testCollectionOutOfMinRange() {
        TestCollectionSize objectUnderTest = new TestCollectionSize();
        List<ErrorReport> errorReports = schemaValidator.validate(objectUnderTest, false);

        assertEquals(1, errorReports.size());
        assertEquals(ErrorCode.E4007, errorReports.get(0).getErrorCode());
    }

    @Test
    public void testCollectionOutOfMaxRange() {
        TestCollectionSize objectUnderTest = new TestCollectionSize();
        objectUnderTest.getItems().add("Item #1");
        objectUnderTest.getItems().add("Item #2");
        objectUnderTest.getItems().add("Item #3");

        List<ErrorReport> errorReports = schemaValidator.validate(objectUnderTest, false);

        assertEquals(1, errorReports.size());
        assertEquals(ErrorCode.E4007, errorReports.get(0).getErrorCode());
    }

    @Test
    public void testCollectionInRange() {
        TestCollectionSize objectUnderTest = new TestCollectionSize();
        objectUnderTest.getItems().add("Item #1");
        objectUnderTest.getItems().add("Item #2");

        List<ErrorReport> errorReports = schemaValidator.validate(objectUnderTest, false);

        assertTrue(errorReports.isEmpty());
    }

    public static class TestCollectionSize {
        private List<String> items = new ArrayList<>();

        public TestCollectionSize() {
        }

        @JsonProperty
        @PropertyRange(min = 1, max = 2)
        public List<String> getItems() {
            return items;
        }

        public void setItems(List<String> items) {
            this.items = items;
        }
    }
}
