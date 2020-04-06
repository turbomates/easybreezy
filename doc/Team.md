
# Teams



## Indices

* [Default](#default)

  * [Remove member](#1-remove-member)
  * [Change member role](#2-change-member-role)
  * [View](#3-view)
  * [Add member](#4-add-member)
  * [Activate](#5-activate)
  * [Close](#6-close)
  * [Add](#7-add)


--------


## Default



### 1. Remove member



***Endpoint:***

```bash
Method: POST
Type: RAW
URL: localhost:3000/api/teams/{team-id}/members/{user-id}/remove
```



***Body:***

```js        
{}
```



### 2. Change member role



***Endpoint:***

```bash
Method: POST
Type: RAW
URL: localhost:3000/api/teams/{team-id}/members/{user-id}/change-role
```



***Body:***

```js        
{
	"newRoleId" : "2721fe9e-d326-4f99-b79f-459ece0fbc6a"
}
```



### 3. View



***Endpoint:***

```bash
Method: GET
Type: RAW
URL: localhost:3000/api/teams/{team-id}
```



***Body:***

```js        
{
	"role" : "20a0b150-6895-4e3b-b7f7-9eaac367bbdb",
	"user" : "{user-id}"
}
```



### 4. Add member



***Endpoint:***

```bash
Method: POST
Type: RAW
URL: localhost:3000/api/teams/{team-id}/members/add
```



***Body:***

```js        
{
	"role" : "20a0b150-6895-4e3b-b7f7-9eaac367bbdb",
	"user" : "{user-id}"
}
```



### 5. Activate



***Endpoint:***

```bash
Method: POST
Type: RAW
URL: localhost:3000/api/teams/{team-id}/activate
```



***Body:***

```js        
{}
```



### 6. Close



***Endpoint:***

```bash
Method: POST
Type: RAW
URL: localhost:3000/api/teams/{team-id}/close
```



***Body:***

```js        
{}
```



### 7. Add



***Endpoint:***

```bash
Method: POST
Type: RAW
URL: localhost:3000/api/teams/add
```



***Body:***

```js        
{
	"name" : "new team",
	"project" : "4a658ede-b156-4f10-9d49-2fbaf802b4f2"
}
```



---
[Back to top](#teams)
> Made with &#9829; by [thedevsaddam](https://github.com/thedevsaddam) | Generated at: 2020-04-06 17:35:59 by [docgen](https://github.com/thedevsaddam/docgen)
