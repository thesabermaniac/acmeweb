/*
 * Copyright 2016 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.acme.statusmgr;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.Test;
import org.junit.jupiter.api.Assertions;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import java.util.Objects;
import static org.hamcrest.Matchers.is;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class ServerStatusControllerTests {

    @Autowired
    private MockMvc mockMvc;

    @Test
    public void noParamGreetingShouldReturnDefaultMessage() throws Exception {

        this.mockMvc.perform(get("/server/status")).andDo(print()).andExpect(status().isOk())
                .andExpect(jsonPath("$.statusDesc").value("Server is up"));
    }

    @Test
    public void paramGreetingShouldReturnTailoredMessage() throws Exception {

        this.mockMvc.perform(get("/server/status").param("name", "RebYid"))
                .andDo(print()).andExpect(status().isOk())
                .andExpect(jsonPath("$.contentHeader").value("Server Status requested by RebYid"));
    }

    @Test
    public void withOutParamShouldReturn_400_error() throws Exception{
        this.mockMvc.perform(get("/server/status/detailed"))
                .andDo(print()).andExpect(status().is4xxClientError())
                .andExpect(status().reason(is("Required List parameter 'details' is not present")));
    }

    @Test
    public void withParamShouldReturnOps() throws Exception{
        this.mockMvc.perform(get("/server/status/detailed?details=operations"))
                .andDo(print()).andExpect(status().isOk())
                .andExpect(jsonPath("$.contentHeader").value("Server Status requested by Anonymous"))
                .andExpect(jsonPath("$.statusDesc").value("Server is up, and is operating normally"));
    }

    @Test
    public void withParamShouldReturnOpsAndExtensions() throws Exception{
        this.mockMvc.perform(get("/server/status/detailed?details=operations,extensions"))
                .andDo(print()).andExpect(status().isOk())
                .andExpect(jsonPath("$.contentHeader").value("Server Status requested by Anonymous"))
                .andExpect(jsonPath("$.statusDesc").value("Server is up" +
                        ", and is operating normally" +
                        ", and is using these extensions - [Hypervisor, Kubernetes, RAID-6]"));
    }

    @Test
    public void withParamShouldReturnOpsExtensionsAndMemory() throws Exception{
        this.mockMvc.perform(get("/server/status/detailed?details=operations,extensions,memory"))
                .andDo(print()).andExpect(status().isOk())
                .andExpect(jsonPath("$.contentHeader").value("Server Status requested by Anonymous"))
                .andExpect(jsonPath("$.statusDesc").value("Server is up" +
                        ", and is operating normally" +
                        ", and is using these extensions - [Hypervisor, Kubernetes, RAID-6]" +
                        ", and its memory is running low"));
    }

    @Test
    public void withParamShouldReturnOpsExtensionsMemoryAndCustomName() throws Exception{
        this.mockMvc.perform(get("/server/status/detailed?details=operations,extensions,memory&name=Noach"))
                .andDo(print()).andExpect(status().isOk())
                .andExpect(jsonPath("$.contentHeader").value("Server Status requested by Noach"))
                .andExpect(jsonPath("$.statusDesc").value("Server is up" +
                        ", and is operating normally" +
                        ", and is using these extensions - [Hypervisor, Kubernetes, RAID-6]" +
                        ", and its memory is running low"));
    }

    @Test
    public void withParamShouldReturnOpsMemoryAndCustomName() throws Exception{
        this.mockMvc.perform(get("/server/status/detailed?details=operations,memory&name=Noach"))
                .andDo(print()).andExpect(status().isOk())
                .andExpect(jsonPath("$.contentHeader").value("Server Status requested by Noach"))
                .andExpect(jsonPath("$.statusDesc").value("Server is up" +
                        ", and is operating normally" +
                        ", and its memory is running low"));
    }

    @Test
    public void withParamShouldReturnExtensionsMemoryAndCustomName() throws Exception{
        this.mockMvc.perform(get("/server/status/detailed?details=extensions,memory&name=Noach"))
                .andDo(print()).andExpect(status().isOk())
                .andExpect(jsonPath("$.contentHeader").value("Server Status requested by Noach"))
                .andExpect(jsonPath("$.statusDesc").value("Server is up" +
                        ", and is using these extensions - [Hypervisor, Kubernetes, RAID-6]" +
                        ", and its memory is running low"));
    }

    @Test
    public void withParamWithNameFirstShouldReturnExtensionsMemoryAndCustomName() throws Exception{
        this.mockMvc.perform(get("/server/status/detailed?name=Noach&details=extensions,memory"))
                .andDo(print()).andExpect(status().isOk())
                .andExpect(jsonPath("$.contentHeader").value("Server Status requested by Noach"))
                .andExpect(jsonPath("$.statusDesc").value("Server is up" +
                        ", and is using these extensions - [Hypervisor, Kubernetes, RAID-6]" +
                        ", and its memory is running low"));
    }

    @Test
    public void withParamDuplicateShouldReturnMemoryOpsExtensionsAndMemory() throws Exception{
        this.mockMvc.perform(get("/server/status/detailed?details=memory,operations,extensions,memory"))
                .andDo(print()).andExpect(status().isOk())
                .andExpect(jsonPath("$.contentHeader").value("Server Status requested by Anonymous"))
                .andExpect(jsonPath("$.statusDesc").value("Server is up" +
                        ", and its memory is running low" +
                        ", and is operating normally" +
                        ", and is using these extensions - [Hypervisor, Kubernetes, RAID-6]" +
                        ", and its memory is running low"));
    }

    @Test
    public void withParamShouldReturnCustomError() throws Exception{
        Assertions.assertEquals(Objects.requireNonNull(this.mockMvc.perform(get("/server/status/detailed?details=memory,operations,junkERROR"))
                .andDo(print()).andExpect(status().is4xxClientError()).andReturn().getResolvedException()).getMessage(),
                "Invalid details option: junkERROR");
    }

    @Test
    public void withParamShouldReturnCustomError2() throws Exception{
        Assertions.assertEquals(Objects.requireNonNull(this.mockMvc.perform(get("/server/status/detailed?details=memory,operations,testError"))
                .andDo(print()).andExpect(status().is4xxClientError()).andReturn().getResolvedException()).getMessage(),
                "Invalid details option: testError");
    }

    @Test
    public void withParamShouldReturnCustomError3() throws Exception{
        Assertions.assertEquals(Objects.requireNonNull(this.mockMvc.perform(get("/server/status/detailed?details=memory,junkERROR,operations"))
                .andDo(print()).andExpect(status().is4xxClientError()).andReturn().getResolvedException()).getMessage(),
                "Invalid details option: junkERROR");
    }

    @Test
    public void withParamShouldReturnCustomError4() throws Exception{
        Assertions.assertEquals(Objects.requireNonNull(this.mockMvc.perform(get("/server/status/detailed?details=testError,memory,operations"))
                .andDo(print()).andExpect(status().is4xxClientError()).andReturn().getResolvedException()).getMessage(),
                "Invalid details option: testError");
    }

}
