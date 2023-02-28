package uz.pdp.spring_boot_security_web.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.DockerImageName;
import uz.pdp.spring_boot_security_web.repository.LanguageRepository;
import uz.pdp.spring_boot_security_web.repository.TaskRepository;
import uz.pdp.spring_boot_security_web.repository.TopicRepository;
import uz.pdp.spring_boot_security_web.repository.UserRepository;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@Testcontainers
public abstract class BaseTest {
    @Autowired
    MockMvc mockMvc;
    @Autowired
    LanguageRepository languageRepository;
    @Autowired
    UserRepository userRepository;
    @Autowired
    TaskRepository taskRepository;
    @Autowired
    TopicRepository topicRepository;

    protected final static PostgreSQLContainer<?> postgres;

    static {
        postgres = (PostgreSQLContainer) new PostgreSQLContainer(DockerImageName.parse("postgres:latest"))
//                .withInitScript("sql/table-init.sql")
                .withExposedPorts(5432);
        postgres.withReuse(true);

    }

    @DynamicPropertySource
    static void setUpPostgresConnectionProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", postgres::getJdbcUrl);
        registry.add("spring.datasource.username", postgres::getUsername);
        registry.add("spring.datasource.password", postgres::getPassword);
        registry.add("spring.datasource.database", postgres::getDatabaseName);
    }
}
