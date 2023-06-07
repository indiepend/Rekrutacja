package me.dworak.rekrutacja.api;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public record GithubUserRepositoryInfo (String repositoryName, String username, boolean fork) {

    @JsonCreator
    public GithubUserRepositoryInfo(@JsonProperty("name") String repositoryName,
                                    @JsonProperty("owner") Owner owner,
                                    @JsonProperty("fork") boolean fork) {
        this(repositoryName, owner.login(), fork);
    }

    public boolean isNotFork() {
        return !fork;
    }
}
