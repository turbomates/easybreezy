

# User



## Indices

* [Default](#default)

  * [Logout](#1-logout)
  * [Login User](#2-login-user)
  * [User confirm](#3-user-confirm)
  * [User Invite](#4-user-invite)


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
URL: localhost:3000/api/login
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

### 3. User confirm



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



### 4. User Invite



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



---
[Back to top](#user)
> Made with &#9829; by [thedevsaddam](https://github.com/thedevsaddam) | Generated at: 2020-02-07 13:18:12 by [docgen](https://github.com/thedevsaddam/docgen)
