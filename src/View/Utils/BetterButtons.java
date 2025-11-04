package View.Utils;

import java.awt.*;
import javax.swing.*;

public class BetterButtons extends JButton {
    public BetterButtons(int posX, int posY, String name, String text) {
        super(text);
        setName(name);
        setAlignmentX(CENTER_ALIGNMENT);
        setBackground(new Color(0, 122, 255));
        setForeground(Color.WHITE);
        setFont(new Font("Segoe UI", Font.BOLD, 14));
        setFocusPainted(false);
        setBorder(BorderFactory.createLineBorder(new Color(0, 122, 255), 1, true));
        setCursor(new Cursor(Cursor.HAND_CURSOR));
        setBounds(posX, posY, 100, 30);
    }
}
