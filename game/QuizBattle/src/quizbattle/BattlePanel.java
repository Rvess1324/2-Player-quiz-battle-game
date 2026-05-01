/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package quizbattle;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import javax.imageio.ImageIO;
import java.io.File;

/**
 *
 * @author End-User
 */

public class BattlePanel extends JPanel implements ActionListener {
    private QuizBattle controller;
    private int p1Lives = 3, p2Lives = 3;
    private int p1HP = 100, p2HP = 100, p1Ult = 0, p2Ult = 0;
    private final int MAX_ULT = 100;
    
    private Image p1Sprite, p2Sprite;
    private final int P1_HOME_X = 150, P2_HOME_X = 540, SPRITE_Y = 270; 
    private int p1X = P1_HOME_X, p2X = P2_HOME_X;
    
    private QuestionManager qm = new QuestionManager();
    private Question currentQ;
    private boolean isPaused = false, isAttacking = false;
    private boolean p1IsAttacking = false, p2IsAttacking = false;
    private int animFrame = 0;

    private JLabel questionLabel;
    private JProgressBar p1HealthBar, p2HealthBar, p1UltBar, p2UltBar;
    private JLabel[] p1OptionLabels = new JLabel[3], p2OptionLabels = new JLabel[3];
    private Timer gameTimer;

    private final Color BG_CREAM = new Color(245, 245, 220);
    private final Color BORDER_BLACK = new Color(40, 40, 40);

    public BattlePanel(QuizBattle controller) {
        this.controller = controller;
        setLayout(null);
        setBackground(BG_CREAM);

        try {
            p1Sprite = ImageIO.read(new File("p1.png"));
            p2Sprite = ImageIO.read(new File("p2.png"));
        } catch (Exception e) {
            System.err.println("Sprites not found. Using fallbacks.");
        }

        setupControls();
        setupQuestionUI();
        setupPlayerCard(true, 30, 15);   
        setupPlayerCard(false, 490, 15); 

        for (int i = 0; i < 3; i++) {
            p1OptionLabels[i] = createChoiceLabel(40 + (i * 85), 145); 
            p2OptionLabels[i] = createChoiceLabel(500 + (i * 85), 145);
        }

        setupKeyBindings();
        gameTimer = new Timer(16, this);
        gameTimer.start();
        refreshGame();
    }

    private void setupControls() {
        int startX = 338; 
        int btnWidth = 40, btnHeight = 35, gap = 3;
        createIconButton("☰", startX, 20, btnWidth, btnHeight).addActionListener(e -> controller.showPanel("MENU"));
        createIconButton("Ⅱ", startX + (btnWidth + gap), 20, btnWidth, btnHeight).addActionListener(e -> isPaused = !isPaused);
        createIconButton("⟳", startX + (btnWidth + gap) * 2, 20, btnWidth, btnHeight).addActionListener(e -> resetGame());
    }

    private void setupQuestionUI() {
        questionLabel = new JLabel("", SwingConstants.CENTER);
        questionLabel.setBounds(40, 440, 720, 110);
        questionLabel.setOpaque(true);
        questionLabel.setBackground(new Color(235, 235, 215)); 
        questionLabel.setForeground(BORDER_BLACK);
        questionLabel.setFont(new Font("Courier New", Font.BOLD, 22));
        questionLabel.setBorder(BorderFactory.createLineBorder(BORDER_BLACK, 4));
        add(questionLabel);
    }

    private JButton createIconButton(String icon, int x, int y, int w, int h) {
        JButton b = new JButton(icon);
        b.setBounds(x, y, w, h);
        b.setFocusable(false);
        b.setBackground(Color.WHITE);
        b.setBorder(BorderFactory.createLineBorder(BORDER_BLACK, 2));
        add(b);
        return b;
    }

    private void setupPlayerCard(boolean isP1, int x, int y) {
        JPanel card = new JPanel(null);
        card.setBounds(x, y, 280, 120);
        card.setBackground(Color.WHITE);
        card.setBorder(BorderFactory.createLineBorder(BORDER_BLACK, 3));

        JLabel nameLbl = new JLabel(isP1 ? "PLAYER 1" : "PLAYER 2");
        nameLbl.setFont(new Font("Courier New", Font.BOLD, 18));
        nameLbl.setBounds(10, 5, 150, 20);
        card.add(nameLbl);

        JProgressBar hp = new JProgressBar(0, 100);
        hp.setValue(100);
        hp.setForeground(new Color(46, 204, 113)); 
        hp.setBounds(10, 35, 260, 18);
        hp.setBorder(BorderFactory.createLineBorder(BORDER_BLACK, 1));
        card.add(hp);

        JLabel ultLbl = new JLabel("ULTIMATE");
        ultLbl.setFont(new Font("Courier New", Font.BOLD, 12));
        ultLbl.setBounds(10, 65, 80, 15);
        card.add(ultLbl);

        JProgressBar ult = new JProgressBar(0, 100);
        ult.setValue(0);
        ult.setForeground(new Color(52, 152, 219)); 
        ult.setBounds(10, 85, 260, 12);
        ult.setBorder(BorderFactory.createLineBorder(BORDER_BLACK, 1));
        card.add(ult);

        if (isP1) { p1HealthBar = hp; p1UltBar = ult; } 
        else { p2HealthBar = hp; p2UltBar = ult; }
        add(card);
    }

