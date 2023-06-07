package me.dworak.rekrutacja.api;

import java.util.List;

public record GithubUserRepositoryDto(String repositoryName, String ownerName, List<Branch> branches) {

    public static GithubUserRepositoryDto toDto(GithubUserRepositoryInfo githubUserRepositoryInfo,
                                                List<Branch> branches) {
        return new GithubUserRepositoryDto(
                githubUserRepositoryInfo.repositoryName(),
                githubUserRepositoryInfo.username(),
                branches
        );
    }
}
