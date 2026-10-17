package arpa.home.hms.service;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import arpa.home.hms.entity.WebAuthnCredentialEntity;
import arpa.home.hms.repository.UserRepository;
import arpa.home.hms.repository.WebAuthnCredentialRepository;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.security.web.webauthn.api.Bytes;
import org.springframework.security.web.webauthn.api.CredentialRecord;

class WebAuthnServiceTest {

  private final UserRepository userRepository = mock(UserRepository.class);
  private final WebAuthnCredentialRepository credentialRepository =
      mock(WebAuthnCredentialRepository.class);
  private final WebAuthnService webAuthnService =
      new WebAuthnService(userRepository, credentialRepository);
  private WebAuthnCredentialEntity credential;

  @BeforeEach
  void setUp() {
    credential = new WebAuthnCredentialEntity();
    credential.setUserId(1);
    credential.setCredentialId("AQID");
    credential.setPublicKey(new byte[] {4});
    credential.setAttestationObject(new byte[] {5});
  }

  @Test
  void findByCredentialIdReturnsCredentialWithEmptyTransports() {
    when(credentialRepository.findByCredentialId("AQID")).thenReturn(Optional.of(credential));

    CredentialRecord result = webAuthnService.findByCredentialId(new Bytes(new byte[] {1, 2, 3}));

    assertNotNull(result);
    assertTrue(result.getTransports().isEmpty());
  }

  @Test
  void findByUserIdReturnsCredentialsWithEmptyTransports() {
    when(credentialRepository.findByUserId(1)).thenReturn(List.of(credential));

    List<CredentialRecord> results =
        webAuthnService.findByUserId(new Bytes("1".getBytes(StandardCharsets.UTF_8)));

    assertNotNull(results);
    assertTrue(results.getFirst().getTransports().isEmpty());
  }
}
