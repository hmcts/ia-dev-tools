package uk.gov.hmcts.reform.iadevtools.ccd;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

import com.google.common.io.Files;

import java.io.File;
import java.io.IOException;

import org.junit.Test;


public class CcdDefinitionComparatorTest {

    /**
     * Test uses real CCD Excel file examples:
     * It is equivalent to:
     * ./gradlew ccdToCompare --args="CCD_DEFINITION_IA_V0.61.xlsx CCD_DEFINITION_IA_V0.62.xlsx"
     */
    @Test
    public void end2EndTest() throws IOException {
        String ccdFile1 = getClass().getResource(ccdDefinitionsSourceV61).getFile();
        String ccdFile2 = getClass().getResource(ccdDefinitionsSourceV62).getFile();

        String ccdComparison = comparator.compare(ccdFile1, ccdFile2);
        String ccdExpectedTxt = getClass().getResource(ccdComparisonSource).getFile();
        assertThat(ccdComparison, is(new String(Files.toByteArray(new File(ccdExpectedTxt)), "UTF-8")));
    }

    private final CcdDefinitionComparator comparator = new CcdDefinitionComparator();
    private final String ccdDefinitionsSourceV61 = "/CCD_DEFINITION_IA_V0.61.xlsx";
    private final String ccdDefinitionsSourceV62 = "/CCD_DEFINITION_IA_V0.62.xlsx";
    private final String ccdComparisonSource = "/V0.61vsV0.62results.txt";

}