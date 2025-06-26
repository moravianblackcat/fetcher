@football
Feature: Football coach request inbox
  
  Scenario: Request for not-yet requested coaches
	When The following list of football coaches is requested to be fetched from Sportmonks:
	  | 1543 |
	  | 1544 |
	Then 4s Those football coach requests are persisted:
	  | id   | source     | state     |
	  | 1543 | Sportmonks | SCHEDULED |
	  | 1544 | Sportmonks | SCHEDULED |
  
  Scenario: Request for coaches with some already requested
	Given Football coach request for coach ID 154421 from Sportmonks exists as SCHEDULED
	When The following list of football coaches is requested to be fetched from Sportmonks:
	  | 1543 |
	  | 1544 |
	Then 2s Those football coach requests are persisted:
	  | id   | source     | state     |
	  | 1543 | Sportmonks | SCHEDULED |
	  | 1544 | Sportmonks | SCHEDULED |