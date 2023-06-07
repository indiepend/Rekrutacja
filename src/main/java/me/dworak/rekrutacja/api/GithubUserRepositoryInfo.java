package me.dworak.rekrutacja.api;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Map;

public class GithubUserRepositoryInfo {

    private final String repositoryName;
    private final String username;
    private final boolean fork;

    @JsonCreator
    public GithubUserRepositoryInfo(@JsonProperty("name") String repositoryName,
                                    @JsonProperty("owner") Map<String, String> owner,
                                    @JsonProperty("fork") boolean fork) {
        this.repositoryName = repositoryName;
        this.username = owner.get("login");
        this.fork = fork;
    }

    public GithubUserRepositoryInfo(String repositoryName,
                                    String owner,
                                    boolean fork) {
        this.repositoryName = repositoryName;
        this.username = owner;
        this.fork = fork;
    }

    public String getRepositoryName() {
        return repositoryName;
    }

    public String getUsername() {
        return username;
    }

    public boolean isNotFork() {
        return !fork;
    }
}
