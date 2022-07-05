package com.abidevel.openbanking.contract.Controller;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class TransactionControllerTest extends ControllerTest {

  
    @BeforeAll() 
    static void setUp() {
    }

    @Test()
    @DisplayName("Perform a transaction request with a valid transaction number, assert that response is ok, and a suitable payload is returned.")
    void testRetrieveAllTransactionForAccountNumber() throws Exception {
        mvc.perform(
            get("/transactions/1234567/")
            .accept(MediaType.APPLICATION_JSON))
        .andDo(print())
        .andExpect(status().isOk())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON))
        .andExpect(MockMvcResultMatchers.jsonPath("$.successful").value(true));

        // Expect the test case to fail
        mvc.perform(
            get("/transactions/s/")
            .accept(MediaType.APPLICATION_JSON))
        .andDo(print())
        .andExpect(status().isBadRequest());
    }
}
