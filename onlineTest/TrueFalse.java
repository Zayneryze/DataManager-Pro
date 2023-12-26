package onlineTest;

import java.io.Serializable;

public class TrueFalse extends Question implements Serializable {
	 private boolean answer;
	 private static final long serialVersionUID = 7L;

     public TrueFalse(int questionNumber, String text, double points, boolean answer) {
         super(questionNumber, text, points);
         this.answer = answer;
     }
     
     public boolean getAnswer() {
         return answer;
     }

     @Override
     public  String getCorrectAnswer() {
         return String.valueOf(answer);
     }
}
