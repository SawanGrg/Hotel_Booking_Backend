feature name:
user login authentication 

feature description:
the user provides their username and password to the system, the system checks the user's credentials
and if they are correct, the user is authenticated and can access the system.
else, the user is not authenticated and cannot access the system.

[//]: # filter interception by the feature
filters:
    none 

[//]: # API Endpoints
feature endpoints:
    - method: POST
    - url: 
    
feature usage or setup by setup procedure

the request is handled with login dto having username and password fields with necessary validation annotations.
as no filter is being intercepted by the feature, the request is handled by the controller and the service layer.


