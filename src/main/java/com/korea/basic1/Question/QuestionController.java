package com.korea.basic1.Question;

import com.korea.basic1.Answer.Answer;
import com.korea.basic1.Answer.AnswerForm;
import com.korea.basic1.Answer.AnswerService;
import com.korea.basic1.User.SiteUser;
import com.korea.basic1.User.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@Controller
@RequestMapping("/question")
@RequiredArgsConstructor
public class QuestionController {
    private final QuestionService questionService;
    private final AnswerService answerService;
    private final UserService userService;

    @GetMapping("/list")
    public String list(Model model, @RequestParam(value="page", defaultValue="0") int page,
                       @RequestParam(value = "kw", defaultValue = "") String kw){
        Page<Question> paging = this.questionService.getList(page, kw);
        model.addAttribute("paging", paging);
        model.addAttribute("kw", kw);
        return "question_list";
    }


    @GetMapping(value = "/detail/{id}")
    public String detail(Model model, @PathVariable("id") Integer id, @RequestParam(value="page", defaultValue="0") int page, AnswerForm answerForm) {
        Question question = this.questionService.getQuestion(id);
        Page<Answer> paging = this.answerService.getList(page);
        model.addAttribute("paging", paging);
        model.addAttribute("question", question);
        return "question_detail";
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/create")
    public String questionCreate(QuestionForm questionForm) {
        return "question_form";
    }

    @PreAuthorize("isAuthenticated()")
    @PostMapping("/create")
    public String questionCreate(@Valid QuestionForm questionForm, BindingResult bindingResult, Principal principal) {
        if (bindingResult.hasErrors()) {
            return "question_form";
        }
        SiteUser siteUser = this.userService.getUser(principal.getName());
        this.questionService.create(questionForm.getSubject(), questionForm.getContent(), questionForm.getCategory(), siteUser);
        return "redirect:/question/list";
    }
}
