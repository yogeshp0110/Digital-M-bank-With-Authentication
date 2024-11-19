http://localhost:8091/bankers/createMaveric-Bank-appication developed by Yogesh

Request and Response for APIs
-------------------------------------------------



Branch API
==================================================
POST : http://localhost:8091/branches/create

{
  "branchName": "South Branch",
  "branchAddress": "123 Bank St, New York, NY",
  "assets": 1000000
}


GET : http://localhost:8091/branches

GET : http://localhost:8091/branches/1

Delete : http://localhost:8091/branches/5

--------------------------------------------------


Account API
==================================================

POST : http://localhost:8091/accounts/create
      {
		"branchId": "BR0001",  
		"accountBalance": 5000.00,
		"accountType": "SAVINGS"  
      }
	  
GET : http://localhost:8091/accounts

GET : http://localhost:8091/accounts/1551218559

--------------------------------------------------

Banker API
==================================================
POST: http://localhost:8091/bankers/create
{
  "bankerName": "YogeshP",
  "branch": {
    "branchId": "BR0001"
  }
}

GET: http://localhost:8091/bankers/create
GET :http://localhost:8091/bankers/100202
DELETE : http://localhost:8091/bankers/100202

--------------------------------------------------




