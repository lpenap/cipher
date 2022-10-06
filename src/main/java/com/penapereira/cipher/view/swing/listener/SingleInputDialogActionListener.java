package com.penapereira.cipher.view.swing.listener;

import com.penapereira.cipher.conf.Messages;
import com.penapereira.cipher.controller.DocumentController;
import com.penapereira.cipher.util.StringUtil;
import com.penapereira.cipher.view.swing.component.JTextFieldLimit;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;

public abstract class SingleInputDialogActionListener extends AbstractActionListener {

    private static final long serialVersionUID = 1L;
    private final static int MAX_INPUT_SIZE = 40;
    private JTextField textField;
    private JLabel lblErrorMessage;
    private JLabel lblTextField;
    private JButton btnOk;

    public SingleInputDialogActionListener(DocumentController documentController, Messages messages) {
        super(documentController, messages);
    }

    protected abstract void actionPerformedDelegate(ActionEvent e);

    protected abstract void buildDelegate();

    protected abstract void okButtonPressedDelegate(String text);

    /**
     * This will show the dialog wrapped by this class when the 'Add Document' action is triggered in the parent
     * component.
     */
    @Override
    public void actionPerformed(ActionEvent e) {
        lblErrorMessage.setVisible(false);
        setVisible(true);
        actionPerformedDelegate(e);
        checkTextField();
    }

    protected void checkTextField() {
        textField.setRequestFocusEnabled(true);
        textField.requestFocusInWindow();
        if (!textField.getText().isEmpty()) {
            textField.setSelectionStart(0);
            textField.setSelectionEnd(textField.getText().length());
        }
    }

    @Override
    protected void build() {
        setResizable(false);
        setSize(427, 120);
        getContentPane().setLayout(new BorderLayout(2, 2));

        final JPanel buttonPanel = new JPanel();
        buttonPanel.setAlignmentX(CENTER_ALIGNMENT);
        getContentPane().add(buttonPanel, BorderLayout.SOUTH);
        addButtons(buttonPanel);

        final JPanel contentPanel = new JPanel();
        GridLayout layout = new GridLayout(0, 2, 10 , 3 );
        contentPanel.setLayout(layout);
        getContentPane().add(contentPanel, BorderLayout.CENTER);
        contentPanel.setBorder(new EmptyBorder(5, 1, 1, 1));
        addFormPanel(contentPanel);

        buildDelegate();
    }

    protected void addButtons(JPanel buttonPanel) {
        btnOk = new JButton();
        btnOk.addActionListener(e -> okButtonPressed());
        buttonPanel.add(btnOk);

        JButton btnCancel = new JButton(messages.getCancel());
        btnCancel.addActionListener(e -> setVisible(false));
        buttonPanel.add(btnCancel);
    }

    protected void addFormPanel(JPanel contentPanel) {
        lblTextField = new JLabel();
        contentPanel.add(lblTextField);

        textField = new JTextField();
        contentPanel.add(textField);
        textField.setDocument(new JTextFieldLimit(MAX_INPUT_SIZE));
        textField.setColumns(MAX_INPUT_SIZE);
        textField.addFocusListener(new FocusListener() {
            @Override
            public void focusLost(FocusEvent e) {}

            @Override
            public void focusGained(FocusEvent e) {
                lblErrorMessage.setVisible(false);
            }
        });

        lblErrorMessage = new JLabel();
        lblErrorMessage.setForeground(Color.RED);
        contentPanel.add(lblErrorMessage, "6, 6");
        lblErrorMessage.setVisible(false);
    }

    protected void okButtonPressed() {
        String text = new StringUtil().sanitizeString(textField.getText());
        if (!text.equals("")) {
            okButtonPressedDelegate(text);
            this.setVisible(false);
        } else {
            lblErrorMessage.setVisible(true);
        }
    }

    protected void setOkButtonText(String text) {
        btnOk.setText(text);
    }

    protected void setTextFieldString(String text) {
        textField.setText(text);
    }

    protected void setErrorText(String text) {
        lblErrorMessage.setText(text);
    }

    protected void setTextFieldLabel(String text) {
        lblTextField.setText(text);
    }
}
