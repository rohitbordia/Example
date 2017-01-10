/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.example.model;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.joda.JodaModule;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import org.joda.time.format.DateTimeFormat;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author rbordia
 */
public class ProcessExample {

    private final Logger log = LoggerFactory.getLogger(this.getClass());

    private String outFileLocation;
    private String feature;

    public ProcessExample() {
    }

    public ProcessExample(String feature) {
        this.feature = feature;
    }

    /**
     * Read transaction from file and convert to Java pojo
     *
     * @param fileName
     * @return
     * @throws java.lang.Exception
     */
    private List<Transaction> readFile(String fileName) throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new JodaModule());

        // mapper.setDateFormat(new SimpleDateFormat("yyyy-MM"));
        ResponseModel obj = mapper.readValue(new File(fileName),
                ResponseModel.class);
        return obj.getTransactions();
    }

    public static void main(String args[]) {
        ProcessExample process = null;
        if(null != args[0]){
        	process = new ProcessExample(args[0]);
	}else{
		process = new ProcessExample();
	}
        process.process();
        
    }

    /**
     * Method to start  processing data.
     */
    public void process(){
        Map<String,List<Transaction>> groupByDate = null;
        Map<String,List<Transaction>> spentData = null;
        Map<String,List<Transaction>> incomeData = null;
        try {
            groupByDate = createGroupByData(
                    readFile("src/main/resources/data.json"));
            
        } catch (Exception e) {
           log.error("error while reading file or processing" + 
                   e.getLocalizedMessage());
        }
        
        if(groupByDate !=null){
            if(null !=feature && "ignore-donuts".equalsIgnoreCase(feature)){
                spentData = getSpentDataWithFeature(groupByDate);
            }else{
                spentData = getSpentData(groupByDate);
            }
            incomeData = getIncomeData(groupByDate);
        }
        
        Map<String, Double> avgSpend = calAvgData(spentData);
        Map<String, Double> avgIncome = calAvgData(incomeData);
        List<Output> out = createOutputData(groupByDate, 
                spentData, incomeData, avgSpend, avgIncome);
        try{
         writeDataToFile(out);
        }catch(Exception e){
            log.error("Error while writing data to a json file");
        }
    }

    /**
     * Method to group data by transaction_time from all transaction
     * data of a account.
     */
    private Map<String,List<Transaction>> 
        createGroupByData(List<Transaction> transactions){
        //collection data for each month
        return transactions.stream()
                .collect(Collectors.groupingBy
                ((Transaction tran) -> 
                        tran.getTransaction_time().
                                toString(DateTimeFormat.
                                        forPattern("yyyy-MM"))));

    }
    
        
   /**
    * Group spent data with filter of a feature
    * @param groupByDate
    * @return 
    */
    private Map<String,List<Transaction>> 
        getSpentDataWithFeature(Map<String,List<Transaction>> groupByDate){
        return
            groupByDate.entrySet().stream()
            .collect(
                Collectors.toMap(Map.Entry::getKey, e -> {
                    return e.getValue().stream()
                    .filter(x -> x.getAmount() < 0)
                    .filter(tran -> {
                        return !("Krispy Kreme Donuts".
                                equalsIgnoreCase(tran.getMerchant())
                                || "DUNKIN #336784".
                                        equalsIgnoreCase(tran.getMerchant()));
                    })
                    .collect(Collectors.toList());
                }));

        }
        
        
    /**
     * Spent data from the actual data minus feature
     * @param groupByDate
     * @return 
     */
    private Map<String,List<Transaction>> 
        getSpentData(Map<String,List<Transaction>> groupByDate){
    return
        groupByDate.entrySet().stream()
        .collect(
            Collectors.toMap(Map.Entry::getKey, e -> {
                return e.getValue().stream()
                .filter(x -> x.getAmount() < 0)
                .collect(Collectors.toList());
            }));

    }
       
    /**
     * Income data from the actual data set.
     * @param groupByDate
     * @return 
     */
    private Map<String,List<Transaction>> 
        getIncomeData(Map<String,List<Transaction>> groupByDate){
    return
        groupByDate.entrySet().stream()
        .collect(
            Collectors.toMap(Map.Entry::getKey, e -> {
                return e.getValue().stream()
                .filter(x -> x.getAmount() > 0)
                .collect(Collectors.toList());
            }));

    }
    
        
    /**
     * Calculate Average data for a month for spent or income sent.
     * @param data
     * @return 
     */
    private Map<String, Double> calAvgData(Map<String,List<Transaction>> data){
        return data.entrySet().stream()
                .collect(
                    Collectors.toMap(Map.Entry::getKey, e -> e.getValue().stream()
                        .collect(Collectors.averagingDouble(Transaction::getAmount))
                   ));
    }
    
    /**
     * Method to stream the array -> groupBy DateTime and filter for donuts,
     * split into 2 maps of Income and spend and then do a average.
     *
     * @param transactions
     */
    /**private void processTransaction(List<Transaction> transactions) throws Exception {
       List<Output> outTransations = new ArrayList();

        
      
        // Do a average on spend transaction of each month
        Map<String, Double> avgSpend
                = spendMap.entrySet().stream()
                .collect(
                        Collectors.toMap(Map.Entry::getKey, e -> e.getValue().stream()
                                .collect(Collectors.averagingDouble(Transaction::getAmount))
                        ));

        //Income  data
        Map<String, List<Transaction>> incomeMap
                = groupByDate.entrySet().stream()
                .collect(
                        Collectors.toMap(Map.Entry::getKey, e -> e.getValue().stream()
                                .filter(x -> x.getAmount() > 0)
                                .collect(Collectors.toList())));

        System.out.println(groupByDate);

        // Do a average on income transaction of each month
        Map<String, Double> avgIncome
                = incomeMap.entrySet().stream()
                .collect(
                        Collectors.toMap(Map.Entry::getKey, e -> e.getValue().stream()
                                .collect(Collectors.averagingDouble(Transaction::getAmount))
                        ));

        for (Map.Entry<String, List<Transaction>> entry : groupByDate.entrySet()) {

            Output output = new Output();
            Double spendTotal = (double) 0;
            Double incomeTotal = (double) 0;
            List<Transaction> spendList = spendMap.get(entry.getKey());
            for (Transaction spendList1 : spendList) {
                spendTotal = spendTotal + spendList1.getAmount();
            }
            List<Transaction> incomeList = incomeMap.get(entry.getKey());
            for (Transaction incomeTran : incomeList) {
                incomeTotal = incomeTotal + incomeTran.getAmount();
            }

            OutputTransaction outTran = new OutputTransaction(spendTotal, incomeTotal);

            Double avgSpendTotal = avgSpend.get(entry.getKey());

            Double avgIncomeTotal = avgIncome.get(entry.getKey());

            Average average = new Average(avgSpendTotal, avgIncomeTotal);
            outTran.setAverage(average);
            output.setOutTransaction(outTran);
            output.setDate(entry.getKey());
            outTransations.add(output);

        }

        writeDataToFile(outTransations);
    }**/
    
    /**
     * Method to create view data from the multiple data-sets.
     * @param groupByDate
     * @param spendMap
     * @param incomeMap
     * @param avgSpentData
     * @param avgIncomeData
     * @return 
     */
    private List<Output> createOutputData(Map<String,List<Transaction>> groupByDate,
            Map<String,List<Transaction>> spendMap, 
            Map<String,List<Transaction>> incomeMap,
            Map<String, Double> avgSpentData,
            Map<String, Double> avgIncomeData
            ){
        List<Output> outTransations = new ArrayList();
        for (Map.Entry<String, List<Transaction>> entry : groupByDate.entrySet()) {

            Output output = new Output();
            Double spendTotal = (double) 0;
            Double incomeTotal = (double) 0;
            List<Transaction> spendList = spendMap.get(entry.getKey());
            for (Transaction spendList1 : spendList) {
                spendTotal = spendTotal + spendList1.getAmount();
            }
            List<Transaction> incomeList = incomeMap.get(entry.getKey());
            for (Transaction incomeTran : incomeList) {
                incomeTotal = incomeTotal + incomeTran.getAmount();
            }

            OutputTransaction outTran = new OutputTransaction(spendTotal, incomeTotal);

            Double avgSpendTotal = avgSpentData.get(entry.getKey());

            Double avgIncomeTotal = avgIncomeData.get(entry.getKey());

            Average average = new Average(avgSpendTotal, avgIncomeTotal);
            outTran.setAverage(average);
            output.setOutTransaction(outTran);
            output.setDate(entry.getKey());
            outTransations.add(output);

        }

        return outTransations;
    }

    /**
     * Method to write data into file
     * @param outTransations
     * @throws Exception 
     */
    public void writeDataToFile(List<Output> outTransations) throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        mapper.writerWithDefaultPrettyPrinter().writeValue(new 
            File("src/main/resources/" + System.currentTimeMillis()+".json"), 
                outTransations);
        
    }

    /**
     * @return the outFileLocation
     */
    public String getOutFileLocation() {
        return outFileLocation;
    }

    /**
     * @return the feature
     */
    public String getFeature() {
        return feature;
    }

}
