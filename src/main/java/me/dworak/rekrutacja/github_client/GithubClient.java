package me.dworak.rekrutacja.github_client;

import feign.FeignException;
import me.dworak.rekrutacja.api.Branch;
import me.dworak.rekrutacja.api.GithubUserRepositoryInfo;
import me.dworak.rekrutacja.api.UserNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public class GithubClient {

    private final static Logger log = LoggerFactory.getLogger(GithubClient.class);

    private final String apiVersion;
    private final String authToken;

    private final GithubFeignClient feignClient;

    public GithubClient(GithubFeignClient githubFeignClient, String apiVersion, String authToken) {
        this.feignClient = githubFeignClient;
        this.apiVersion = apiVersion;
        this.authToken = authToken;
    }

    public List<GithubUserRepositoryInfo> getUserInformation(String username) {
         try {
             log.trace("Requesting data about repositories for user " + username);
             return feignClient.getUserReposInfo(username, apiVersion, authToken);
         } catch (FeignException.NotFound e) {
             log.warn("User " + username + " not found");
             throw new UserNotFoundException(username);
         } catch (FeignException e) {
             log.warn("Feign client exception", e);
             throw new RuntimeException("Client exception");
         }
    }

    public List<Branch> getBranches(String username, String reponame) {
        try {
            log.trace("Requesting data about branches for user " + username + " in repository " + reponame);
            return feignClient.getRepositoryBranches(username, reponame, apiVersion, authToken);
        } catch (FeignException e) {
            log.warn("Feign client exception ", e);
            throw new RuntimeException("Client exception");
        }
    }
}
