package com.componentbid.user.web;

import com.componentbid.user.dto.RegisterRequest;
import com.componentbid.user.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequiredArgsConstructor
public class AuthController {
    private final UserService userService;

    @GetMapping("/register")
    public String registerPage(Model model) {

        model.addAttribute(
                "registerRequest",
                new RegisterRequest()
        );

        return "register";
    }

    @PostMapping("/register")
    public String register(
            @Valid
            @ModelAttribute RegisterRequest request,
            BindingResult bindingResult,
            Model model,
            RedirectAttributes redirectAttributes) {

        if (bindingResult.hasErrors()) {
            model.addAttribute(
                    "errorMessage",
                    "Please fix the highlighted registration fields."
            );

            return "register";
        }

        try {
            userService.register(request);
        } catch (IllegalArgumentException | IllegalStateException exception) {
            model.addAttribute(
                    "errorMessage",
                    exception.getMessage()
            );

            return "register";
        }

        redirectAttributes.addFlashAttribute(
                "successMessage",
                "Registration successful. You can log in now."
        );

        return "redirect:/login";
    }
    @GetMapping("/login")
    public String loginPage() {
        return "login";
    }
}
