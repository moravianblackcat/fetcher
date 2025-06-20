@football
Feature: Football team request inbox
  
  Scenario: Request for not-yet requested teams
	When The following list of football teams is requested to be fetched from Sportmonks:
	  | 35464 |
	  | 27854 |
	Then 2s Those football team requests are persisted:
	  | id    | source     | state     |
	  | 35464 | Sportmonks | SCHEDULED |
	  | 27854 | Sportmonks | SCHEDULED |
  
  Scenario: Request for teams with some already requested
	Given Football team request for team ID 35464 from Sportmonks exists as SCHEDULED
	When The following list of football teams is requested to be fetched from Sportmonks:
	  | 35464 |
	  | 27854 |
	Then 2s Those football team requests are persisted:
	  | id    | source     | state     |
	  | 35464 | Sportmonks | SCHEDULED |
	  | 27854 | Sportmonks | SCHEDULED |