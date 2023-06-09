package me.dworak.rekrutacja.github_client;

import feign.Headers;
import me.dworak.rekrutacja.api.Branch;
import me.dworak.rekrutacja.api.GithubUserRepositoryInfo;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;

import java.util.List;

@FeignClient(value = "githubclient", url = "https://api.github.com", primary = false)
@Headers({
        "X-GitHub-Api-Version: {apiVersion}",
        "Accept: application/vnd.github+json",
        "Authorization: Bearer {token}"
})
public interface GithubFeignClient {

    @GetMapping(value = "/users/{username}/repos")
    List<GithubUserRepositoryInfo> getUserReposInfo(@PathVariable String username,
                                                    @RequestHeader String apiVersion,
                                                    @RequestHeader String token);

    @GetMapping(value = "/repos/{username}/{repoName}/branches")
    List<Branch> getRepositoryBranches(@PathVariable String username,
                                       @PathVariable String repoName,
                                       @RequestHeader String apiVersion,
                                       @RequestHeader String token);
}
