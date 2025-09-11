package com.loopers.interfaces.api.popularity;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.loopers.application.popularity.PopularityService;
import com.loopers.application.popularity.PopularityCommand;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.beans.factory.annotation.Autowired;

@WebMvcTest(PopularityV1Controller.class)
class PopularityV1ControllerTest {

  @Autowired
  MockMvc mvc;

  @MockBean
  PopularityService popularityService;

  @Test
  void top_ok() throws Exception {
    when(popularityService.topN(any(Integer.class), any())).thenReturn(List.of(new PopularityCommand.Entry(1L, 10)));
    mvc.perform(get("/api/v1/popularity/top").param("limit", "5"))
        .andExpect(status().isOk());
  }

  @Test
  void top_limit_invalid() throws Exception {
    mvc.perform(get("/api/v1/popularity/top").param("limit", "0"))
        .andExpect(status().is4xxClientError());
  }

  @Test
  void rank_ok() throws Exception {
    mvc.perform(get("/api/v1/popularity/10"))
        .andExpect(status().isOk());
  }
}
