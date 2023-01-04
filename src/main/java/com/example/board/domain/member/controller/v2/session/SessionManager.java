package com.example.board.domain.member.controller.v2.session;

import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class SessionManager {

  @Value("${session.cookie.name}")
  private String sessionId;

  @Value("${session.duration}")
  private int duration;

  private final SessionStorage sessionStorage;

  public SessionManager(SessionStorage sessionStorage){
    this.sessionStorage = sessionStorage;
  }

  public UUID login(Long memberId, String email){
    Optional<UUID> optionalSessionId = sessionStorage.getLoginedSessionId(memberId, email);
    if(optionalSessionId.isPresent()){
      return reissue(optionalSessionId.get(), memberId, email);
    } else{
      return createSession(memberId, email);
    }
  }

  private UUID createSession(Long memberId, String email) {
    return sessionStorage.save(memberId, email, duration);
  }

  private UUID reissue(UUID sessionId, Long memberId, String email) {
    return sessionStorage.save(memberId, email, duration);
  }

  public AuthenticatedMember getSession(UUID sessionId) {
    return sessionStorage.get(sessionId);
  }
}
