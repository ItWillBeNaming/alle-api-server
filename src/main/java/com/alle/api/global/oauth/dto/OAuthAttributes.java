package com.alle.api.global.oauth.dto;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;

import java.util.HashMap;
import java.util.Map;

@Getter
@ToString
@Slf4j
@Builder(access = AccessLevel.PRIVATE)
public class OAuthAttributes {

    private Map<String, Object> attributes; // 소셜 로그인 사용자의 속성 정보를 담는 Map
    private String attributeKey; // 사용자 속성의 키 값
    private String email; // 이메일
    private String name; // 이름
    private String picture; // 프로필 사진
    private String provider; // 플랫폼
    private String socialAccessToken;

    public static OAuthAttributes of(String provider, String attributeKey, Map<String, Object> attributes, String socialAccessToken) {
        // 각 플랫폼 별로 제공해주는 데이터가 조금씩 다르기 때문에 분기 처리
        return switch (provider) {
            case "google" -> google(provider, attributeKey, attributes, socialAccessToken);
            case "kakao" -> kakao(provider, attributeKey, attributes, socialAccessToken);
            case "naver" -> naver(provider, attributeKey, attributes, socialAccessToken);
            default -> throw new RuntimeException("지원하는 플랫폼이 아닙니다.");
        };
    }

    private static OAuthAttributes google(String provider, String attributeKey, Map<String, Object> attributes, String socialAccessToken) {
        return OAuthAttributes.builder()
                .email((String) attributes.get("email"))
                .name((String) attributes.get("name"))
                .picture((String) attributes.get("picture"))
                .attributes(attributes)
                .attributeKey(attributeKey)
                .provider(provider)
                .socialAccessToken(socialAccessToken)
                .build();
    }

    private static OAuthAttributes kakao(String provider, String attributeKey, Map<String, Object> attributes, String socialAccessToken) {
        Map<String, Object> kakaoAccount = (Map<String, Object>) attributes.get("kakao_account");
        Map<String, Object> kakaoProfile = (Map<String, Object>) kakaoAccount.get("profile");

        String email = (String) kakaoAccount.get("email");
        String name = kakaoProfile != null ? (String) kakaoProfile.get("nickname") : null;
        String picture = kakaoProfile != null ? (String) kakaoProfile.get("profile_image_url") : null;

        return OAuthAttributes.builder()
                .email(email)
                .name(name)
                .picture(picture)
                .attributes(kakaoAccount)
                .attributeKey(attributeKey)
                .provider(provider)
                .socialAccessToken(socialAccessToken)
                .build();
    }


    private static OAuthAttributes naver(String provider, String attributeKey, Map<String, Object> attributes, String socialAccessToken) {
        Map<String, Object> response = (Map<String, Object>) attributes.get("response");

        return OAuthAttributes.builder()
                .email((String) response.get("email"))
                .name((String) response.get("name"))
                .picture((String) response.get("profile_image"))
                .attributes(response)
                .attributeKey(attributeKey)
                .provider(provider)
                .socialAccessToken(socialAccessToken)
                .build();
    }

    public Map<String, Object> convertToMap() {
        Map<String, Object> map = new HashMap<>();
        map.put("id", attributeKey);
        map.put("key", attributeKey);
        map.put("email", email);
        map.put("name", name);
        map.put("picture", picture);
        map.put("provider", provider);
        map.put("socialAccessToken", socialAccessToken);

        return map;
    }
}
