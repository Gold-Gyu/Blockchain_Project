package org.oao.eticket.adapter.in.web;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.oao.eticket.application.domain.service.LoadMyReservationsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class LoadMyReservationsControllerTests {

  @Autowired private MockMvc mockMvc;

  @MockBean LoadMyReservationsService loadMyReservationsService;

  @Test
  @WithMockUser
  @DisplayName("유저아이디가 유효하다면 존재하는 예매내역 가져오기")
  void whenGood_FindReservations() throws Exception {
    mockMvc.perform(get("/reservations/1")
                    .contentType(MediaType.APPLICATION_JSON)
            ).andDo(print())
            .andExpect(status().isOk());
  }
}
