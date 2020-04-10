
# Hr/Absence

Api for Absences

## Indices

* [Default](#default)

  * [Absence Show](#7-absence-show)
  * [Absences](#8-absences)
  * [Absences Me](#9-absences-me)
  * [Absence Remove](#10-absence-remove)
  * [Absence update](#11-absence-update)
  * [Absence create](#12-absence-create)


--------


## Default


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
