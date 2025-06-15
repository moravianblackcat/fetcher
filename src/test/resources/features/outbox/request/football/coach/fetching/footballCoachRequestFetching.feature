@football
Feature: Fetching data for football coach request
  
  Scenario: Obtaining football coach profile
	Given Football coach request for coach ID 455800 from Sportmonks exists as SCHEDULED
	And Football coach request for coach ID 529482 from Sportmonks exists as RETRY
	And Football coach request for coach ID 154461 from Sportmonks exists as ERROR
	And Football coach request for coach ID 154481 from Sportmonks exists as RESOURCE_NOT_FOUND
	And Sportmonks API returns response defined in sportmonks/football/coach/455800.json for the /v3/football/coaches/455800 endpoint with those inclusions:
	  | nationality |
	And Sportmonks API returns response defined in sportmonks/football/coach/529482.json for the /v3/football/coaches/529482 endpoint with those inclusions:
	  | nationality |
	When footballCoach job is triggered
	Then 2s Those persons are saved:
	  | id | source_id | source     |
	  | 1  | 455800    | Sportmonks |
	  | 2  | 529482    | Sportmonks |
	And 2s Those persons are persisted to outbox:
	  | id | nationality | first_name | last_name | name            | display_name    | date_of_birth |
	  | 1  | ITA         | Carlo      | Ancelotti | Carlo Ancelotti | Carlo Ancelotti | 1959-06-10    |
	  | 2  | FRA         | Régis      | Le Bris   | Régis Le Bris   | Régis Le Bris   | 1975-12-06    |
	And 2s Those football coach requests are marked as COMPLETED:
	  | id     | source     |
	  | 455800 | Sportmonks |
	  | 529482 | Sportmonks |
  
  Scenario Outline: Obtaining football coach profile with API unreachable
	Given Football coach request for coach ID 455800 from Sportmonks exists as SCHEDULED
	And Sportmonks API returns <httpStatusCode> status code for the /v3/football/coaches/455800 endpoint with those inclusions:
	  | nationality |
	When footballCoach job is triggered
	Then 2s No persons are saved
	And 2s Those football coach requests are marked as RETRY:
	  | id     | source     |
	  | 455800 | Sportmonks |
	And 2s The football coach request for 455800 has '<failureContent>' in failure reason
	Examples:
	  | httpStatusCode | failureContent                          |
	  | 500            | [500 Server Error] during [GET] to      |
	  | 429            | [429 Too Many Requests] during [GET] to |
  
  Scenario: Obtaining football coach profile with client error
	Given Football coach request for coach ID 455800 from Sportmonks exists as SCHEDULED
	And Sportmonks API returns 400 status code for the /v3/football/coaches/455800 endpoint with those inclusions:
	  | nationality |
	When footballCoach job is triggered
	Then 2s No persons are saved
	And 2s Those football coach requests are marked as ERROR:
	  | id     | source     |
	  | 455800 | Sportmonks |
	And 2s The football coach request for 455800 has '[400 Bad Request] during [GET] to' in failure reason
  
  Scenario: Obtaining non-existing football coach profile
	Given Football coach request for coach ID 151 from Sportmonks exists as SCHEDULED
	And Sportmonks API returns response defined in sportmonks/nonExistingId.json for the /v3/football/coaches/151 endpoint with those inclusions:
	  | nationality |
	When footballCoach job is triggered
	Then 2s No persons are saved
	And 2s Those football coach requests are marked as RESOURCE_NOT_FOUND:
	  | id  | source     |
	  | 151 | Sportmonks |