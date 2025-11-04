package View.Utils;

import java.awt.Font;
import javax.swing.*;

public class BetterLabels extends JLabel {
    public BetterLabels(int posX, int posY, String text) {
        super(text);
        setBounds(posX, posY, 100, 20);
        setFont(new Font("Segoe UI", Font.BOLD, 14));
    }
}
