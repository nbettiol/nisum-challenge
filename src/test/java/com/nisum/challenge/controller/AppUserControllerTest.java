package com.nisum.challenge.controller;

import com.nisum.challenge.model.AppUser;
import com.nisum.challenge.model.AppUserRepository;
import com.nisum.challenge.security.CustomUserDetailsService;
import com.nisum.challenge.service.AppUserService;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.List;
import java.util.NoSuchElementException;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@WebMvcTest(AppUserController.class)
@AutoConfigureMockMvc(addFilters = false)
public class AppUserControllerTest {

    @Autowired
    private MockMvc mvc;

    @MockBean
    private AppUserService appUserService;

    @MockBean
    private CustomUserDetailsService customUserDetailsService;

    @MockBean
    private AppUserRepository appUserRepository;

    @Test
    public void testGetUser()
            throws Exception {

        AppUser appUser = new AppUser();
        appUser.setPassword("password");
        appUser.setName("name");
        Mockito.when(appUserService.getUser(Mockito.any())).thenReturn(appUser);

        mvc.perform(get("/users/d166e393-1a54-41ee-97ed-0eecad5a71c8").contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(content().string("{\"id\":null,\"email\":null,\"name\":\"name\",\"password\":\"password\"," +
                        "\"created\":null,\"modified\":null,\"lastLogin\":null,\"token\"" +
                        ":null,\"phones\":[],\"active\":false}"));
    }

    @Test
    public void testGetUserList()
            throws Exception {

        AppUser appUser = new AppUser();
        appUser.setPassword("password");
        appUser.setName("name");

        List<AppUser> userList = Arrays.asList(appUser);

        Mockito.when(appUserService.getUserList()).thenReturn(userList);

        mvc.perform(get("/users").contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(content().string("[{\"id\":null,\"email\":null,\"name\":\"name\",\"password\":\"password\"," +
                        "\"created\":null,\"modified\":null,\"lastLogin\":null,\"token\"" +
                        ":null,\"phones\":[],\"active\":false}]"));
    }

    @Test
    public void testPostUser() throws Exception {

        AppUser appUser = new AppUser();
        appUser.setPassword("password");
        appUser.setName("name");
        appUser.setName("email@email.com");
        Mockito.when(appUserService.createUser(Mockito.any())).thenReturn(appUser);

        mvc.perform(post("/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\n" +
                        "\t\"name\": \"name\",\n" +
                        "\t\"password\": \"password\",\n" +
                        "\t\"email\": \"email@email.com\"\n"+
                        "}"))
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(content().string("{\"id\":null,\"email\":null,\"name\":\"email@email.com\",\"password\":\"password\"," +
                        "\"created\":null,\"modified\":null,\"lastLogin\"" +
                        ":null,\"token\":null,\"phones\":[],\"active\":false}"))
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON));
    }

    @Test
    public void testGetUserNotFound()
            throws Exception {
        Mockito.when(appUserService.getUser(Mockito.any())).thenThrow(new NoSuchElementException("The user not exists"));

        mvc.perform(get("/users/d166e393-1a54-41ee-97ed-0eecad5a71c8").contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isNotFound());
    }
}