

# Location



## Indices

* [Default](#default)

  * [User location listing](#1-user-location-listing)
  * [User location show](#2-user-location-show)
  * [User location delete](#3-user-location-delete)
  * [User location edit](#4-user-location-edit)
  * [User location assign](#5-user-location-assign)
  * [Locations Listing](#6-locations-listing)
  * [Location Remove](#7-location-remove)
  * [Location create](#8-location-create)


--------


## Default



### 1. User location listing



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
	"startedAt": "2020-07-19",
	"endedAt": "2020-08-19",
	"locationId": "67429926-5ec0-4017-b50a-d99c172d89b6"
}
```



### 6. Locations Listing



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
	"name": "USA2"
}
```



---
[Back to top](#hrlocation)
> Made with &#9829; by [thedevsaddam](https://github.com/thedevsaddam) | Generated at: 2020-02-07 13:21:28 by [docgen](https://github.com/thedevsaddam/docgen)
