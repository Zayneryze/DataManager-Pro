package cmdLineInterpreter;
import java.util.Arrays;
import java.util.Scanner;

import onlineTest.Manager;
import onlineTest.SystemManager;

public class Interpreter {

    public static void main(String[] args) {
        SystemManager manager = new SystemManager();
        Scanner scanner = new Scanner(System.in);

        while (true) {
            System.out.println("Enter a command (type 'help' for available commands):");
            String command = scanner.nextLine();

            if (command.equalsIgnoreCase("exit")) {
                System.out.println("Exiting...");
                break;
            } else if (command.equalsIgnoreCase("help")) {
                printHelp();
            } else {
                processCommand(manager, command);
            }
        }

        scanner.close();
    }

    private static void printHelp() {
        System.out.println("Available commands:");
        System.out.println("addExam <examId> <title>");
        System.out.println("addTrueFalseQuestion <examId> <questionNumber> <text> <points> <answer>");
        System.out.println("addMultipleChoiceQuestion <examId> <questionNumber> <text> <points> <answer1> <answer2> ...");
        System.out.println("addFillInTheBlanksQuestion <examId> <questionNumber> <text> <points> <answer1> <answer2> ...");
        System.out.println("addStudent <name>");
        System.out.println("answerTrueFalseQuestion <studentName> <examId> <questionNumber> <answer>");
        System.out.println("answerMultipleChoiceQuestion <studentName> <examId> <questionNumber> <answer1> <answer2> ...");
        System.out.println("answerFillInTheBlanksQuestion <studentName> <examId> <questionNumber> <answer1> <answer2> ...");
        System.out.println("getExamScore <studentName> <examId>");
        System.out.println("getGradingReport <studentName> <examId>");
        System.out.println("setLetterGradesCutoffs <letterGrade1> <letterGrade2> ... <cutoff1> <cutoff2> ...");
        System.out.println("getCourseNumericGrade <studentName>");
        System.out.println("getCourseLetterGrade <studentName>");
        System.out.println("getCourseGrades");
        System.out.println("getMaxScore <examId>");
        System.out.println("getMinScore <examId>");
        System.out.println("getAverageScore <examId>");
        System.out.println("saveManager <fileName>");
        System.out.println("restoreManager <fileName>");
        System.out.println("exit");
    }

