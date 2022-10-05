package com.penapereira.cipher.conf;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;
import lombok.Data;

@Component
@ConfigurationProperties(prefix = "search")
@Data
public class SearchConfiguration {

    private long keyPressedThresholdMillis;
    private int arrowSize;
    private int closeSize;
    private int iconColorR;
    private int iconColorG;
    private int iconColorB;
    private int paddingSize;
}
