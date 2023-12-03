package org.oao.eticket.application.port.out;

public interface GetOrderPort {
  Long getOrder(String key, Integer userId);
}
