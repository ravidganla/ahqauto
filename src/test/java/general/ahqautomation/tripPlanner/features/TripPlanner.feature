Feature: Trip Planner

  Scenario: A trip request can be executed and results returned
    Given Phileas is planning a trip using "chrome" browser
    When he executes a trip plan from "North Sydney Station" to "Town Hall Station"
    Then a list of trips should be provided