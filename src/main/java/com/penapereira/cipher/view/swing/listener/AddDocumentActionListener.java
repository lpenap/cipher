package com.penapereira.cipher.view.swing.listener;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
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

public class AddDocumentActionListener extends AbstractActionListener {

    private static final Logger log = LoggerFactory.getLogger(AddDocumentActionListener.class);

    private static final long serialVersionUID = 1L;
    private JTextField documentTitleTextField;

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

        JPanel formPanel = new JPanel();
        getContentPane().add(formPanel, BorderLayout.CENTER);
        formPanel.setLayout(new FormLayout(
                new ColumnSpec[] {FormSpecs.RELATED_GAP_COLSPEC, FormSpecs.DEFAULT_COLSPEC,
                    FormSpecs.RELATED_GAP_COLSPEC, FormSpecs.DEFAULT_COLSPEC, FormSpecs.RELATED_GAP_COLSPEC,
                    ColumnSpec.decode("default:grow"),},
                new RowSpec[] {FormSpecs.RELATED_GAP_ROWSPEC, FormSpecs.DEFAULT_ROWSPEC, FormSpecs.RELATED_GAP_ROWSPEC,
                    FormSpecs.DEFAULT_ROWSPEC,}));

        JLabel lblDocumentTitle = new JLabel(messages.getDocumentTitle());
        formPanel.add(lblDocumentTitle, "4, 4, right, default");

        documentTitleTextField = new JTextField();
        formPanel.add(documentTitleTextField, "6, 4, fill, default");
        documentTitleTextField.setColumns(10);
    }

    protected void addDocument() {
        String title = new StringUtil().sanitizeString(documentTitleTextField.getText());
        log.info("Adding new document with title: " + title);
        documentController.createDocument(title, "");
        this.setVisible(false);
    }

    /**
     * This will show the dialog wrapped by this class when the action is triggered in the related component.
     */
    @Override
    public void actionPerformed(ActionEvent e) {
        this.setVisible(true);
        documentTitleTextField.setRequestFocusEnabled(true);
    }

}
