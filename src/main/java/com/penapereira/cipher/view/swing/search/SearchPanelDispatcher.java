package com.penapereira.cipher.view.swing.search;

import java.awt.KeyEventDispatcher;
import java.awt.event.KeyEvent;

public class SearchPanelDispatcher implements KeyEventDispatcher {

    private final SearchPanel searchPanel;

    public SearchPanelDispatcher(SearchPanel searchPanel) {
        this.searchPanel = searchPanel;
    }

    @Override
    public boolean dispatchKeyEvent(KeyEvent e) {
        int onMask = KeyEvent.CTRL_DOWN_MASK;
        int offMask = KeyEvent.SHIFT_DOWN_MASK | KeyEvent.ALT_DOWN_MASK;

        if ((e.getKeyCode() == KeyEvent.VK_F) && ((e.getModifiersEx() & (onMask | offMask)) == onMask)) {
            showSearchPanel();
            return true;
        }

        if ((e.getKeyCode() == KeyEvent.VK_ESCAPE) && searchPanel.isVisible()) {
            hideSearchPanel();
            return true;
        }

        return false;
    }

    protected void hideSearchPanel() {
        searchPanel.close();
    }

    protected void showSearchPanel() {
        searchPanel.setVisible(true);
        searchPanel.requestFocus();
    }
}
