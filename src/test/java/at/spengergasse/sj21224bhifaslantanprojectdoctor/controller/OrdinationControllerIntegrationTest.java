package at.spengergasse.sj21224bhifaslantanprojectdoctor.controller;

import at.spengergasse.sj21224bhifaslantanprojectdoctor.domain.Address;
import at.spengergasse.sj21224bhifaslantanprojectdoctor.domain.Ordination;
import at.spengergasse.sj21224bhifaslantanprojectdoctor.persistence.OrdinationRepository;
import at.spengergasse.sj21224bhifaslantanprojectdoctor.presentation.api.OrdinationRestController;
import at.spengergasse.sj21224bhifaslantanprojectdoctor.services.OrdinationService;
import at.spengergasse.sj21224bhifaslantanprojectdoctor.services.dtos.OrdinationDto;
import at.spengergasse.sj21224bhifaslantanprojectdoctor.foundation.TemporalValueFactory;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Before;
import org.junit.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
@DataJpaTest
public class OrdinationControllerIntegrationTest {

    private MockMvc mockMvc;

    @Mock
    private OrdinationService ordinationService;

    @Mock
    private OrdinationRepository ordinationRepository;

    @MockBean
    private TemporalValueFactory temporalValueFactory;

    @InjectMocks
    private OrdinationRestController ordinationRestController;

    private JacksonTester<OrdinationDto> ordinationDtoJacksonTester;


    @Before
    public void setup() {
        JacksonTester.initFields(this, new ObjectMapper());
        mockMvc = MockMvcBuilders.standaloneSetup(ordinationRestController).build();
    }


    @Test
    public void canRetrieveByIdWhenExists()  throws Exception{
        //given
        Ordination ordination = new Ordination();
        ordination.setName("Test");
        ordination.setAbbrevitation("T");
        ordination.setAddress(new Address());

        given(ordinationRepository.findById(1L)).willReturn(Optional.of(ordination));
        mockMvc.perform(get("/ordinations/1"));
        //when
        //then
        mockMvc.perform(get("/ordinations/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Test"))
                .andExpect(jsonPath("$.abbreviation").value("T"))
                .andExpect(jsonPath("$.address").isNotEmpty())
                .andExpect(jsonPath("$.address.street").value(""))
                .andExpect(jsonPath("$.address.city").value(""))
                .andExpect(jsonPath("$.address.state").value(""))
                .andExpect(jsonPath("$.address.zipCode").value(""));
    }
}
