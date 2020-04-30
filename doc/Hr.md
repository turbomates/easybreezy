
# HR



## Indices

* [Default](#default)

  * [Employees](#1-employees)
  * [Employee](#2-employee)
  * [Update My Contacts](#3-update-my-contacts)
  * [Update User Contacts](#4-update-user-contacts)
  * [Update Birthday](#5-update-birthday)
  * [Specify Skills](#6-specify-skills)
  * [Apply Salary](#7-apply-salary)
  * [Apply position](#8-apply-position)
  * [Write Note](#9-write-note)
  * [Fire](#10-fire)
  * [Hire](#11-hire)


--------


## Default



### 1. Employees



***Endpoint:***

```bash
Method: GET
Type: RAW
URL: localhost:3000/api/hr/employees
```


***Headers:***

| Key | Value | Description |
| --- | ------|-------------|
| Content-Type | application/json |  |



***Responses:***


Status: Employees | Code: 200



```js
{
    "pageSize": 30,
    "currentPage": 1,
    "hasMore": false,
    "data": [
        {
            "userId": "a2b40e7c-ed59-4b9b-a1bf-6854e611a78c",
            "firstName": null,
            "lastName": null
        }
    ]
}
```



### 2. Employee



***Endpoint:***

```bash
Method: GET
Type: RAW
URL: localhost:3000/api/hr/employee/a2b40e7c-ed59-4b9b-a1bf-6854e611a78c
```


***Headers:***

| Key | Value | Description |
| --- | ------|-------------|
| Content-Type | application/json |  |



***Responses:***


Status: Employee | Code: 200



```js
{
    "data": {
        "userId": "a2b40e7c-ed59-4b9b-a1bf-6854e611a78c",
        "firstName": null,
        "lastName": null,
        "skills": [
            "Kotlin",
            "JSON",
            "Agile"
        ],
        "birthday": "2000-01-01",
        "bio": "Employee's nice bio",
        "notes": [
            {
                "id": "6d9458b5-ce94-47a0-9ec9-f761f891ec83",
                "text": "note about employee",
                "archived": false,
                "authorId": "0c48c7c8-baa7-4fa3-a5f4-3d4bc5eede75",
                "createdAt": "2020-03-11T16:16:10.647"
            }
        ],
        "salaries": [
            {
                "id": "07c189da-9565-4197-afce-3cf16bd2e08d",
                "amount": 1000,
                "comment": "",
                "since": "2020-03-11",
                "till": "2020-03-10"
            },
            {
                "id": "82e8ae8f-a812-42b6-a3b5-cfb64a5a5f51",
                "amount": 1200,
                "comment": "he said that leave if we won't up his salary",
                "since": "2020-03-11",
                "till": "null"
            }
        ],
        "positions": [
            {
                "id": "61530765-ffa8-417a-922c-017691f74bd0",
                "title": "Senior CSS Developer",
                "since": "2020-03-11",
                "till": "null"
            },
            {
                "id": "57b9418f-eeb5-4c63-8ad8-e7a69130d5be",
                "title": "PM",
                "since": "2020-03-11",
                "till": "2020-03-10"
            }
        ]
    }
}
```



### 3. Update My Contacts



***Endpoint:***

```bash
Method: POST
Type: RAW
URL: localhost:3000/api/users/update-contacts
```


***Headers:***

| Key | Value | Description |
| --- | ------|-------------|
| Content-Type | application/json |  |



***Body:***

```js        
{
    "contacts": [
        {
            "type": "SKYPE",
            "value": "specific contact"
        },
        {
            "type": "PHONE",
            "value": "specific contact"
        },
        {
            "type": "EMAIL",
            "value": "specific@gmail.com"
        },
        {
            "type": "TELEGRAM",
            "value": "specific contact"
        },
        {
            "type": "SLACK",
            "value": "specific contact"
        }
    ]
}
```



***Responses:***


Status: Apply Salary | Code: 200



```js
{
    "status": "ok"
}
```

### 4. Update User Contacts



***Endpoint:***

```bash
Method: POST
Type: RAW
URL: localhost:3000/api/users/update-contacts/{id}
```


***Headers:***

| Key | Value | Description |
| --- | ------|-------------|
| Content-Type | application/json |  |



***Body:***

```js        
{
    "contacts": [
        {
            "type": "SKYPE",
            "value": "specific contact"
        },
        {
            "type": "PHONE",
            "value": "specific contact"
        },
        {
            "type": "EMAIL",
            "value": "specific@gmail.com"
        },
        {
            "type": "TELEGRAM",
            "value": "specific contact"
        },
        {
            "type": "SLACK",
            "value": "specific contact"
        }
    ]
}
```



***Responses:***


Status: Apply Salary | Code: 200



```js
{
    "status": "ok"
}
```


### 5. Update Birthday



***Endpoint:***

```bash
Method: POST
Type: RAW
URL: localhost:3000/api/hr/employee/a2b40e7c-ed59-4b9b-a1bf-6854e611a78c/update-birthday
```


***Headers:***

| Key | Value | Description |
| --- | ------|-------------|
| Content-Type | application/json |  |



***Body:***

```js        
{
	"birthday": "2000-01-01"
}
```



### 6. Specify Skills



***Endpoint:***

```bash
Method: POST
Type: RAW
URL: localhost:3000/api/hr/employee/a2b40e7c-ed59-4b9b-a1bf-6854e611a78c/specify-skills
```


***Headers:***

| Key | Value | Description |
| --- | ------|-------------|
| Content-Type | application/json |  |



***Body:***

```js        
{
	"skills": ["Kotlin", "JSON", "Agile"]
}
```



### 7. Apply Salary



***Endpoint:***

```bash
Method: POST
Type: RAW
URL: localhost:3000/api/hr/employee/a2b40e7c-ed59-4b9b-a1bf-6854e611a78c/apply-salary
```


***Headers:***

| Key | Value | Description |
| --- | ------|-------------|
| Content-Type | application/json |  |



***Body:***

```js        
{
	"amount": 1200,
	"comment": "he said that leave if we won't up his salary"
}
```



***Responses:***


Status: Apply Salary | Code: 200



```js
{
    "status": "ok"
}
```



### 8. Apply position



***Endpoint:***

```bash
Method: POST
Type: RAW
URL: localhost:3000/api/hr/employee/a2b40e7c-ed59-4b9b-a1bf-6854e611a78c/apply-position
```


***Headers:***

| Key | Value | Description |
| --- | ------|-------------|
| Content-Type | application/json |  |



***Body:***

```js        
{
	"position": "Senior CSS Developer"
}
```



### 9. Write Note



***Endpoint:***

```bash
Method: POST
Type: RAW
URL: localhost:3000/api/hr/employee/a2b40e7c-ed59-4b9b-a1bf-6854e611a78c/write-note
```


***Headers:***

| Key | Value | Description |
| --- | ------|-------------|
| Content-Type | application/json |  |



***Body:***

```js        
{
	"text": "note about employee"
}
```



### 10. Fire



***Endpoint:***

```bash
Method: POST
Type: RAW
URL: localhost:3000/api/hr/employee/a2b40e7c-ed59-4b9b-a1bf-6854e611a78c/fire
```


***Headers:***

| Key | Value | Description |
| --- | ------|-------------|
| Content-Type | application/json |  |



***Body:***

```js        
{
	"comment": "he became PUBG streamer"
}
```



***Responses:***


Status: Fire | Code: 200



```js
{
    "status": "ok"
}
```



### 11. Hire



***Endpoint:***

```bash
Method: POST
Type: RAW
URL: localhost:3000/api/hr/employee/a2b40e7c-ed59-4b9b-a1bf-6854e611a78c/hire
```


***Headers:***

| Key | Value | Description |
| --- | ------|-------------|
| Content-Type | application/json |  |



***Body:***

```js        
{
	"position": "PM",
	"salary": 1000,
	"birthday": "2000-01-01",
	"bio":  "Employee's nice bio",
	"skills": ["PHP", "CSS", "CAN COUNT TO 100"]
}
```



---
[Back to top](#hr)
> Made with &#9829; by [thedevsaddam](https://github.com/thedevsaddam) | Generated at: 2020-03-11 17:14:51 by [docgen](https://github.com/thedevsaddam/docgen)
