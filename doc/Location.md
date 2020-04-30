# Hr/Location

## Indices

- [Default](#default)

  - [User locations](#1-user-locations)
  - [User location show](#2-user-location-show)
  - [User location close](#3-user-location-close)
  - [User location edit](#4-user-location-edit)
  - [User location assign](#5-user-location-assign)
  - [Locations](#6-locations)
  - [Location Remove](#7-location-remove)
  - [Location create](#8-location-create)

---

## Default

### 1. User locations

**_Endpoint:_**

```bash
Method: GET
Type: RAW
URL: localhost:3000/api/hr/locations/user
```

**_Headers:_**

| Key          | Value            | Description |
| ------------ | ---------------- | ----------- |
| Content-Type | application/json |             |

**_Query params:_**

| Key  | Value      | Description |
| ---- | ---------- | ----------- |
| from | 2006-12-12 |             |
| to   | 2030-06-16 |             |

**_Responses:_**

Status: Success | Code: 200

**_Response Headers:_**

```js
{
    "data": {
        "userLocations": {
            "8f631c73-a516-4576-b19b-4929b665e9dc": [
                {
                    "id": "54e667d1-02bd-4395-92d9-1b0f9c0041c3",
                    "startedAt": "2019-03-19",
                    "endedAt": "2020-04-15",
                    "location": {
                        "id": "5d4892b1-5c41-485a-a177-d1c19b895230",
                        "name": "Poland",
                        "vacationDays": 25
                    },
                    "userId": "8f631c73-a516-4576-b19b-4929b665e9dc",
                    "email": "novikov.ivan.work@gmail.com",
                    "firstName": "Ivan",
                    "lastName": "Novikov"
                },
                {
                    "id": "063b3fdb-f066-4c1d-8b69-dae325a2e7fa",
                    "startedAt": "2020-09-12",
                    "endedAt": "null",
                    "location": {
                        "id": "5d4892b1-5c41-485a-a177-d1c19b895230",
                        "name": "Poland",
                        "vacationDays": 25
                    },
                    "userId": "8f631c73-a516-4576-b19b-4929b665e9dc",
                    "email": "novikov.ivan.work@gmail.com",
                    "firstName": "Ivan",
                    "lastName": "Novikov"
                }
            ]
        }
    }
}
```

### 2. User location show

**_Endpoint:_**

```bash
Method: GET
Type: RAW
URL: localhost:3000/api/hr/locations/user/063b3fdb-f066-4c1d-8b69-dae325a2e7fa
```

**_Headers:_**

| Key          | Value            | Description |
| ------------ | ---------------- | ----------- |
| Content-Type | application/json |             |

**_Responses:_**

Status: Success | Code: 200

**_Response Headers:_**

```js
{
    "data": {
        "id": "063b3fdb-f066-4c1d-8b69-dae325a2e7fa",
        "startedAt": "2020-09-12",
        "endedAt": "null",
        "location": {
            "id": "5d4892b1-5c41-485a-a177-d1c19b895230",
            "name": "Poland",
            "vacationDays": 25
        },
        "userId": "8f631c73-a516-4576-b19b-4929b665e9dc",
        "email": "novikov.ivan.work@gmail.com",
        "firstName": "Ivan",
        "lastName": "Novikov"
    }
}
```

### 3. User location close

Role: Admin

**_Endpoint:_**

```bash
Method: POST
Type: RAW
URL: localhost:3000/api/hr/locations/user/54e667d1-02bd-4395-92d9-1b0f9c0041c3/close
```

**_Responses:_**

Status: Success | Code: 200

**_Response Headers:_**

```js
{
    "status": "ok"
}
```

Status: Already Closed | Code: 422

```js
{
    "message": "User location have been already closed"
}
```

### 4. User location edit

**_Endpoint:_**

```bash
Method: POST
Type: RAW
URL: localhost:3000/api/hr/locations/user/063b3fdb-f066-4c1d-8b69-dae325a2e7fa
```

**_Headers:_**

| Key          | Value            | Description |
| ------------ | ---------------- | ----------- |
| Content-Type | application/json |             |

**_Body:_**

```js
{
	"startedAt": "2020-09-12",
	"locationId": "5d4892b1-5c41-485a-a177-d1c19b895230"
}
```

**_Responses:_**

Status: Success | Code: 200

**_Response Headers:_**

```js
{
    "status": "ok"
}
```

### 5. User location assign

**_Endpoint:_**

```bash
Method: POST
Type: RAW
URL: localhost:3000/api/hr/locations/user
```

**_Headers:_**

| Key          | Value            | Description |
| ------------ | ---------------- | ----------- |
| Content-Type | application/json |             |

**_Body:_**

```js
{
	"userId": "8f631c73-a516-4576-b19b-4929b665e9dc",
	"startedAt": "2020-07-19",
	"extraVacationDays": 0,
	"locationId": "5d4892b1-5c41-485a-a177-d1c19b895230"
}
```

**_Responses:_**

Status: Success | Code: 200

```js
{
    "status": "ok"
}
```

### 6. Locations

**_Endpoint:_**

```bash
Method: GET
Type: RAW
URL: localhost:3000/api/hr/locations
```

**_Headers:_**

| Key          | Value            | Description |
| ------------ | ---------------- | ----------- |
| Content-Type | application/json |             |

**_Responses:_**

Status: Success | Code: 200

**_Response Headers:_**

```js
{
    "data": {
        "locations": [
            {
                "id": "5d4892b1-5c41-485a-a177-d1c19b895230",
                "name": "Poland",
                "vacationDays": 25
            },
            {
                "id": "fc60cb5a-2867-4a67-ad68-b07f33829c98",
                "name": "Argentine",
                "vacationDays": 25
            }
        ]
    }
}
```

### 7. Location Remove

**_Endpoint:_**

```bash
Method: DELETE
Type: RAW
URL: localhost:3000/api/hr/locations/fc60cb5a-2867-4a67-ad68-b07f33829c98
```

**_Headers:_**

| Key          | Value            | Description |
| ------------ | ---------------- | ----------- |
| Content-Type | application/json |             |

**_Responses:_**

Status: Success | Code: 200

```js
{
    "status": "ok"
}
```

### 8. Location create

**_Endpoint:_**

```bash
Method: POST
Type: RAW
URL: localhost:3000/api/hr/locations
```

**_Headers:_**

| Key          | Value            | Description |
| ------------ | ---------------- | ----------- |
| Content-Type | application/json |             |

**_Body:_**

```js
{
	"name": "Argentine",
	"vacationDays": 25
}
```

**_Responses:_**

Status: Success | Code: 200

```js
{
    "status": "ok"
}
```

---

[Back to top](#hrlocation)

> Made with &#9829; by [thedevsaddam](https://github.com/thedevsaddam) | Generated at: 2020-04-15 13:36:18 by [docgen](https://github.com/thedevsaddam/docgen)
