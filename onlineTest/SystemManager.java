package onlineTest;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.io.ByteArrayOutputStream;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.io.ObjectInputStream;
import java.io.FileOutputStream;
import java.io.FileInputStream;
import java.io.IOException;

public class SystemManager implements Manager, Serializable {
	
	
		private static final long serialVersionUID = 1L;
	  	private Map<Integer, Exam> exams;
	    private Map<String, Student> students;
	    private String[] letterGrades;
	    private double[] cutoffs;

	    public SystemManager() {
	        exams = new HashMap<>();
	        students = new HashMap<>();
	    }

	    @Override
	    public boolean addExam(int examId, String title) {
	        if (exams.containsKey(examId)) {
	            return false;
	        }
	        exams.put(examId, new Exam(examId, title));
	        return true;
	    }

	    @Override
	    public void addTrueFalseQuestion(int examId, int questionNumber, String text, double points, boolean answer) {
	        Exam exam = exams.getOrDefault(examId, null);
	        if (exam != null) {
	            exam.addQuestion(new TrueFalse(questionNumber, text, points, answer));
	        }
	    }

	    @Override
	    public void addMultipleChoiceQuestion(int examId, int questionNumber, String text, double points, String[] answer) {
	        Exam exam = exams.getOrDefault(examId, null);
	        if (exam != null) {
	            exam.addQuestion(new MultipleChoice(questionNumber, text, points, answer));
	        }
	    }

	    @Override
	    public void addFillInTheBlanksQuestion(int examId, int questionNumber, String text, double points, String[] answer) {
	        Exam exam = exams.getOrDefault(examId, null);
	        if (exam != null) {
	            
	            Arrays.sort(answer);

	            exam.addQuestion(new FillInTheBlank(questionNumber, text, points, answer));
	        }
	    }



	    @Override
	    public String getKey(int examId) {
	        Exam exam = exams.getOrDefault(examId, null);
	        if (exam == null) {
	            return "Exam not found";
	        }
	        return exam.getKey();
	    }

	    @Override
	    public boolean addStudent(String name) {
	        if (students.containsKey(name)) {
	            return false;
	        }
	        students.put(name, new Student(name,exams));
	        return true;
	    }

	    @Override
	    public void answerTrueFalseQuestion(String studentName, int examId, int questionNumber, boolean answer) {
	        Student student = students.getOrDefault(studentName, null);
	        if (student != null) {
	            student.answerTrueFalseQuestion(examId, questionNumber, answer);
	        }
	        
	    }

	    public void answerMultipleChoiceQuestion(String studentName, int examId, int questionNumber, String[] answer) {
	        Student student = students.get(studentName);
	        if (student != null) {
	            Map<Integer, String[]> studentAnswers = student.getAnswers().getOrDefault(examId, new HashMap<Integer, String[]>());
	            studentAnswers.put(questionNumber, answer);
	            student.getAnswers().put(examId, studentAnswers);
	        }
	    }



	    @Override
	    public void answerFillInTheBlanksQuestion(String studentName, int examId, int questionNumber, String[] answer) {
	    	 Student student = students.get(studentName);
		        if (student != null) {
		            Map<Integer, String[]> studentAnswers = student.getAnswers().getOrDefault(examId, new HashMap<Integer, String[]>());
		            studentAnswers.put(questionNumber, answer);
		            student.getAnswers().put(examId, studentAnswers);
		        }
	    }

	    @Override
	    public double getExamScore(String studentName, int examId) {
	        Student student = students.getOrDefault(studentName, null);
	        if (student == null) {
	            return 0;
	        }
	        return student.getExamScore(examId);
	    }

	    public String getGradingReport(String studentName, int examId) {
	        Student student = students.getOrDefault(studentName, null);
	        if (student == null) {
	            return "Student not found";
	        }

	        Exam exam = exams.getOrDefault(examId, null);
	        if (exam == null) {
	            return "Exam not found";
	        }

	        StringBuilder report = new StringBuilder();
	        Map<Integer, String[]> examAnswers = student.getAnswers().getOrDefault(examId, new HashMap<Integer, String[]>());
	        for (Question question : exam.getQuestions()) {
	            int questionNumber = question.getQuestionNumber();
	            double questionScore = question.getPoints();
	            double totalQuestionPoints = exam.getTotalScore();
	            double partialScore = calculatePartialScore(question, examAnswers.getOrDefault(questionNumber, null));
	            report.append("Question #").append(questionNumber)
	                    .append(" ").append(partialScore).append(" points out of ")
	                    .append(question.getPoints()).append("\n");
	        }

	        double finalScore = getExamScore(studentName, examId);
	        double totalExamPoints = exam.getTotalScore();
	        report.append("Final Score: ").append(finalScore).append(" out of ").append(totalExamPoints);

	        return report.toString();
	    }

	    




