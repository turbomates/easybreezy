
# Projects



## Indices

* [Default](#default)

  * [View](#1-view)
  * [Remove role from project](#2-remove-role-from-project)
  * [Change role to project](#3-change-role-to-project)
  * [Add role to project](#4-add-role-to-project)
  * [Write description](#5-write-description)
  * [Activate](#6-activate)
  * [Close](#7-close)
  * [Suspend](#8-suspend)
  * [Add project](#9-add-project)


--------


## Default



### 1. View



***Endpoint:***

```bash
Method: GET
Type: 
URL: localhost:3000/api/projects/{project-slug}
```

***Response:***

```js        
{
    "data": {
        "id": "4a658ede-b156-4f10-9d49-2fbaf802b4f2",
        "slug": "myproject",
        "name": "My project",
        "status": "Active",
        "description": "New description",
        "roles": [
            {
                "id": "4ea00b9d-890d-4bf5-a9d7-0603b86c5c16",
                "name": "Project Manager",
                "permissions": []
            },
            {
                "id": "1ec6800d-091a-441e-8884-a58993fc8258",
                "name": "Team Lead",
                "permissions": []
            },
            {
                "id": "2721fe9e-d326-4f99-b79f-459ece0fbc6a",
                "name": "Developer",
                "permissions": []
            },
            {
                "id": "20a0b150-6895-4e3b-b7f7-9eaac367bbdb",
                "name": "Testerq",
                "permissions": [
                    "testing",
                    "building",
                    "scripting"
                ]
            }
        ],
        "teams": [
            {
                "id": "a59588c1-3718-4810-bec1-b205aee9abb9",
                "name": "new team"
            }
        ]
    }
}
```

### 2. Remove role from project



***Endpoint:***

```bash
Method: POST
Type: RAW
URL: localhost:3000/api/projects/{project-slug}/roles/{role-id}/remove
```



***Body:***

```js        
{}
```



### 3. Change role to project



***Endpoint:***

```bash
Method: POST
Type: RAW
URL: localhost:3000/api/projects/{project-slug}/roles/{role-id}/change
```



***Body:***

```js        
{
  "name": "Testerq",
  "permissions" : [
  	"testing",
  	"building",
  	"scripting"
  ]
}
```



### 4. Add role to project



***Endpoint:***

```bash
Method: POST
Type: RAW
URL: localhost:3000/api/projects/{project-slug}/roles/add
```



***Body:***

```js        
{
  "name": "Tester",
  "permissions" : [
  	"testing",
  	"building"
  ]
}
```



### 5. Write description



***Endpoint:***

```bash
Method: POST
Type: RAW
URL: localhost:3000/api/projects/{project-slug}/write-description
```



***Body:***

```js        
{
  "description": "New description"
}
```



### 6. Activate



***Endpoint:***

```bash
Method: POST
Type: RAW
URL: localhost:3000/api/projects/{project-slug}/activate
```



***Body:***

```js        
{}
```



### 7. Close



***Endpoint:***

```bash
Method: POST
Type: RAW
URL: localhost:3000/api/projects/{project-slug}/close
```



***Body:***

```js        
{}
```



### 8. Suspend



***Endpoint:***

```bash
Method: POST
Type: RAW
URL: localhost:3000/api/projects/{project-slug}/suspend
```



***Body:***

```js        
{}
```



### 9. Add project



***Endpoint:***

```bash
Method: POST
Type: RAW
URL: localhost:3000/api/projects
```



***Body:***

```js        
{
	"name": "My project", 
	"description": "My project description"
}
```



---
[Back to top](#projects)
> Made with &#9829; by [thedevsaddam](https://github.com/thedevsaddam) | Generated at: 2020-04-06 16:45:49 by [docgen](https://github.com/thedevsaddam/docgen)