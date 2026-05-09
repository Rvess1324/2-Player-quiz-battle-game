/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package quizbattle;

/**
 *
 * @author End-User
 */
public class Question {
    String text;
    String[] options;
    int correctIndex;
    String difficulty;
    String topic;

    public Question(String text, String[] options, int correctIndex, String difficulty, String topic) {
        this.text = text;
        this.options = options;
        this.correctIndex = correctIndex;
        this.difficulty = difficulty;
        this.topic = topic;
    }
}
