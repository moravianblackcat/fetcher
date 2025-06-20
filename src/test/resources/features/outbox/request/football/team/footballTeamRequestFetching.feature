@football
Feature: Fetching data for football team request
  
  Scenario: Obtaining football team profile
	Given Football team request for team ID 3 from Sportmonks exists as SCHEDULED
	And Football team request for team ID 15 from Sportmonks exists as RETRY
	And Football team request for team ID 102 from Sportmonks exists as ERROR
	And Football team request for team ID 55 from Sportmonks exists as RESOURCE_NOT_FOUND
	And Sportmonks API returns response defined in sportmonks/football/team/3.json for the /v3/football/teams/3 endpoint with those inclusions:
	  | venue      |
	  | country    |
	  | venue.city |
	And Sportmonks API returns response defined in sportmonks/football/team/15.json for the /v3/football/teams/15 endpoint with those inclusions:
	  | venue      |
	  | country    |
	  | venue.city |
	When footballTeam job is triggered
	Then 2s Those teams are saved:
	  | id | source_id | source     |
	  | 1  | 3         | Sportmonks |
	  | 2  | 15        | Sportmonks |
	And 2s Those football teams are persisted to outbox:
	  | id | name        | city       | founded | stadium          | country |
	  | 1  | Sunderland  | Sunderland | 1879    | Stadium of Light | ENG     |
	  | 2  | Aston Villa | Birmingham | 1874    | Villa Park       | ENG     |
	And 2s Those football team requests are marked as COMPLETED:
	  | id | source     |
	  | 3  | Sportmonks |
	  | 15 | Sportmonks |
  
  Scenario Outline: Obtaining football team profile with API unreachable
	Given Football team request for team ID 3 from Sportmonks exists as SCHEDULED
	And Sportmonks API returns <httpStatusCode> status code for the /v3/football/teams/3 endpoint with those inclusions:
	  | venue      |
	  | country    |
	  | venue.city |
	When footballTeam job is triggered
	Then 2s No teams are saved
	And 2s Those football team requests are marked as RETRY:
	  | id | source     |
	  | 3  | Sportmonks |
	And 2s The football team request for 3 has '<failureContent>' in failure reason
	Examples:
	  | httpStatusCode | failureContent                          |
	  | 500            | [500 Server Error] during [GET] to      |
	  | 429            | [429 Too Many Requests] during [GET] to |
  
  Scenario: Obtaining football team profile with client error
	Given Football team request for team ID 3 from Sportmonks exists as SCHEDULED
	And Sportmonks API returns 400 status code for the /v3/football/teams/3 endpoint with those inclusions:
	  | venue      |
	  | country    |
	  | venue.city |
	When footballTeam job is triggered
	Then 2s No teams are saved
	And 2s Those football team requests are marked as ERROR:
	  | id | source     |
	  | 3  | Sportmonks |
	And 2s The football team request for 3 has '[400 Bad Request] during [GET] to' in failure reason
  
  Scenario: Obtaining non-existing football team profile
	Given Football team request for team ID 15 from Sportmonks exists as SCHEDULED
	And Sportmonks API returns response defined in sportmonks/nonExistingId.json for the /v3/football/teams/15 endpoint with those inclusions:
	  | venue      |
	  | country    |
	  | venue.city |
	When footballTeam job is triggered
	Then 2s No teams are saved
	And 2s Those football team requests are marked as RESOURCE_NOT_FOUND:
	  | id | source     |
	  | 15 | Sportmonks |