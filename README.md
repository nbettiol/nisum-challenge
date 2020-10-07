# nisum-challenge

This is the App user service.

1) To build execute:

```
gradle build
```

2) To run it, execute:

```
gradle bootRun
```

After that, should be a new service running in localhost:8080.

First of all, you need to login into the app using the first user 'root' already created: 

```
curl -i -X POST 
  http://localhost:8080/login 
  -H 'content-type: application/json' 
  -d '{
    "email": "root@root.com",
    "password": "root"
}'
```

After that you can create new users to login using the jwt token. 

* To get all users:

```
GET http://localhost:8080/users
```

* To get some particular user:

```
GET http://localhost:8080/users/{id}
```

* To create a new user:

```
POST http://localhost:8080/users 
  -H 'content-type: application/json' 
  -d '{
    "name": "Juan Rodriguez",
    "email": "asd@rodriguez.org",
    "password": "Hunter21",
    "phones": [
        {
            "number": "1234567",
            "citycode": "1",
            "contrycode": "57"
        }
    ]
}'
```

* To edit a user

```
PUT http://localhost:8080/users 
 -H 'content-type: application/json' 
 -d '{
   "name": "Juan Rodriguez",
   "email": "asd@rodriguez.org",
   "password": "Hunter21",
   "phones": [
       {
           "number": "1234567",
           "citycode": "1",
           "contrycode": "57"
       }
   ]
}'
```
