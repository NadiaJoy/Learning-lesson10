Using JDK 11, JUnit framework v5, Maven Plugins

Check-list in ApiTest:
N | Check Name   | Status
-- | -------------|--------
1 | Pos: Check (GET) status code 200 for id between 1 and 10  | Pass
2 | Neg: Check (GET) status code 400 for id less 1 and more 10     | Pass
3 | Pos: Check (POST) creating a correct order (code 200)    | Pass
4 | Neg: Check (POST) creating  an order with wrong parameters (code 415)     | Pass
5 | Neg: Check (GET)  an existing order has status "open"     | Pass

++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++

Check-list  in PetStoreAPI:

N | Check Name   | Status
-- | -------------|--------
1 | Pos: Check (GET) status code 200 for existing orders  | Pass
2 | Pos: Check (GET) status code 404 for not existing orders     | Pass
3 | Neg: Check (GET) status code 404 for orders with wrong id     | Pass
4 | Pos: Check (GET)  an existing order has status "approved"     | Pass


Check-list for UI Tallinn Delivery:

N | Check Name   |
-- | -------------|
1 | Log-in successfully with correct login and pass |
2 | Log-in failed with incorrect login\pass     |
3 | Incorrect credentials message when login failed     |
4 | Sign-In button incative when Login field is empty or has less then 2 symbols    |
5 | Sign-In button incative when Password field is empty or has less then 8 symbols      |

Using Xpath:

N | Element   | XPath
-- | -------------|--------
1 | Login Field  | //input[@data-name="username-input"]
2 | Password Field  | //input[@data-name="password-input"]
3 | Autorization error popup | //div[@data-name="authorizationError-popup"]
4 | Close button for autorization error popup | //button[@data-name="authorizationError-popup-close-button"]
5 | Sing-in button | //button[@data-name="signIn-button"]




