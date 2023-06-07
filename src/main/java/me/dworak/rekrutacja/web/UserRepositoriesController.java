package me.dworak.rekrutacja.web;

import me.dworak.rekrutacja.api.GithubUserRepositoryDto;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "/api")
public class UserRepositoriesController {

    private final UserRepositoriesService userRepositoriesService;

    public UserRepositoriesController(UserRepositoriesService userRepositoriesService) {
        this.userRepositoriesService = userRepositoriesService;
    }

    @GetMapping(path = "/repositories", produces = MediaType.APPLICATION_JSON_VALUE)
    public List<GithubUserRepositoryDto> getGithubUserNotForkRepositories(@RequestParam String username) {
        return userRepositoriesService.getGithubUserNotForkRepositories(username);
    }

}
