package me.dworak.rekrutacja.github_client;

import feign.FeignException;
import me.dworak.rekrutacja.api.Branch;
import me.dworak.rekrutacja.api.GithubUserRepositoryInfo;
import me.dworak.rekrutacja.api.UserNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static java.util.Collections.singletonList;
import static me.dworak.rekrutacja.TestObjectsFactory.createBranch;
import static me.dworak.rekrutacja.TestObjectsFactory.createGithubUserRepositoryInfo;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class GithubClientTest {

    private GithubFeignClient feignClient;
    private GithubClient client;

    @BeforeEach
    public void setup() {
        feignClient = mock(GithubFeignClient.class);
        client = new GithubClient(feignClient, "1.0", "token");
    }

    @Test
    public void throwsUserNotFoundExceptionWhenFeignClientReturns404() {
        //given
        when(feignClient.getUserReposInfo(any(), any(), any()))
                .thenThrow(FeignException.NotFound.class);

        //when then
        assertThrows(
                UserNotFoundException.class,
                () -> client.getUserInformation("NonExistingUser"));
    }

    @Test
    public void returnsBranchesForGivenRepositoryAndUser() {
        //given
        Branch branch = createBranch("branch1");
        when(feignClient.getRepositoryBranches(eq("user1"), eq("repo1"), any(), any()))
                .thenReturn(singletonList(branch));

        //when
        List<Branch> branches = client.getBranches("user1", "repo1");

        //then
        assertThat(branches.get(0))
                .usingRecursiveComparison()
                .isEqualTo(branch);
    }

    @Test
    public void returnsUserRepositoriesInfoForGivenUser() {
        //given
        GithubUserRepositoryInfo repositoryInfo = createGithubUserRepositoryInfo("repo1","user1", true);

        when(feignClient.getUserReposInfo(eq("user1"), any(), any()))
                .thenReturn(singletonList(repositoryInfo));

        //when
        List<GithubUserRepositoryInfo> repos = client.getUserInformation("user1");

        //then
        assertThat(repos.get(0))
                .usingRecursiveComparison()
                .isEqualTo(repositoryInfo);
    }
}