    private static void processCommand(SystemManager manager, String command) throws NumberFormatException {
        String[] parts = command.trim().split("\\s+");
        String commandName = parts[0];

        try {
            switch (commandName) {
                case "addExam":
                    int examId = Integer.parseInt(parts[1]);
                    String title = parts[2];
                    manager.addExam(examId, title);
                    System.out.println("Exam added.");
                    break;

                case "addTrueFalseQuestion":
                    int examIdTF = Integer.parseInt(parts[1]);
                    int questionNumberTF = Integer.parseInt(parts[2]);
                    String textTF = String.join(" ", Arrays.copyOfRange(parts, 3, parts.length - 2));
                    double pointsTF = Double.parseDouble(parts[parts.length - 2]);
                    boolean answerTF = Boolean.parseBoolean(parts[parts.length - 1]);
                    manager.addTrueFalseQuestion(examIdTF, questionNumberTF, textTF, pointsTF, answerTF);
                    System.out.println("True/False question added.");
                    break;

                case "addMultipleChoiceQuestion":
                    int examIdMC = Integer.parseInt(parts[1]);
                    int questionNumberMC = Integer.parseInt(parts[2]);
                    String textMC = String.join(" ", Arrays.copyOfRange(parts, 3, parts.length - 1));
                    double pointsMC = Double.parseDouble(parts[parts.length - 1]);
                    String[] answerMC = Arrays.copyOfRange(parts, 5, parts.length - 1); // Corrected this line
                    manager.addMultipleChoiceQuestion(examIdMC, questionNumberMC, textMC, pointsMC, answerMC);
                    System.out.println("Multiple choice question added.");
                    break;

                case "addFillInTheBlanksQuestion":
                    int examIdFB = Integer.parseInt(parts[1]);
                    int questionNumberFB = Integer.parseInt(parts[2]);
                    String textFB = parts[3];
                    double pointsFB = Double.parseDouble(parts[4]);
                    String[] answerFB = Arrays.copyOfRange(parts, 5, parts.length);
                    manager.addFillInTheBlanksQuestion(examIdFB, questionNumberFB, textFB, pointsFB, answerFB);
                    System.out.println("Fill in the blanks question added.");
                    break;

                case "addStudent":
                    String name = parts[1];
                    manager.addStudent(name);
                    System.out.println("Student added.");
                    break;

                case "answerTrueFalseQuestion":
                    String studentNameTF = parts[1];
                    int examIdATF = Integer.parseInt(parts[2]);
                    int questionNumberATF = Integer.parseInt(parts[3]);
                    boolean answerATF = Boolean.parseBoolean(parts[4]);
                    manager.answerTrueFalseQuestion(studentNameTF, examIdATF, questionNumberATF, answerATF);
                    System.out.println("True/False question answered.");
                    break;

                case "answerMultipleChoiceQuestion":
                    String studentNameMC = parts[1];
                    int examIdAMC = Integer.parseInt(parts[2]);
                    int questionNumberAMC = Integer.parseInt(parts[3]);
                    String[] answerAMC = Arrays.copyOfRange(parts, 4, parts.length);
                    manager.answerMultipleChoiceQuestion(studentNameMC, examIdAMC, questionNumberAMC, answerAMC);
                    System.out.println("Multiple choice question answered.");
                    break;

                case "answerFillInTheBlanksQuestion":
                    String studentNameFB = parts[1];
                    int examIdAFB = Integer.parseInt(parts[2]);
                    int questionNumberAFB = Integer.parseInt(parts[3]);
                    String[] answerAFB = Arrays.copyOfRange(parts, 4, parts.length);
                    manager.answerFillInTheBlanksQuestion(studentNameFB, examIdAFB, questionNumberAFB, answerAFB);
                    System.out.println("Fill in the blanks question answered.");
                    break;

                case "getExamScore":
                    String studentNameGS = parts[1];
                    int examIdGS = Integer.parseInt(parts[2]);
                    double score = manager.getExamScore(studentNameGS, examIdGS);
                    System.out.println("Exam score for " + studentNameGS + " in exam " + examIdGS + ": " + score);
                    break;

                case "getGradingReport":
                    String studentNameGR = parts[1];
                    int examIdGR = Integer.parseInt(parts[2]);
                    String report = manager.getGradingReport(studentNameGR, examIdGR);
                    System.out.println(report);
                    break;

                case "setLetterGradesCutoffs":
                    int letterGradeCount = (parts.length - 1) / 2;
                    String[] letterGrades = Arrays.copyOfRange(parts, 1, 1 + letterGradeCount);
                    double[] cutoffs = new double[letterGradeCount];
                    for (int i = 0; i < letterGradeCount; i++) {
                        cutoffs[i] = Double.parseDouble(parts[1 + letterGradeCount + i]);
                    }
                    manager.setLetterGradesCutoffs(letterGrades, cutoffs);
                    System.out.println("Letter grades and cutoffs set.");
                    break;

                case "getCourseNumericGrade":
                    String studentNameNG = parts[1];
                    double numericGrade = manager.getCourseNumericGrade(studentNameNG);
                    System.out.println("Numeric grade for " + studentNameNG + ": " + numericGrade);
                    break;

                case "getCourseLetterGrade":
                    String studentNameLG = parts[1];
                    String letterGrade = manager.getCourseLetterGrade(studentNameLG);
                    System.out.println("Letter grade for " + studentNameLG + ": " + letterGrade);
                    break;

                case "getCourseGrades":
                    String courseGrades = manager.getCourseGrades();
                    System.out.println(courseGrades);
                    break;

                case "getMaxScore":
                    int examIdMax = Integer.parseInt(parts[1]);
                    double maxScore = manager.getMaxScore(examIdMax);
                    System.out.println("Max score for exam " + examIdMax + ": " + maxScore);
                    break;

                case "getMinScore":
                    int examIdMin = Integer.parseInt(parts[1]);
                    double minScore = manager.getMinScore(examIdMin);
                    System.out.println("Min score for exam " + examIdMin + ": " + minScore);
                    break;

                case "getAverageScore":
                    int examIdAvg = Integer.parseInt(parts[1]);
                    double avgScore = manager.getAverageScore(examIdAvg);
                    System.out.println("Average score for exam " + examIdAvg + ": " + avgScore);
                    break;

                case "saveManager":
                    String fileNameSave = parts[1];
                    manager.saveManager(manager, fileNameSave);
                    System.out.println("Manager data saved to file: " + fileNameSave);
                    break;

                case "restoreManager":
                    String fileNameRestore = parts[1];
                    Manager restoredManager = manager.restoreManager(fileNameRestore);
                    if (restoredManager != null) {
                        System.out.println("Manager data restored from file: " + fileNameRestore);
                        manager = (SystemManager) restoredManager;
                    } else {
                        System.out.println("Failed to restore manager data from file.");
                    }
                    break;

                default:
                    System.out.println("Invalid command. Type 'help' for available commands.");
                    break;
            }
        } catch (IndexOutOfBoundsException | IllegalArgumentException e) {
            System.out.println("Invalid arguments for the command. Type 'help' for available commands.");
        }
    }

    }

