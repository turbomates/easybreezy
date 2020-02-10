

# Absence

Api for Absences and Working Hours

## Indices

* [Default](#default)

  * [WorkingHours Show](#1-workinghours-show)
  * [WorkingHours Listing](#2-workinghours-listing)
  * [Working Hours Delete](#3-working-hours-delete)
  * [Working Hours Edit](#4-working-hours-edit)
  * [Working Hours Create](#5-working-hours-create)
  * [Absence Show](#6-absence-show)
  * [Absence Listing](#7-absence-listing)
  * [Absence Remove](#8-absence-remove)
  * [Absence update](#9-absence-update)
  * [Absence create](#10-absence-create)


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



### 2. WorkingHours Listing



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



### 3. Working Hours Delete



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



### 4. Working Hours Edit



***Endpoint:***

```bash
Method: PUT
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



### 5. Working Hours Create



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



### 6. Absence Show



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



### 7. Absence Listing



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



### 8. Absence Remove



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



### 9. Absence update



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



### 10. Absence create



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
> Made with &#9829; by [thedevsaddam](https://github.com/thedevsaddam) | Generated at: 2020-02-07 13:04:28 by [docgen](https://github.com/thedevsaddam/docgen)
