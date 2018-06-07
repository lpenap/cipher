package com.penapereira.cipher.service.impl;

import com.penapereira.cipher.service.UserInterfaceService;


public class UserInterfaceServiceImpl implements UserInterfaceService {
    protected static UserInterfaceService singletonInstance = null;
    
    protected UserInterfaceServiceImpl () {
    }
    
    public static UserInterfaceService instance() {
        if (singletonInstance == null) {
            singletonInstance = new UserInterfaceServiceImpl();
        }
        return singletonInstance;
    }

}
