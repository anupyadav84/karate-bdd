Feature: sample karate test script

  Background:
    * url 'http://localhost:8080'

  Scenario: get all companies

    Given path 'companies'
    When method get
    Then status 200
    And match $ == '#[2]'
    And match each $ contains {name: '#notnull'}
    And match each $ contains {email: '#regex [A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\.[A-Za-z]{2,4}'}


  Scenario Outline: check company name

    Given path 'companies', '<cif>'
    When method get
    Then status 200
    And match $ contains {name: '<name>'}

    Examples:
      | cif       | name              |
      | B84946656 | Paradigma Digital |
      | B82627019 | Minsait by Indra  |

  Scenario Outline: create new companies

    Given path 'companies'
    And def company =
    """
    {
      "cif": "<cif>",
      "name": "<name>",
      "email": "<email>"
    }
    """
    And request company
    When method post
    Then status 201
    And match $ contains company
    And match $ contains {cif: '#notnull'}

    Examples:
      | cif       | name    | email              |
      | B18996504 | Stratio | info@stratio.com   |
      | B82657941 | Redhat  | apac@redhat.comcom |
