Power Plant System API documentation
======================
## Framework Tools
#### Spring Boot 3
#### Java Jdk 17
#### Mysql 5.7

## Table of Contents

[TOC]: #
1. [Battery](#battery)
    - [CREATE](#create)
    - [READ](#read)

### CREATE
### Create Battery
```
  POST http://localhost:8081/battery/create
  Header: Content-Type application/json
  Body:
  {
    "name": "Hay Street",
    "postcode": "6000",
    "capacity": 23500
  }
```
```
Response:
Status: 200 (Success)
Body:
{
    "id": 21,
    "name": "Hay Street",
    "postcode": "6000",
    "capacity": 23500
}
```
##### Create Battery Failed Request
```
  POST http://localhost:8081/battery/create
  Header: Content-Type application/json
  Body:
  {
    "name": "Hay Street",
    "postcode": "60000",
    "capacity": 23500
	}
```
```
Response:
Status: 400 (Bad Request)
Body:
{
    "message": "Battery already exists with battery post code: 60000",
    "status": false
}
```
```
  POST http://localhost:8081/battery/create
  Header: Content-Type application/json
  Body:
  {
    "name": "",
    "postcode": "60000",
    "capacity": 23500
   }
```
```
Response:
Status: 400 (Bad Request)
Body:
{
    "name": "Name is mandatory"
}
```
### Create Batteries
```
  POST http://localhost:8081/battery/batteries
  Header: Content-Type application/json
  Body:
  [
    {
        "name": "Hay Street",
        "postcode": "60000",
        "capacity": 23500
    },
    {
        "name": "Cannington",
        "postcode": "6107",
        "capacity": 13500
    }
]
```
```
Response:
Status: 200 (Success)
Body:
[
    {
        "id": 22,
        "name": "Hay Street",
        "postcode": "60000",
        "capacity": 23500
    },
    {
        "id": 23,
        "name": "Cannington",
        "postcode": "6107",
        "capacity": 13500
    }
]
```
### READ
### Retrieve Batteries
```
  GET http://localhost:8081/battery/batteries
```
```
Response:
Status: 200 (Success)
Body:
[
    {
        "id": 22,
        "name": "Hay Street",
        "postcode": "60000",
        "capacity": 23500
    },
    {
        "id": 23,
        "name": "Cannington",
        "postcode": "6107",
        "capacity": 13500
    }
]
```
### Retrieve Batteries In Postcode Range
```
  POST http://localhost:8081/battery/range
  Header: Content-Type application/json
  Body:
  {
    "startPostcode": "6076",
    "endPostcode": "6107"
  }
```
```
Response:
Status: 200 (Success)
Body:
{
    "batteriesInRange": [
        {
            "id": 10,
            "name": "Bentley",
            "postcode": "6102",
            "capacity": 85000
        },
        {
            "id": 1,
            "name": "Cannington",
            "postcode": "6107",
            "capacity": 13500
        },
        {
            "id": 9,
            "name": "Carmel",
            "postcode": "6076",
            "capacity": 36000
        },
        {
            "id": 8,
            "name": "Kalamunda",
            "postcode": "6076",
            "capacity": 13500
        },
        {
            "id": 7,
            "name": "Lesmurdie",
            "postcode": "6076",
            "capacity": 13500
        }
    ],
    "totalWattCapacity": 161500,
    "averageWattCapacity": 32300
}
```