	    private double calculatePartialScore(Question question, String[] studentAnswer) {
	        if (studentAnswer != null) {
	            if (question instanceof TrueFalse) {
	                // True/False question
	                boolean correctAnswer = ((TrueFalse) question).getAnswer();
	                boolean studentChoice = Boolean.parseBoolean(studentAnswer[0]);

	                if (studentChoice == correctAnswer) {
	                    return question.getPoints();
	                }
	            } else if (question instanceof MultipleChoice) {
	                // Multiple choice question
	                String[] correctAnswers = ((MultipleChoice) question).getAnswer();
	                Set<String> studentChoices = new HashSet<>(Arrays.asList(studentAnswer));

	                if (Arrays.equals(correctAnswers, studentAnswer)) {
	                    return question.getPoints();
	                }
	            } else if (question instanceof FillInTheBlank) {
	                // Fill in the blank question
	                String[] correctAnswers = ((FillInTheBlank) question).getAnswer();
	                int correctChoices = 0;

	                for (String answer : studentAnswer) {
	                    if (Arrays.asList(correctAnswers).contains(answer)) {
	                        correctChoices++;
	                    }
	                }

	                return (question.getPoints() / correctAnswers.length) * correctChoices;
	            }
	        }

	        return 0.0;
	    }




		@Override
	    public void setLetterGradesCutoffs(String[] letterGrades, double[] cutoffs) {
	        if (letterGrades.length != cutoffs.length) {
	            throw new IllegalArgumentException("Invalid cutoffs");
	        }
	        this.letterGrades = letterGrades;
	        this.cutoffs = cutoffs;
	    }

		@Override
		public double getCourseNumericGrade(String studentName) {
		    Student student = students.get(studentName);
		    if (student == null) {
		        throw new IllegalArgumentException("Student not found: " + studentName);
		    }

		    double totalPoints = 0.0;
		    double totalMaxPoints = 0.0;

		    for (Exam exam : exams.values()) {
		        double examScore = student.getExamScore(exam.getExamId());
		        double maxPoints = exam.getTotalScore();
		        totalPoints += (examScore / maxPoints);
		        totalMaxPoints += 1.0;  // Assuming each exam has equal weight
		    }

		    double average = totalPoints / totalMaxPoints;
		    double result = average * 100;
		    return result;
		}

		


		@Override
		public String getCourseLetterGrade(String studentName) {
		    if (letterGrades == null || cutoffs == null) {
		        throw new IllegalStateException("Letter grades and cutoffs not set");
		    }

		    double numericGrade = getCourseNumericGrade(studentName);
		    int index = 0;
		    for (double cutoff : cutoffs) {
		        if (numericGrade >= cutoff) {
		            return letterGrades[index];
		        }
		        index++;
		    }
		    return letterGrades[letterGrades.length - 1];
		}

	    @Override
	    public String getCourseGrades() {
	        List<String> gradesList = new ArrayList<>();

	        for (Student student : students.values()) {
	            double numericGrade = getCourseNumericGrade(student.getName());
	            String letterGrade = getCourseLetterGrade(student.getName());
	            gradesList.add(student.getName() + " " + numericGrade + " " + letterGrade);
	        }

	        gradesList.sort(new Comparator<String>() {
	            @Override
	            public int compare(String s1, String s2) {
	                return s1.compareTo(s2);
	            }
	        });

	        StringBuilder gradesReport = new StringBuilder();
	        for (String grade : gradesList) {
	            gradesReport.append(grade).append("\n");
	        }

	        return gradesReport.toString();
	    }

	    @Override
	    public double getMaxScore(int examId) {
	        Exam exam = exams.getOrDefault(examId, null);
	        if (exam == null) {
	            return 0;
	        }

	        double maxScore = Double.MIN_VALUE;

	        for (Student student : students.values()) {
	            double examScore = student.getExamScore(examId);
	            if (examScore > maxScore) {
	                maxScore = examScore;
	            }
	        }

	        return maxScore;
	    }

	    @Override
	    public double getMinScore(int examId) {
	        Exam exam = exams.getOrDefault(examId, null);
	        if (exam == null) {
	            return 0;
	        }

	        double minScore = Double.MAX_VALUE;

	        for (Student student : students.values()) {
	            double examScore = student.getExamScore(examId);
	            if (examScore < minScore) {
	                minScore = examScore;
	            }
	        }

	        return minScore;
	    }

	    @Override
	    public double getAverageScore(int examId) {
	        Exam exam = exams.getOrDefault(examId, null);
	        if (exam == null) {
	            return 0;
	        }

	        double totalScore = 0;
	        int studentCount = students.size();

	        for (Student student : students.values()) {
	            totalScore += student.getExamScore(examId);
	        }

	        return totalScore / studentCount;
	    }

	  
	 


		@Override
		public void saveManager(Manager manager,  java.lang.String fileName) {	
			if (fileName == null) {
		        throw new IllegalArgumentException("File name cannot be null.");
		    }
			try{
				FileOutputStream f = new FileOutputStream(fileName);
				BufferedOutputStream b = new BufferedOutputStream(f);
				ObjectOutputStream o = new ObjectOutputStream(b);   
				o.writeObject(manager);
				o.close();
				
			}  
			catch(IOException e){
			}

		}

		@Override
		public Manager restoreManager(String fileName) {
			Manager value = null;
			if (fileName == null) {
		        throw new IllegalArgumentException("File name cannot be null.");
		    }
			try {
				FileInputStream f = new FileInputStream(fileName);
		        BufferedInputStream b = new BufferedInputStream(f);
		        ObjectInputStream o = new ObjectInputStream(b);
		        Manager manager = (Manager) o.readObject();
		        value = manager;
		        o.close();
		        
				
			}catch(IOException | ClassNotFoundException e){
				
			}
			return value;
		}

	      
	    }






    
