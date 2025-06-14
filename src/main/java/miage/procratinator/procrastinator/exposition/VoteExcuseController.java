package miage.procratinator.procrastinator.exposition;

import miage.procratinator.procrastinator.entities.Excuse;
import miage.procratinator.procrastinator.entities.Procrastinateur;
import miage.procratinator.procrastinator.metier.VoteExcuseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/excuse")
public class VoteExcuseController {

    @Autowired
    private VoteExcuseService voteExcuseService;

    @PostMapping("/creeExcuse")
    public ResponseEntity<Excuse> createExcuse(@RequestBody Excuse excuse) {
        Excuse savedExcuse = voteExcuseService.createExcuse(excuse);
        return new ResponseEntity<>(savedExcuse, HttpStatus.CREATED);
    }


}
