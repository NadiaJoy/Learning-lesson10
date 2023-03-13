
Swagger Endpoints: http://51.250.6.164:8080/test-orders/
Using JDK 11, JUnit framework v5, Maven Plugins

Check-list in ApiTest:
Swagger Endpoints: http://51.250.6.164:8080/test-orders/
Using JDK 11, JUnit framework v5, Maven Plugins

N | Check Name   | Status
-- | -------------|--------
1 | Pos: Check (GET) status code 200 for id between 1 and 10  | Pass
2 | Neg: Check (GET) status code 400 for id less 1 and more 10     | Pass
3 | Pos: Check (POST) creating a correct order (code 200)    | Pass
4 | Neg: Check (POST) creating  an order with wrong parameters (code 415)     | Pass
5 | Neg: Check (GET)  an existing order has status "open"     | Pass

++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++

Check-list  in PetStoreAPI:

N | Check Name   | Status
-- | -------------|--------
1 | Pos: Check (GET) status code 200 for existing orders  | Pass
2 | Pos: Check (GET) status code 404 for not existing orders     | Pass
3 | Neg: Check (GET) status code 404 for orders with wrong id     | Pass
4 | Pos: Check (GET)  an existing order has status "approved"     | Pass





