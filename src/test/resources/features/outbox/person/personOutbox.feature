@person
Feature: Person outbox
  
  Scenario: Sending persons
	Given Those persons are prepared for dispatching:
	  | id | nationality | firstName        | lastName            | name                                 | displayName                  | dateOfBirth |
	  | 1  | ARG         | Mauricio Roberto | Pochettino Trossero | Mauricio Roberto Pochettino Trossero | Mauricio Pochettino Trossero | 1972-03-02  |
	  | 2  | ENG         | Sam              | Allardyce           | Sam Allardyce                        | Sam Allardyce                | 1954-10-19  |
	When personOutbox job is triggered
	Then 2s Persons defined in outbox/person/personOutboxPayloads.json are sent
	And 2s There are no persons prepared for dispatching