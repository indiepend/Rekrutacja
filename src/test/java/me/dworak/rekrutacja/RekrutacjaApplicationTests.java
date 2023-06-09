package me.dworak.rekrutacja;

import org.junit.jupiter.api.Test;

import java.io.FileInputStream;
import java.io.IOException;

import static com.github.tomakehurst.wiremock.client.WireMock.notFound;
import static com.github.tomakehurst.wiremock.client.WireMock.ok;
import static com.github.tomakehurst.wiremock.client.WireMock.get;
import static com.github.tomakehurst.wiremock.client.WireMock.urlMatching;
import static org.springframework.http.HttpHeaders.ACCEPT;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.http.MediaType.APPLICATION_XML_VALUE;

class RekrutacjaApplicationTests extends IntegrationTestBase {

	private static final String CONTROLLER_URL = "/api/repositories";
	private static final String USER = "rdworak37";
	private static final String EMPTY_JSON = "[]";

	private static final String GH_API_REPO_REQ_URL = "/users/" + USER + "/repos";
	private static final String GH_API_BRANCH_REQ_URL = "/repos/" + USER + "/.*/branches";
	private static final String GH_API_CONTENT_TYPE = "application/vnd.github+json";

	@Test
	void applicationHappyPathTestUserWithRepos() throws IOException {
		//given
		server.stubFor(get(GH_API_REPO_REQ_URL)
				.willReturn(ok()
						.withHeader("Content-Type", GH_API_CONTENT_TYPE)
						.withBody(getJsonFile("reposResponse.json"))));

		server.stubFor(get(urlMatching(GH_API_BRANCH_REQ_URL))
				.willReturn(ok()
						.withHeader("Content-Type", GH_API_CONTENT_TYPE)
						.withBody(getJsonFile("branchResponse.json"))));

		//when then
		client.get()
				.uri(uriBuilder -> uriBuilder
						.path(CONTROLLER_URL)
						.queryParam("username", USER)
						.build())
				.header(ACCEPT,APPLICATION_JSON_VALUE)
				.exchange()
				.expectStatus()
				.is2xxSuccessful()
				.expectHeader()
				.contentType(APPLICATION_JSON)
				.expectBody()
				.json(getJsonFile("happyPathResponse.json"));
	}

	@Test
	void applicationHappyPathTestUserWithForkReposOnly() throws IOException {
		//given
		server.stubFor(get(GH_API_REPO_REQ_URL)
				.willReturn(ok()
						.withHeader("Content-Type", GH_API_CONTENT_TYPE)
						.withBody(getJsonFile("forkReposOnlyResponse.json"))));

		//when then
		client.get()
				.uri(uriBuilder -> uriBuilder
						.path(CONTROLLER_URL)
						.queryParam("username", USER)
						.build())
				.header(ACCEPT,APPLICATION_JSON_VALUE)
				.exchange()
				.expectStatus()
				.is2xxSuccessful()
				.expectHeader()
				.contentType(APPLICATION_JSON)
				.expectBody()
				.json(EMPTY_JSON);
	}

	@Test
	void applicationHappyPathTestUserWithoutRepos() {
		//given
		server.stubFor(get(GH_API_REPO_REQ_URL)
				.willReturn(ok()
						.withHeader("Content-Type", GH_API_CONTENT_TYPE)
						.withBody(EMPTY_JSON)));

		//when then
		client.get()
				.uri(uriBuilder -> uriBuilder
						.path(CONTROLLER_URL)
						.queryParam("username", USER)
						.build())
				.header(ACCEPT,APPLICATION_JSON_VALUE)
				.exchange()
				.expectStatus()
				.is2xxSuccessful()
				.expectHeader()
				.contentType(APPLICATION_JSON)
				.expectBody()
				.json(EMPTY_JSON);
	}

	@Test
	void applicationUnhappyPathTestInvalidAcceptHeader() {
		//when then
		client.get()
				.uri(uriBuilder -> uriBuilder
						.path(CONTROLLER_URL)
						.queryParam("username", USER)
						.build())
				.header(ACCEPT, APPLICATION_XML_VALUE)
				.exchange()
				.expectStatus()
				.isEqualTo(406)
				.expectHeader()
				.contentType(APPLICATION_JSON)
				.expectBody()
				.jsonPath("$.status").isEqualTo("406 NOT_ACCEPTABLE")
				.jsonPath("$.message").isEqualTo("No acceptable representation");
	}

	@Test
	void applicationUnhappyPathTestUserDoesNotExist() {
		//given
		server.stubFor(get(GH_API_REPO_REQ_URL)
				.willReturn(notFound()));

		//when then
		client.get()
				.uri(uriBuilder -> uriBuilder
						.path(CONTROLLER_URL)
						.queryParam("username", USER)
						.build())
				.header(ACCEPT, APPLICATION_JSON_VALUE)
				.exchange()
				.expectStatus()
				.isEqualTo(404)
				.expectHeader()
				.contentType(APPLICATION_JSON)
				.expectBody()
				.jsonPath("$.status").isEqualTo("404 NOT_FOUND")
				.jsonPath("$.message").isEqualTo("User with name " + USER + " was not found");
	}

	private String getJsonFile(String filename) throws IOException {
		String fileUri = getClass().getClassLoader().getResource(filename).getFile();
		FileInputStream fileInputStream = new FileInputStream(fileUri);
		return new String(fileInputStream.readAllBytes());
	}
}
