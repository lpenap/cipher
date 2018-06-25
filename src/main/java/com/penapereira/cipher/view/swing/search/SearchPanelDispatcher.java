package com.penapereira.cipher.view.swing.search;

import java.awt.KeyEventDispatcher;
import java.awt.event.KeyEvent;

public class SearchPanelDispatcher<P> implements KeyEventDispatcher {

    private SearchPanel<P> searchPanel;

    public SearchPanelDispatcher(SearchPanel<P> searchPanel) {
        this.searchPanel = searchPanel;
    }

    @Override
    public boolean dispatchKeyEvent(KeyEvent e) {
        int onmask = KeyEvent.CTRL_DOWN_MASK;
        int offmask = KeyEvent.SHIFT_DOWN_MASK | KeyEvent.ALT_DOWN_MASK;

        if ((e.getKeyCode() == KeyEvent.VK_F) && ((e.getModifiersEx() & (onmask | offmask)) == onmask)) {
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
        searchPanel.setVisible(false);
    }

    protected void showSearchPanel() {
        searchPanel.setVisible(true);
        searchPanel.getSearchTextField().requestFocusInWindow();
    }

}