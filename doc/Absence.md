
# Hr/Absence

Api for Absences and Working Hours

## Indices

* [Default](#default)

  * [WorkingHours Show](#1-workinghours-show)
  * [WorkingHours](#2-workinghours)
  * [WorkingHours Me](#3-workinghours-me)
  * [Working Hours Delete](#4-working-hours-delete)
  * [Working Hours Edit](#5-working-hours-edit)
  * [Working Hours Create](#6-working-hours-create)
  * [Absence Show](#7-absence-show)
  * [Absences](#8-absences)
  * [Absences Me](#9-absences-me)
  * [Absence Remove](#10-absence-remove)
  * [Absence update](#11-absence-update)
  * [Absence create](#12-absence-create)


--------


## Default



### 1. WorkingHours Show



***Endpoint:***

```bash
Method: GET
Type: RAW
URL: localhost:3000/api/hr/absences/working-hours/c34fd09b-2668-4e11-ae3d-0a04a8345d1b
```


***Headers:***

| Key | Value | Description |
| --- | ------|-------------|
| Content-Type | application/json |  |



### 2. WorkingHours



***Endpoint:***

```bash
Method: GET
Type: RAW
URL: localhost:3000/api/hr/absences/working-hours
```


***Headers:***

| Key | Value | Description |
| --- | ------|-------------|
| Content-Type | application/json |  |



### 3. WorkingHours Me



***Endpoint:***

```bash
Method: GET
Type: RAW
URL: localhost:3000/api/hr/absences/working-hours/me
```


***Headers:***

| Key | Value | Description |
| --- | ------|-------------|
| Content-Type | application/json |  |



### 4. Working Hours Delete



***Endpoint:***

```bash
Method: DELETE
Type: RAW
URL: localhost:3000/api/hr/absences/working-hours
```


***Headers:***

| Key | Value | Description |
| --- | ------|-------------|
| Content-Type | application/json |  |



***Body:***

```js        
{
    "workingHours": [
        "4c4af1e4-0ca1-4437-9f48-3fa7372b2323",
        "bb5a4203-0601-416f-ba76-e1f6d4a2bd26"
    ]
}
```



### 5. Working Hours Edit



***Endpoint:***

```bash
Method: POST
Type: RAW
URL: localhost:3000/api/hr/absences/working-hours/update
```


***Headers:***

| Key | Value | Description |
| --- | ------|-------------|
| Content-Type | application/json |  |



***Body:***

```js        
{
    "workingHours": {
        "4c4af1e4-0ca1-4437-9f48-3fa7372b2323": {
            "day": "2020-04-14",
            "count": 6
        },
        "bb5a4203-0601-416f-ba76-e1f6d4a2bd26": {
            "day": "2020-04-15",
            "count": 7
        }
    }
}
```



### 6. Working Hours Create



***Endpoint:***

```bash
Method: POST
Type: RAW
URL: localhost:3000/api/hr/absences/working-hours
```


***Headers:***

| Key | Value | Description |
| --- | ------|-------------|
| Content-Type | application/json |  |



***Body:***

```js        
{
	"userId": "433ea435-c7cc-47a8-a518-9cc1e45c09cb",
    "workingHours": [
        {
            "day": "2020-03-12",
            "count": 5
        },
        {
            "day": "2020-03-13",
            "count": 4
        }
    ]
}
```



### 7. Absence Show



***Endpoint:***

```bash
Method: GET
Type: RAW
URL: localhost:3000/api/hr/absences/e13eb3fa-f3dd-4e60-b1f0-515c060a4e0c
```


***Headers:***

| Key | Value | Description |
| --- | ------|-------------|
| Content-Type | application/json |  |



### 8. Absences



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



### 9. Absences Me



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



### 10. Absence Remove



***Endpoint:***

```bash
Method: DELETE
Type: RAW
URL: localhost:3000/api/hr/absences/33f44946-68cb-42fe-8aaa-9e5243056181
```


***Headers:***

| Key | Value | Description |
| --- | ------|-------------|
| Content-Type | application/json |  |



### 11. Absence update



***Endpoint:***

```bash
Method: POST
Type: RAW
URL: localhost:3000/api/hr/absences/b0cffbc3-eb51-4799-ad79-62b0fbf177cf
```


***Headers:***

| Key | Value | Description |
| --- | ------|-------------|
| Content-Type | application/json |  |



***Body:***

```js        
{
	"startedAt": "2119-03-12",
	"endedAt": "2120-03-12",
	"reason": "DAYON",
	"comment": "can be nullable",
	"location": "Belarus (can be nullable)"
}
```



### 12. Absence create



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
	"startedAt": "2020-07-19",
	"endedAt": "2020-08-19",
	"reason": "VACATION",
	"userId": "433ea435-c7cc-47a8-a518-9cc1e45c09cb",
	"comment": "can be nullable",
	"location": "Belarus (can be nullable)"
}
```



---
[Back to top](#hrabsence)
> Made with &#9829; by [thedevsaddam](https://github.com/thedevsaddam) | Generated at: 2020-02-18 15:32:21 by [docgen](https://github.com/thedevsaddam/docgen)
