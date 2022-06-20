package com.abidevel.openbanking.contract.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.test.web.servlet.MockMvc;

import com.abidevel.openbanking.contract.ContractApplicationTests;

@AutoConfigureMockMvc
public abstract class ControllerTest extends ContractApplicationTests {
    @Autowired
    protected MockMvc mvc;
    
}
