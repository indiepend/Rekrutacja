package me.dworak.rekrutacja;

import me.dworak.rekrutacja.api.Branch;
import me.dworak.rekrutacja.api.GithubUserRepositoryDto;
import me.dworak.rekrutacja.api.GithubUserRepositoryInfo;
import net.bytebuddy.utility.RandomString;

import java.util.List;

public class TestObjectsFactory {

    public static GithubUserRepositoryInfo createGithubUserRepositoryInfo(String repoName, String username, boolean fork) {
        return new GithubUserRepositoryInfo(repoName, username, fork);
    }

    public static Branch createBranch(String branchName) {
        return new Branch(branchName, RandomString.make(10));
    }

    public static GithubUserRepositoryDto createGithubUserRepositoryDto(String reponame, String username, List<Branch> branches) {
        return new GithubUserRepositoryDto(reponame, username, branches);
    }
}
