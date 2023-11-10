package com.korea.basic1.manager;

import com.korea.basic1.Answer.Answer;
import com.korea.basic1.Answer.AnswerService;
import com.korea.basic1.Question.Question;
import com.korea.basic1.Question.QuestionService;
import com.korea.basic1.User.SiteUser;
import com.korea.basic1.User.UserRepository;
import com.korea.basic1.User.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.server.ResponseStatusException;

import java.io.File;
import java.security.Principal;
import java.util.List;

import static org.springframework.security.authorization.AuthorityAuthorizationManager.hasRole;

@Controller
@RequestMapping("/manager")
@RequiredArgsConstructor
@PreAuthorize("isAuthenticated() and (hasRole('SUPER_ADMIN') or hasRole('ADMIN'))")
public class ManagerController {
    private final QuestionService questionService;
    private final AnswerService answerService;
    private final UserService userService;
    private final UserRepository userRepository;

    @GetMapping("/questionlist")
    public String list(Model model){
        List<Question> adminQuestionList = this.questionService.getList();
        model.addAttribute("adminQuestionList", adminQuestionList);
        return "manager/manager_question_list";
    }

    @GetMapping("/answerlist")
    public String answerlist(Model model){
        List<Answer> adminAnswerList = this.answerService.getAnswerList();
        model.addAttribute("adminAnswerList",adminAnswerList);
        return "manager/manager_answer_list";
    }

    @GetMapping("/userlist")
    public String userlist(Model model){
        List<SiteUser> adminUserList = this.userService.getUserList("user");
        model.addAttribute("adminUserList", adminUserList);
        return "manager/manager_user_list";
    }

    @GetMapping("/userUpdate/{id}")
    public String registerAsAdmin(@PathVariable("id") String userId) {
        SiteUser user = userService.getUser(userId);

        if ("user".equals(user.getRole())) {
            user.setRole("admin");
        } else if ("admin".equals(user.getRole())) {
            user.setRole("user");
        }
        this.userRepository.save(user);
        return "redirect:/manager/userlist";
    }

    @GetMapping("/userDelete/{id}")
    public String userDelete(@PathVariable("id") String id) {
        SiteUser siteUser = this.userService.getUser(id);
        this.userService.delete(siteUser);
        return "redirect:/manager/userlist";
    }

    @GetMapping("/answerDelete/{id}")
    public String answerDelete(@PathVariable("id") Integer id) {
        Answer answer = this.answerService.getAnswer(id);
        this.answerService.delete(answer);
        return "redirect:/manager/answerlist";
    }

    @GetMapping("/questionDelete/{id}")
    public String questionDelete(Principal principal, @PathVariable("id") Integer id, Authentication authentication) {
        Question question = this.questionService.getQuestion(id);

        String imagePath = question.getFilepath();
        File imageFile = new File(imagePath);
        if (imageFile.exists()) {
            imageFile.delete();
        }
        this.questionService.delete(question);
        return "redirect:/manager/questionlist";
    }
}
