package com.abidevel.openbanking.contract.model.Response;

import lombok.Data;

@Data
public class OpenBankingAccessTokenResponse {
    private String access_token;
    private String token_type;
    private Long expires_in;
    private String scope;
}

