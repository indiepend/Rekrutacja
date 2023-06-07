package me.dworak.rekrutacja.web;

import me.dworak.rekrutacja.api.Branch;
import me.dworak.rekrutacja.api.GithubUserRepositoryDto;
import me.dworak.rekrutacja.api.GithubUserRepositoryInfo;
import me.dworak.rekrutacja.github_client.GithubClient;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static java.util.Collections.*;
import static me.dworak.rekrutacja.TestObjectsFactory.createBranch;
import static me.dworak.rekrutacja.TestObjectsFactory.createGithubUserRepositoryDto;
import static me.dworak.rekrutacja.TestObjectsFactory.createGithubUserRepositoryInfo;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class UserRepositoriesServiceTest {

    private GithubClient client;
    private UserRepositoriesService service;

    @BeforeEach
    public void setup() {
        client = mock(GithubClient.class);
        service = new UserRepositoriesService(client);
    }

    @Test
    public void returnsOnlyNonForkRepositories() {
        //given
        GithubUserRepositoryInfo repositoryInfo1 = createGithubUserRepositoryInfo("repo1", "user1", true);
        GithubUserRepositoryInfo repositoryInfo2 = createGithubUserRepositoryInfo("repo2", "user1", false);

        when(client.getUserInformation("user1"))
                .thenReturn(List.of(repositoryInfo1, repositoryInfo2));

        Branch branch = createBranch("branch1");
        when(client.getBranches("user1", "repo1")).thenReturn(emptyList());
        when(client.getBranches("user1", "repo2")).thenReturn(singletonList(branch));

        //when
        List<GithubUserRepositoryDto> repos = service.getGithubUserNotForkRepositories("user1");

        //then
        assertThat(repos)
                .hasSize(1)
                .contains(createGithubUserRepositoryDto(
                                repositoryInfo2.getRepositoryName(),
                                repositoryInfo2.getUsername(),
                                singletonList(branch)));
    }
}
