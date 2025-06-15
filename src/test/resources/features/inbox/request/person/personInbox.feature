@person
Feature: Person inbox
  
  Scenario: Incoming person request
	When The following persons are requested to be registered:
	  | firstName | lastName       | name                   | displayName | nationality | dateOfBirth |
	  | Ellis     | Short          |                        |             | USA         |             |
	  | Romário   | de Souza Faria | Romário de Souza Faria | Romário     | BRA         | 1966-01-29  |
	Then 2s Those persons are saved:
	  | id | source_id | source |
	  | 1  |           | Custom |
	  | 2  |           | Custom |
	And 2s Those persons are persisted to outbox:
	  | id | nationality | first_name | last_name      | name                   | display_name | date_of_birth |
	  | 1  | USA         | Ellis      | Short          | Ellis Short            | Ellis Short  |               |
	  | 2  | BRA         | Romário    | de Souza Faria | Romário de Souza Faria | Romário      | 1966-01-29    |