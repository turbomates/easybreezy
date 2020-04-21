
# User



## Indices

* [Default](#default)

  * [User](#1-user)
  * [Users](#2-users)
  * [Logout](#3-logout)
  * [Login User](#4-login-user)
  * [Login Admin](#5-login-admin)
  * [User confirm](#6-user-confirm)
  * [User archive](#7-user-archive)
  * [User invite exist user](#8-user-invite-exist-user)
  * [User invite new user](#9-user-invite-new-user)
  * [User create](#10-user-create)


--------


## Default



### 1. User



***Endpoint:***

```bash
Method: GET
Type: RAW
URL: localhost:3000/api/users/me
```


***Headers:***

| Key | Value | Description |
| --- | ------|-------------|
| Content-Type | application/json |  |



***Responses:***


Status: Success | Code: 200

```js
{
    "data": {
        "id": "98e397c1-589f-424e-a641-e433aa33d00d",
        "email": "admin@admin.my",
        "status": "ACTIVE",
        "comment": null,
        "roles": [
            "ADMIN"
        ]
    }
}
```



### 2. Users



***Endpoint:***

```bash
Method: GET
Type: RAW
URL: localhost:3000/api/users
```


***Headers:***

| Key | Value | Description |
| --- | ------|-------------|
| Content-Type | application/json |  |



***Responses:***


Status: Success | Code: 200

```js
{
    "pageSize": 30,
    "currentPage": 1,
    "hasMore": false,
    "data": [
        {
            "id": "98e397c1-589f-424e-a641-e433aa33d00d",
            "email": "admin@admin.my",
            "status": "ACTIVE",
            "comment": null,
            "roles": [
                "ADMIN"
            ]
        },
        {
            "id": "8f631c73-a516-4576-b19b-4929b665e9dc",
            "email": "novikov.ivan.work@gmail.com",
            "status": "ACTIVE",
            "comment": null,
            "roles": [
                "MEMBER"
            ]
        },
        {
            "id": "47cbf3c0-de71-4770-834f-e25c31c927ef",
            "email": "novikov.ivan.work6@gmail.com",
            "status": "WAIT_CONFIRM",
            "comment": null,
            "roles": [
                "MEMBER"
            ]
        },
        {
            "id": "1628ccbb-81b1-4667-b3a9-d61ec14fe7de",
            "email": "novikov.ivan.work7@gmail.com",
            "status": "ARCHIVED",
            "comment": "Some reason to archive user",
            "roles": [
                "MEMBER",
                "PROJECT_LIST"
            ]
        }
    ]
}
```



### 3. Logout



***Endpoint:***

```bash
Method: GET
Type: RAW
URL: localhost:3000/api/logout
```


***Headers:***

| Key | Value | Description |
| --- | ------|-------------|
| Content-Type | application/json |  |



***Body:***

```js        
{
    "email": "novikov.ivan.work@gmail.com",
    "password": "123"
}
```



### 4. Login User



***Endpoint:***

```bash
Method: POST
Type: RAW
URL: localhost:3000/api/login
```


***Headers:***

| Key | Value | Description |
| --- | ------|-------------|
| Content-Type | application/json |  |



***Body:***

```js        
{
    "email": "admin@admin.my",
    "password": "123"
}
```



### 5. Login Admin



***Endpoint:***

```bash
Method: POST
Type: RAW
URL: localhost:3000/api/login
```


***Headers:***

| Key | Value | Description |
| --- | ------|-------------|
| Content-Type | application/json |  |



***Body:***

```js        
{
    "email": "admin@admin.my",
    "password": "123"
}
```



### 6. User confirm



***Endpoint:***

```bash
Method: POST
Type: RAW
URL: localhost:3000/api/users/confirm
```


***Headers:***

| Key | Value | Description |
| --- | ------|-------------|
| Content-Type | application/json |  |



***Body:***

```js        
{
	"token": "BhAZF6jq6ST8e8sqKacGOpeaOYKsIMWsAD24ki7K",
	"password": "123",
	"firstName": "Ivan",
	"lastName": "Novikov"
}
```



### 7. User archive



***Endpoint:***

```bash
Method: POST
Type: RAW
URL: localhost:3000/api/users/1628ccbb-81b1-4667-b3a9-d61ec14fe7de/archive
```


***Headers:***

| Key | Value | Description |
| --- | ------|-------------|
| Content-Type | application/json |  |



***Body:***

```js        
{
	"reason": "Some reason to archive user"
}
```



***Responses:***


Status: Error already invited | Code: 422



***Response Headers:***

```js
{
    "message": "User have been already invited"
}
```



Status: Success | Code: 200

```js
{
    "status": "ok"
}
```



Status: Success | Code: 200



***Response Headers:***

```js
{
    "status": "ok"
}
```



### 8. User invite exist user



***Endpoint:***

```bash
Method: POST
Type: RAW
URL: localhost:3000/api/users/47cbf3c0-de71-4770-834f-e25c31c927ef/invite
```


***Headers:***

| Key | Value | Description |
| --- | ------|-------------|
| Content-Type | application/json |  |



***Responses:***


Status: Error already invited | Code: 422

```js
{
    "message": "User have been already invited"
}
```



Status: Success | Code: 200

```js
{
    "status": "ok"
}
```



### 9. User invite new user



***Endpoint:***

```bash
Method: POST
Type: RAW
URL: localhost:3000/api/users/invite
```


***Headers:***

| Key | Value | Description |
| --- | ------|-------------|
| Content-Type | application/json |  |



***Body:***

```js        
{
    "email": "novikov.ivan.work3@gmail.com",
    "role": [
        "MEMBER",
        "PROJECTS_LIST"
    ]
}
```



### 10. User create



***Endpoint:***

```bash
Method: POST
Type: RAW
URL: localhost:3000/api/users
```


***Headers:***

| Key | Value | Description |
| --- | ------|-------------|
| Content-Type | application/json |  |



***Body:***

```js        
{
    "email": "novikov.ivan.work7@gmail.com",
    "role": [
        "MEMBER",
        "PROJECT_LIST"
    ],
    "firstName": "Ivan",
    "lastName": "Novikov"
}
```



***Responses:***


Status: Success | Code: 200

```js
{
    "status": "ok"
}
```



---
[Back to top](#user)
> Made with &#9829; by [thedevsaddam](https://github.com/thedevsaddam) | Generated at: 2020-04-21 18:18:59 by [docgen](https://github.com/thedevsaddam/docgen)
