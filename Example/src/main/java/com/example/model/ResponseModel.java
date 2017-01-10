/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.example.model;

import java.util.List;
import lombok.Data;

/**
 *
 * @author rbordia
 */
@Data
public class ResponseModel {
    
    private List<Transaction> transactions;
 
    private String error;
}
