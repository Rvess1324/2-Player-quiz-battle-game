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
import java.util.ArrayList;

/**
 *
 * @author End-User
 */

public class BattlePanel extends JPanel implements ActionListener {
    private QuizBattle controller;
    // ── lives / HP ──────────────────────────────────────────────
    private int p1Lives = 3, p2Lives = 3;
    private int p1HP = 100, p2HP = 100;
    // ── points (battle currency) ─────────────────────────────────
    private int p1Pts = 0, p2Pts = 0;
    private static final int POINT_PENALTY_THRESHOLD = 3;
    // ── SCORE = match wins ────────────────────────────────────────
    private int p1Score = 0, p2Score = 0;
    // ── turn (0 = P1, 1 = P2) ────────────────────────────────────
    private int currentTurn = 0;
    // ── shield / double ───────────────────────────────────────────
    private boolean p1Shield = false, p2Shield = false;
    private boolean p1Double = false, p2Double = false;
    // ── sprite / animation ───────────────────────────────────────
    private Image p1Sprite, p2Sprite;
    private final int P1_HOME_X = 160, P2_HOME_X = 640, SPRITE_Y = 315;
    private int p1X = P1_HOME_X, p2X = P2_HOME_X;
    private boolean isPaused = false, isAttacking = false;
    private boolean p1IsAttacking = false, p2IsAttacking = false;
    private int animFrame = 0;
    // ── win overlay ──────────────────────────────────────────────
    private String winnerName = "";
    private float overlayAlpha = 0f;
    private Timer overlayFadeTimer;
    private JPanel overlayPane;
    // ── pause overlay ────────────────────────────────────────────
    private float pauseAlpha = 0f;
    private Timer pauseFadeTimer;
    private JPanel pauseOverlayPane;
    // ── menu exit overlay ──────────────────────────────────
    private float menuAlpha = 0f;
    private Timer menuFadeTimer;
    private JPanel menuOverlayPane;
    // ── question ─────────────────────────────────────────────────
    private QuestionManager qm = new QuestionManager();
    private Question currentQ;
    private boolean answered = false;
    // ── colors ───────────────────────────────────────────────────
    private final Color BG_CREAM  = new Color(245, 245, 220);
    private final Color BORDER_BK = new Color(40,  40,  40);
    private final Color GREEN_HP  = new Color(46, 204, 113);
    private final Color BLUE_ULT  = new Color(52, 152, 219);
    private final Color P1_COL    = new Color(52, 152, 219);
    private final Color P2_COL    = new Color(231, 76, 60);
    // ── UI elements ───────────────────────────────────────────────
    private JLabel questionLabel;
    private JProgressBar p1HealthBar, p2HealthBar;
    private JLabel[] p1OptionLabels = new JLabel[3], p2OptionLabels = new JLabel[3];
    private JLabel p1PtsLbl, p2PtsLbl, p1LivesLbl, p2LivesLbl, p1ShieldLbl, p2ShieldLbl;
    private JLabel turnIndicator;
    private JTextArea logArea;
    // ── skill buttons ─────────────────────────────────────────────
    private JButton btnStrike, btnShield, btnDouble, btnDrain, btnCurse, btnLethal;

    private ArrayList<JButton> topButtons = new ArrayList<>();
    private Timer gameTimer;

    public BattlePanel(QuizBattle controller) {
        this.controller = controller;
        setLayout(null);
        setBackground(BG_CREAM);
        try {
            p1Sprite = ImageIO.read(new File("p1.png"));
            p2Sprite = ImageIO.read(new File("p2.png"));
        } catch (Exception e) {
            System.err.println("Sprites not found.");
        }
        setupTopBar();
        setupPlayerCards();
        setupSkillPanel();
        setupQuestionArea();
        setupChoiceLabels();
        setupKeyBindings();
        setupLog();
        setupOverlayPane();
        setupPauseOverlayPane();
        setupMenuOverlayPane(); 
        gameTimer = new Timer(16, this);
        gameTimer.start();
        qm.setDifficulty("easy");
        refreshQuestion();
        refreshUI();
    }

    public void setDifficulty(String diff) {
        qm.setDifficulty(diff);
    }

