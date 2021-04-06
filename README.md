#Article API

Article api application is used to create an api response based on request to create,update,delete,retrive and generate metrics based on the request.

#Find time to read for an article using its slug id
For this tag metrics calculation average human reading speed is made configurable by giving value in the application.properties. 

average.humanReadingSpeed = 5

#Database

The application uses H2 database.
Based on the entity and the values given in application.properties,the Article.java entity and its pojo the tables are created when the application is started.

The request is given and api call is done and response is shown in JSON format.
