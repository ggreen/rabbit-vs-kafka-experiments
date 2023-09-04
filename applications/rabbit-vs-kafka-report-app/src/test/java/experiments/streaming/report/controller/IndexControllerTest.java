package experiments.streaming.report.controller;

import experiments.streaming.report.service.JobQueryService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.ui.Model;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class IndexControllerTest {

    @Mock
    private Model model;

    @Mock
    private JobQueryService service;

    private IndexController subject;

    @BeforeEach
    void setUp() {
        subject = new IndexController(service);
    }

    @Test
    void homePage() {

        subject.homePage(model);

        verify(service).findJobs();
    }
}