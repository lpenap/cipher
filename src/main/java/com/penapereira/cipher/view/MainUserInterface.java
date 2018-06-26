package com.penapereira.cipher.view;

import java.util.Observable;

public interface MainUserInterface {

    boolean init();

    void launch();

    String getUserInterfaceName();

    void update(Observable o, Object arg);

}
