package com.ap.framework;

import javax.security.auth.Subject;
import java.security.Principal;

public class PrincipalImpl implements Principal {
    private String name;

    public PrincipalImpl(String name) {
        this.name = name;
    }

    @Override
    public String getName() {
        return name;
    }

}
