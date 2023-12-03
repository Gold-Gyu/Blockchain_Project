package org.oao.eticket.application.domain.service;

import lombok.RequiredArgsConstructor;
import org.oao.eticket.application.domain.model.Performance;
import org.oao.eticket.application.port.in.SearchPerformancesUseCase;
import org.oao.eticket.common.annotation.UseCase;

import java.util.List;

@UseCase
@RequiredArgsConstructor
public class SearchPerformancesService implements SearchPerformancesUseCase {

  @Override
  public List<Performance> search(String keyword) {
    return null;
  }
}
