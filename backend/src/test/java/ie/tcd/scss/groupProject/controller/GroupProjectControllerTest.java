package ie.tcd.scss.groupProject.controller;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static org.assertj.core.api.Assertions.assertThat;

//
// Implement tests for CI/CD here
//
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class GroupProjectControllerTest extends BaseGroupProjectControllerTest {
    Logger logger = LoggerFactory.getLogger(GroupProjectControllerTest.class);

    @Test
    public void firstTest() {
        int test = -1;
        assertThat(test==-1);
    }
}