    private void setupMenuOverlayPane() {
        menuOverlayPane = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                if (menuAlpha <= 0f) return;
                Graphics2D g2d = (Graphics2D) g.create();
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                int W = getWidth(), H = getHeight();
                g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, menuAlpha * 0.75f));
                g2d.setColor(new Color(20, 10, 10));
                g2d.fillRect(0, 0, W, H);
                g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, menuAlpha));
                int cardW = 400, cardH = 200;
                int cardX = (W - cardW) / 2;
                int cardY = (H - cardH) / 2;
                g2d.setColor(new Color(0, 0, 0, 130));
                g2d.fillRoundRect(cardX + 8, cardY + 8, cardW, cardH, 30, 30);
                g2d.setColor(Color.WHITE);
                g2d.fillRoundRect(cardX, cardY, cardW, cardH, 30, 30);
                g2d.setColor(BORDER_BK);
                g2d.setStroke(new BasicStroke(5));
                g2d.drawRoundRect(cardX, cardY, cardW, cardH, 30, 30);
                g2d.setFont(new Font("Monospaced", Font.BOLD, 32));
                String head = "EXIT TO MENU?";
                FontMetrics fm = g2d.getFontMetrics();
                g2d.drawString(head, cardX + (cardW - fm.stringWidth(head)) / 2, cardY + 60);
                g2d.setFont(new Font("Courier New", Font.PLAIN, 16));
                g2d.setColor(new Color(100, 100, 100));
                String sub = "All progress & scores will reset.";
                fm = g2d.getFontMetrics();
                g2d.drawString(sub, cardX + (cardW - fm.stringWidth(sub)) / 2, cardY + 90);
                g2d.dispose();
            }
        };
        menuOverlayPane.setLayout(null);
        menuOverlayPane.setOpaque(false);
        menuOverlayPane.setBounds(0, 0, 1000, 750);
        menuOverlayPane.setVisible(false);

        JButton btnYes = new JButton("YES, EXIT");
        btnYes.setBounds(100, 125, 120, 40);
        btnYes.setFocusable(false);
        btnYes.setBackground(P2_COL);
        btnYes.setForeground(Color.WHITE);
        btnYes.setFont(new Font("Monospaced", Font.BOLD, 14));
        btnYes.addActionListener(e -> {
            p1Score = 0; // Completely reset Match Wins
            p2Score = 0;
            resetGame();
            controller.showPanel("MENU");
        });

        JButton btnNo = new JButton("NO, STAY");
        btnNo.setBounds(230, 125, 120, 40);
        btnNo.setFocusable(false);
        btnNo.setBackground(GREEN_HP);
        btnNo.setForeground(Color.WHITE);
        btnNo.setFont(new Font("Monospaced", Font.BOLD, 14));
        btnNo.addActionListener(e -> hideMenuConfirm());

        btnYes.setLocation( (400 - 250)/2 + 500 - 200, 375 + 30);
        btnNo.setLocation(  (400 - 250)/2 + 130 + 500 - 200, 375 + 30);
        menuOverlayPane.add(btnYes);
        menuOverlayPane.add(btnNo);
        add(menuOverlayPane);
    }

    private void showMenuConfirm() {
        if (p1Lives <= 0 || p2Lives <= 0 || pauseOverlayPane.isVisible()) return;
        isPaused = true;
        menuAlpha = 0f;
        menuOverlayPane.setVisible(true);
        updateZOrder(menuOverlayPane);
        if (menuFadeTimer != null) menuFadeTimer.stop();
        menuFadeTimer = new Timer(16, e -> {
            menuAlpha = Math.min(1f, menuAlpha + 0.1f);
            menuOverlayPane.repaint();
            if (menuAlpha >= 1f) ((Timer)e.getSource()).stop();
        });
        menuFadeTimer.start();
    }

    private void hideMenuConfirm() {
        menuOverlayPane.setVisible(false);
        menuAlpha = 0f;
        isPaused = false;
    }

    private void setupPauseOverlayPane() {
        pauseOverlayPane = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                if (pauseAlpha <= 0f) return;
                Graphics2D g2d = (Graphics2D) g.create();
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                int W = getWidth(), H = getHeight();
                g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, pauseAlpha * 0.75f));
                g2d.setColor(new Color(10, 10, 30));
                g2d.fillRect(0, 0, W, H);
                g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, pauseAlpha));
                int cardW = 400, cardH = 150;
                int cardX = (W - cardW) / 2;
                int cardY = (H - cardH) / 2;
                g2d.setColor(new Color(0, 0, 0, 130));
                g2d.fillRoundRect(cardX + 8, cardY + 8, cardW, cardH, 30, 30);
                g2d.setColor(Color.WHITE);
                g2d.fillRoundRect(cardX, cardY, cardW, cardH, 30, 30);
                g2d.setColor(BORDER_BK);
                g2d.setStroke(new BasicStroke(5));
                g2d.drawRoundRect(cardX, cardY, cardW, cardH, 30, 30);
                g2d.setFont(new Font("Monospaced", Font.BOLD, 42));
                g2d.setColor(BORDER_BK);
                FontMetrics fmP = g2d.getFontMetrics();
                String pauseText = "PAUSED";
                g2d.drawString(pauseText, cardX + (cardW - fmP.stringWidth(pauseText)) / 2, cardY + 70);
                g2d.setFont(new Font("Courier New", Font.PLAIN, 18));
                g2d.setColor(new Color(80, 80, 80));
                FontMetrics fmS = g2d.getFontMetrics();
                String sub = "Press Ⅱ to Resume";
                g2d.drawString(sub, cardX + (cardW - fmS.stringWidth(sub)) / 2, cardY + 110);
                g2d.dispose();
            }
        };
        pauseOverlayPane.setOpaque(false);
        pauseOverlayPane.setBounds(0, 0, 1000, 750);
        pauseOverlayPane.setVisible(false);
        add(pauseOverlayPane);
    }

    private void setupOverlayPane() {
        overlayPane = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                if (overlayAlpha <= 0f) return;
                Graphics2D g2d = (Graphics2D) g.create();
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                int W = getWidth(), H = getHeight();
                g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, overlayAlpha * 0.75f));
                g2d.setColor(new Color(10, 10, 30));
                g2d.fillRect(0, 0, W, H);
                g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, overlayAlpha));
                int cardW = 520, cardH = 250;
                int cardX = (W - cardW) / 2;
                int cardY = (H - cardH) / 2 - 20;
                g2d.setColor(new Color(0, 0, 0, 130));
                g2d.fillRoundRect(cardX + 8, cardY + 8, cardW, cardH, 30, 30);
                boolean p1Wins = winnerName.contains("1");
                Color winCol = p1Wins ? P1_COL : P2_COL;
                g2d.setColor(Color.WHITE);
                g2d.fillRoundRect(cardX, cardY, cardW, cardH, 30, 30);
                g2d.setColor(winCol);
                g2d.setStroke(new BasicStroke(5));
                g2d.drawRoundRect(cardX, cardY, cardW, cardH, 30, 30);
                g2d.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 38));
                String trophy = "🏆";
                FontMetrics fmT = g2d.getFontMetrics();
                g2d.setColor(new Color(200, 160, 0));
                g2d.drawString(trophy, cardX + (cardW - fmT.stringWidth(trophy)) / 2, cardY + 60);
                g2d.setFont(new Font("Monospaced", Font.BOLD, 42));
                g2d.setColor(winCol);
                FontMetrics fmW = g2d.getFontMetrics();
                String winText = winnerName + " WINS!";
                g2d.drawString(winText, cardX + (cardW - fmW.stringWidth(winText)) / 2, cardY + 130);
                g2d.setFont(new Font("Courier New", Font.PLAIN, 18));
                g2d.setColor(new Color(80, 80, 80));
                FontMetrics fmS = g2d.getFontMetrics();
                String sub = "Press  ⟳  to play again";
                g2d.drawString(sub, cardX + (cardW - fmS.stringWidth(sub)) / 2, cardY + 175);
                g2d.setFont(new Font("Courier New", Font.BOLD, 16));
                g2d.setColor(new Color(60, 60, 60));
                FontMetrics fmSc = g2d.getFontMetrics();
                String scoreStr = "Match Wins  —  P1: " + p1Score + "   P2: " + p2Score;
                g2d.drawString(scoreStr, cardX + (cardW - fmSc.stringWidth(scoreStr)) / 2, cardY + 218);
                g2d.dispose();
            }
        };
        overlayPane.setOpaque(false);
        overlayPane.setBounds(0, 0, 1000, 750);
        overlayPane.setVisible(false);
        add(overlayPane);
    }

    private void setupTopBar() {
        int bw = 40, bh = 26, gap = 6;
        int totalBtnW = bw * 3 + gap * 2;
        int bx = (1000 - totalBtnW) / 2;
        JButton menuBtn = createIconButton("☰", bx, 5, bw, bh);
        menuBtn.addActionListener(e -> showMenuConfirm());
        JButton pauseBtn = createIconButton("Ⅱ", bx + bw + gap, 5, bw, bh);
        pauseBtn.addActionListener(e -> togglePause());
        JButton resetBtn = createIconButton("⟳", bx+(bw+gap)*2, 5, bw, bh);
        resetBtn.addActionListener(e -> resetGame()); // Resets battle only
        topButtons.add(menuBtn);
        topButtons.add(pauseBtn);
        topButtons.add(resetBtn);
        JLabel title = new JLabel("QUIZ BATTLE", SwingConstants.CENTER);
        title.setFont(new Font("Monospaced", Font.BOLD, 24));
        title.setForeground(BORDER_BK);
        title.setBounds(300, 35, 400, 28);
        add(title);
        turnIndicator = new JLabel("▶ PLAYER 1's TURN  (A/S/D)", SwingConstants.CENTER);
        turnIndicator.setFont(new Font("Monospaced", Font.BOLD, 14));
        turnIndicator.setForeground(P1_COL);
        turnIndicator.setBounds(300, 65, 400, 22);
        add(turnIndicator);
    }

    private void togglePause() {
        if (p1Lives <= 0 || p2Lives <= 0 || menuOverlayPane.isVisible()) return;
        isPaused = !isPaused;
        if (isPaused) {
            pauseAlpha = 0f;
            pauseOverlayPane.setVisible(true);
            updateZOrder(pauseOverlayPane);
            if (pauseFadeTimer != null) pauseFadeTimer.stop();
            pauseFadeTimer = new Timer(16, e -> {
                pauseAlpha = Math.min(1f, pauseAlpha + 0.1f);
                pauseOverlayPane.repaint();
                if (pauseAlpha >= 1f) ((Timer)e.getSource()).stop();
            });
            pauseFadeTimer.start();
        } else {
            pauseOverlayPane.setVisible(false);
            pauseAlpha = 0f;
        }
    }

    private void triggerWinOverlay(String winner) {
        winnerName = winner;
        overlayAlpha = 0f;
        overlayPane.setVisible(true);
        updateZOrder(overlayPane);
        isPaused = true;
        if (overlayFadeTimer != null) overlayFadeTimer.stop();
        overlayFadeTimer = new Timer(16, null);
        overlayFadeTimer.addActionListener(e -> {
            overlayAlpha = Math.min(1f, overlayAlpha + 0.04f);
            overlayPane.repaint();
            if (overlayAlpha >= 1f) overlayFadeTimer.stop();
        });
        overlayFadeTimer.start();
    }

    private void updateZOrder(JPanel overlay) {
        setComponentZOrder(overlay, 0);
        for(int i = 0; i < topButtons.size(); i++) {
            setComponentZOrder(topButtons.get(i), 0);
        }
        revalidate();
        repaint();
    }

    private JButton createIconButton(String icon, int x, int y, int w, int h) {
        JButton b = new JButton(icon);
        b.setBounds(x, y, w, h);
        b.setFocusable(false);
        b.setBackground(Color.WHITE);
        b.setBorder(BorderFactory.createLineBorder(BORDER_BK, 2));
        add(b);
        return b;
    }

    private void setupPlayerCards() {
        setupCard(true,  20,  8);
        setupCard(false, 690, 8);
        JLabel p1Banner = new JLabel("P1 MATCHES: 0", SwingConstants.CENTER);
        p1Banner.setFont(new Font("Monospaced", Font.BOLD, 20));
        p1Banner.setForeground(Color.WHITE);
        p1Banner.setBackground(P1_COL);
        p1Banner.setOpaque(true);
        p1Banner.setBounds(20, 143, 290, 32);
        p1Banner.setBorder(BorderFactory.createLineBorder(BORDER_BK, 2));
        p1Banner.setName("p1banner");
        add(p1Banner);
        JLabel p2Banner = new JLabel("P2 MATCHES: 0", SwingConstants.CENTER);
        p2Banner.setFont(new Font("Monospaced", Font.BOLD, 20));
        p2Banner.setForeground(Color.WHITE);
        p2Banner.setBackground(P2_COL);
        p2Banner.setOpaque(true);
        p2Banner.setBounds(690, 143, 290, 32);
        p2Banner.setBorder(BorderFactory.createLineBorder(BORDER_BK, 2));
        p2Banner.setName("p2banner");
        add(p2Banner);
    }

    private void setupCard(boolean isP1, int x, int y) {
        JPanel card = new JPanel(null);
        card.setBounds(x, y, 290, 135);
        card.setBackground(Color.WHITE);
        card.setBorder(BorderFactory.createLineBorder(BORDER_BK, 3));
        JLabel nameLbl = new JLabel(isP1 ? "PLAYER 1" : "PLAYER 2");
        nameLbl.setFont(new Font("Courier New", Font.BOLD, 17));
        nameLbl.setForeground(isP1 ? P1_COL : P2_COL);
        nameLbl.setBounds(10, 5, 170, 22);
        card.add(nameLbl);
        JLabel livLbl = new JLabel("LIVES: " + (isP1 ? p1Lives : p2Lives));
        livLbl.setFont(new Font("Courier New", Font.BOLD, 14));
        livLbl.setBounds(10, 28, 130, 18);
        card.add(livLbl);
        if (isP1) p1LivesLbl = livLbl; else p2LivesLbl = livLbl;
        JLabel hpLbl = new JLabel("HP");
        hpLbl.setFont(new Font("Courier New", Font.BOLD, 12));
        hpLbl.setBounds(10, 50, 30, 15);
        card.add(hpLbl);
        JProgressBar hp = new JProgressBar(0, 100);
        hp.setValue(100);
        hp.setForeground(GREEN_HP);
        hp.setBounds(40, 52, 240, 15);
        hp.setBorder(BorderFactory.createLineBorder(BORDER_BK, 1));
        card.add(hp);
        if (isP1) p1HealthBar = hp; else p2HealthBar = hp;
        JLabel ptsLbl = new JLabel("POINTS: 0");
        ptsLbl.setFont(new Font("Courier New", Font.BOLD, 14));
        ptsLbl.setBounds(10, 70, 150, 18);
        card.add(ptsLbl);
        if (isP1) p1PtsLbl = ptsLbl; else p2PtsLbl = ptsLbl;
        JLabel shLbl = new JLabel("[SHIELD ACTIVE]");
        shLbl.setFont(new Font("Courier New", Font.BOLD, 12));
        shLbl.setForeground(BLUE_ULT);
        shLbl.setBounds(10, 90, 200, 15);
        shLbl.setVisible(false);
        card.add(shLbl);
        if (isP1) p1ShieldLbl = shLbl; else p2ShieldLbl = shLbl;
        JLabel dblLbl = new JLabel("[2X POINTS READY]");
        dblLbl.setFont(new Font("Courier New", Font.BOLD, 12));
        dblLbl.setForeground(new Color(180, 120, 0));
        dblLbl.setBounds(10, 108, 220, 15);
        dblLbl.setVisible(false);
        dblLbl.setName(isP1 ? "p1dbl" : "p2dbl");
        card.add(dblLbl);
        add(card);
    }

    private void setupSkillPanel() {
        JPanel panel = new JPanel(null);
        panel.setBounds(20, 180, 960, 95);
        panel.setBackground(new Color(235, 235, 210));
        panel.setBorder(BorderFactory.createLineBorder(BORDER_BK, 2));
        JLabel lbl = new JLabel("SKILLS  (spend points before answering)");
        lbl.setFont(new Font("Courier New", Font.BOLD, 13));
        lbl.setBounds(8, 5, 500, 17);
        panel.add(lbl);
        int bx = 8, by = 28, bw = 152, bh = 34, gap = 6;
        btnStrike = makeSkillBtn(" ⚡  STRIKE  [3pts]",  bx,             by, bw,   bh, new Color(220, 80,  80));
        btnShield = makeSkillBtn(" 🛡  SHIELD  [4pts]",  bx+bw+gap,     by, bw,   bh, BLUE_ULT);
        btnDouble = makeSkillBtn(" ✨  DOUBLE  [5pts]",  bx+(bw+gap)*2, by, bw,   bh, new Color(180,130,  0));
        btnDrain  = makeSkillBtn(" 🌀  DRAIN   [6pts]",  bx+(bw+gap)*3, by, bw,   bh, new Color( 80,160, 80));
        btnCurse  = makeSkillBtn(" 💀  CURSE   [7pts]",  bx+(bw+gap)*4, by, bw,   bh, new Color(120, 40,120));
        btnLethal = makeSkillBtn(" ☠  LETHAL [15pts]",  bx+(bw+gap)*5, by, bw+4, bh, new Color(140, 20, 20));
        panel.add(btnStrike); panel.add(btnShield); panel.add(btnDouble);
        panel.add(btnDrain);  panel.add(btnCurse);  panel.add(btnLethal);
        btnStrike.addActionListener(e -> useSkill("strike"));
        btnShield.addActionListener(e -> useSkill("shield"));
        btnDouble.addActionListener(e -> useSkill("double"));
        btnDrain .addActionListener(e -> useSkill("drain"));
        btnCurse .addActionListener(e -> useSkill("curse"));
        btnLethal.addActionListener(e -> useSkill("lethal"));
        JLabel note = new JLabel("  Correct answer = +1 pt (or +2 with Double). Wrong = -HP (and -1 pt if pts > 3)");
        note.setFont(new Font("Courier New", Font.PLAIN, 12));
        note.setForeground(new Color(90, 90, 70));
        note.setBounds(8, 66, 940, 15);
        panel.add(note);
        add(panel);
    }

    private JButton makeSkillBtn(String text, int x, int y, int w, int h, Color col) {
        JButton b = new JButton("<html><center>" + text + "</center></html>");
        b.setBounds(x, y, w, h);
        b.setFocusable(false);
        b.setBackground(Color.WHITE);
        b.setForeground(col);
        b.setFont(new Font("Courier New", Font.BOLD, 12));
        b.setBorder(BorderFactory.createLineBorder(col, 2));
        return b;
    }

    private void setupQuestionArea() {
        questionLabel = new JLabel("", SwingConstants.CENTER);
        questionLabel.setBounds(20, 528, 960, 100);
        questionLabel.setOpaque(true);
        questionLabel.setBackground(new Color(235, 235, 215));
        questionLabel.setForeground(BORDER_BK);
        questionLabel.setFont(new Font("Courier New", Font.BOLD, 19));
        questionLabel.setBorder(BorderFactory.createLineBorder(BORDER_BK, 4));
        add(questionLabel);
    }

    private void setupChoiceLabels() {
        int p1x = 20, p2x = 664, y = 288, w = 100, h = 42, gap = 108;
        for (int i = 0; i < 3; i++) {
            p1OptionLabels[i] = createChoiceLabel(p1x + i * gap, y, w, h, P1_COL);
            p2OptionLabels[i] = createChoiceLabel(p2x + i * gap, y, w, h, P2_COL);
        }
    }

    private JLabel createChoiceLabel(int x, int y, int w, int h, Color col) {
        JLabel l = new JLabel("", SwingConstants.CENTER);
        l.setBounds(x, y, w, h);
        l.setBackground(Color.WHITE);
        l.setOpaque(true);
        l.setFont(new Font("Courier New", Font.BOLD, 13));
        l.setBorder(BorderFactory.createLineBorder(col, 2));
        add(l);
        return l;
    }

    private void setupLog() {
        logArea = new JTextArea();
        logArea.setFont(new Font("Courier New", Font.PLAIN, 12));
        logArea.setBackground(new Color(230, 230, 210));
        logArea.setForeground(new Color(60, 60, 40));
        logArea.setEditable(false);
        logArea.setBorder(BorderFactory.createEmptyBorder(4, 6, 4, 6));
        JScrollPane scroll = new JScrollPane(logArea);
        scroll.setBounds(20, 636, 960, 58);
        scroll.setBorder(BorderFactory.createLineBorder(BORDER_BK, 2));
        add(scroll);
    }

    private void log(String msg) {
        logArea.append(msg + "\n");
        logArea.setCaretPosition(logArea.getDocument().getLength());
    }

    private void setupKeyBindings() {
        InputMap im = getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW);
        ActionMap am = getActionMap();
        char[] p1Keys = {'a','s','d'};
        char[] p2Keys = {'j','k','l'};
        for (int i = 0; i < 3; i++) {
            final int idx = i;
            im.put(KeyStroke.getKeyStroke(p1Keys[i]), "p1_" + i);
            am.put("p1_" + i, new AbstractAction() {
                public void actionPerformed(ActionEvent e) { handleInput(idx, true); }
            });
            im.put(KeyStroke.getKeyStroke(p2Keys[i]), "p2_" + i);
            am.put("p2_" + i, new AbstractAction() {
                public void actionPerformed(ActionEvent e) { handleInput(idx, false); }
            });
        }
    }

    private void handleInput(int choice, boolean isP1) {
        if (isPaused || isAttacking || currentQ == null || answered) return;
        if ( isP1 && currentTurn != 0) { log("Wait! It's Player 2's turn."); return; }
        if (!isP1 && currentTurn != 1) { log("Wait! It's Player 1's turn."); return; }
        answered = true;
        boolean correct = (choice == currentQ.correctIndex);
        String pName = isP1 ? "Player 1" : "Player 2";
        if (correct) {
            int pts = 1;
            if ( isP1 && p1Double) { pts = 2; p1Double = false; log("P1 Double triggered!"); }
            if (!isP1 && p2Double) { pts = 2; p2Double = false; log("P2 Double triggered!"); }
            if (isP1) p1Pts += pts; else p2Pts += pts;
            boolean blocked = false;
            if ( isP1 && p2Shield) { p2Shield=false; blocked=true; log("P2's Shield blocked!"); }
            if (!isP1 && p1Shield) { p1Shield=false; blocked=true; log("P1's Shield blocked!"); }
            if (!blocked) {
                if (isP1) { p2HP -= 20; triggerAttackAnim(true); }
                else       { p1HP -= 20; triggerAttackAnim(false); }
            }
            log(pName + " CORRECT! +" + pts + " pt(s).");
        } else {
            if (isP1) p1HP -= 10; else p2HP -= 10;
            if ( isP1 && p1Pts > POINT_PENALTY_THRESHOLD) { p1Pts = Math.max(0,p1Pts-1); log("P1 penalty: -1 pt!"); }
            if (!isP1 && p2Pts > POINT_PENALTY_THRESHOLD) { p2Pts = Math.max(0,p2Pts-1); log("P2 penalty: -1 pt!"); }
            log(pName + " WRONG! -10 HP.");
        }
        checkKnockouts();
        refreshUI();
        if (p1Lives > 0 && p2Lives > 0) {
            Timer delay = new Timer(600, ev -> {
                currentTurn = 1 - currentTurn;
                refreshQuestion();
                refreshUI();
                answered = false;
            });
            delay.setRepeats(false);
            delay.start();
        }
    }

    private void useSkill(String skill) {
        if (isPaused) return;
        boolean isP1 = (currentTurn == 0);
        int pts = isP1 ? p1Pts : p2Pts;
        String pName = isP1 ? "Player 1" : "Player 2";
        int cost = switch (skill) {
            case "strike" -> 3; case "shield" -> 4; case "double" -> 5;
            case "drain"  -> 6; case "curse"  -> 7; case "lethal" -> 15;
            default -> 999;
        };
        if (pts < cost) { log("Need " + cost + " pts."); return; }
        if (isP1) p1Pts -= cost; else p2Pts -= cost;
        switch (skill) {
            case "strike" -> {
                boolean blocked = false;
                if ( isP1 && p2Shield) { p2Shield=false; blocked=true; log("P2 Shield blocked Strike!"); }
                if (!isP1 && p1Shield) { p1Shield=false; blocked=true; log("P1 Shield blocked Strike!"); }
                if (!blocked) {
                    if (isP1) { p2HP-=20; triggerAttackAnim(true); }
                    else       { p1HP-=20; triggerAttackAnim(false); }
                    log(pName + " STRIKE! -20 HP.");
                }
            }
            case "shield" -> { if(isP1) p1Shield=true; else p2Shield=true; log(pName+" SHIELD activated!"); }
            case "double" -> { if(isP1) p1Double=true; else p2Double=true; log(pName+" DOUBLE POINTS activated!"); }
            case "drain"  -> {
                if(isP1){ p2HP-=20; p1HP=Math.min(100,p1HP+20); triggerAttackAnim(true); }
                else     { p1HP-=20; p2HP=Math.min(100,p2HP+20); triggerAttackAnim(false); }
                log(pName+" HP DRAIN! Stole 20 HP.");
            }
            case "curse"  -> { if(isP1) p2Pts=Math.max(0,p2Pts-2); else p1Pts=Math.max(0,p1Pts-2); log(pName+" CURSE! Opponent -2 pts."); }
            case "lethal" -> { if(isP1){ p2HP=0; p2Pts=0; triggerAttackAnim(true); } else { p1HP=0; p1Pts=0; triggerAttackAnim(false);
                } log("☠ " + pName + " LETHAL BLOW! Instant KO!"); }
        }
        checkKnockouts();
        refreshUI();
    }

    private void checkKnockouts() {
        if (p1HP <= 0) { p1Lives--; p1HP = 100; log("P1 KO! Lives: " + p1Lives); }
        if (p2HP <= 0) { p2Lives--; p2HP = 100; log("P2 KO! Lives: " + p2Lives); }
        if (p1Lives <= 0) { p2Score++; log(">>> PLAYER 2 WINS! <<<"); triggerWinOverlay("PLAYER 2"); answered = true; }
        else if (p2Lives <= 0) { p1Score++; log(">>> PLAYER 1 WINS! <<<"); triggerWinOverlay("PLAYER 1"); answered = true; }
    }

    private void refreshUI() {
        p1HealthBar.setValue(p1HP);
        p2HealthBar.setValue(p2HP);
        p1HealthBar.setForeground(p1HP>50?GREEN_HP:p1HP>25?new Color(230,160,20):new Color(200,50,50));
        p2HealthBar.setForeground(p2HP>50?GREEN_HP:p2HP>25?new Color(230,160,20):new Color(200,50,50));
        p1LivesLbl.setText("LIVES: " + "❤".repeat(Math.max(0,p1Lives)) + " (" + p1Lives + ")");
        p2LivesLbl.setText("LIVES: " + "❤".repeat(Math.max(0,p2Lives)) + " (" + p2Lives + ")");
        p1PtsLbl.setText("POINTS: " + p1Pts);
        p2PtsLbl.setText("POINTS: " + p2Pts);
        p1ShieldLbl.setVisible(p1Shield);
        p2ShieldLbl.setVisible(p2Shield);
        for (Component c : getComponents()) {
            if ("p1banner".equals(c.getName())) ((JLabel)c).setText("P1 MATCHES: " + p1Score);
            if ("p2banner".equals(c.getName())) ((JLabel)c).setText("P2 MATCHES: " + p2Score);
        }
        if (currentTurn == 0) { turnIndicator.setText("▶ PLAYER 1's TURN  (A/S/D)"); turnIndicator.setForeground(P1_COL); }
        else { turnIndicator.setText("▶ PLAYER 2's TURN  (J/K/L)"); turnIndicator.setForeground(P2_COL); }
        int ap = (currentTurn==0) ? p1Pts : p2Pts;
        btnStrike.setEnabled(ap>=3); btnShield.setEnabled(ap>=4); btnDouble.setEnabled(ap>=5);
        btnDrain .setEnabled(ap>=6); btnCurse .setEnabled(ap>=7); btnLethal.setEnabled(ap>=15);
    }

    private void refreshQuestion() {
        if (p1Lives<=0 || p2Lives<=0) return;
        currentQ = qm.next();
        if (currentQ != null) {
            String html = "<html><center>[" + currentQ.topic.toUpperCase() + " | " + currentQ.difficulty.toUpperCase() + "]<br>" + currentQ.text + "</center></html>";
            questionLabel.setText(html);
            for (int i = 0; i < 3; i++) {
                String opt = (i==0?"A":i==1?"S/K":"D/L") + ": " + currentQ.options[i];
                p1OptionLabels[i].setText("<html><center>" + opt + "</center></html>");
                p2OptionLabels[i].setText("<html><center>" + opt + "</center></html>");
            }
        }
    }

    private void triggerAttackAnim(boolean p1Attacks) { isAttacking=true; p1IsAttacking=p1Attacks; p2IsAttacking=!p1Attacks; animFrame=0; }

    private void resetGame() {
        p1Lives=3; p2Lives=3; p1HP=100; p2HP=100; p1Pts=0; p2Pts=0;
        p1Shield=false; p2Shield=false; p1Double=false; p2Double=false;
        currentTurn=0; answered=false;
        overlayAlpha=0f; overlayPane.setVisible(false);
        pauseAlpha=0f; pauseOverlayPane.setVisible(false);
        menuAlpha=0f; menuOverlayPane.setVisible(false);
        isPaused=false;
        p1X=P1_HOME_X; p2X=P2_HOME_X;
        qm.reset(); refreshUI(); refreshQuestion();
        log("--- Battle Reset ---");
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (!isPaused && isAttacking) {
            animFrame++;
            if (p1IsAttacking) { if (animFrame < 10) p1X += 30; else if (animFrame < 20) p1X -= 30; else finishAttack();
            }
            else if (p2IsAttacking) { if (animFrame < 10) p2X -= 30; else if (animFrame < 20) p2X += 30;
            else finishAttack(); }
        }
        repaint();
    }

    private void finishAttack() { isAttacking=false; p1IsAttacking=false; p2IsAttacking=false; animFrame=0; p1X=P1_HOME_X; p2X=P2_HOME_X; }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2d.setColor(new Color(20,20,20,100));
        g2d.fillOval(P1_HOME_X-40, SPRITE_Y+100, 200, 42);
        g2d.fillOval(P2_HOME_X-40, SPRITE_Y+100, 200, 42);
        if (p1Sprite != null) g2d.drawImage(p1Sprite, p1X,      SPRITE_Y,  120, 140, null);
        if (p2Sprite != null) g2d.drawImage(p2Sprite, p2X+120,  SPRITE_Y, -120, 140, null);
        if (p1Sprite == null) { g2d.setColor(P1_COL); g2d.fillRoundRect(p1X+20, SPRITE_Y+10, 80,120,14,14); }
        if (p2Sprite == null) { g2d.setColor(P2_COL); g2d.fillRoundRect(p2X+20, SPRITE_Y+10, 80,120,14,14); }
    }
}
