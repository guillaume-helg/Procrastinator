package miage.procratinator.procrastinator.exposition;

import miage.procratinator.procrastinator.entities.PiegeProductivite;
import miage.procratinator.procrastinator.entities.Recompense;
import miage.procratinator.procrastinator.metier.AntiprocrastinateurService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = AntiProcrastinateurController.class)
public class AntiProcrastinateurControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private AntiprocrastinateurService antiprocrastinateurService;

    @Test
    public void creerPiegeProductivite_ShouldReturnCreatedPiegeProductivite_WhenInputIsValid() throws Exception {
        PiegeProductivite piegeProductivite = new PiegeProductivite();
        Recompense recompense = new Recompense();
        recompense.setIdRecompense(1L);
        piegeProductivite.setIdPiegeProductivite(1L);
        piegeProductivite.setRecompense(recompense);

        Mockito.when(antiprocrastinateurService.creerPiegeProductivite(Mockito.any(PiegeProductivite.class)))
                .thenReturn(piegeProductivite);

        String piegeProductiviteJson = """
                {
                    "recompense": {
                        "idRecompense": 1
                    }
                }
                """;
        mockMvc.perform(post("/api/antiprocrastinateur/creerPiegeProductivite")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(piegeProductiviteJson))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.idPiegeProductivite", is(1)))
                .andExpect(jsonPath("$.recompense.idRecompense", is(1)));
    }
}