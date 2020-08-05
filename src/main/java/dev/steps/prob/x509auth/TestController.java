/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dev.steps.prob.x509auth;

import java.io.IOException;
import java.security.Principal;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import sun.security.x509.X500Name;

/**
 *
 * @author stepin
 */
@Controller
public class TestController {

    @GetMapping("favicon.ico")
    @ResponseBody
    void returnNoFavicon() {
    }

    @GetMapping({"/", "/home"})
    public String getHome(Model model, Principal principal) throws IOException {

        System.out.println("!!! OK !!!");

        X500Name user;
        String username = null;
        System.out.println("principal=" + principal);

        if (principal != null
                && (user = (X500Name) ((Authentication) principal).getPrincipal()) != null) {

            username = user.getCommonName();
        }

        model.addAttribute("username", username);

        return "home";
    }

    @GetMapping("/test")
    public String getTest(Model model, Principal principal) {

        model.addAttribute("testForm", new TestForm());
        return "/test";
    }

    @PostMapping("test")
    public String postTest(TestForm testForm) {
        
        System.out.println("### testForm=" + testForm);
        return "/test";
    }

}
