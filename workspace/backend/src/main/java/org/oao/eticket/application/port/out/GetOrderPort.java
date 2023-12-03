package org.oao.eticket.application.port.out;

public interface GetOrderPort {
  boolean getOrder(String key, Integer userId);
}
