# trelloAPI
test trello api

##How to use:</br>
1. Download .jar file</br>
2. Run in command line: java -jar trelloAPI.jar [application Key] [token]</br>

##Test suite</br>
1. cardManipulation.class, including test cases about add cards, delete cards, response status check</br>
2. Edit card.class, including add label, change card description, check response status, check content test</br>

##External libraries:</br>
1. Junit 4.12</br>
2. httpclient 4.5.2</br>
3. javax.json 1.0.4 </br>

##Note:</br>
Through manual test, I found filtering cards on the board is processed by front-end. Through chrome network monitor, I could not find any request sending back to server. Based on their API reference, there is no specific API to perform filering cards on the board. Therefore, I didn't add filtering cards test cases.
