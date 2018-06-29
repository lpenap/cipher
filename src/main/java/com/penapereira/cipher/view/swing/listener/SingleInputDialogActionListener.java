package com.penapereira.cipher.view.swing.listener;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import com.jgoodies.forms.layout.ColumnSpec;
import com.jgoodies.forms.layout.FormLayout;
import com.jgoodies.forms.layout.FormSpecs;
import com.jgoodies.forms.layout.RowSpec;
import com.penapereira.cipher.conf.Messages;
import com.penapereira.cipher.controller.DocumentController;
import com.penapereira.cipher.shared.StringUtil;
import com.penapereira.cipher.view.swing.component.JTextFieldLimit;

public abstract class SingleInputDialogActionListener extends AbstractActionListener {

    private static final long serialVersionUID = 1L;
    private final static int MAX_INPUT_SIZE = 20;
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
        setSize(427, 172);
        getContentPane().setLayout(new BorderLayout(0, 0));

        JPanel buttonPanel = new JPanel();
        buttonPanel.setAlignmentX(CENTER_ALIGNMENT);
        getContentPane().add(buttonPanel, BorderLayout.SOUTH);

        addButtons(buttonPanel);
        addFormPanel();
        buildDelegate();
    }

    protected void addButtons(JPanel buttonPanel) {
        btnOk = new JButton();
        btnOk.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                okButtonPressed();
            }
        });
        buttonPanel.add(btnOk);

        JButton btnCancel = new JButton(messages.getCancel());
        btnCancel.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                setVisible(false);
            }
        });
        buttonPanel.add(btnCancel);
    }

    protected void addFormPanel() {
        JPanel formPanel = new JPanel();
        getContentPane().add(formPanel, BorderLayout.CENTER);
        formPanel.setLayout(new FormLayout(
                new ColumnSpec[] {FormSpecs.RELATED_GAP_COLSPEC, FormSpecs.DEFAULT_COLSPEC,
                    FormSpecs.RELATED_GAP_COLSPEC, FormSpecs.DEFAULT_COLSPEC, FormSpecs.RELATED_GAP_COLSPEC,
                    ColumnSpec.decode("default:grow"),},
                new RowSpec[] {FormSpecs.RELATED_GAP_ROWSPEC, FormSpecs.DEFAULT_ROWSPEC, FormSpecs.RELATED_GAP_ROWSPEC,
                    FormSpecs.DEFAULT_ROWSPEC, FormSpecs.RELATED_GAP_ROWSPEC, FormSpecs.DEFAULT_ROWSPEC,}));

        lblTextField = new JLabel();
        formPanel.add(lblTextField, "4, 4, right, default");

        textField = new JTextField();
        formPanel.add(textField, "6, 4, left, default");
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
        formPanel.add(lblErrorMessage, "6, 6");
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

    protected String getTextFieldString() {
        return textField.getText();
    }

    protected void setErrorText(String text) {
        lblErrorMessage.setText(text);
    }

    protected JTextField getTextField() {
        return textField;
    }

    protected void setTextFieldLabel(String text) {
        lblTextField.setText(text);
    }

}
