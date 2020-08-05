/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dev.steps.prob.x509auth;

import java.security.Principal;
import java.security.cert.X509Certificate;
import java.util.Collection;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;

/**
 *
 * @author stepin
 */
public class MyX509Authentication extends AbstractAuthenticationToken{
    
    private final Principal principal;
    private final X509Certificate x509Certificate;

    public MyX509Authentication(
            Principal principal,
            X509Certificate x509Certificate,
            Collection<? extends GrantedAuthority> authorities) {
        
        super(authorities);
        this.principal = principal;
        this.x509Certificate = x509Certificate;
    }

    @Override
    public Object getCredentials() {
        return x509Certificate;
    }

    @Override
    public Object getPrincipal() {
        return principal;
    }
    
}
