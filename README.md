
## Code Example
 
    Program to parse data collected from GetAllTransactions. The Data was collected through curl command 
   
    curl -H 'Accept: application/json' -H 'Content-Type: application/json' -X POST -d '{"args": {"uid": 1110590645, "token": "XX", "api-token": "AppTokenForInterview", "json-strict-mode": false, "json-verbose-response": false}}'

## Installation

   Program is written in Java8. Code can be downloaded using GIT command. 
   To Run 

   ```
   mvn clean package

   ```

   ```
   java -jar <jar-name> <feature-name> 

   ```
## This small Example flow.

   Program is using static data downloaded using the above curl command saved the data into data.json file under $project.build/resoruces. Program reads the data into a stream and then group the data using transaction_time. Latter, the data is splitted for spent and income and corresponding average transaction is calculated from the spent and income data-sets. Here, the average is assumed to be monthly avergae data for income and spent calculated separately.
      
