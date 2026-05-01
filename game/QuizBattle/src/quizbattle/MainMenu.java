/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package quizbattle;

import javax.swing.*;
import java.awt.*;

/**
 *
 * @author End-User
 */
public class MainMenu extends JPanel {
    private QuizBattle controller;

    public MainMenu(QuizBattle controller) {
        this.controller = controller;
        setLayout(null);
        setBackground(new Color(245, 245, 220)); // Matches BattlePanel background

        JLabel title = new JLabel("QUIZ BATTLE", SwingConstants.CENTER);
        title.setFont(new Font("Monospaced", Font.BOLD, 60));
        title.setForeground(new Color(40, 40, 40));
        title.setBounds(150, 80, 500, 80);
        add(title);

        // Buttons now use the "Battle Menu" style: White background, Dark Gray border
        addButton("START BATTLE", 290, 220, e -> controller.showPanel("GAME"));
        addButton("TUTORIAL", 290, 290, e -> showInstructions());
        addButton("EXIT GAME", 290, 360, e -> System.exit(0));
    }

    private void addButton(String text, int x, int y, java.awt.event.ActionListener al) {
        JButton btn = new JButton(text);
        btn.setBounds(x, y, 220, 50);
        btn.setFocusable(false);
        btn.setBackground(Color.WHITE);
        btn.setForeground(Color.BLACK);
        btn.setFont(new Font("Monospaced", Font.BOLD, 18));
        btn.setBorder(BorderFactory.createLineBorder(new Color(60, 60, 60), 3));
        btn.addActionListener(al);
        add(btn);
    }

    private void showInstructions() {
        JOptionPane.showMessageDialog(this, 
            "1. Read the question at the bottom.\n" + 
            "2. Press keys (A,S,D or J,K,L) to answer.\n" +
            "3. Correct answers damage the opponent.\n" +
            "4. Fill ULTIMATE for a massive strike!", 
            "Help", JOptionPane.PLAIN_MESSAGE);
    }
}