    private JLabel createChoiceLabel(int x, int y) {
        JLabel l = new JLabel("", SwingConstants.CENTER);
        l.setBounds(x, y, 80, 40);
        l.setBackground(Color.WHITE);
        l.setOpaque(true);
        l.setFont(new Font("Courier New", Font.BOLD, 16));
        l.setBorder(BorderFactory.createLineBorder(BORDER_BLACK, 2));
        add(l);
        return l;
    }

    private void setupKeyBindings() {
        InputMap im = getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW);
        ActionMap am = getActionMap();
        char[] p1Keys = {'a', 's', 'd'}, p2Keys = {'j', 'k', 'l'};
        for (int i = 0; i < 3; i++) {
            final int idx = i;
            im.put(KeyStroke.getKeyStroke(p1Keys[i]), "p1_" + i);
            am.put("p1_" + i, new AbstractAction() { public void actionPerformed(ActionEvent e) { handleRaceInput(idx, true); } });
            im.put(KeyStroke.getKeyStroke(p2Keys[i]), "p2_" + i);
            am.put("p2_" + i, new AbstractAction() { public void actionPerformed(ActionEvent e) { handleRaceInput(idx, false); } });
        }
    }

    private void handleRaceInput(int choice, boolean isP1) {
        if (isPaused || isAttacking || currentQ == null) return;
        if (choice == currentQ.correctIndex) {
            isAttacking = true;
            if (isP1) { p1IsAttacking = true; p2HP -= (p1Ult >= MAX_ULT) ? 50 : 20; p1Ult = Math.min(MAX_ULT, p1Ult + 25); } 
            else { p2IsAttacking = true; p1HP -= (p2Ult >= MAX_ULT) ? 50 : 20; p2Ult = Math.min(MAX_ULT, p2Ult + 25); }
            checkKnockouts(); updateBars(); refreshGame();
        } else { 
            if (isP1) p1HP -= 10; else p2HP -= 10; 
            checkKnockouts(); updateBars(); 
        }
    }

    private void checkKnockouts() {
        if (p1HP <= 0) { p1Lives--; p1HP = 100; p1Ult = 0; }
        if (p2HP <= 0) { p2Lives--; p2HP = 100; p2Ult = 0; }
    }

    private void refreshGame() {
        if (p1Lives <= 0 || p2Lives <= 0) { questionLabel.setText("BATTLE FINISHED!"); return; }
        currentQ = qm.next();
        if (currentQ != null) {
            questionLabel.setText(currentQ.text);
            for (int i = 0; i < 3; i++) {
                p1OptionLabels[i].setText(currentQ.options[i]);
                p2OptionLabels[i].setText(currentQ.options[i]);
            }
        }
    }

    private void updateBars() {
        p1HealthBar.setValue(p1HP); p2HealthBar.setValue(p2HP);
        p1UltBar.setValue(p1Ult); p2UltBar.setValue(p2Ult);
    }

    private void resetGame() {
        p1Lives = 3; p2Lives = 3; p1HP = 100; p2HP = 100; p1Ult = 0; p2Ult = 0;
        p1X = P1_HOME_X; p2X = P2_HOME_X; updateBars(); refreshGame();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (!isPaused && isAttacking) {
            animFrame++;
            if (p1IsAttacking) {
                if (animFrame < 10) p1X += 30; else if (animFrame < 20) p1X -= 30; else finishAttack();
            } else if (p2IsAttacking) {
                if (animFrame < 10) p2X -= 30; else if (animFrame < 20) p2X += 30; else finishAttack();
            }
        }
        repaint();
    }

    private void finishAttack() {
        isAttacking = false; p1IsAttacking = false; p2IsAttacking = false;
        animFrame = 0; p1X = P1_HOME_X; p2X = P2_HOME_X;
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        
        g2d.setColor(new Color(20, 20, 20, 160)); 
        
        g2d.fillOval(P1_HOME_X - 50, SPRITE_Y + 85, 210, 55); 
        g2d.fillOval(P2_HOME_X - 50, SPRITE_Y + 85, 210, 55); 

        
        if (p1Sprite != null) {
            g2d.drawImage(p1Sprite, p1X, SPRITE_Y, 110, 130, null);
        }
        if (p2Sprite != null) {
            
            g2d.drawImage(p2Sprite, p2X + 110, SPRITE_Y, -110, 130, null);
        }
    }
}
