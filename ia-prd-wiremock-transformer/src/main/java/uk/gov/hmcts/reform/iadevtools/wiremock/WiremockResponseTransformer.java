package uk.gov.hmcts.reform.iadevtools.wiremock;

import static com.squareup.okhttp.internal.Internal.logger;

import com.github.tomakehurst.wiremock.client.ResponseDefinitionBuilder;
import com.github.tomakehurst.wiremock.common.FileSource;
import com.github.tomakehurst.wiremock.extension.Parameters;
import com.github.tomakehurst.wiremock.extension.ResponseDefinitionTransformer;
import com.github.tomakehurst.wiremock.http.Request;
import com.github.tomakehurst.wiremock.http.ResponseDefinition;
import java.io.IOException;

public class WiremockResponseTransformer extends ResponseDefinitionTransformer {

    @Override
    public ResponseDefinition transform(Request request, ResponseDefinition responseDefinition, FileSource files, Parameters parameters) {

        final String companyOneOrgId = "D1HRWLA";
        final String companyTwoOrgId = "0UFUG4Z";

        String userDetails = null;
        String shareCaseOrgId = null;
        String shareCaseAid = null;
        String shareCaseBid = null;
        String shareCaseOrg2Id = null;
        String shareCaseCid = null;
        String shareCaseDid = null;

        String userToken = request.getHeader("Authorization");
        String requestUrl = request.getUrl();

        /* Get the logged in user details */
        try {
            userDetails = new IdamHttpClient().getUserDetails(userToken);
        } catch (IOException e) {
            e.printStackTrace();
        }

        /* Internal endpoint required for ACA mock */
        if (requestUrl.contains("/refdata/external/v1/organisations/users") || requestUrl.contains("/refdata/internal/v1/organisations")) {

            shareCaseOrgId = parameters.getString("shareCaseOrgId");
            shareCaseAid = parameters.getString("shareCaseAid");
            shareCaseBid = parameters.getString("shareCaseBid");
            shareCaseOrg2Id = parameters.getString("shareCaseOrg2Id");
            shareCaseCid = parameters.getString("shareCaseCid");
            shareCaseDid = parameters.getString("shareCaseDid");
        }

        /* Get the list of organisation users */
        if (requestUrl.contains("/refdata/external/v1/organisations/users")) {

            if (userDetails.contains("@fake.hmcts.net")) {

                return new ResponseDefinitionBuilder()
                    .withHeader("Content-Type", "application/json")
                    .withStatus(200)
                    .withBody(getCompanyOneUsers(companyOneOrgId, shareCaseOrgId, shareCaseAid, shareCaseBid))
                    .build();
            } else if (userDetails.contains("@fake2.hmcts.net")) {

                return new ResponseDefinitionBuilder()
                    .withHeader("Content-Type", "application/json")
                    .withStatus(200)
                    .withBody(getCompanyTwoUsers(companyTwoOrgId, shareCaseOrg2Id, shareCaseCid, shareCaseDid))
                    .build();
            } else {
                logger.info("User email domain not recognized");
            }
        }

        /* Get the organisation details */
        if (requestUrl.contains("/refdata/external/v1/organisations")) {

            String transformationMsg = "Transforming wiremock response for user with domain: ";
            String organisationIdentifier = "";
            String organisationName = "";
            String superUserEmail = "";
            String addressLine1 = "";
            String addressLine2 = "";
            String addressLine3 = "";
            String postCode = "";

            if (userDetails != null) {

                if (userDetails.contains("@fake.hmcts.net")) {
                    logger.info(transformationMsg + "\"@fake.hmcts.net\"");
                    organisationIdentifier = companyOneOrgId;
                    organisationName = "Fake Org Ltd";
                    superUserEmail = "ia-law-firm-org-sc@fake.hmcts.net";
                    addressLine1 = "45 Lunar House";
                    addressLine2 = "Spa Road";
                    addressLine3 = "Woolworth";
                    postCode = "SE1 3HP";
                }

                if (userDetails.contains("@fake2.hmcts.net")) {
                    logger.info(transformationMsg + "\"@fake2.hmcts.net\"");
                    organisationIdentifier = companyTwoOrgId;
                    organisationName = "Fake Org2 Ltd";
                    superUserEmail = "ia-law-firm-org2-sc@fake2.hmcts.net";
                    addressLine1 = "145A";
                    addressLine2 = "Putney High Street";
                    addressLine3 = "London";
                    postCode = "SW15 1SU";
                }
            }

            return new ResponseDefinitionBuilder()
                .withHeader("Content-Type", "application/json")
                .withStatus(200)
                .withBody(" {"
                          + " \"organisationIdentifier\": \"" + organisationIdentifier + "\","
                          + " \"name\": \"" + organisationName + "\","
                          + " \"status\": \"ACTIVE\","
                          + " \"sraRegulated\": \"false\","
                          + " \"superUser\": {"
                          + " \"firstName\": \"legalrep\","
                          + " \"lastName\": \"orgcreator\","
                          + " \"email\": \"" + superUserEmail + "\""
                          + " },"
                          + " \"paymentAccount\": ["
                          + " \"PBA0087535\","
                          + " \"PBA0087240\","
                          + " \"PBA0088063\","
                          + " \"PBA0087442\""
                          + " ],"
                          + " \"contactInformation\": ["
                          + " {"
                          + " \"addressLine1\": \"" + addressLine1 + "\","
                          + " \"addressLine2\": \"" + addressLine2 + "\","
                          + " \"addressLine3\": \"" + addressLine3 + "\","
                          + " \"country\": \"UK\","
                          + " \"county\": \"London\","
                          + " \"postCode\": \"" + postCode + "\","
                          + " \"townCity\": \"London\","
                          + " \"dxAddress\": []"
                          + " }"
                          + " ]"
                          + " }")
                .build();
        }

        /* Internal endpoint required for ACA mock */
        if (requestUrl.contains("/refdata/internal/v1/organisations")) {

            if (requestUrl.contains("/" + companyOneOrgId + "/users")) {

                return new ResponseDefinitionBuilder()
                    .withHeader("Content-Type", "application/json")
                    .withStatus(200)
                    .withBody(getCompanyOneUsers(companyOneOrgId, shareCaseOrgId, shareCaseAid, shareCaseBid))
                    .build();
            }
            if (requestUrl.contains("/" + companyTwoOrgId + "/users")) {

                return new ResponseDefinitionBuilder()
                    .withHeader("Content-Type", "application/json")
                    .withStatus(200)
                    .withBody(getCompanyTwoUsers(companyTwoOrgId, shareCaseOrg2Id, shareCaseCid, shareCaseDid))
                    .build();
            }
        }
        return null;
    }

