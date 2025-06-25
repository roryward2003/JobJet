package job.jet;

import job.jet.service.ProfileService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import com.jayway.jsonpath.JsonPath;

@SpringBootTest
@AutoConfigureMockMvc
class JobJetApplicationTests {
	
    @Autowired
    private MockMvc mockMvc;

	@Autowired
    private ProfileService profileService;

	///////////////
	// P2P TESTS //
	///////////////
	
	// Check that the application context loads correctly
	@Test
	void contextLoads() {
	}

	// Check that the '/auth/login' endpoint with valid user data returns status 200 OK
	@Test
	void verifyLoginWithValidUserStatus() throws Exception {
        
		// Test user's login data
		String requestBody = """
			{
				"username": "test@test.com",
				"password": "Password123!"
			}
		""";

		// Assert that the login endpoint returned status 200 OK
		mockMvc.perform(post("/auth/login")
				.contentType(MediaType.APPLICATION_JSON)
				.content(requestBody))
				.andExpect(status().isOk());
	}
	
	// Check that the '/auth/login' endpoint with invalid user data doesn't return a token
	@Test
	void verifyLoginWithInvalidUserDataToken() throws Exception {
        
		// Test user's login data
		String requestBody = """
			{
				"username": "invalidusernmae@test.com",
				"password": "invalidpassword"
			}
		""";

		// Assert that the login endpoint did not return a jwt token
		mockMvc.perform(post("/auth/login")
				.contentType(MediaType.APPLICATION_JSON)
				.content(requestBody))
				.andExpect(jsonPath("$.token").doesNotExist());
	}

	///////////////////
	// Non P2P Tests //
	///////////////////
	
	// Check that the '/auth/login' endpoint with valid user data returns an auth token
	@Test
	void verifyLoginWithValidUserToken() throws Exception {
        
		// Test user's login data
		String requestBody = """
			{
				"username": "test@test.com",
				"password": "Password123!"
			}
		""";

		// Assert that the login endpoint returned a jwt token
		mockMvc.perform(post("/auth/login")
				.contentType(MediaType.APPLICATION_JSON)
				.content(requestBody))
				.andExpect(jsonPath("$.token").exists());
	}

	// Check that the '/auth/login' endpoint with valid user data returns an auth token
	@Test
	void verifyAuthTokensExpire() throws Exception {
        
		// Test user's login data
		String requestBody = """
			{
				"username": "test@test.com",
				"password": "Password123!"
			}
		""";

		// Assert that the jwt token has an expiry
		mockMvc.perform(post("/auth/login")
				.contentType(MediaType.APPLICATION_JSON)
				.content(requestBody))
				.andExpect(jsonPath("$.expiresIn").exists());
	}

	// Check that the '/auth/login' endpoint with invalid username returns status UNAUTHORIZED 401
	@Test
	void verifyLoginWithInvalidUsernameStatus() throws Exception {

        // Test user's login data
		String requestBody = """
			{
				"username": "invalidusername@test.com",
				"password": "Password123!"
			}
		""";

		// Assert that the login endpoint returned status UNAUTHORIZED 401
		mockMvc.perform(post("/auth/login")
				.contentType(MediaType.APPLICATION_JSON)
				.content(requestBody))
				.andExpect(status().isUnauthorized());
	}

	// Check that the '/auth/login' endpoint with invalid password returns status UNAUTHORIZED 401
	@Test
	void verifyLoginWithInvalidPasswordStatus() throws Exception {

        // Test user's login data
		String requestBody = """
			{
				"username": "test@test.com",
				"password": "invalidpassword"
			}
		""";

		// Assert that the login endpoint returned status UNAUTHORIZED 401
		mockMvc.perform(post("/auth/login")
				.contentType(MediaType.APPLICATION_JSON)
				.content(requestBody))
				.andExpect(status().isUnauthorized());
	}

	// Check that '/id' endpoint traffic is intercepted by the jwt authentication filter
	@Test
	void verifyAuthInterceptsIdWithoutToken() throws Exception {

		// Assert that the id endpoint returned status FORBIDDEN 403
        mockMvc.perform(get("/jobjet/id"))
				.andExpect(status().isForbidden());
	}

	// Check that valid '/id' endpoint traffic is allowed through by the jwt authentication filter
	@Test
	void verifyAuthAllowsIdWithValidToken() throws Exception {

		// Test user's login data
		String requestBody = """
			{
				"username": "test@test.com",
				"password": "Password123!"
			}
		""";

		// Get a valid auth token through the 'auth/login' endpoint
		MvcResult result = mockMvc.perform(post("/auth/login")
				.contentType(MediaType.APPLICATION_JSON)
				.content(requestBody))
    			.andExpect(status().isOk())
				.andExpect(jsonPath("$.token").exists())
				.andReturn();
			
		// Parse the jwt token from the login response
		String responseBody = result.getResponse().getContentAsString();
		String token = JsonPath.read(responseBody, "$.token");

		// Use the valid jwt token to fetch the user id
		mockMvc.perform(get("/jobjet/id")
        		.header("Authorization", "Bearer " + token))
    			.andExpect(status().isOk())
				.andExpect(content().string("6859d9bfa8fc1117fb4008ff"));
	}

	// Check that the '/signup' endpoint returns status 201 CREATED on success
	@Test
	void verifySignupStatus() throws Exception {

		// Clear the created user if it wasn't deleted properly after the last test
		deleteCreationTestProfile();

		// Test user's singup data
		String requestBody = """
			{
				"username": "createduser@test.com",
				"password": "Password123!",
				"name": "created user",
				"keywords": "accounting",
				"location": "oregon",
				"jobType": "full-time"
			}
		""";

		MvcResult result = null;
		String id;

		try {
			// Create a user through the '/auth/signup' endpoint
			result = mockMvc.perform(post("/auth/signup")
					.contentType(MediaType.APPLICATION_JSON)
					.content(requestBody))
					.andExpect(status().isCreated())
					.andReturn();
				
		} finally {
			// Delete the created user if creation was succesfful
			deleteCreationTestProfile();
		}
	}

	// Check that the '/signup' endpoint creates non verified accounts
	@Test
	void verifySignupCreatesNonVerifiedAccounts() throws Exception {

		// Clear the created user if it wasn't deleted properly after the last test
		deleteCreationTestProfile();

		// Test user's singup data
		String requestBody = """
			{
				"username": "createduser@test.com",
				"password": "Password123!",
				"name": "created user",
				"keywords": "accounting",
				"location": "oregon",
				"jobType": "full-time"
			}
		""";

		MvcResult result = null;
		String id;

		try {
			// Create a user through the '/auth/signup' endpoint and check its verification is false
			result = mockMvc.perform(post("/auth/signup")
					.contentType(MediaType.APPLICATION_JSON)
					.content(requestBody))
					.andExpect(status().isCreated())
    				.andExpect(jsonPath("$.verified").value(false))
					.andReturn();
				
		} finally {
			// Delete the created user if creation was succesfful
			deleteCreationTestProfile();
		}
	}

	// Helper method for clearing the created user for '/signup' tests
	private void deleteCreationTestProfile() {
		try {
			// Delete the test user
			profileService.deleteProfileByUsername("createduser@test.com");
		} catch (Throwable t) {
			// Silently ignore assertion failures to avoid failing the caller test
			return;
		}
	}
}
