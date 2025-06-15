package miage.procratinator.procrastinator.exposition;

import miage.procratinator.procrastinator.entities.GrandConcours;
import miage.procratinator.procrastinator.metier.GestionnaireService;
import miage.procratinator.procrastinator.utilities.UtilisateurCourant;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;

import static org.mockito.ArgumentMatchers.any;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(GestionnaireController.class)
public class GestionnaireControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private GestionnaireService gestionnaireService;

    @MockBean
    private UtilisateurCourant utilisateurCourant;

    @Test
    void shouldCreateGrandConcourAnnuelSuccessfully() throws Exception {
        // Arrange
        GrandConcours grandConcours = new GrandConcours();
        grandConcours.setIdGrandConcour(1L);
        grandConcours.setNom("Test Concours");
        grandConcours.setDateDebut(LocalDate.now());
        grandConcours.setDateFin(LocalDate.now().plusDays(30));
        grandConcours.setRecompense("Une récompense");

        Mockito.when(gestionnaireService.creerGrandConcours(any(GrandConcours.class))).thenReturn(grandConcours);

        // Act & Assert
        mockMvc.perform(post("/api/gestionnaires/concours")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"nom\":\"Test Concours\",\"dateDebut\":\"" + LocalDate.now() + "\",\"dateFin\":\"" + LocalDate.now().plusDays(30) + "\",\"recompense\":\"Une récompense\"}"))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.idGrandConcour").value(1))
                .andExpect(jsonPath("$.nom").value("Test Concours"))
                .andExpect(jsonPath("$.recompense").value("Une récompense"));
    }
}