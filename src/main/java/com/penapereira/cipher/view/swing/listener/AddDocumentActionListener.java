package com.penapereira.cipher.view.swing.listener;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JFrame;
import com.penapereira.cipher.conf.Messages;
import com.penapereira.cipher.controller.DocumentController;
import java.awt.BorderLayout;
import javax.swing.JPanel;
import java.awt.GridBagLayout;
import javax.swing.JButton;
import java.awt.GridBagConstraints;
import java.awt.Insets;
import com.jgoodies.forms.layout.FormLayout;
import com.jgoodies.forms.layout.ColumnSpec;
import com.jgoodies.forms.layout.RowSpec;
import com.jgoodies.forms.layout.FormSpecs;
import javax.swing.JLabel;
import javax.swing.JTextField;

public class AddDocumentActionListener extends JFrame implements ActionListener {

    private static final long serialVersionUID = 1L;
    private DocumentController documentController;
    private Messages messages;
    private JTextField documentTitleTextField;

    public AddDocumentActionListener(DocumentController documentController, Messages msgs) {
        super();
        this.documentController = documentController;
        this.messages = msgs;

        setTitle(messages.getAddDocumentMenu());
        getContentPane().setLayout(new BorderLayout(0, 0));

        JPanel buttonPanel = new JPanel();
        getContentPane().add(buttonPanel, BorderLayout.SOUTH);
        GridBagLayout gbl_buttonPanel = new GridBagLayout();
        gbl_buttonPanel.columnWidths = new int[] {0, 0, 0, 0, 0, 0, 0};
        gbl_buttonPanel.rowHeights = new int[] {0, 0};
        gbl_buttonPanel.columnWeights = new double[] {0.0, 0.0, 0.0, 0.0, 0.0, 0.0, Double.MIN_VALUE};
        gbl_buttonPanel.rowWeights = new double[] {0.0, Double.MIN_VALUE};
        buttonPanel.setLayout(gbl_buttonPanel);

        JButton btnCreate = new JButton(messages.getCreate());
        GridBagConstraints gbc_btnCreate = new GridBagConstraints();
        gbc_btnCreate.insets = new Insets(0, 0, 0, 5);
        gbc_btnCreate.gridx = 2;
        gbc_btnCreate.gridy = 0;
        buttonPanel.add(btnCreate, gbc_btnCreate);

        JButton btnCancel = new JButton(messages.getCancel());
        GridBagConstraints gbc_btnCancel = new GridBagConstraints();
        gbc_btnCancel.gridx = 5;
        gbc_btnCancel.gridy = 0;
        buttonPanel.add(btnCancel, gbc_btnCancel);

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

    @Override
    public void actionPerformed(ActionEvent e) {
        this.setVisible(true);
    }

}
