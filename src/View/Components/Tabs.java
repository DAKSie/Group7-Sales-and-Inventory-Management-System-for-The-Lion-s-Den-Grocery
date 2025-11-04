package View.Components;

import javax.swing.*;

public class Tabs {
    private String _currentUser;

    public Tabs(String currentUser) {
        this._currentUser = currentUser;
    }

    public JPanel SalesManager() {
        return new SalesManagerPanel(_currentUser);
    }
}
