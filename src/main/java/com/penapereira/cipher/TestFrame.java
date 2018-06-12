package com.penapereira.cipher;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class TestFrame extends JFrame {
    
    protected Configuration config;

    public void alert() {
        JOptionPane.showMessageDialog(null, "Hello");
//        try {
//            Thread.sleep(2000);
//        } catch (InterruptedException e) {
//            // TODO Auto-generated catch block
//            e.printStackTrace();
//        }
//        System.exit(0);
    }
    
    @Autowired
    public TestFrame(Configuration config) {
        super();
        this.config = config;
        JLabel label = new JLabel(config.getPropertiesFile());
        this.add(label);
    }
}
