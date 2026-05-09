/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package quizbattle;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 *
 * @author End-User
 */
public class QuestionManager {
    private ArrayList<Question> allQuestions = new ArrayList<>();
    private ArrayList<Question> pool = new ArrayList<>();
    private int currentIdx = 0;
    private String difficulty = "easy";

    public QuestionManager() {
        // ── EASY ──────────────────────────────────────────────────
        // Science
        add("What planet is closest to the Sun?", new String[]{"Venus","Mercury","Mars"}, 1, "easy","Science");
        add("What gas do plants absorb from the air?", new String[]{"Oxygen","Nitrogen","Carbon Dioxide"}, 2, "easy","Science");
        add("How many legs does an insect have?", new String[]{"4","6","8"}, 1, "easy","Science");
        add("What is the boiling point of water (°C)?", new String[]{"90","100","110"}, 1, "easy","Science");
        add("Which planet is known as the Red Planet?", new String[]{"Jupiter","Mars","Saturn"}, 1, "easy","Science");
        // History
        add("Who was the first US President?", new String[]{"Lincoln","Jefferson","Washington"}, 2, "easy","History");
        add("Which ancient wonder was in Egypt?", new String[]{"Colosseum","Great Pyramid","Parthenon"}, 1, "easy","History");
        add("What year did World War II end?", new String[]{"1943","1945","1947"}, 1, "easy","History");
        // Geography
        add("What is the capital of France?", new String[]{"Berlin","Paris","Rome"}, 1, "easy","Geography");
        add("What is the largest ocean?", new String[]{"Atlantic","Indian","Pacific"}, 2, "easy","Geography");
        add("Which country has the most people?", new String[]{"USA","China","India"}, 1, "easy","Geography");
        // Math
        add("What is 2 + 2?", new String[]{"3","4","5"}, 1, "easy","Math");
        add("What is 10 - 5?", new String[]{"2","5","8"}, 1, "easy","Math");
        add("What is 3 x 3?", new String[]{"6","9","12"}, 1, "easy","Math");
        add("What is 20 / 4?", new String[]{"4","5","6"}, 1, "easy","Math");
        // Pop Culture
        add("What color is SpongeBob?", new String[]{"Blue","Yellow","Green"}, 1, "easy","Pop Culture");
        add("How many players are on a basketball team (on court)?", new String[]{"4","5","6"}, 1, "easy","Pop Culture");
        add("What animal is Pikachu?", new String[]{"Mouse","Rabbit","Cat"}, 0, "easy","Pop Culture");
        // Food
        add("What is the main ingredient in guacamole?", new String[]{"Tomato","Avocado","Pepper"}, 1, "easy","Food");
        add("Which fruit is known as the 'king of fruits'?", new String[]{"Mango","Durian","Pineapple"}, 1, "easy","Food");

        // ── MEDIUM ────────────────────────────────────────────────
        // Science
        add("What is the chemical symbol for gold?", new String[]{"Ag","Au","Fe"}, 1, "medium","Science");
        add("What is the speed of light (km/s)?", new String[]{"200,000","300,000","400,000"}, 1, "medium","Science");
        add("How many bones are in the adult human body?", new String[]{"186","206","226"}, 1, "medium","Science");
        add("What force keeps planets in orbit?", new String[]{"Magnetism","Gravity","Friction"}, 1, "medium","Science");
        add("What is the powerhouse of the cell?", new String[]{"Nucleus","Ribosome","Mitochondria"}, 2, "medium","Science");
        // History
        add("In what year did the Berlin Wall fall?", new String[]{"1987","1989","1991"}, 1, "medium","History");
        add("Who painted the Mona Lisa?", new String[]{"Michelangelo","Da Vinci","Raphael"}, 1, "medium","History");
        add("Which empire built Machu Picchu?", new String[]{"Aztec","Mayan","Inca"}, 2, "medium","History");
        add("Who wrote 'The Art of War'?", new String[]{"Confucius","Sun Tzu","Laozi"}, 1, "medium","History");
        // Geography
        add("What is the longest river in the world?", new String[]{"Amazon","Mississippi","Nile"}, 2, "medium","Geography");
        add("Which country has the most natural lakes?", new String[]{"Russia","Canada","USA"}, 1, "medium","Geography");
        add("What is the capital of Australia?", new String[]{"Sydney","Melbourne","Canberra"}, 2, "medium","Geography");
        // Math
        add("What is the square root of 144?", new String[]{"11","12","13"}, 1, "medium","Math");
        add("What is 15% of 200?", new String[]{"25","30","35"}, 1, "medium","Math");
        add("If x + 7 = 15, what is x?", new String[]{"6","7","8"}, 2, "medium","Math");
        // Technology
        add("What does CPU stand for?", new String[]{"Central Process Unit","Central Processing Unit","Computer Processing Unit"}, 1, "medium","Technology");
        add("What language is used for web styling?", new String[]{"HTML","CSS","PHP"}, 1, "medium","Technology");
        add("What does URL stand for?", new String[]{"Universal Resource Link","Uniform Resource Locator","Unique Record Locator"}, 1, "medium","Technology");
        // Pop Culture
        add("Which movie features the quote 'May the Force be with you'?", new String[]{"Star Trek","Star Wars","Interstellar"}, 1, "medium","Pop Culture");
        add("Who sang 'Thriller'?", new String[]{"Prince","Michael Jackson","Madonna"}, 1, "medium","Pop Culture");

        // ── HARD ──────────────────────────────────────────────────
        // Science
        add("What is the atomic number of carbon?", new String[]{"4","6","8"}, 1, "hard","Science");
        add("What is the half-life of Carbon-14 (years)?", new String[]{"3,700","5,730","8,000"}, 1, "hard","Science");
        add("What is Newton's 2nd Law formula?", new String[]{"E=mc²","F=ma","P=mv"}, 1, "hard","Science");
        add("Which part of the brain controls balance?", new String[]{"Cerebrum","Hippocampus","Cerebellum"}, 2, "hard","Science");
        // History
        add("In what year was the Magna Carta signed?", new String[]{"1215","1315","1415"}, 0, "hard","History");
        add("Who was the last Tsar of Russia?", new String[]{"Nicholas I","Alexander III","Nicholas II"}, 2, "hard","History");
        add("The Battle of Thermopylae involved which two sides?", new String[]{"Rome vs Carthage","Greece vs Persia","Egypt vs Assyria"}, 1, "hard","History");
        // Geography
        add("What is the capital of Kazakhstan?", new String[]{"Almaty","Astana","Shymkent"}, 1, "hard","Geography");
        add("Which country has the most time zones?", new String[]{"Russia","USA","France"}, 2, "hard","Geography");
        add("What is the deepest lake in the world?", new String[]{"Caspian Sea","Lake Superior","Lake Baikal"}, 2, "hard","Geography");
        // Math
        add("What is the value of Pi to 4 decimal places?", new String[]{"3.1415","3.1416","3.1417"}, 1, "hard","Math");
        add("What is the derivative of sin(x)?", new String[]{"cos(x)","-cos(x)","tan(x)"}, 0, "hard","Math");
        add("How many prime numbers are below 20?", new String[]{"7","8","9"}, 1, "hard","Math");
        // Technology
        add("What does SQL stand for?", new String[]{"Structured Query Language","Simple Query Logic","System Query Language"}, 0, "hard","Technology");
        add("What is the time complexity of binary search?", new String[]{"O(n)","O(log n)","O(n²)"}, 1, "hard","Technology");
        add("What year was the first iPhone released?", new String[]{"2006","2007","2008"}, 1, "hard","Technology");
        // Pop Culture
        add("Who directed the movie 'Inception'?", new String[]{"Spielberg","Nolan","Villeneuve"}, 1, "hard","Pop Culture");
        add("In chess, how many squares are on the board?", new String[]{"48","56","64"}, 2, "hard","Pop Culture");
        add("What is the most spoken language in the world?", new String[]{"English","Spanish","Mandarin"}, 2, "hard","Pop Culture");
        add("Who wrote '1984'?", new String[]{"Huxley","Orwell","Kafka"}, 1, "hard","Literature");
    }

    private void add(String text, String[] opts, int correct, String diff, String topic) {
        allQuestions.add(new Question(text, opts, correct, diff, topic));
    }

    public void setDifficulty(String difficulty) {
        this.difficulty = difficulty;
        rebuildPool();
    }

    private void rebuildPool() {
        pool.clear();
        for (Question q : allQuestions) {
            if (q.difficulty.equals(difficulty)) pool.add(q);
        }
        Collections.shuffle(pool);
        currentIdx = 0;
    }

    public void reset() {
        rebuildPool();
    }

    public Question next() {
        if (pool.isEmpty()) rebuildPool();
        if (currentIdx >= pool.size()) {
            Collections.shuffle(pool);
            currentIdx = 0;
        }
        return pool.get(currentIdx++);
    }
}