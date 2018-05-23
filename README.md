### Demo - RESTful api for manipulating users based on Akka Http and Slick

### How to run
```
sbt run
```
It starts a server listening on 0.0.0.0:8585

### Routes
| Method   | Route                                        |
| -------- |--------------------------------------------- |
| GET      | http://localhost:8585/api/v1/users           |
| GET      | http://localhost:8585/api/v1/users/{user id} |
| POST     | http://localhost:8585/api/v1/users/          |
| PUT      | http://localhost:8585/api/v1/users/{user id} |
| PUT      | http://localhost:8585/api/v1/users/{user id}/changePass |
| DELETE   | http://localhost:8585/api/v1/users/{user id} |

### Create new user
Request:
```
  curl -d '{"username":"mostafa", "password":"123456" , "balance":20}' -H "Content-Type: application/json" http://localhost:8585/api/v1/users
```
Response:
```
{"userId":1,"success":true}
```
Create another user with the same username as previous request. You should get response like this:
```
{"message":"Username has already taken","success":false}
``` 
Try create another user with short password less than 6 character. You should get response like this:
```
{"message":"Password is too short","success":false}
```
### Get all users
Request:
```
curl -H "Content-Type: application/json" http://localhost:8585/api/v1/users
```
Response:
```
[{"id":1,"username":"mostafa","balance":20.0}]
```
### Get a specific user by id
Request:
```
curl -H "Content-Type: application/json" http://localhost:8585/api/v1/users/1
```
Response:
```
{"id":1,"username":"mostafa","balance":20.0}
```
### Update a user
Request:
```
curl -d '{"username":"asgari", "balance":100}' -H "Content-Type: application/json" -X PUT http://localhost:8585/api/v1/users/1
```
Response:
```
{"message":"Updated","success":true}
```
### Change password
Request:
```
curl -d '{"oldPass":"123456", "newPass":"987654"}' -H "Content-Type: application/json" -X PUT http://localhost:8585/api/v1/users/1/changePass
```
Response:
```
{"message":"Password has been changed","success":true}
```
### Delete a user by id
Request:
```
curl -H "Content-Type: application/json" -X DELETE http://localhost:8585/api/v1/users/1
```
Response:
```
ok
```
