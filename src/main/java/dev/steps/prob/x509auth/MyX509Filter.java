/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dev.steps.prob.x509auth;

import java.io.IOException;
import java.security.cert.CertificateExpiredException;
import java.security.cert.CertificateNotYetValidException;
import java.security.cert.X509Certificate;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.preauth.AbstractPreAuthenticatedProcessingFilter;
import org.springframework.web.filter.GenericFilterBean;

/**
 *
 * @author stepin
 */
public class MyX509Filter extends GenericFilterBean {//AbstractPreAuthenticatedProcessingFilter {

//    @Override
//    protected Object getPreAuthenticatedPrincipal(HttpServletRequest httpRequest) {
//        return extractUserCertificate(httpRequest).getSubjectDN();
//    }
//
//    @Override
//    protected Object getPreAuthenticatedCredentials(HttpServletRequest httpRequest) {
//        return extractUserCertificate(httpRequest);
//    }
    @Override
    public void doFilter(
            ServletRequest request,
            ServletResponse response,
            FilterChain filterchain) throws IOException, ServletException {

        System.out.println("============   START FILTER   ================================== ");

        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpServletResponse httpResponse = (HttpServletResponse) response;

        Authentication authentication = SecurityContextHolder
                .getContext()
                .getAuthentication();

        System.out.println("### authentication:" + authentication);
        if (authentication == null
                || !authentication.isAuthenticated()
                || authentication.getClass().isAssignableFrom(AnonymousAuthenticationToken.class)) {

            System.out.println("### Trying to obtain certificate ...");

            try {

                X509Certificate cert = extractUserCertificate(httpRequest);
                //System.out.println("### cert=" + cert);

                MyX509Authentication auth = createX509Authentication(cert);
                auth.setAuthenticated(true);
                System.out.println("### auth=" + auth);

                SecurityContextHolder.getContext().setAuthentication(auth);

            } catch (AuthenticationException ex) {
                this.handleAuthenticationException(httpResponse, ex);
                return;

            } catch (Exception ex) {
                this.handleException(httpResponse, ex);
                return;
            }
        }
        
        System.out.println("============   PROCEED CHAIN   ================================== ");
        filterchain.doFilter(request, response);
    }

    private MyX509Authentication createX509Authentication(X509Certificate x509Certificate) {

        if (x509Certificate == null) {
            String errMsg = " No User-Certificate found in request. ";
            System.out.println(errMsg);
            throw new BadCredentialsException(errMsg);
        }

        try {
            x509Certificate.checkValidity();

        } catch (CertificateExpiredException ex) {
            String errMsg = ex.getMessage() + " User-Certificate is expired on date:" + x509Certificate.getNotAfter();
            System.out.println(errMsg);
            ex.printStackTrace();
            throw new BadCredentialsException(errMsg);

        } catch (CertificateNotYetValidException ex) {
            String errMsg = ex.getMessage() + " User-Certificate is not valid before:" + x509Certificate.getNotBefore();
            System.out.println(errMsg);
            ex.printStackTrace();
            throw new BadCredentialsException(errMsg);

        }

        return new MyX509Authentication(
                x509Certificate.getSubjectDN(),
                x509Certificate,
                AuthorityUtils.commaSeparatedStringToAuthorityList("BEST_CLIENTS"));
    }

    private X509Certificate extractUserCertificate(HttpServletRequest httpRequest) {

        X509Certificate[] certs = (X509Certificate[]) httpRequest.getAttribute("javax.servlet.request.X509Certificate");

        if (certs == null || certs.length <= 0) {
            String errMsg = "No User-Certificate found in request.";
            System.out.println(errMsg);
            throw new BadCredentialsException(errMsg);
        }

        X509Certificate x509Certificate = certs[0];
        System.out.println("X.509 User-Certificate :" + x509Certificate);

//        try {
//            x509Certificate.checkValidity();
//
//        } catch (CertificateExpiredException ex) {
//            String errMsg = ex.getMessage() + " User-Certificate is expired on date:" + x509Certificate.getNotAfter();
//            System.out.println(errMsg);
//            ex.printStackTrace();
//            throw new BadCredentialsException(errMsg);
//
//        } catch (CertificateNotYetValidException ex) {
//            String errMsg = ex.getMessage() + " User-Certificate is not valid before:" + x509Certificate.getNotBefore();
//            System.out.println(errMsg);
//            ex.printStackTrace();
//            throw new BadCredentialsException(errMsg);
//
//        }
        logCertificateInfo(x509Certificate);
        return x509Certificate;
    }

    private void handleAuthenticationException(HttpServletResponse httpResponse, AuthenticationException ex) throws IOException {
        String errMsg = " Cannot authenticate user.  " + ex.getMessage();
        ex.printStackTrace();
        System.out.println(errMsg);
        httpResponse.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
        httpResponse.getWriter().print(errMsg);
    }

    private void handleException(HttpServletResponse httpResponse, Exception ex) throws IOException {
        String errMsg = " Error during user authentication: " + ex.getMessage();
        ex.printStackTrace();
        System.out.println(errMsg);
        httpResponse.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
        httpResponse.getWriter().print(errMsg);
    }

    private void logCertificateInfo(X509Certificate cert) {

        if (cert == null) {
            return;
        }

        System.out.println("######################################################");
        System.out.println("### Version:" + cert.getVersion());
        System.out.println("### NotBefore:" + cert.getNotBefore());
        System.out.println("### NotAfter:" + cert.getNotAfter());
        System.out.println("### SerialNumber:" + cert.getSerialNumber());
        System.out.println("### IssuerDN:" + cert.getIssuerDN());
        System.out.println("### :" + cert.getIssuerUniqueID());
        System.out.println("### :" + cert.getIssuerX500Principal());
        System.out.println("### SubjectDN():" + cert.getSubjectDN());
        System.out.println("### :" + cert.getSubjectUniqueID());
        System.out.println("### :" + cert.getSubjectX500Principal());
        System.out.println("######################################################");
    }

}
