package onlineTest;

import java.io.Serializable;
import java.util.Arrays;

public class MultipleChoice extends Question implements Serializable{
	
	 private String[] answer;
	 private static final long serialVersionUID = 4L;

     public MultipleChoice(int questionNumber, String text, double points, String[] answer) {
         super(questionNumber, text, points);
         this.answer = answer;
     }
     
     public String[] getAnswer() {
         return answer;
     }

     @Override
     public String getCorrectAnswer() {
         StringBuilder sb = new StringBuilder();
         int optionsSize = answer.length;
         for (int i = 0; i < optionsSize; i++) {
             sb.append(answer[i]);
             if (i < optionsSize - 1) {
                 sb.append(", ");
             }
         }
         return "[" + sb.toString() + "]";
     }

	
	
	
}
