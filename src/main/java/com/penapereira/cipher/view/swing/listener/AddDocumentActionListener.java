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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.jgoodies.forms.layout.ColumnSpec;
import com.jgoodies.forms.layout.FormLayout;
import com.jgoodies.forms.layout.FormSpecs;
import com.jgoodies.forms.layout.RowSpec;
import com.penapereira.cipher.conf.Messages;
import com.penapereira.cipher.controller.DocumentController;
import com.penapereira.cipher.shared.StringUtil;
import com.penapereira.cipher.view.swing.component.JTextFieldLimit;

public class AddDocumentActionListener extends AbstractActionListener {

    private static final long serialVersionUID = 1L;
    private final Logger log = LoggerFactory.getLogger(AddDocumentActionListener.class);
    public final static int MAX_INPUT_SIZE = 20;
    private JTextField documentTitleTextField;
    private JLabel lblErrorMessage;

    public AddDocumentActionListener(DocumentController documentController, Messages messages) {
        super(documentController, messages);
    }

    public void build() {
        setResizable(false);
        setSize(427, 172);
        setTitle(messages.getAddDocumentMenu());
        getContentPane().setLayout(new BorderLayout(0, 0));

        JPanel buttonPanel = new JPanel();
        buttonPanel.setAlignmentX(CENTER_ALIGNMENT);
        getContentPane().add(buttonPanel, BorderLayout.SOUTH);

        addButtons(buttonPanel);
        addFormPanel();
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

        JLabel lblDocumentTitle = new JLabel(messages.getDocumentTitle());
        formPanel.add(lblDocumentTitle, "4, 4, right, default");

        documentTitleTextField = new JTextField();
        formPanel.add(documentTitleTextField, "6, 4, left, default");
        documentTitleTextField.setDocument(new JTextFieldLimit(MAX_INPUT_SIZE));
        documentTitleTextField.setColumns(MAX_INPUT_SIZE);
        documentTitleTextField.addFocusListener(new FocusListener() {

            @Override
            public void focusLost(FocusEvent e) {}

            @Override
            public void focusGained(FocusEvent e) {
                lblErrorMessage.setVisible(false);
            }
        });

        lblErrorMessage = new JLabel(messages.getDocumentTitleEmpty());
        lblErrorMessage.setForeground(Color.RED);
        formPanel.add(lblErrorMessage, "6, 6");
        lblErrorMessage.setVisible(false);
    }

    protected void addButtons(JPanel buttonPanel) {
        JButton btnCreate = new JButton(messages.getCreate());
        btnCreate.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                addDocument();
            }
        });
        buttonPanel.add(btnCreate);

        JButton btnCancel = new JButton(messages.getCancel());
        btnCancel.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                setVisible(false);
            }
        });
        buttonPanel.add(btnCancel);
    }

    protected void addDocument() {
        String title = new StringUtil().sanitizeString(documentTitleTextField.getText());
        if (!title.equals("")) {
            log.info("Adding new empty document with title: " + title);
            documentController.createAndSaveDocument(title, "");
            this.setVisible(false);
        } else {
            lblErrorMessage.setVisible(true);
        }
    }

    /**
     * This will show the dialog wrapped by this class when the action is triggered in the related component.
     */
    @Override
    public void actionPerformed(ActionEvent e) {
        lblErrorMessage.setVisible(false);
        this.setVisible(true);
        documentTitleTextField.setRequestFocusEnabled(true);
    }

}
