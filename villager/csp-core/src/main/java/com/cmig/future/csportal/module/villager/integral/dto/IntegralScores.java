package com.cmig.future.csportal.module.villager.integral.dto;

/**Auto Generated By Hap Code Generator**/
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import com.hand.hap.mybatis.annotation.ExtensionAttribute;
import javax.persistence.Table;
import com.hand.hap.system.dto.BaseDTO;
@ExtensionAttribute(disable=true)
@Table(name = "xl_integral_scores")
public class IntegralScores extends BaseDTO {
     @Id
     @GeneratedValue
      private Long id;

      private String scoresCode;

      private Long scores;

      private String scoresDescribe;

      private Long frequencys;

      private String cycles;

      private String isvalid;


     public void setId(Long id){
         this.id = id;
     }

     public Long getId(){
         return id;
     }

     public void setScoresCode(String scoresCode){
         this.scoresCode = scoresCode;
     }

     public String getScoresCode(){
         return scoresCode;
     }

     public void setScores(Long scores){
         this.scores = scores;
     }

     public Long getScores(){
         return scores;
     }

     public void setScoresDescribe(String scoresDescribe){
         this.scoresDescribe = scoresDescribe;
     }

     public String getScoresDescribe(){
         return scoresDescribe;
     }

     public void setFrequencys(Long frequencys){
         this.frequencys = frequencys;
     }

     public Long getFrequencys(){
         return frequencys;
     }

     public void setCycles(String cycles){
         this.cycles = cycles;
     }

     public String getCycles(){
         return cycles;
     }

     public void setIsvalid(String isvalid){
         this.isvalid = isvalid;
     }

     public String getIsvalid(){
         return isvalid;
     }

     }
