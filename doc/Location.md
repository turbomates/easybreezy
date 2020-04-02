
# Hr/Location



## Indices

* [Default](#default)

  * [User locations](#1-user-locations)
  * [User location show](#2-user-location-show)
  * [User location delete](#3-user-location-delete)
  * [User location edit](#4-user-location-edit)
  * [User location assign](#5-user-location-assign)
  * [Locations](#6-locations)
  * [Location Remove](#7-location-remove)
  * [Location create](#8-location-create)


--------


## Default



### 1. User locations



***Endpoint:***

```bash
Method: GET
Type: RAW
URL: localhost:3000/api/hr/locations/user
```


***Headers:***

| Key | Value | Description |
| --- | ------|-------------|
| Content-Type | application/json |  |



***Query params:***

| Key | Value | Description |
| --- | ------|-------------|
| from | 2006-12-12 |  |
| to | 2020-06-16 |  |



### 2. User location show



***Endpoint:***

```bash
Method: GET
Type: RAW
URL: localhost:3000/api/hr/locations/user/40598ad4-3063-44ff-a6f7-2e8bd1b60a6a
```


***Headers:***

| Key | Value | Description |
| --- | ------|-------------|
| Content-Type | application/json |  |



### 3. User location delete



***Endpoint:***

```bash
Method: DELETE
Type: RAW
URL: localhost:3000/api/hr/locations/user/1ba9bbe3-c677-4f30-9112-a03befa24eb6
```


***Headers:***

| Key | Value | Description |
| --- | ------|-------------|
| Content-Type | application/json |  |



### 4. User location edit



***Endpoint:***

```bash
Method: POST
Type: RAW
URL: localhost:3000/api/hr/locations/user/1ba9bbe3-c677-4f30-9112-a03befa24eb6
```


***Headers:***

| Key | Value | Description |
| --- | ------|-------------|
| Content-Type | application/json |  |



***Body:***

```js        
{
	"startedAt": "2020-07-12",
	"endedAt": "2020-08-12",
	"locationId": "67429926-5ec0-4017-b50a-d99c172d89b6"
}
```



### 5. User location assign



***Endpoint:***

```bash
Method: POST
Type: RAW
URL: localhost:3000/api/hr/locations/user
```


***Headers:***

| Key | Value | Description |
| --- | ------|-------------|
| Content-Type | application/json |  |



***Body:***

```js        
{
	"userId": "984ad62f-efa2-4633-a377-708931096331",
	"startedAt": "2019-03-19",
	"endedAt": "2020-09-19",
	"extraVacationDays": 0,
	"locationId": "67429926-5ec0-4017-b50a-d99c172d89b6"
}
```



### 6. Locations



***Endpoint:***

```bash
Method: GET
Type: RAW
URL: localhost:3000/api/hr/locations
```


***Headers:***

| Key | Value | Description |
| --- | ------|-------------|
| Content-Type | application/json |  |



### 7. Location Remove



***Endpoint:***

```bash
Method: DELETE
Type: RAW
URL: localhost:3000/api/hr/locations/fc16b88c-67e6-4606-aa01-bb1459615123
```


***Headers:***

| Key | Value | Description |
| --- | ------|-------------|
| Content-Type | application/json |  |



### 8. Location create



***Endpoint:***

```bash
Method: POST
Type: RAW
URL: localhost:3000/api/hr/locations
```


***Headers:***

| Key | Value | Description |
| --- | ------|-------------|
| Content-Type | application/json |  |



***Body:***

```js        
{
	"name": "Poland2"
}
```



---
[Back to top](#hrlocation)
> Made with &#9829; by [thedevsaddam](https://github.com/thedevsaddam) | Generated at: 2020-04-02 20:38:08 by [docgen](https://github.com/thedevsaddam/docgen)
