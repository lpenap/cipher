package com.penapereira.cipher.conf;

import java.util.ArrayList;
import java.util.List;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;
import lombok.Data;

@Component
@ConfigurationProperties(prefix = "message")
@Data
public class Messages {

    private String windowTitle;
    
    private String cipherMenu;
    private String aboutMenu;
    private List<String> about = new ArrayList<>();
    private String exitMenu;

    private String documentMenu;
    private String addDocumentMenu;
    private String saveAllMenu;
    private String deleteDocumentMenu;
    private String deleteDocumentConfirmPre;
    private String deleteDocumentConfirmPost;

    private String setupConfirmTitle;
    private String setupConfirmMsg;
    private String helpDocumentTitle;
    private List<String> helpDocument = new ArrayList<>();
    
    private String create;
    private String cancel;
    private String documentTitle;
    
    private String next;
    private String previous;
    private String closeSearchBar;
    
}
