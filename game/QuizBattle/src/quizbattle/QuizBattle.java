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

    /**
     * @param args the command line arguments
     */
    public QuizBattle() {
        setTitle("Quiz Battle - WMSU CS Edition");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);

        mainContainer.add(new MainMenu(this), "MENU");
        mainContainer.add(new BattlePanel(this), "GAME");

        add(mainContainer);
        showPanel("MENU"); 
    }

    public void showPanel(String name) {
        cardLayout.show(mainContainer, name);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new QuizBattle().setVisible(true);
        });
    }
}
