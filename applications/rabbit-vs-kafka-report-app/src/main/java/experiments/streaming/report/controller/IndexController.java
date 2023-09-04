package experiments.streaming.report.controller;

import experiments.streaming.report.service.JobQueryService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequiredArgsConstructor
public class IndexController {
    private final JobQueryService service;

    @RequestMapping("/")
    public String homePage(Model model)
    {
        model.addAttribute("jobs", service.findJobs());

        return "index";
    }
}
