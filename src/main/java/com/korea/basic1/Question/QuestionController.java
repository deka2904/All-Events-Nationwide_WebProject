package com.korea.basic1.Question;

import com.korea.basic1.Answer.Answer;
import com.korea.basic1.Answer.AnswerForm;
import com.korea.basic1.Answer.AnswerRepository;
import com.korea.basic1.User.SiteUser;
import com.korea.basic1.User.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
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
    private final AnswerRepository.AnswerService answerService;
    private final UserService userService;

    @GetMapping("/categorylist/{id}")
    public String categorylist(Model model, @RequestParam(value="page", defaultValue="0") int page, @RequestParam(value = "kw", defaultValue = "") String kw, @PathVariable("id") Integer id) {
        if (id.equals(0)){
            Page<Question> paging = this.questionService.getList(page, kw);
            model.addAttribute("paging", paging);
            model.addAttribute("kw", kw);
            model.addAttribute("id", 0);
        }else{
            Page<Question> paging = this.questionService.getCategoryList(page, id);
            model.addAttribute("paging", paging);
            model.addAttribute("id", id);
        }
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
    public String questionCreate(@Valid QuestionForm questionForm, BindingResult bindingResult,
                                 Principal principal, MultipartFile file) throws Exception {
        if (bindingResult.hasErrors()) {
            return "question_form";
        }
        SiteUser siteUser = this.userService.getUser(principal.getName());
        this.questionService.create(questionForm.getSubject(), questionForm.getContent(),
                questionForm.getCategory(), siteUser, file);
        return "redirect:/";
    }
    @PreAuthorize("isAuthenticated()")
    @GetMapping("/modify/{id}")
    public String questionModify(QuestionForm questionForm, @PathVariable("id") Integer id, Principal principal) {
        Question question = this.questionService.getQuestion(id);
        if (!question.getAuthor().getUserid().equals(principal.getName())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "수정권한이 없습니다.");
        }
        questionForm.setSubject(question.getSubject());
        questionForm.setContent(question.getContent());
        questionForm.setCategory(question.getCategory());
        questionForm.setFile(null); // 파일을 업로드하도록 설정
        return "question_form";
    }

    @PreAuthorize("isAuthenticated()")
    @PostMapping("/modify/{id}")
    public String questionModify(@Valid QuestionForm questionForm, BindingResult bindingResult,
                                 Principal principal, @PathVariable("id") Integer id) {
        if (bindingResult.hasErrors()) {
            return "question_form";
        }
        Question question = this.questionService.getQuestion(id);
        if (!question.getAuthor().getUserid().equals(principal.getName())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "수정권한이 없습니다.");
        }

        MultipartFile newFile = questionForm.getFile();
        String projectPath = questionService.imgLocation;

        if (newFile == null || newFile.isEmpty()) {
            String defaultImageFileName = "no_img.jpg";
            String defaultImageFilePath = projectPath + defaultImageFileName;

            question.setFilename(defaultImageFileName);
            question.setFilepath(defaultImageFilePath);
        } else {
            UUID uuid = UUID.randomUUID();
            String fileName = uuid + "_" + newFile.getOriginalFilename();
            File saveFile = new File(projectPath, fileName);
            try {
                newFile.transferTo(saveFile);
                question.setFilename(fileName);
                question.setFilepath(projectPath + fileName);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        this.questionService.modify(question, questionForm.getSubject(), questionForm.getContent(), questionForm.getCategory());
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
    public String questionDelete(Principal principal, @PathVariable("id") Integer id) {
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
