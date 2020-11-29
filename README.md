# User Story
As a user I would like to make a request to an API that will determine from a set of recipes what I can 
have for lunch today based on the contents of my fridge, so that I can quickly decide what I’ll be having 
to eat and the ingredients required to prepare the meal.

## Acceptance Criteria

* GIVEN that I am an API client AND have made a GET request to the /lunch endpoint THEN I should receive a JSON response of the recipes that I 
can prepare based on the availability of the ingredients in my fridge
* GIVEN that I am an API client AND I have made a GET request to the /lunch endpoint 
AND an ingredient is past its use-by date THEN I should not receive any recipes containing this ingredient
* GIVEN that I am an API client AND I have made a GET request to the endpoint AND 
an ingredient is past its best-before date AND is still within its date THEN any recipe containing this 
ingredient should be sorted to the bottom of the JSON response object

## Test using maven
NOTE: It is important to start the server first because it will
generate the ingredients.json file during startup

* Clone project from repository
* Go to <project-dir> and execute mvn spring-boot:run
* Generate project distribution using mvn clean install command on <project-dir> folder.This will execute client and server unit test and will be dependent on the server running successfully on default port 8080
* Open browser on http://localhost:8082/lunch

## Prepopulated test data
* recipes.json is static data based 
* ingredients.json is created on-the-fly during server startup with static/dynamic data. Static data for the list of available ingredients and dynamic best-before and use-by dates
* Ham and Cheese Toastie and Omelette will return valid ingredients, the rest have expired ingredients
