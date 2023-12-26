package onlineTest;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Exam implements Serializable {
	private static final long serialVersionUID = 2L;
	 private int examId;
     private String title;
     private List<Question> questions;

     public Exam(int examId, String title) {
         this.examId = examId;
         this.title = title;
         this.questions = new ArrayList<>();
     }
     
     public int getExamId() {
    	 return examId;
     }
     
     public String getTitle() {
    	 return title;
     }

     public void addQuestion(Question question) {
         questions.add(question);
     }

     public double getTotalScore() {
         double totalScore = 0;
         for (Question question : questions) {
             totalScore += question.getPoints();
         }
         return totalScore;
     }

     public List<Question> getQuestions() {
         return questions;
     }
    
     public String getKey() {
         StringBuilder sb = new StringBuilder();
         for (Question question : questions) {
             sb.append("Question Text: ").append(question.getText()).append("\n");
             sb.append("Points: ").append(question.getPoints()).append("\n");
             String correct = question.getCorrectAnswer();
             if(question instanceof TrueFalse) {
            	 if(correct == "true")
             sb.append("Correct Answer: ").append(correct.toUpperCase().charAt(0)).append(correct.charAt(1)).append(correct.charAt(2))
             .append(correct.charAt(3)).append("\n");
            	 else {
            		 sb.append("Correct Answer: ").append(correct.toUpperCase().charAt(0)).append(correct.charAt(1)).append(correct.charAt(2))
                     .append(correct.charAt(3)).append(correct.charAt(4)).append("\n");
            	 }
         }
             else {
            	 sb.append("Correct Answer: ").append(correct).append("\n");
             }
         
     }
         return sb.toString();
    
    
    

 
}
}
