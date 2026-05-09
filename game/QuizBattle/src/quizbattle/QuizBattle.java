/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package quizbattle;

import javax.swing.*;
import java.awt.*;

/**
 *
 * @author End-User
 */
public class QuizBattle extends JFrame {
    private CardLayout cardLayout = new CardLayout();
    private JPanel mainContainer = new JPanel(cardLayout);
    private BattlePanel battlePanel;
    private String difficulty = "easy";

    public QuizBattle() {
        setTitle("Quiz Battle - WMSU CS Edition");
        setSize(1000, 750);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);

        battlePanel = new BattlePanel(this);
        mainContainer.add(new MainMenu(this), "MENU");
        mainContainer.add(battlePanel, "GAME");
        add(mainContainer);
        showPanel("MENU");
    }

    public void setDifficulty(String diff) {
        this.difficulty = diff;
        battlePanel.setDifficulty(diff);
    }

    public void showPanel(String name) {
        cardLayout.show(mainContainer, name);
        if (name.equals("GAME")) mainContainer.getComponent(1).requestFocusInWindow();
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new QuizBattle().setVisible(true));
    }
}
