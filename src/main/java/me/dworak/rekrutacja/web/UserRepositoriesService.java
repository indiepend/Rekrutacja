package me.dworak.rekrutacja.web;

import me.dworak.rekrutacja.api.Branch;
import me.dworak.rekrutacja.api.GithubUserRepositoryInfo;
import me.dworak.rekrutacja.api.GithubUserRepositoryDto;
import me.dworak.rekrutacja.github_client.GithubClient;

import java.util.List;

public class UserRepositoriesService {

    private final GithubClient client;

    public UserRepositoriesService(GithubClient client) {
        this.client = client;
    }

    public List<GithubUserRepositoryDto> getGithubUserNotForkRepositories(String username) {
        return client.getUserInformation(username)
                .stream()
                .filter(GithubUserRepositoryInfo::isNotFork)
                .map(repositoryInfo ->
                        GithubUserRepositoryDto.toDto(
                                repositoryInfo,
                                getBranchesInformation(username, repositoryInfo.getRepositoryName())))
                .toList();
    }

    private List<Branch> getBranchesInformation(String username, String reponame) {
        return client.getBranches(username, reponame);
    }
}
