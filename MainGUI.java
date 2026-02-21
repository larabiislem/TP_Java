import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.Arrays;

public class MainGUI extends JFrame {

    private JTextArea rulesArea;
    private JTextField factsField;
    private JTextField goalField;
    private JTextArea resultArea;

    // ===== COULEURS DU THÈME =====
    private Color bgDark      = new Color(30, 30, 46);
    private Color bgPanel     = new Color(40, 42, 58);
    private Color bgInput     = new Color(50, 52, 70);
    private Color bgResult    = new Color(24, 24, 37);
    private Color accent      = new Color(137, 180, 250);
    private Color accentGreen = new Color(166, 227, 161);
    private Color accentRed   = new Color(243, 139, 168);
    private Color accentGold  = new Color(249, 226, 175);
    private Color textColor   = new Color(205, 214, 244);
    private Color textDim     = new Color(147, 153, 178);

    public MainGUI() {
        setTitle("🧠 Système Expert — Chaînage Avant");
        setSize(850, 750);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        getContentPane().setBackground(bgDark);

        // ===== PANNEAU PRINCIPAL =====
        JPanel main = new JPanel(new BorderLayout(12, 12));
        main.setBackground(bgDark);
        main.setBorder(new EmptyBorder(20, 20, 20, 20));

        // ===== TITRE =====
        JLabel title = new JLabel("🧠  Système Expert — Chaînage Avant", JLabel.CENTER);
        title.setFont(new Font("Segoe UI", Font.BOLD, 24));
        title.setForeground(accent);
        title.setBorder(new EmptyBorder(0, 0, 10, 0));
        main.add(title, BorderLayout.NORTH);

        // ===== CENTRE : Règles + Entrées =====
        JPanel center = new JPanel(new BorderLayout(10, 10));
        center.setBackground(bgDark);

        // --- Zone des règles ---
        JPanel rulesPanel = createStyledPanel("📜  Règles  (une par ligne : A,B -> F)");
        rulesArea = createTextArea(8, true);
        rulesPanel.add(new JScrollPane(rulesArea) {{
            setBorder(null);
            getViewport().setBackground(bgInput);
        }}, BorderLayout.CENTER);
        center.add(rulesPanel, BorderLayout.CENTER);

        // --- Faits + Objectif ---
        JPanel inputGrid = new JPanel(new GridLayout(2, 1, 8, 8));
        inputGrid.setBackground(bgDark);

        // Faits
        JPanel factsPanel = new JPanel(new BorderLayout(10, 0));
        factsPanel.setBackground(bgPanel);
        factsPanel.setBorder(new EmptyBorder(10, 15, 10, 15));
        JLabel factsLabel = createLabel("📦  Faits initiaux :");
        factsField = createTextField();
        factsPanel.add(factsLabel, BorderLayout.WEST);
        factsPanel.add(factsField, BorderLayout.CENTER);
        addRoundBorder(factsPanel);

        // Objectif
        JPanel goalPanel = new JPanel(new BorderLayout(10, 0));
        goalPanel.setBackground(bgPanel);
        goalPanel.setBorder(new EmptyBorder(10, 15, 10, 15));
        JLabel goalLabel = createLabel("🎯  Objectif :           ");
        goalField = createTextField();
        goalPanel.add(goalLabel, BorderLayout.WEST);
        goalPanel.add(goalField, BorderLayout.CENTER);
        addRoundBorder(goalPanel);

        inputGrid.add(factsPanel);
        inputGrid.add(goalPanel);
        center.add(inputGrid, BorderLayout.SOUTH);

        main.add(center, BorderLayout.CENTER);

        // ===== BAS : Boutons + Résultats =====
        JPanel south = new JPanel(new BorderLayout(10, 10));
        south.setBackground(bgDark);

        // --- Boutons ---
        JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 8));
        btnPanel.setBackground(bgDark);

        JButton exampleBtn = createButton("📋  Exemple", accentGold, bgDark);
        JButton solveBtn   = createButton("▶  Résoudre", bgDark, accentGreen);
        JButton clearBtn   = createButton("🗑  Effacer", bgDark, accentRed);

        btnPanel.add(exampleBtn);
        btnPanel.add(solveBtn);
        btnPanel.add(clearBtn);
        south.add(btnPanel, BorderLayout.NORTH);

        // --- Résultats ---
        JPanel resultPanel = createStyledPanel("📊  Résultats");
        resultArea = createTextArea(14, false);
        resultArea.setBackground(bgResult);
        resultArea.setForeground(accentGreen);
        resultPanel.add(new JScrollPane(resultArea) {{
            setBorder(null);
            getViewport().setBackground(bgResult);
        }}, BorderLayout.CENTER);
        south.add(resultPanel, BorderLayout.CENTER);

        main.add(south, BorderLayout.SOUTH);

        // ===== ACTIONS =====
        exampleBtn.addActionListener(e -> loadExample());
        solveBtn.addActionListener(e -> solve());
        clearBtn.addActionListener(e -> clearAll());

        add(main);
        setVisible(true);
    }

    // ===== CRÉER UN PANNEAU STYLISÉ =====
    private JPanel createStyledPanel(String title) {
        JPanel panel = new JPanel(new BorderLayout(5, 5));
        panel.setBackground(bgPanel);
        panel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(60, 62, 80), 1, true),
            BorderFactory.createCompoundBorder(
                new EmptyBorder(8, 12, 8, 12),
                BorderFactory.createEmptyBorder()
            )
        ));

        // Titre du panneau
        JLabel lbl = new JLabel("  " + title);
        lbl.setFont(new Font("Segoe UI", Font.BOLD, 13));
        lbl.setForeground(accent);
        lbl.setBorder(new EmptyBorder(0, 0, 5, 0));
        panel.add(lbl, BorderLayout.NORTH);

        return panel;
    }

    // ===== CRÉER UN TEXTAREA =====
    private JTextArea createTextArea(int rows, boolean editable) {
        JTextArea ta = new JTextArea(rows, 40);
        ta.setFont(new Font("JetBrains Mono", Font.PLAIN, 14));
        ta.setBackground(bgInput);
        ta.setForeground(textColor);
        ta.setCaretColor(accent);
        ta.setEditable(editable);
        ta.setMargin(new Insets(10, 10, 10, 10));
        ta.setBorder(null);
        return ta;
    }

    // ===== CRÉER UN TEXTFIELD =====
    private JTextField createTextField() {
        JTextField tf = new JTextField();
        tf.setFont(new Font("JetBrains Mono", Font.PLAIN, 14));
        tf.setBackground(bgInput);
        tf.setForeground(textColor);
        tf.setCaretColor(accent);
        tf.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(60, 62, 80)),
            new EmptyBorder(5, 10, 5, 10)
        ));
        return tf;
    }

    // ===== CRÉER UN LABEL =====
    private JLabel createLabel(String text) {
        JLabel lbl = new JLabel(text);
        lbl.setFont(new Font("Segoe UI", Font.BOLD, 13));
        lbl.setForeground(textDim);
        return lbl;
    }

    // ===== CRÉER UN BOUTON =====
    private JButton createButton(String text, Color fg, Color bg) {
        JButton btn = new JButton(text);
        btn.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btn.setForeground(fg);
        btn.setBackground(bg);
        btn.setFocusPainted(false);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btn.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(fg.darker(), 2, true),
            new EmptyBorder(10, 28, 10, 28)
        ));

        // Effet hover
        btn.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) {
                btn.setBackground(fg.darker());
                btn.setForeground(Color.WHITE);
            }
            public void mouseExited(MouseEvent e) {
                btn.setBackground(bg);
                btn.setForeground(fg);
            }
        });

        return btn;
    }

    // ===== BORDURE ARRONDIE =====
    private void addRoundBorder(JPanel panel) {
        panel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(60, 62, 80), 1, true),
            new EmptyBorder(10, 15, 10, 15)
        ));
    }

    // ===== CHARGER L'EXEMPLE =====
    private void loadExample() {
        rulesArea.setText(
            "A,B -> F\n" +
            "F,H -> I\n" +
            "D,H,G -> A\n" +
            "O,G -> H\n" +
            "E,H -> B\n" +
            "G,A -> B\n" +
            "G,H -> P\n" +
            "G,H -> O\n" +
            "D,O,G -> J"
        );
        factsField.setText("D,O,G");
        goalField.setText("I");
        resultArea.setText("");
    }

    // ===== EFFACER =====
    private void clearAll() {
        rulesArea.setText("");
        factsField.setText("");
        goalField.setText("");
        resultArea.setText("");
    }

    // ===== RÉSOUDRE =====
    private void solve() {
        try {
            // Parser les règles
            String[] lines = rulesArea.getText().trim().split("\n");
            Rule[] bdr = new Rule[lines.length];

            for (int i = 0; i < lines.length; i++) {
                String line = lines[i].trim();
                if (!line.contains("->")) {
                    resultArea.setText("❌ Erreur ligne " + (i + 1) + " : format attendu A,B -> F");
                    return;
                }
                String[] parts = line.split("->");
                ArrayList<String> p = new ArrayList<>();
                for (String s : parts[0].trim().split(",")) p.add(s.trim());
                ArrayList<String> c = new ArrayList<>();
                for (String s : parts[1].trim().split(",")) c.add(s.trim());
                bdr[i] = new Rule(i, p, c);
            }

            // Parser les faits
            ArrayList<String> bdp = new ArrayList<>();
            for (String s : factsField.getText().trim().split(",")) {
                String fact = s.trim();
                if (!fact.isEmpty()) bdp.add(fact);
            }

            // Objectif
            String goal = goalField.getText().trim();
            if (goal.isEmpty()) {
                resultArea.setText("❌ Entrez un objectif !");
                return;
            }

            // Lancer le moteur
            ArrayList<String> log = Engine.forward(bdr, bdp, goal);

            // Afficher
            StringBuilder sb = new StringBuilder();
            for (String l : log) sb.append(l).append("\n");
            resultArea.setText(sb.toString());
            resultArea.setCaretPosition(0);

        } catch (Exception ex) {
            resultArea.setText("❌ Erreur : " + ex.getMessage());
        }
    }

    // ===== POINT D'ENTRÉE =====
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new MainGUI());
    }
}