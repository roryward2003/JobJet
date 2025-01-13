package ie.tcd.scss.groupProject.service;

import ie.tcd.scss.groupProject.config.JoobleAPIConfig;
import ie.tcd.scss.groupProject.domain.Career;

import ie.tcd.scss.groupProject.repo.CareerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

// This class will be responsible for access to the <a href="jobsApiUrlGoesHere">jobsApiUrlGoesHere</a>
// REST API to retrieve information about Careers

@Service
public class CareerService {
    // Create logger for this class
    private static final Logger logger = LoggerFactory.getLogger(CareerService.class);

    private final RestTemplate restTemplate;
    private final JoobleAPIConfig joobleAPIConfig;
    private final CareerRepository careerRepository;

    @Autowired
    public CareerService(RestTemplate restTemplate, JoobleAPIConfig joobleAPIConfig, CareerRepository careerRepository) {
        this.restTemplate = restTemplate;
        this.joobleAPIConfig = joobleAPIConfig;
        this.careerRepository = careerRepository;
    }

    /**
     * Retrieves a list of job postings based on the provided search parameters.
     *
     * @param keywords Keywords for job search.
     * @param location Location for job search.
     * @return A List of Career objects representing job postings.
     */
    public List<Career> getJobs(String keywords, String location) {
        String url = "";
        try {
            url = joobleAPIConfig.getBaseUrl().concat(joobleAPIConfig.getApiKey());   // URL with API key
        } catch(Exception e) {
            logger.error("API KEY UNDEFINED");
        }

        // Prepare search conditions here.
        Map<String, String> requestPayload = new HashMap<>();
        requestPayload.put("keywords", keywords);
        requestPayload.put("location", location);

        // Attempt to search.
        try {
            // Send POST request to the API under search conditions.
            @SuppressWarnings("rawtypes")
            ResponseEntity<Map> response = restTemplate.postForEntity(url, requestPayload, Map.class);
            
            // If no HttpsStatus == ok and jobs present.
            if (response.getStatusCode() == HttpStatus.OK && response.getBody() != null) {
                @SuppressWarnings("unchecked")
                List<Career> careers = parseCareers(response.getBody());
                careerRepository.saveAll(careers);
                return careers;
            }

            else { logger.warn("ERROR: API call returned status " + response.getStatusCode()); }
        } 

        // Error during API call occured.
        catch (HttpClientErrorException e) {
            // No jobs found.
            if(e.getStatusCode() == HttpStatus.NOT_FOUND) { return null; }

            // Unexpected error.
            else{ logger.error("ERROR: API call returned status " + e.getStatusCode() + " - " + e.getResponseBodyAsString()); }
        } 

        // Error during returning jobs occured.
        catch (Exception e) { logger.error("ERROR: ", e); }

        // Return empty list if error occurs.
        return Collections.emptyList();  
    }

    /**
     * Parses the API response into a list of Career objects.
     *
     * @param responseBody The response body from the API.
     * @return A List of Career objects.
     */
    private List<Career> parseCareers(Map<String, Object> responseBody) {
        List<Career> careers = new ArrayList<>();
        @SuppressWarnings("unchecked")
        List<Map<String, Object>> jobs = (List<Map<String, Object>>) responseBody.get("jobs");

        // could use Streams here? Check countryinfo.
        if (jobs != null) {
            for (Map<String, Object> jobData : jobs) {
                Career career = new Career();
                career.setTitle((String) jobData.get("title"));
                career.setCompanyName((String) jobData.get("company"));
                career.setLocation((String) jobData.get("location"));
                career.setDescription((String) jobData.get("snippet"));
                career.setSalary((String) jobData.get("salary"));
                career.setUrl((String) jobData.get("link"));
                career.setId(((Long)jobData.get("id")).toString());
                careers.add(career);
            }
        }
        return careers;
    }

    public Career getCareerById(String id) {
        return careerRepository.findById(id).orElse(null);
    }
}