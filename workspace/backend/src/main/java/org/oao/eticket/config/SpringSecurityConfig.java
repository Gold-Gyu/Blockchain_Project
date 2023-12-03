package org.oao.eticket.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.oao.eticket.application.port.in.CreateAuthTokenUseCase;
import org.oao.eticket.application.port.in.VerifyAccessTokenUseCase;
import org.oao.eticket.application.port.out.LoadChallengeWordPort;
import org.oao.eticket.application.port.out.LoadUserPort;
import org.oao.eticket.infrastructure.security.*;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.ExceptionTranslationFilter;
import org.springframework.security.web.authentication.AuthenticationFilter;
import org.springframework.security.web.authentication.logout.LogoutFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.web.filter.GenericFilterBean;

@Configuration
@RequiredArgsConstructor
class SpringSecurityConfig {

  private final ApplicationContext context;

  @Bean
  EticketUserDetailsService userDetailsService(final LoadUserPort loadUserPort) {
    return new EticketUserDetailsService(loadUserPort);
  }

  @Bean
  PasswordEncoder passwordEncoder() {
    return new BCryptPasswordEncoder();
  }

  @Bean
  @Qualifier("eticketConcreteAuthenticationConverter")
  ConcreteAuthenticationConverter usernamePasswordAuthenticationTokenConverter(
      final ObjectMapper objectMapper) {

    return new UsernamePasswordAuthenticationTokenConverter(objectMapper);
  }

  @Bean
  @Qualifier("eticketConcreteAuthenticationConverter")
  ConcreteAuthenticationConverter personalSignAuthenticationTokenConverter(
      final ObjectMapper objectMapper) {

    return new PersonalSignAuthenticationTokenConverter(objectMapper);
  }

  @Bean
  EticketAuthenticationConverter eticketAuthenticationConverter(
      @Qualifier("eticketConcreteAuthenticationConverter")
          final List<ConcreteAuthenticationConverter> converters) {

    return new EticketAuthenticationConverter(converters);
  }

  @Bean
  @Qualifier("eticketAuthenticationProvider")
  AuthenticationProvider eticketDaoAuthenticationProvider(
      final UserDetailsService userDetailsService, final PasswordEncoder passwordEncoder) {

    final var authenticationProvider = new DaoAuthenticationProvider();
    authenticationProvider.setUserDetailsService(userDetailsService);
    authenticationProvider.setPasswordEncoder(passwordEncoder);
    authenticationProvider.setHideUserNotFoundExceptions(true);
    return authenticationProvider;
  }

  @Bean
  @Qualifier("eticketAuthenticationProvider")
  AuthenticationProvider eticketPersonalSignAuthenticationProvider(
      final LoadUserPort loadUserPort, final LoadChallengeWordPort loadChallengeWordPort) {

    return new PersonalSignAuthenticationProvider(loadUserPort, loadChallengeWordPort);
  }

  @Bean("eticketAuthenticationManager")
  AuthenticationManager eticketAuthenticationManager(
      @Qualifier("eticketAuthenticationProvider")
          final List<AuthenticationProvider> authenticationProviders) {

    final var providerManager = new ProviderManager(authenticationProviders);
    providerManager.setEraseCredentialsAfterAuthentication(true);
    return providerManager;
  }

  @Bean("eticketAuthenticationFilter")
  AuthenticationFilter eticketAuthenticationFilter(
      final CreateAuthTokenUseCase createAuthTokenUseCase,
      final AuthenticationManager eticketAuthenticationManager) {

    final var authenticationFilter =
        new AuthenticationFilter(
            eticketAuthenticationManager, context.getBean(EticketAuthenticationConverter.class));

    final var authenticationResultHandler =
        new EticketAuthenticationResultHandler(createAuthTokenUseCase);
    authenticationFilter.setSuccessHandler(authenticationResultHandler);
    authenticationFilter.setFailureHandler(authenticationResultHandler);

    authenticationFilter.setRequestMatcher(new AntPathRequestMatcher("/api/auth/signin", "POST"));

    return authenticationFilter;
  }

  @Bean
  GenericFilterBean eticketAuthorizationHeaderFilter(
      final VerifyAccessTokenUseCase verifyAccessTokenUseCase) {

    return new EticketAuthorizationHeaderFilter(verifyAccessTokenUseCase);
  }

  @Bean
  SecurityFilterChain filterChain(final HttpSecurity http) throws Exception {

    final var eticketAuthenticationFilter =
        context.getBean("eticketAuthenticationFilter", AuthenticationFilter.class);

    final var eticketAuthorizationHeaderFilter =
        context.getBean(EticketAuthorizationHeaderFilter.class);

    return http.cors(cors -> cors.disable())
        .csrf(csrf -> csrf.disable())
        .addFilterBefore(eticketAuthorizationHeaderFilter, LogoutFilter.class)
        .addFilterBefore(eticketAuthenticationFilter, ExceptionTranslationFilter.class)
        .authorizeHttpRequests(
            httpRequests ->
                httpRequests
                    .requestMatchers(
                        "/api/test",
                        "/api/nfts",
                        "/api/nfts/*",
                        "/api/auth/challenge",
                        "/api/auth/signin",
                        "/api/membership/join",
                        "/api/performances/*")
                    .permitAll()
                    .requestMatchers("/api/*")
                    .authenticated()
                    .anyRequest()
                    .permitAll())
        .build();
  }
}
