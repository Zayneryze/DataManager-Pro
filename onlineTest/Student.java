package onlineTest;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class Student implements Serializable {
	
	 private String name;
	 private static final long serialVersionUID = 6L;
	 private Map<Integer, Map<Integer, String[]>> answers;
	 private Map<Integer, Exam> exams; 
	 private Map<String, Map<Integer, ArrayList<Double>>> studentScores = new HashMap<String, Map<Integer, ArrayList<Double>>>();

     public Student(String name, Map<Integer, Exam> exams ) {
         this.name = name;
         this.answers = new HashMap<>();
         this.exams = exams;
     }
     
     public String getName() {
    	 return name;
     }
     
     public Map<Integer, Map<Integer, String[]>> getAnswers(){
    	 return answers;
     }

     public void answerTrueFalseQuestion(int examId, int questionNumber, boolean answer) {
         String[] answerArr = {String.valueOf(answer)};
         answerQuestion(examId, questionNumber, answerArr);
     }

     public void answerMultipleChoiceQuestion(int examId, int questionNumber, String[] answer) {
         answerQuestion(examId, questionNumber, answer);
     }

     public void answerFillInTheBlanksQuestion(int examId, int questionNumber, String[] answer) {
         answerQuestion(examId, questionNumber, answer);
     }

     private void answerQuestion(int examId, int questionNumber, String[] answer) {
    	    Map<Integer, String[]> examAnswers = answers.getOrDefault(examId, new HashMap<Integer, String[]>());
    	    examAnswers.put(questionNumber, answer);
    	    answers.put(examId, examAnswers);
    	}


     public double getExamScore(int examId) {
    	    Map<Integer, String[]> examAnswers = answers.getOrDefault(examId, null);
    	    if (examAnswers == null) {
    	        return 0;
    	    }

    	    if (exams == null) {
    	        return 0;
    	    }

    	    Exam exam = exams.getOrDefault(examId, null);
    	    if (exam == null) {
    	        return 0;
    	    }

    	    double studentScore = 0;

    	    for (Question question : exam.getQuestions()) {
    	        int questionNumber = question.getQuestionNumber();
    	        String[] studentAnswer = examAnswers.getOrDefault(questionNumber, null);
    	        if (studentAnswer != null && studentAnswer.length > 0) {
    	            double questionScore = question.getPoints();
    	            double partialScore = 0;

    	            if (question instanceof TrueFalse) {
    	                TrueFalse trueFalseQuestion = (TrueFalse) question;
    	                boolean correctAnswer = trueFalseQuestion.getAnswer();
    	                boolean studentResponse = Boolean.parseBoolean(studentAnswer[0]);
    	                if (correctAnswer == studentResponse) {
    	                    partialScore = questionScore;
    	                }
    	            } else if (question instanceof MultipleChoice) {
    	                MultipleChoice multipleChoiceQuestion = (MultipleChoice) question;
    	                String[] correctAnswer = multipleChoiceQuestion.getAnswer();
    	                if (Arrays.equals(correctAnswer, studentAnswer)) {
    	                    partialScore = questionScore;
    	                }
    	            } else if (question instanceof FillInTheBlank) {
    	                FillInTheBlank fillInTheBlanksQuestion = (FillInTheBlank) question;
    	                String[] correctAnswers = fillInTheBlanksQuestion.getAnswer();
    	                int correctChoices = 0;

    	                for (String answer : studentAnswer) {
    	                    if (Arrays.asList(correctAnswers).contains(answer)) {
    	                        correctChoices++;
    	                    }
    	                }

    	                partialScore = (questionScore / correctAnswers.length) * correctChoices;
    	            }

    	            studentScore += partialScore;
    	        }
    	    }

    	    return studentScore;
    	}



 }
	 
	    
	   
	

	    



