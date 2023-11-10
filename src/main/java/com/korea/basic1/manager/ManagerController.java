package com.korea.basic1.manager;

import com.korea.basic1.Question.Question;
import com.korea.basic1.Question.QuestionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.server.ResponseStatusException;

import java.io.File;
import java.security.Principal;
import java.util.List;

@Controller
@RequestMapping("/manager")
@RequiredArgsConstructor
public class ManagerController {
    private final QuestionService questionService;

    @GetMapping("/list")
    public String list(Model model){
        List<Question> adminQuestionList = this.questionService.getList();
        model.addAttribute("adminQuestionList", adminQuestionList);
        return "/manager/manager_list";
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/delete/{id}")
    public String questionDelete(Principal principal, @PathVariable("id") Integer id, Authentication authentication) {
        Question question = this.questionService.getQuestion(id);

        if (!question.getAuthor().getUserid().equals(principal.getName())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "삭제권한이 없습니다.");
        }
        String imagePath = question.getFilepath();
        File imageFile = new File(imagePath);
        if (imageFile.exists()) {
            imageFile.delete();
        }
        this.questionService.delete(question);
        return "redirect:/";
    }

    @GetMapping("/login")
    public String login() {
        return "user/login_form";
    }
}
