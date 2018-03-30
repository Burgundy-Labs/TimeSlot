# TimeSlot
[![Travis CI](https://travis-ci.org/MTUHIDE/Project_Burgundy.svg?branch=master)](https://travis-ci.org/MTUHIDE/Project_Burgundy) [![Heroku App](https://heroku-badge.herokuapp.com/?app=project-burgundy&style=flat)](http://project-burgundy.herokuapp.com)

A web application to allow learning centers to schedule + manage appointments, view schedules, and gather statistics about the center itself.

# Set-up TimeSlot
In order to build and run Project Burgundy you will need multiple things.
* SBT - [Download SBT here](http://www.scala-sbt.org/)
* IntelliJ (Or another IDE)
* Credentials for a Cloud Firestore connection
* Credentials for a Google Firebase Auth account
* An email account to send appointment reminders with

## First steps:
* Clone the TimeSlot repository
* Import as a current project in your IDE
* Set up as a SBT shell (if available)

## To build:
To build and compile Project Burgundy you will need to either open a terminal (or use an integrated IDE one) and navigate to the project root.

First, ensure that the /conf/ folder contains a complete application.conf file, and a credentials.json file (from Cloud Firestore)
The application.conf file must contain a valid email account + smtp information, as well as the web configuration for the Firebase Auth credentials. These are separate from Cloud Firestore and can be found at:

https://console.firebase.google.com/u/0/project/{PROJECT_NAME}/authentication/users and clicking "Web Setup" on the upper right.

If you are on an SBT shell you will simply execute the following command:
` run `

If you are on a regular terminal, you must execute the following:
` sbt run`

This will compile all sources, download plugins, and launch the application on:
`http://localhost:9000`

Please note that the initial load may take awhile, as source compilation will take place. Future page loads will be much quicker. Any changes should then be reflected live, as they occur.

In order to allow users to sign in, make sure that the Firebase Auth contains the correct Authorized domains (localhost, etc).
These can be changed at: https://console.firebase.google.com/u/0/project/{POROJECT_NAME}/authentication/providers
The first user to log into the application will be automatically made the role of Admin.

## To Deploy (Linux):
This following instructions are written assuming a linux-based server (such as a hosted Cpanel). 

1. Within the SBT shell (or terminal) run the command ` dist `.
2. Move the generated .zip file onto the server it is to be deployed on.
3. Unzip the folder and move into the corresponding directory.
4. Inside of the /bin/ directory run the following: ` nohup ./Project_Burgundy -Dhttps.port=9443 & `
5. This will launch the server in the background, defaulting to http port 9000, and https port 9443.
6. You can now see the PID of the program in the project's directory as RUNNING_PID.
7. You can now see console output of the program inside /bin/nohup.out
8. You're all set.
