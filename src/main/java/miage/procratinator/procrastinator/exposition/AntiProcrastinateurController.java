package miage.procratinator.procrastinator.exposition;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/antiprocrastinateur")
public class AntiProcrastinateurController {

    @GetMapping("/hello")
    public String sayHello() {
        return "Anti-procrastinator here to help!";
    }

}
