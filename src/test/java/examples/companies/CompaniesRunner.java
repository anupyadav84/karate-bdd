package examples.companies;

import com.github.tomakehurst.wiremock.WireMockServer;
import com.intuit.karate.junit4.Karate;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.runner.RunWith;

import static com.github.tomakehurst.wiremock.client.WireMock.aResponse;
import static com.github.tomakehurst.wiremock.client.WireMock.configureFor;
import static com.github.tomakehurst.wiremock.client.WireMock.containing;
import static com.github.tomakehurst.wiremock.client.WireMock.equalTo;
import static com.github.tomakehurst.wiremock.client.WireMock.get;
import static com.github.tomakehurst.wiremock.client.WireMock.post;
import static com.github.tomakehurst.wiremock.client.WireMock.stubFor;
import static com.github.tomakehurst.wiremock.client.WireMock.urlEqualTo;
import static com.github.tomakehurst.wiremock.client.WireMock.urlMatching;

@RunWith(Karate.class)
public class CompaniesRunner {

    private static final WireMockServer wireMockServer = new WireMockServer();

    private static final String URL = "/companies";

    @BeforeClass
    public static void setUp() {
        wireMockServer.start();

        configureFor("localhost", 8080);

        stubForGetAllCompanies();

        stufForGetCompanyByCIF("B84946656", getParadigmaDigitalCompany());
        stufForGetCompanyByCIF("B82627019", getMinsaitCompany());

        stubForCreateCompany("B18996504", "Stratio", "info@stratio.com");
    }

    private static void stubForGetAllCompanies() {
        stubFor(get(urlEqualTo(URL))
                .willReturn(aResponse()
                        .withStatus(200)
                        .withHeader("Content-Type", "application/json")
                        .withBody(getAllCompanies())));
    }

    private static void stufForGetCompanyByCIF(String cif, String companyByCIFResponse) {
        stubFor(get(urlMatching(URL + "/" + cif))
                .willReturn(aResponse()
                        .withStatus(200)
                        .withHeader("Content-Type", "application/json")
                        .withBody(companyByCIFResponse)));
    }

    private static String getAllCompanies() {
        return "[" + getParadigmaDigitalCompany() + ", " + getMinsaitCompany() + "]";
    }

    private static String getParadigmaDigitalCompany() {
        return "{" +
                " \"cif\":\"B84946656\"," +
                " \"name\":\"Paradigma Digital\"," +
                " \"username\":\"paradigmadigital\"," +
                " \"email\":\"info@paradigmadigital.com\"," +
                " \"address\":{" +
                "    \"street\":\"Atica 4, Via de las Dos Castillas\"," +
                "    \"suite\":\"33\"," +
                "    \"city\":\"Pozuelo de Alarcon, Madrid\"," +
                "    \"zipcode\":\"28224\"" +
                " }," +
                " \"website\":\"https://www.paradigmadigital.com\"" +
                "}";
    }

    private static String getMinsaitCompany() {
        return "{" +
                " \"cif\":\"B82627019\"," +
                " \"name\":\"Minsait by Indra\"," +
                " \"username\":\"minsaitbyindra\"," +
                " \"email\":\"info@minsait.com\"," +
                " \"address\":{" +
                "    \"street\":\"Av. de Bruselas\"," +
                "    \"suite\":\"35\"," +
                "    \"city\":\"Alcobendas, Madrid\"," +
                "    \"zipcode\":\"28108\"" +
                " }," +
                " \"website\":\"https://www.minsait.com\"" +
                "}";
    }

    private static void stubForCreateCompany(String cif, String name, String email) {
        stubFor(post(urlEqualTo(URL))
                .withHeader("content-type", equalTo("application/json"))
                .withRequestBody(containing("cif"))
                .withRequestBody(containing("name"))
                .withRequestBody(containing("email"))
                .willReturn(aResponse()
                        .withStatus(201)
                        .withHeader("Content-Type", "application/json")
                        .withBody(
                                "{" +
                                        "   \"cif\": \"" + cif + "\"," +
                                        "   \"name\": \"" + name + "\"," +
                                        "   \"email\": \"" + email + "\"" +
                                        "}")));
    }

    @AfterClass
    public static void tearDown() {
        wireMockServer.stop();
    }

}
