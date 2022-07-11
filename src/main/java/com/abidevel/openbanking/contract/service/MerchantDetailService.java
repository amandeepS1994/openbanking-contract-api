package com.abidevel.openbanking.contract.service;

import java.util.Optional;


public interface MerchantDetailService {
    Optional<String>retrieveMerchantLogo (String merchant);
}
