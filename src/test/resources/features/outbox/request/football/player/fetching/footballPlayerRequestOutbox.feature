@football
Feature: Football player request inbox
  
  Scenario: Sending football players
	Given Those football players are prepared for dispatching:
	  | id | sourceId | nationality | position   | firstName     | lastName | name                  | displayName   | dateOfBirth | source     |
	  | 1  | 556      | CZE         | midfielder | Tomáš         | Rosický  | Tomáš Rosický         | Tomáš Rosický | 1980-10-04  | Sportmonks |
	  | 2  | 167      | ENG         | defender   | Titus Malachi | Bramble  | Titus Malachi Bramble | Titus Bramble | 1981-07-21  | Sportmonks |
	When footballPlayerOutbox job is triggered
	Then 2s Football players defined in outbox/football/player/footballPlayerOutboxPayloads.json are sent
	And 2s There are no football players prepared for dispatching