package com.abidevel.openbanking.contract.model.Response;

import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@ToString
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ApiResponse <T> {
    private boolean isSuccessful;
    private String message;
    private T data;
    private Date apireponse;
}
