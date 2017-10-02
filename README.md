# Project_Burgundy
![Travis CI](https://travis-ci.org/MTUHIDE/Project_Burgundy.svg?branch=master) ![Heroku](https://heroku-badge.herokuapp.com/?app=project-burgundy&style=flat)


A web application to allow learning centers to schedule + manage appointments, view schedules, and gather statistics about the center itself.

#Set-up Burgundy

In order to build and run Project Burgundy you will need multiple things.
* SBT - [Download SBT here](http://www.scala-sbt.org/)
* IntelliJ (Or another IDE)

## First steps:

* Clone the Project Burgundy repository
* Import as a current project in your IDE
* Set up as a SBT shell (if available)

## To build:
To build and compile Project Burgundy you will need to either open a terminal (or use an integrated IDE one) and navigate to the project root.

If you are on an SBT shell you will simply execute the following command:
` run `

If you are on a regular terminal, you must execute the following:
` sbt run`

This will compile all sources, download plugins, and launch the application on:
`http://localhost:9000`

Please note that the initial load may take awhile, as source compilation will take place. Future page loads will be much quicker. Any changes should then be reflected live, as they occur.
