Feature: Trip Planner

  Scenario: A stop providing multiple transport options can be located
    Given Phileas is looking for a stop
    When he searches for "Wynyard Station"
    Then a stop named "Wynyard Station, Sydney" is found
    And the stop provides more than one mode of transport

  Scenario: A trip request can be executed and results returned
    Given Phileas is planning a trip
    When he executes a trip plan from "North Sydney Station, North Sydney" to "Town Hall Station, Sydney"
    Then a list of trips should be provided
