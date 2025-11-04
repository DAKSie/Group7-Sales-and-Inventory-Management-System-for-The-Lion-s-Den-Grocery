package View.Utils;

import java.awt.*;
import javax.swing.*;


public class BetterInputs extends JTextField {
    public BetterInputs(int posX, int posY, String name, String text) {
        super(text);
        setName(name);
        setMaximumSize(new Dimension(140, 36));
        setAlignmentX(CENTER_ALIGNMENT);
        setBackground(new Color(255, 255, 255));
        setForeground(Color.BLACK);
        setFont(new Font("Segoe UI", Font.PLAIN, 14));
        setBorder(BorderFactory.createLineBorder(new Color(0, 122, 255), 1, true));
        setBounds(posX, posY, 100, 20);
    }
}