    public String getCompanyOneUsers(
        String companyOneOrgId,
        String shareCaseOrgId,
        String shareCaseAid,
        String shareCaseBid
    ) {
        return
            " {"
            + " \"organisationIdentifier\": \"" + companyOneOrgId + "\","
            + " \"users\": ["
            + " {"
            + " \"userIdentifier\": \"" + shareCaseOrgId + "\","
            + " \"firstName\": \"Org Creator\","
            + " \"lastName\": \"Legal Rep\","
            + " \"email\": \"ia-law-firm-org-sc@fake.hmcts.net\","
            + " \"roles\": ["
            + " \"caseworker-publiclaw-solicitor\","
            + " \"pui-case-manager\","
            + " \"caseworker\","
            + " \"caseworker-divorce-solicitor\","
            + " \"caseworker-ia\","
            + " \"pui-user-manager\","
            + " \"caseworker-publiclaw\","
            + " \"caseworker-ia-legalrep-solicitor\","
            + " \"caseworker-probate-solicitor\","
            + " \"pui-organisation-manager\","
            + " \"caseworker-divorce\","
            + " \"caseworker-divorce-financialremedy\","
            + " \"prd-admin\","
            + " \"pui-finance-manager\","
            + " \"caseworker-probate\","
            + " \"caseworker-divorce-financialremedy-solicitor\""
            + " ],"
            + " \"idamStatus\": \"ACTIVE\","
            + " \"idamStatusCode\": \"200\","
            + " \"idamMessage\": \"11 OK\""
            + " },"
            + " {"
            + " \"userIdentifier\": \"" + shareCaseAid + "\","
            + " \"firstName\": \"Share A\","
            + " \"lastName\": \"Legal Rep\","
            + " \"email\": \"ia-law-firm-a-sc@fake.hmcts.net\","
            + " \"roles\": ["
            + " \"caseworker-publiclaw-solicitor\","
            + " \"pui-case-manager\","
            + " \"caseworker\","
            + " \"caseworker-divorce\","
            + " \"caseworker-divorce-financialremedy\","
            + " \"caseworker-divorce-solicitor\","
            + " \"caseworker-divorce-financialremedy-solicitor\","
            + " \"caseworker-probate\","
            + " \"caseworker-probate-solicitor\","
            + " \"caseworker-publiclaw\","
            + " \"caseworker-ia\","
            + " \"caseworker-ia-legalrep-solicitor\""
            + " ],"
            + " \"idamStatus\": \"ACTIVE\","
            + " \"idamStatusCode\": \"200\","
            + " \"idamMessage\": \"11 OK\""
            + " },"
            + " {"
            + " \"userIdentifier\": \"" + shareCaseBid + "\","
            + " \"firstName\": \"Share B\","
            + " \"lastName\": \"Legal Rep\","
            + " \"email\": \"ia-law-firm-b-sc@fake.hmcts.net\","
            + " \"roles\": ["
            + " \"caseworker-publiclaw-solicitor\","
            + " \"pui-case-manager\","
            + " \"caseworker\","
            + " \"caseworker-divorce\","
            + " \"caseworker-divorce-financialremedy\","
            + " \"caseworker-divorce-solicitor\","
            + " \"caseworker-divorce-financialremedy-solicitor\","
            + " \"caseworker-probate\","
            + " \"caseworker-probate-solicitor\","
            + " \"caseworker-publiclaw\","
            + " \"caseworker-ia\","
            + " \"caseworker-ia-legalrep-solicitor\""
            + " ],"
            + " \"idamStatus\": \"ACTIVE\","
            + " \"idamStatusCode\": \"200\","
            + " \"idamMessage\": \"11 OK\""
            + " }"
            + " ]"
            + " }";
    }

