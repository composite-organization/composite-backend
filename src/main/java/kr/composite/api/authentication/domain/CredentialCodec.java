package kr.composite.api.authentication.domain;

public interface CredentialCodec {

    String encode(CredentialPayload payload);

    CredentialPayload decode(String credential);
}
