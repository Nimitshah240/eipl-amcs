package com.eipl.amcs.base.dto;

import com.fasterxml.jackson.databind.JsonNode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.springframework.lang.Nullable;

@Setter
@Getter
@NoArgsConstructor
@ToString
public class MyRequest {
    private String token;
    private String imei;
    private String deviceId;
    private String requestTime;
    private String identityCode;
    private String organizationCode;
    private String organizationType;
    private String dbVersion;
    private JsonNode content;

    @Nullable
    public JsonNode getContent() {
        return content;
    }
}
