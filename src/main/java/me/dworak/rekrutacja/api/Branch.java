package me.dworak.rekrutacja.api;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Map;

public class Branch {

    private final String name;
    private final String lastCommitSha;

    @JsonCreator
    public Branch(@JsonProperty("name") String name,
                  @JsonProperty("commit") Map<String, String> lastCommit) {
        this.name = name;
        this.lastCommitSha = lastCommit.get("sha");
    }

    public Branch(String name, String lastCommitSha) {
        this.name = name;
        this.lastCommitSha = lastCommitSha;
    }

    public String getLastCommitSha() {
        return lastCommitSha;
    }

    public String getName() {
        return name;
    }
}
