package com.loopers.interfaces.api.popularity;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.loopers.application.popularity.PopularityService;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.beans.factory.annotation.Autowired;

@WebMvcTest(PopularityV1Controller.class)
@Import(MockConfig.class)
class PopularityV1ControllerTest {

  @Autowired
  MockMvc mvc;

  @Autowired
  PopularityService popularityService;

  @Test
  void top_ok() throws Exception {
    when(popularityService.topN(any(Integer.class), any()))
        .thenReturn(new PopularityResponse.TopRanking(List.of()));
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

@TestConfiguration
class MockConfig {
  @Bean
  PopularityService popularityService() {
    return Mockito.mock(PopularityService.class);
  }
}
