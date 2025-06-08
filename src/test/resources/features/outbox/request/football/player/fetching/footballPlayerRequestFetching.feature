@football
Feature: Fetching data for football player request
  
  Scenario: Obtaining football player profile
	Given Football player request for player ID 154421 from Sportmonks exists as SCHEDULED
	And Football player request for player ID 154441 from Sportmonks exists as RETRY
	And Football player request for player ID 154461 from Sportmonks exists as ERROR
	And Football player request for player ID 154481 from Sportmonks exists as RESOURCE_NOT_FOUND
	And Sportmonks API returns response defined in football/player/154421.json for the /v3/football/players/154421 endpoint with those inclusions:
	  | nationality |
	  | position    |
	And Sportmonks API returns response defined in football/player/154441.json for the /v3/football/players/154441 endpoint with those inclusions:
	  | nationality |
	  | position    |
	When footballPlayer job is triggered
	Then 2s Those football players are persisted to outbox:
	  | id | source     | source_id | nationality | position | first_name   | last_name | name            | display_name     | date_of_birth |
	  | 1  | Sportmonks | 154421    | NOR         | forward  | Erling Braut | Haaland   | Erling Håland   | Erling Haaland   | 2000-07-21    |
	  | 2  | Sportmonks | 154441    | POL         | defender | Batosz       | Rymaniak  | Batosz Rymaniak | Bartosz Rymaniak | 1989-11-13    |
	And 2s Those football player requests are marked as COMPLETED:
	  | id     | source     |
	  | 154421 | Sportmonks |
	  | 154441 | Sportmonks |
  
  Scenario Outline: Obtaining football player profile with API unreachable
	Given Football player request for player ID 154421 from Sportmonks exists as SCHEDULED
	And Sportmonks API returns <httpStatusCode> status code for the /v3/football/players/154421 endpoint with those inclusions:
	  | nationality |
	  | position    |
	When footballPlayer job is triggered
	Then 2s Those football player requests are marked as RETRY:
	  | id     | source     |
	  | 154421 | Sportmonks |
	And 2s The football player request for 154421 has '<failureContent>' in failure reason
	Examples:
	  | httpStatusCode | failureContent                          |
	  | 500            | [500 Server Error] during [GET] to      |
	  | 429            | [429 Too Many Requests] during [GET] to |
  
  Scenario: Obtaining football player profile with client error
	Given Football player request for player ID 154421 from Sportmonks exists as SCHEDULED
	And Sportmonks API returns 400 status code for the /v3/football/players/154421 endpoint with those inclusions:
	  | nationality |
	  | position    |
	When footballPlayer job is triggered
	Then 2s Those football player requests are marked as ERROR:
	  | id     | source     |
	  | 154421 | Sportmonks |
	And 2s The football player request for 154421 has '[400 Bad Request] during [GET] to' in failure reason
	
  Scenario: Obtaining non-existing football player profile
	Given Football player request for player ID 15 from Sportmonks exists as SCHEDULED
	And Sportmonks API returns response defined in football/player/nonExistingId.json for the /v3/football/players/15 endpoint with those inclusions:
	  | nationality |
	  | position    |
	When footballPlayer job is triggered
	Then 2s Those football player requests are marked as RESOURCE_NOT_FOUND:
	  | id | source     |
	  | 15 | Sportmonks |