
# User



## Indices

* [Default](#default)

  * [Logout](#1-logout)
  * [Login User](#2-login-user)
  * [Login Admin](#3-login-admin)
  * [User confirm](#4-user-confirm)
  * [User invite exist user](#5-user-invite-exist-user)
  * [User invite new user](#6-user-invite-new-user)
  * [User create](#7-user-create)


--------


## Default



### 1. Logout



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



### 2. Login User



***Endpoint:***

```bash
Method: POST
Type: RAW
URL: https://easybreezy.tmsoft.dev/api/login
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



### 3. Login Admin



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



### 4. User confirm



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



### 5. User invite exist user



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



### 6. User invite new user



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
	"email": "novikov.ivan.work@gmail.com",
	"role": "MEMBER"
}
```



### 7. User create



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
	"email": "novikov.ivan.work6@gmail.com",
	"role": "MEMBER",
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
> Made with &#9829; by [thedevsaddam](https://github.com/thedevsaddam) | Generated at: 2020-04-21 15:07:51 by [docgen](https://github.com/thedevsaddam/docgen)
