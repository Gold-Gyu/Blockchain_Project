package org.oao.eticket.application.port.out.dto;

import org.oao.eticket.application.domain.model.AuthTokenId;
import org.oao.eticket.application.domain.model.User;

public record LoadAccessTokenMetadataCommand(User.UserID ownerId, AuthTokenId accessTokenId) {}
