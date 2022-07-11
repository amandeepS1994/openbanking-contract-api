package com.abidevel.openbanking.contract.service.implementation;

import java.util.Objects;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.abidevel.openbanking.contract.service.MerchantDetailService;

@Service
public class MerchantDetaiServiceImplementation implements MerchantDetailService {

    @Override
    public Optional<String> retrieveMerchantLogo(String merchant) {
        return Objects.nonNull(merchant) ? Optional.ofNullable(String.format("%s.png", merchant)) : Optional.empty();
    }
    
}
