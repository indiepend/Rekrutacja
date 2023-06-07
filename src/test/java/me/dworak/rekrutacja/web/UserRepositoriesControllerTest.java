package me.dworak.rekrutacja.web;

import me.dworak.rekrutacja.api.UserNotFoundException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class UserRepositoriesControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserRepositoriesService userRepositoriesService;

    @Test
    public void userNotFoundExceptionHandlingTest() throws Exception {
        //given
        when(userRepositoriesService.getGithubUserNotForkRepositories(any()))
                .thenThrow(UserNotFoundException.class);

        //when
        mockMvc.perform(get("/api/repositories?username=nonExistingUser")
                        .accept(MediaType.APPLICATION_JSON))
        //then
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status").value("404 NOT_FOUND"))
                .andExpect(jsonPath("$.message").hasJsonPath());
    }

    @Test
    public void invalidMediaTypeExceptionHandlingTest() throws Exception {
        //when
        mockMvc.perform(get("/api/repositories?username=user")
                        .accept(MediaType.APPLICATION_ATOM_XML))
        //then
                .andExpect(status().isNotAcceptable())
                .andExpect(jsonPath("$.message").hasJsonPath())
                .andExpect(jsonPath("$.status").value("406 NOT_ACCEPTABLE"));
    }
}
