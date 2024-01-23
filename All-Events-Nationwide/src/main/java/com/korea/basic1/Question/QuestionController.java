package com.korea.basic1.Question;

import com.korea.basic1.Answer.Answer;
import com.korea.basic1.Answer.AnswerForm;
import com.korea.basic1.Answer.AnswerRepository;
import com.korea.basic1.Answer.AnswerService;
import com.korea.basic1.Category.Category;
import com.korea.basic1.CustomUser;
import com.korea.basic1.User.SiteUser;
import com.korea.basic1.User.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.neo4j.Neo4jProperties;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.io.File;
import java.io.IOException;
import java.security.Principal;
import java.util.UUID;

@Controller
@RequestMapping("/question")
@RequiredArgsConstructor
public class QuestionController {
    private final QuestionService questionService;
    private final AnswerService answerService;
    private final UserService userService;

    @GetMapping("/categorylist/{id}")
    public String categorylist(Model model, @RequestParam(value="page", defaultValue="0") int page, @RequestParam(value = "kw", defaultValue = "") String kw,
                               @PathVariable("id") Integer id, Authentication authentication) {
        if (authentication != null) {
            CustomUser user = (CustomUser) authentication.getPrincipal();
        }
        if (id.equals(0)){
            Page<Question> paging = this.questionService.getList(page, kw);
            model.addAttribute("paging", paging);
            model.addAttribute("kw", kw);
            model.addAttribute("id", 0);
        } else {
            Page<Question> paging = this.questionService.getCategoryList(page, id);
            model.addAttribute("paging", paging);
            model.addAttribute("id", id);
        }
        return "board/question_list";
    }

    @GetMapping("/detail/{id}")
    public String detail(Model model, @PathVariable("id") Integer id, @RequestParam(value="page", defaultValue="0") int page, AnswerForm answerForm) {
        Question question = this.questionService.getQuestion(id);

        if(question.getCategory().getName().equals("자유게시판")){
            Page<Answer> answerPaging = this.answerService.getList(question, page);
            model.addAttribute("answerPaging", answerPaging);
            model.addAttribute("question", question);
            return "board/question_detail";
        } else if (question.getCategory().getName().equals("문의사항")) {
            Page<Answer> answerPaging = this.answerService.getList(question, page);
            model.addAttribute("answerPaging", answerPaging);
            model.addAttribute("question", question);
            return "board/question_detail";
        } else if (question.getCategory().getName().equals("문의사항")) {
            Page<Answer> answerPaging = this.answerService.getList(question, page);
            model.addAttribute("answerPaging", answerPaging);
            model.addAttribute("question", question);
            return "board/question_detail";
        }else{
            Page<Answer> answerPaging = this.answerService.getList(question, page);
            model.addAttribute("answerPaging", answerPaging);
            model.addAttribute("question", question);
            return "board/question_detail_events";
        }
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/create")
    public String questionCreate(QuestionForm questionForm) {
        return "board/question_form";
    }

    @PreAuthorize("isAuthenticated()")
    @PostMapping("/create")
    public String questionCreate(@Valid QuestionForm questionForm, BindingResult bindingResult,
                                 Principal principal, MultipartFile file) throws Exception {
        if (bindingResult.hasErrors()) {
            return "board/question_form";
        }
        SiteUser siteUser = this.userService.getUser(principal.getName());
        this.questionService.create(questionForm.getSubject(), questionForm.getContent(), questionForm.getPostcode(), questionForm.getRoadAddress(), questionForm.getJibunAddress(),
                questionForm.getDetailAddress(), questionForm.getExtraAddress(), questionForm.getCategory(), siteUser, file);
        return "redirect:/";
    }
    @PreAuthorize("isAuthenticated()")
    @GetMapping("/modify/{id}")
    public String questionModify(QuestionForm questionForm, @PathVariable("id") Integer id, Principal principal, Model model) {
        Question question = this.questionService.getQuestion(id);
        if (!question.getAuthor().getUserid().equals(principal.getName())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "수정권한이 없습니다.");
        }
        questionForm.setSubject(question.getSubject());
        questionForm.setContent(question.getContent());
        questionForm.setCategory(question.getCategory());
        questionForm.setPostcode(question.getPostcode());
        questionForm.setRoadAddress(question.getRoadAddress());
        questionForm.setJibunAddress(question.getJibunAddress());
        questionForm.setDetailAddress(question.getDetailAddress());
        questionForm.setExtraAddress(question.getExtraAddress());
        return "board/question_form";
    }

    @PreAuthorize("isAuthenticated()")
    @PostMapping("/modify/{id}")
    public String questionModify(@Valid QuestionForm questionForm, BindingResult bindingResult,
                                 Principal principal, @PathVariable("id") Integer id) throws Exception {
        if (bindingResult.hasErrors()) {
            return "board/question_form";
        }
        Question question = this.questionService.getQuestion(id);
        if (!question.getAuthor().getUserid().equals(principal.getName())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "수정권한이 없습니다.");
        }
        this.questionService.modify(question, questionForm.getSubject(), questionForm.getContent(),questionForm.getPostcode(),
                questionForm.getRoadAddress(), questionForm.getJibunAddress(), questionForm.getDetailAddress(), questionForm.getExtraAddress(), questionForm.getCategory(), questionForm.getFile());
        return String.format("redirect:/question/detail/%s", id);
    }


    @PreAuthorize("isAuthenticated()")
    @GetMapping("/vote/{id}")
    public String questionVote(Principal principal, @PathVariable("id") Integer id) {
        Question question = this.questionService.getQuestion(id);
        SiteUser siteUser = this.userService.getUser(principal.getName());
        this.questionService.vote(question, siteUser);
        return String.format("redirect:/question/detail/%s", id);
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
}
