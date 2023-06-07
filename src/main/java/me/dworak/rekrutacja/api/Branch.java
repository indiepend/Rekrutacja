package me.dworak.rekrutacja.api;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public record Branch (String name, String lastCommitSha) {

    @JsonCreator
    public Branch(@JsonProperty("name") String name,
                  @JsonProperty("commit") Commit lastCommit) {
        this(name, lastCommit.sha());
    }

}
