package miage.procratinator.procrastinator;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/antiprocrastinateur")
public class AntiProcrastinateur {

    @GetMapping("/status")
    public String getStatus() {
        return "Anti-procrastinator is active and ready!";
    }
}


