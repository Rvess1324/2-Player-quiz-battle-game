/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package quizbattle;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

/**
 *
 * @author End-User
 */
public class MainMenu extends JPanel {
    private QuizBattle controller;
    private String selectedDifficulty = "easy";

    private final Color BG      = new Color(245, 245, 220);
    private final Color FG      = new Color(40,  40,  40);
    private final Color BORDER  = new Color(60,  60,  60);
    private final Color HL_EASY = new Color(46, 204, 113);
    private final Color HL_MED  = new Color(230, 160, 20);
    private final Color HL_HARD = new Color(200, 50, 50);

    private JButton btnEasy, btnMed, btnHard;

    public MainMenu(QuizBattle controller) {
        this.controller = controller;
        setLayout(null);
        setBackground(BG);

        // Title
        JLabel title = new JLabel("QUIZ BATTLE", SwingConstants.CENTER);
        title.setFont(new Font("Monospaced", Font.BOLD, 60));
        title.setForeground(FG);
        title.setBounds(100, 55, 800, 85);
        add(title);

        JLabel sub = new JLabel("WMSU CS EDITION", SwingConstants.CENTER);
        sub.setFont(new Font("Monospaced", Font.BOLD, 16));
        sub.setForeground(new Color(120, 120, 100));
        sub.setBounds(100, 145, 800, 22);
        add(sub);

        // Difficulty label
        JLabel diffLbl = new JLabel("SELECT DIFFICULTY:", SwingConstants.CENTER);
        diffLbl.setFont(new Font("Monospaced", Font.BOLD, 17));
        diffLbl.setForeground(FG);
        diffLbl.setBounds(100, 195, 800, 28);
        add(diffLbl);

        // Difficulty buttons — centered in 1000px wide panel
        int bw = 140, bh = 45, gap = 18;
        int totalW = bw * 3 + gap * 2;
        int bx = (1000 - totalW) / 2;
        int by = 230;

        btnEasy = makeDiffBtn("EASY",   bx,              by, bw, bh, HL_EASY);
        btnMed  = makeDiffBtn("MEDIUM", bx + bw + gap,   by, bw, bh, HL_MED);
        btnHard = makeDiffBtn("HARD",   bx+(bw+gap)*2,   by, bw, bh, HL_HARD);

        btnEasy.addActionListener(e -> setDifficulty("easy",   btnEasy));
        btnMed .addActionListener(e -> setDifficulty("medium", btnMed));
        btnHard.addActionListener(e -> setDifficulty("hard",   btnHard));
        selectDiffBtn(btnEasy, HL_EASY); // default highlight

        // Divider
        JSeparator sep = new JSeparator();
        sep.setBounds(150, 296, 700, 3);
        sep.setForeground(BORDER);
        add(sep);

        // Skill info box
        JTextArea info = new JTextArea(
            "  SKILLS & MOVES\n" +
            "  Strike (3pts)  |  Shield (4pts)  |  Double (5pts)\n" +
            "  Drain HP (6pts)|  Point Curse (7pts) | LETHAL (15pts)\n" +
            "  > Exceed 3 pts: wrong answer costs 1 point!\n" +
            "  > Lethal wipes ALL opponent HP & points!\n" +
            "  > Use skills between turns."
        );
        info.setFont(new Font("Monospaced", Font.PLAIN, 13));
        info.setForeground(new Color(80, 80, 60));
        info.setBackground(new Color(235, 235, 210));
        info.setBorder(BorderFactory.createLineBorder(BORDER, 2));
        info.setEditable(false);
        info.setBounds(150, 308, 700, 110);
        add(info);

        // Main buttons — centered
        int btnX = (1000 - 230) / 2;
        addButton("START BATTLE", btnX, 445, e -> {
            controller.setDifficulty(selectedDifficulty);
            controller.showPanel("GAME");
        });
        addButton("TUTORIAL",     btnX, 505, e -> showTutorial());
        addButton("EXIT GAME",    btnX, 565, e -> System.exit(0));
    }

    private JButton makeDiffBtn(String text, int x, int y, int w, int h, Color col) {
        JButton b = new JButton(text);
        b.setBounds(x, y, w, h);
        b.setFocusable(false);
        b.setBackground(Color.WHITE);
        b.setForeground(FG);
        b.setFont(new Font("Monospaced", Font.BOLD, 15));
        b.setBorder(BorderFactory.createLineBorder(BORDER, 2));
        add(b);
        return b;
    }

    private void setDifficulty(String diff, JButton selected) {
        selectedDifficulty = diff;
        selectDiffBtn(btnEasy, HL_EASY);
        selectDiffBtn(btnMed,  HL_MED);
        selectDiffBtn(btnHard, HL_HARD);
        Color col = diff.equals("easy") ? HL_EASY : diff.equals("medium") ? HL_MED : HL_HARD;
        selected.setBackground(col);
        selected.setForeground(Color.WHITE);
    }

    private void selectDiffBtn(JButton b, Color col) {
        b.setBackground(Color.WHITE);
        b.setForeground(new Color(40, 40, 40));
    }

    private void addButton(String text, int x, int y, java.awt.event.ActionListener al) {
        JButton btn = new JButton(text);
        btn.setBounds(x, y, 230, 48);
        btn.setFocusable(false);
        btn.setBackground(Color.WHITE);
        btn.setForeground(Color.BLACK);
        btn.setFont(new Font("Monospaced", Font.BOLD, 18));
        btn.setBorder(BorderFactory.createLineBorder(new Color(60, 60, 60), 3));
        btn.addActionListener(al);
        add(btn);
    }

    private void showTutorial() {
        JOptionPane.showMessageDialog(this,
            "HOW TO PLAY\n" +
            "─────────────────────────────────\n" +
            "• Questions are shown at the bottom.\n" +
            "• Player 1: Press A, S, D to choose option 1, 2, 3.\n" +
            "• Player 2: Press J, K, L to choose option 1, 2, 3.\n" +
            "• Correct answer → opponent loses HP, you earn a point.\n" +
            "• Wrong answer → you lose HP.\n\n" +
            "POINT RULES\n" +
            "─────────────────────────────────\n" +
            "• Over 3 pts? Wrong answers cost you 1 point!\n" +
            "• Spend points on SKILLS before answering.\n\n" +
            "SKILLS (press their button during your turn)\n" +
            "─────────────────────────────────\n" +
            "⚡ Strike   (3 pts) → -1 HP to opponent\n" +
            "🛡 Shield   (4 pts) → block next hit\n" +
            "✨ Double   (5 pts) → next correct = 2 pts\n" +
            "🌀 HP Drain (6 pts) → steal 1 HP from opponent\n" +
            "💀 Curse    (7 pts) → opponent loses 2 pts\n" +
            "☠ LETHAL  (15 pts) → wipe opponent's HP to zero!\n\n" +
            "Lose all HP → lose a life. Lose all lives → DEFEAT.",
            "TUTORIAL", JOptionPane.PLAIN_MESSAGE);
    }
}
