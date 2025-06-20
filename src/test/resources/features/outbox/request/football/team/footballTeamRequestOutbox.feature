@football
Feature: Football team request outbox
  
  Scenario: Sending football teams
	Given Those football teams are prepared for dispatching:
	  | id | name       | city    | stadium                 | founded | country |
	  | 1  | Empoli     | Empoli  | Stadio Carlo Castellani | 1920    | ITA     |
	  | 2  | Aalborg BK | Aalborg | Aalborg Portland Park   | 1885    | DEN     |
	When footballTeamOutbox job is triggered
	Then 2s Football teams defined in outbox/football/team/footballTeamOutboxPayloads.json are sent
	And 2s There are no football teams prepared for dispatching