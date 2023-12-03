package org.oao.eticket.adapter.out.persistence.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import org.mapstruct.Mappings;
import org.oao.eticket.adapter.out.persistence.entity.PerformanceScheduleJpaEntity;
import org.oao.eticket.adapter.out.persistence.entity.ReservationJpaEntity;
import org.oao.eticket.adapter.out.persistence.entity.SeatJpaEntity;
import org.oao.eticket.adapter.out.persistence.entity.UserJpaEntity;
import org.oao.eticket.application.domain.model.Reservation;
import org.oao.eticket.application.domain.model.User;
import org.oao.eticket.application.port.in.dto.CancelMyReservationCommand;
import org.oao.eticket.application.port.out.dto.CreateReservationCommand;

@Mapper(
    componentModel = MappingConstants.ComponentModel.SPRING,
    uses = {UserMapper.class, SeatMapper.class, PerformanceScheduleMapper.class})
public interface ReservationMapper {

  @Mappings({
    @Mapping(target = "user", source = "userJpaEntity"),
    @Mapping(target = "seat", source = "seatJpaEntity"),
    @Mapping(target = "performanceSchedule", source = "performanceScheduleJpaEntity"),
  })
  Reservation mapToDomainEntity(ReservationJpaEntity reservationJpaEntity);

  @Mappings({
    @Mapping(target = "userJpaEntity", source = "user"),
    @Mapping(target = "seatJpaEntity", source = "seat"),
    @Mapping(target = "performanceScheduleJpaEntity", source = "performanceSchedule")
  })
  ReservationJpaEntity mapToJpaEntity(Reservation reservationDomainEntity);

  @Mapping(target = "id", ignore = true)
  @Mapping(target = "userJpaEntity", source = "buyerId")
  @Mapping(target = "seatJpaEntity", source = "seatId")
  @Mapping(target = "performanceScheduleJpaEntity", source = "performanceScheduleId")
  @Mapping(target = "cancellationTime", ignore = true)
  ReservationJpaEntity mapToJpaEntity(CreateReservationCommand createReservationCommand);

  @Mapping(target = "userJpaEntity", source = "user")
  @Mapping(target = "seatJpaEntity", source = "seat")
  @Mapping(target = "performanceScheduleJpaEntity", source = "performanceSchedule")
  ReservationJpaEntity mapToJpaEntity(CancelMyReservationCommand cancelMyReservationCommand);

  default UserJpaEntity mapToUserJpaEntity(final User.UserID userId) {
    final var userJpaEntity = new UserJpaEntity();
    userJpaEntity.setId(userId.getValue());
    return userJpaEntity;
  }

  default SeatJpaEntity mapToSeatJpaEntity(final int seatId) {
    final var seatJpaEntity = new SeatJpaEntity();
    seatJpaEntity.setId(seatId);
    return seatJpaEntity;
  }

  default PerformanceScheduleJpaEntity mapToPerformanceScheduleJpaEntity(
      final int performanceScheduleId) {
    final var performanceScheduleJpaEntity = new PerformanceScheduleJpaEntity();
    performanceScheduleJpaEntity.setId(performanceScheduleId);
    return performanceScheduleJpaEntity;
  }
}
