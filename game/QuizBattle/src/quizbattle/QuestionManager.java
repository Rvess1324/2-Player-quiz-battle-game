/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package quizbattle;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 *
 * @author End-User
 */
public class QuestionManager {
    private ArrayList<Question> questions = new ArrayList<>();
    private int currentIdx = 0;

    public QuestionManager() {
        questions.add(new Question("What is 2 + 2?", new String[]{"3", "4", "5"}, 1));
        questions.add(new Question("What color is grass?", new String[]{"Red", "Blue", "Green"}, 2));
        questions.add(new Question("Which animal barks?", new String[]{"Dog", "Cat", "Fish"}, 0));
        questions.add(new Question("What is 10 - 5?", new String[]{"2", "5", "8"}, 1));
        questions.add(new Question("How many legs does a spider have?", new String[]{"6", "8", "10"}, 1));
        questions.add(new Question("Which fruit is yellow?", new String[]{"Apple", "Banana", "Grape"}, 1));
        Collections.shuffle(questions);
    }

    public Question next() {
        if (currentIdx >= questions.size()) currentIdx = 0;
        return questions.get(currentIdx++);
    }
}