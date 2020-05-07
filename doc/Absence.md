
# Hr/Absence

Api for Absences and Working Hours

## Indices

* [Default](#default)

  * [Show](#1-show)
  * [All](#2-all)
  * [Absences user](#3-absences-user)
  * [Absences Me](#4-absences-me)
  * [Approve](#5-approve)
  * [Remove](#6-remove)
  * [Update](#7-update)
  * [Create](#8-create)


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



### 3. Absences user



***Endpoint:***

```bash
Method: GET
Type: RAW
URL: localhost:3000/api/hr/user/2e2ba3b5-1be5-4c77-b877-43d4cc6959aa/absences
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



### 4. Absences Me



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



### 5. Approve



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



### 6. Remove



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



### 7. Update


Possible reasons:
DAYON (absence without a reason, but it's paid, 1-5 days per year)
VACATION
SICK
PERSONAL (other cases, need to think paid or not)


***Endpoint:***

```bash
Method: POST
Type: RAW
URL: localhost:3000/api/hr/absences/a67dbf72-0d41-4cb6-beca-d8c2479568f4
```


***Headers:***

| Key | Value | Description |
| --- | ------|-------------|
| Content-Type | application/json |  |



***Body:***

```js        
{
	"startedAt": "2019-07-12",
	"endedAt": "2020-07-12",
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



### 8. Create



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
	"startedAt": "2020-05-19",
	"endedAt": "2020-05-25",
	"reason": "VACATION",
	"userId": "98e397c1-589f-424e-a641-e433aa33d00d",
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
> Made with &#9829; by [thedevsaddam](https://github.com/thedevsaddam) | Generated at: 2020-05-07 12:12:49 by [docgen](https://github.com/thedevsaddam/docgen)
