/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.example.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import org.apache.commons.lang.builder.ReflectionToStringBuilder;
import org.joda.time.DateTime;

/**
 *
 * @author rbordia
 */
@Data
public class Transaction {
    private Double amount;  
    
    @JsonProperty("is-pending")
    private boolean is_pending;
    
    @JsonProperty("aggregation-time")
    private String aggregation_time;
    
    @JsonProperty("account-id")
    private String account_id;
    
    @JsonProperty("clear-date")
    private String clear_date;
    
    @JsonProperty("transaction-id")
    private String transaction_id;
    
    @JsonProperty("raw-merchant")
    private String raw_merchant;
    
    private String categorization;
    
    private String merchant;
    
    @JsonProperty("transaction-time")
    
    private DateTime transaction_time;
    
    @JsonProperty("payee-name-only-for-testing")
    private String payee_name_for_testing;
    
    @JsonProperty("memo-only-for-testing")
    private String memo_for_testing;
    
     @Override
    public String toString() {
        return ReflectionToStringBuilder.toString(this);
    }
    
    
   
}
