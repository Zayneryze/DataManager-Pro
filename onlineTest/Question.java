package onlineTest;

import java.io.Serializable;
import java.util.Arrays;

public abstract class Question implements Serializable {
	
	private static final long serialVersionUID = 3L;
	 private int questionNumber;
     private String text;
     private double points;

     public Question(int questionNumber, String text, double points) {
         this.questionNumber = questionNumber;
         this.text = text;
         this.points = points;
     }

     public int getQuestionNumber() {
         return questionNumber;
     }

     public String getText() {
         return text;
     }

     public double getPoints() {
         return points;
     }
     
     
    

     public abstract String getCorrectAnswer();
 }
	

    
	

	
	

