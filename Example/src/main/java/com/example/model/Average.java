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
public class Average {
    
    private Double spent;
    
    private Double income;
    
    public Average (Double spent, Double income){
        this.income = income;
        this.spent = spent;
    }
    
    public Average (){}
    
}
