package org.oao.eticket.adapter.out.persistence.repository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import jakarta.persistence.PersistenceContext;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.oao.eticket.adapter.out.persistence.entity.UserJpaEntity;
import org.oao.eticket.application.domain.model.NftTicket;
import org.oao.eticket.application.domain.model.User;
import org.oao.eticket.application.port.out.LoadOwnedNftTicketsPort;
import org.oao.eticket.exception.ExternalServiceException;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class NftTicketRepository implements LoadOwnedNftTicketsPort {

  @PersistenceContext private EntityManager em;

  @Override
  public List<NftTicket> load(final User.UserID ownerID) {
    try {
      final var owner =
          em.createQuery(
                  """
                          SELECT u
                            FROM UserJpaEntity u
                            JOIN FETCH u.nftTickets
                           WHERE u.id=:ownerID
                           """,
                  UserJpaEntity.class)
              .setParameter("ownerID", ownerID.getValue())
              .getSingleResult();
      return owner.getNftTickets().stream()
          .map(nft -> new NftTicket(NftTicket.NftTicketID.of(nft.getTokenID()), ownerID))
          .toList();
    } catch (NoResultException e) {
      return List.of();
    } catch (Exception e) {
      throw new ExternalServiceException(e);
    }
  }
}
