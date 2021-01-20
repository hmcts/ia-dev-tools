package uk.gov.hmcts.reform.iadevtools.ccd;

import static com.google.common.collect.Lists.newArrayList;
import static com.google.common.collect.Sets.newLinkedHashSet;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.io.Files;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Map;

import org.junit.Test;

public class CcdDefinitionConverterTest {

    /**
     * Test uses real CCD Excel file example:
     * It is equivalent to:
     * ./gradlew ccdToJson --args="CCD_DEFINITION_IA_V0.61.xlsx"
     */
    @SuppressWarnings("unchecked")
    @Test
    public void end2EndTest() throws IOException {

        String ccdFile = getClass().getResource(ccdDefinitionsSource).getFile();
        String ccdJsonString = converter.toJsonString(ccdFile);

        // assert valid JSON string
        Map ccd = new ObjectMapper().readValue(ccdJsonString, Map.class);
        assertThat(ccd.keySet(), is(newLinkedHashSet(excelSheets)));
        ccd.values().forEach(sheet ->
            assertThat(
                ((Map) sheet).keySet(),
                is(newLinkedHashSet(newArrayList("headers", "comments", "colNames", "rows")))
            )
        );
        String ccdExpectedJson = getClass().getResource(ccdDEfinitionsJson).getFile();
        assertThat(ccdJsonString, is(new String(Files.toByteArray(new File(ccdExpectedJson)), "UTF-8")));
    }

    private final CcdDefinitionConverter converter = new CcdDefinitionConverter();
    private final String ccdDefinitionsSource = "/CCD_DEFINITION_IA_V0.61.xlsx";
    private final String ccdDEfinitionsJson = "/CCD_DEFINITION_IA_V0.61.json";

    private final List<String> excelSheets = newArrayList(
        "Jurisdiction",
        "CaseField",
        "CaseType",
        "AuthorisationCaseField",
        "AuthorisationComplexType",
        "ComplexTypes",
        "FixedLists",
        "CaseTypeTab",
        "State",
        "CaseEvent",
        "AuthorisationCaseEvent",
        "CaseEventToFields",
        "SearchInputFields",
        "SearchResultFields",
        "WorkBasketInputFields",
        "WorkBasketResultFields",
        "UserProfile",
        "AuthorisationCaseType",
        "AuthorisationCaseState"
    );
}