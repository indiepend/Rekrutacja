GitHub repository info project
=====

This service requests repositories and its branches for given username using Github API.
Then filters off repositories that are forks and puts in response the remaining ones.

Before start put your PAT token into `application.properties` file at property `application.github.auth-token`.

You can also change API version that client uses by modifying `application.github.api-version` property 
**BEWARE:** changing to other version than `2022-11-28` may break the application. Use with precaution.

Response schema
```json
[
  {
    "repositoryName": String,
    "ownerName": String,
    "branches": [
      {
        "name": String,
        "lastCommitSha": String
      }
    ]
  }  
]
```

* Example working request 
```curl
curl --request GET \
  --url 'http://localhost:8080/api/repositories?username=indiepend' \
  --header 'Accept: application/json'
```
And a example response
```json
[
  {	    
    "repositoryName": "CardsAgainstHumanityClone",
    "ownerName": "indiepend",
    "branches": [
      {
        "name": "master", 
        "lastCommitSha": "de7c3a30a18a88fbd7ae260fad36008e3b7c1b72"
      }
    ]
  },	  
  {	    
    "repositoryName": "compass",
    "ownerName": "indiepend",
    "branches": [
      {
        "name": "master",
        "lastCommitSha": "05c8de5d3ec6626f89160801045cfcb1f09e3574"
      }
    ]
  }
]
```
* Example request returning 404 (user doesn't exist)
```curl
curl --request GET \
  --url 'http://localhost:8080/api/repositories?username=indiepend2137' \
  --header 'Accept: application/json'
```
Response example:
```json
{
	"message": "User with name indiepend2137 was not found",
	"status": "404 NOT_FOUND"
}
```

* Example request returning 406 (invalid Accept header)
```curl
curl --request GET \
  --url 'http://localhost:8080/api/repositories?username=indiepend' \
  --header 'Accept: applicatkion/json'
```
Response example:
```json
{
	"message": "No acceptable representation",
	"status": "406 NOT_ACCEPTABLE"
}
```

Project contains Dockerfile therefore it's possible to dockerize this service by building it with command
`docker build --tag=rekrutacja-1.0.0`
And then running with
`docker run rekrutacja-1.0.0`