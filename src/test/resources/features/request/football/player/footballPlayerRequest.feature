@football
Feature: Football player request
  
  Scenario: Request for not-yet requested players
	When The following list of football players is requested to be fetched from Sportmonks:
	  | 154421 |
	  | 154422 |
	Then 2s Those football player requests are persisted:
	  | player_id | source     | state     |
	  | 154421    | Sportmonks | SCHEDULED |
	  | 154422    | Sportmonks | SCHEDULED |
	
  Scenario: Request for players with some already requested
	Given Football player request for player ID 154421 from Sportmonks already exists as SCHEDULED
	When The following list of football players is requested to be fetched from Sportmonks:
	  | 154421 |
	  | 154422 |
	Then 2s Those football player requests are persisted:
	  | player_id | source     | state     |
	  | 154421    | Sportmonks | SCHEDULED |
	  | 154422    | Sportmonks | SCHEDULED |