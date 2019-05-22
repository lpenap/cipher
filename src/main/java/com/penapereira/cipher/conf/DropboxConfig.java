package com.penapereira.cipher.conf;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;
import lombok.Data;

@Component
@ConfigurationProperties(prefix = "dropbox")
@Data
public class DropboxConfig {

}
