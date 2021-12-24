# AppointmentTracker

This application is for a single user to track their appointments.
Appointments must contain the date and time of their occurrence as well as a description of what the appointment is.
Optionally, the appointment may include the person or group the user is meeting with and/or the location the appointment is taking place

## Setting up application locally
The only external dependent service needed is a database. I am using a MySQL 8.0 database running on port 3306 that includes a database appropriately named "bestdb", as shown in the application.properties file.

You will need a database user created with at least "select", "insert" and "delete" privileges on the following tables, which are roughly defined in the mysqlDatabaseConfiguraitn.sql resource file:
 - appointments - Contains all the appointments for the user
 - people - Contains all the people an appointment can be with
 - locations - Contains all the locations an appointment can be held

To run the application, simply start the main method in the AppointmentApplication class. This will start up the server and will be listening to port 8080 by default.

Open up a Chrome tab and navigate to localhost:8080/appointments where you can view the webpage.