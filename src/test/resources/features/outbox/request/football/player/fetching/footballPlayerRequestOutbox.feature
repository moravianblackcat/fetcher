@football
Feature: Football player request outbox
  
  Scenario: Sending football players
	Given Those football players are prepared for dispatching:
	  | id | nationality | position   | firstName     | lastName | name                  | displayName   | dateOfBirth |
	  | 1  | CZE         | midfielder | Tomáš         | Rosický  | Tomáš Rosický         | Tomáš Rosický | 1980-10-04  |
	  | 2  | ENG         | defender   | Titus Malachi | Bramble  | Titus Malachi Bramble | Titus Bramble | 1981-07-21  |
	When footballPlayerOutbox job is triggered
	Then 2s Football players defined in outbox/football/player/footballPlayerOutboxPayloads.json are sent
	And 2s There are no football players prepared for dispatching