    public String getCompanyTwoUsers(
        String companyTwoOrgId,
        String shareCaseOrg2Id,
        String shareCaseCid,
        String shareCaseDid
    ) {
        return
            " {"
            + " \"organisationIdentifier\": \"" + companyTwoOrgId + "\","
            + " \"users\": ["
            + " {"
            + " \"userIdentifier\": \"" + shareCaseOrg2Id + "\","
            + " \"firstName\": \"Org Creator2\","
            + " \"lastName\": \"Legal Rep\","
            + " \"email\": \"ia-law-firm-org2-sc@fake2.hmcts.net\","
            + " \"roles\": ["
            + " \"caseworker-publiclaw-solicitor\","
            + " \"pui-case-manager\","
            + " \"caseworker\","
            + " \"caseworker-divorce-solicitor\","
            + " \"caseworker-ia\","
            + " \"pui-user-manager\","
            + " \"caseworker-publiclaw\","
            + " \"caseworker-ia-legalrep-solicitor\","
            + " \"caseworker-probate-solicitor\","
            + " \"pui-organisation-manager\","
            + " \"caseworker-divorce\","
            + " \"caseworker-divorce-financialremedy\","
            + " \"prd-admin\","
            + " \"pui-finance-manager\","
            + " \"caseworker-probate\","
            + " \"caseworker-divorce-financialremedy-solicitor\""
            + " ],"
            + " \"idamStatus\": \"ACTIVE\","
            + " \"idamStatusCode\": \"200\","
            + " \"idamMessage\": \"11 OK\""
            + " },"
            + " {"
            + " \"userIdentifier\": \"" + shareCaseCid + "\","
            + " \"firstName\": \"Share C\","
            + " \"lastName\": \"Legal Rep\","
            + " \"email\": \"ia-law-firm-c-sc@fake2.hmcts.net\","
            + " \"roles\": ["
            + " \"caseworker-publiclaw-solicitor\","
            + " \"pui-case-manager\","
            + " \"caseworker\","
            + " \"caseworker-divorce\","
            + " \"caseworker-divorce-financialremedy\","
            + " \"caseworker-divorce-solicitor\","
            + " \"caseworker-divorce-financialremedy-solicitor\","
            + " \"caseworker-probate\","
            + " \"caseworker-probate-solicitor\","
            + " \"caseworker-publiclaw\","
            + " \"caseworker-ia\","
            + " \"caseworker-ia-legalrep-solicitor\""
            + " ],"
            + " \"idamStatus\": \"ACTIVE\","
            + " \"idamStatusCode\": \"200\","
            + " \"idamMessage\": \"11 OK\""
            + " },"
            + " {"
            + " \"userIdentifier\": \"" + shareCaseDid + "\","
            + " \"firstName\": \"Share D\","
            + " \"lastName\": \"Legal Rep\","
            + " \"email\": \"ia-law-firm-d-sc@fake2.hmcts.net\","
            + " \"roles\": ["
            + " \"caseworker-publiclaw-solicitor\","
            + " \"pui-case-manager\","
            + " \"caseworker\","
            + " \"caseworker-divorce\","
            + " \"caseworker-divorce-financialremedy\","
            + " \"caseworker-divorce-solicitor\","
            + " \"caseworker-divorce-financialremedy-solicitor\","
            + " \"caseworker-probate\","
            + " \"caseworker-probate-solicitor\","
            + " \"caseworker-publiclaw\","
            + " \"caseworker-ia\","
            + " \"caseworker-ia-legalrep-solicitor\""
            + " ],"
            + " \"idamStatus\": \"ACTIVE\","
            + " \"idamStatusCode\": \"200\","
            + " \"idamMessage\": \"11 OK\""
            + " }"
            + " ]"
            + " }";
    }

    @Override
    public boolean applyGlobally() {
        return false;
    }

    @Override
    public String getName() {
        return "body-transformer";
    }
}
