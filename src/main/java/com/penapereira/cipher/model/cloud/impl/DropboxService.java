package com.penapereira.cipher.model.cloud.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.penapereira.cipher.conf.DropboxConfig;

@Service
public class DropboxService extends AbstractCloudService {
    
    protected DropboxConfig config;

    @Autowired
    public DropboxService(DropboxConfig config) {
        this.config = config;
    }
}
