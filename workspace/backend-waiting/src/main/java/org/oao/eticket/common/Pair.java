package org.oao.eticket.common;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public class Pair<T, U> {

  public final T x;
  public final U y;

  public static <T, U> Pair<T, U> of(final T x, final U y) {
    return new Pair<>(x, y);
  }
}
