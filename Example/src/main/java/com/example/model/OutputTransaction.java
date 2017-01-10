/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.example.model;

import lombok.Data;

/**
 *
 * @author rbordia
 */
@Data
public class OutputTransaction {
    
    public OutputTransaction(){}
    
    public OutputTransaction (Double spent, Double income){
        this.income = income;
        this.spent = spent;
    }
    
    private Double income;
    
    private Double spent;
    
    private Average average;
    
}
