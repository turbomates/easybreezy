# Hr/Absence

Api for Absences and Working Hours

## Indices

* [Default](#default)

  * [Show](#1-show)
  * [All](#2-all)
  * [Absences Me](#3-absences-me)
  * [Approve](#4-approve)
  * [Remove](#5-remove)
  * [Update](#6-update)
  * [Create](#7-create)


--------


## Default



### 1. Show



***Endpoint:***

```bash
Method: GET
Type: RAW
URL: localhost:3000/api/hr/absences/36833a4f-37ab-4bbd-9586-d1c627558101
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
        "id": "36833a4f-37ab-4bbd-9586-d1c627558101",
        "startedAt": "2020-02-19",
        "endedAt": "2020-02-25",
        "comment": "can be nullable",
        "reason": "VACATION",
        "userId": "8f631c73-a516-4576-b19b-4929b665e9dc",
        "isApproved": true
    }
}
```



### 2. All



***Endpoint:***

```bash
Method: GET
Type: RAW
URL: localhost:3000/api/hr/absences
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
        "absences": {
            "8f631c73-a516-4576-b19b-4929b665e9dc": [
                {
                    "id": "36833a4f-37ab-4bbd-9586-d1c627558101",
                    "startedAt": "2020-02-19",
                    "endedAt": "2020-02-25",
                    "comment": "can be nullable",
                    "reason": "VACATION",
                    "userId": "8f631c73-a516-4576-b19b-4929b665e9dc",
                    "isApproved": true
                }
            ]
        }
    }
}
```



### 3. Absences Me



***Endpoint:***

```bash
Method: GET
Type: RAW
URL: localhost:3000/api/hr/absences/me
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
        "absences": []
    }
}
```



### 4. Approve



***Endpoint:***

```bash
Method: POST
Type: RAW
URL: localhost:3000/api/hr/absences/36833a4f-37ab-4bbd-9586-d1c627558101/approve
```


***Headers:***

| Key | Value | Description |
| --- | ------|-------------|
| Content-Type | application/json |  |



***Responses:***


Status: Success | Code: 200



```js
{
    "status": "ok"
}
```



### 5. Remove



***Endpoint:***

```bash
Method: DELETE
Type: RAW
URL: localhost:3000/api/hr/absences/4025cfa9-162e-4f69-bc44-c2d39c3d9af1
```


***Headers:***

| Key | Value | Description |
| --- | ------|-------------|
| Content-Type | application/json |  |



***Responses:***


Status: Success | Code: 200



```js
{
    "status": "ok"
}
```



### 6. Update


Possible reasons:
DAYON (absence without a reason, but it's paid, 1-5 days per year)
VACATION
SICK
PERSONAL (other cases, need to think paid or not)


***Endpoint:***

```bash
Method: POST
Type: RAW
URL: localhost:3000/api/hr/absences/4025cfa9-162e-4f69-bc44-c2d39c3d9af1
```


***Headers:***

| Key | Value | Description |
| --- | ------|-------------|
| Content-Type | application/json |  |



***Body:***

```js
{
	"startedAt": "2019-04-12",
	"endedAt": "2020-04-12",
	"reason": "DAYON",
	"comment": "can be nullable"
}
```



***Responses:***


Status: Success | Code: 200



```js
{
    "status": "ok"
}
```



### 7. Create



***Endpoint:***

```bash
Method: POST
Type: RAW
URL: localhost:3000/api/hr/absences
```


***Headers:***

| Key | Value | Description |
| --- | ------|-------------|
| Content-Type | application/json |  |



***Body:***

```js
{
	"startedAt": "2020-03-19",
	"endedAt": "2020-03-25",
	"reason": "VACATION",
	"userId": "8f631c73-a516-4576-b19b-4929b665e9dc",
	"comment": "can be nullable"
}
```



***Responses:***


Status: Create | Code: 200



```js
{
    "status": "ok"
}
```



---
[Back to top](#hrabsence)
> Made with &#9829; by [thedevsaddam](https://github.com/thedevsaddam) | Generated at: 2020-04-15 12:35:17 by [docgen](https://github.com/thedevsaddam/docgen)
