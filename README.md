# notification

A service providing a RESTful API for sending notifications

Requires Gradle.

`$ gradle bootRun` will build and run the application locally making it available at http://localhost:8080/

## Resources
User
```json
{
  "username": "aUser",
  "accessToken": "anAccessToken",
  "creationTime": "2019-04-07T22:44:40",
  "numOfNotificationsPushed": 0
}
```
`creationTime` and `numOfNotificationsPushed` are optional

Message
```json
{
  "username": "aUser",
  "text": "a message"
}
```
## Usage

**Register a user**

Post a User without `creationTime` or `numOfNotificationsPushed` to `/user`

e.g.
```
curl -s -X POST localhost:8080/user -H 'Content-type:application/json' -d '{"username": "dxc", "accessToken": "accessToken"}'
```

**Get all registered users**

Send a get request to `/users` returns an array of Users

e.g.
```
curl -s http://localhost:8080/users
```

**Send a post notification to a user**

Post a Message to `/message`

e.g.
```
curl -v -s -X POST localhost:8080/message -H 'Content-type:application/json' -d '{"username": "dxc", "text": "hi"}'
```