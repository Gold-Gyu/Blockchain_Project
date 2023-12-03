package org.oao.eticket.adapter.in.web;

import lombok.RequiredArgsConstructor;
import org.oao.eticket.application.port.in.SearchPerformancesUseCase;
import org.oao.eticket.common.annotation.WebAdapter;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@WebAdapter
@RequiredArgsConstructor
public class SearchPerformancesController {
  private final SearchPerformancesUseCase searchPerformancesUseCase;

  record SearchPerformancesResponseBody(Object result) {}

  @GetMapping("/api/performances/search")
  ResponseEntity<SearchPerformancesResponseBody> searchPerformances(@RequestParam String keyword) {
    try {
      final var searchResults = searchPerformancesUseCase.search(keyword);
      return ResponseEntity.ok(new SearchPerformancesResponseBody(searchResults));
    } catch (Exception e) {
      throw ApiException.builder().withCause(e).withMessage(e.getMessage()).build();
    }
  }
}
