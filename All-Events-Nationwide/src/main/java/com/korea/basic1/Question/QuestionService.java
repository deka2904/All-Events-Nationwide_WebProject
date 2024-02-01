package com.korea.basic1.Question;

import com.korea.basic1.AppConfig;
import com.korea.basic1.Category.Category;
import com.korea.basic1.DataNotFoundException;
import com.korea.basic1.User.SiteUser;
import jakarta.persistence.criteria.*;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class QuestionService {
    //파일저장할위치
    @Value("${ImgLocation}")
    public String imgLocation;

    private final QuestionRepository questionRepository;

    public List<Question> getList() {
        return this.questionRepository.findAll();
    }

    public void create(String subject, String content, String postcode, String roadAddress, String jibunAddress, String detailAddress, String extraAddress, Category category, SiteUser user, MultipartFile file) throws Exception {
        String projectPath = AppConfig.getImageFileDirPath(); // 파일 저장 위치 = projectPath
        UUID uuid = UUID.randomUUID(); // 식별자. 랜덤으로 이름 생성
        String fileName;

        // 이미지가 업로드되지 않았을 때 기본 이미지를 사용
        if (file.isEmpty()) {
            fileName = "no_img.jpg"; // 기본 이미지 파일명
            File defaultImageFile = new File(projectPath, fileName);

            // 기본 이미지를 static 폴더에서 복사
            ClassPathResource defaultImageResource = new ClassPathResource("static/no_img.jpg");
            Files.copy(defaultImageResource.getInputStream(), defaultImageFile.toPath(), StandardCopyOption.REPLACE_EXISTING);
        } else {
            fileName = uuid + "_" + file.getOriginalFilename(); // 저장될 파일 이름 지정 = 랜덤 식별자_원래 파일 이름
            File saveFile = new File(projectPath, fileName); // 빈 껍데기 생성, 이름은 fileName, projectPath라는 경로에 담김
            file.transferTo(saveFile);
        }

        Question q = new Question();
        q.setSubject(subject);
        q.setContent(content);
        q.setPostcode(postcode);
        q.setRoadAddress(roadAddress);
        q.setJibunAddress(jibunAddress);
        q.setDetailAddress(detailAddress);
        q.setExtraAddress(extraAddress);
        q.setCreateDate(LocalDateTime.now());
        q.setCategory(category);
        q.setAuthor(user);

        q.setFilename(fileName); // 파일 이름
        q.setFilepath(projectPath + fileName); // 저장 경로, 파일 이름

        this.questionRepository.save(q);
    }

    public void vote(Question question, SiteUser siteUser) {
        question.getVoter().add(siteUser);
        this.questionRepository.save(question);
    }

    public Page<Question> getList(int page, String kw) {
        List<Sort.Order> sorts = new ArrayList<>();
        sorts.add(Sort.Order.desc("createDate"));
        Pageable pageable = PageRequest.of(page, 6, Sort.by(sorts));
        Specification<Question> spec = search(kw);
        return this.questionRepository.findAllByKeyword(kw, pageable);
    }
    public Page<Question> getCategoryList(int page, int id) {
        List<Sort.Order> sorts = new ArrayList<>();
        sorts.add(Sort.Order.desc("createDate"));
        Pageable pageable = PageRequest.of(page, 6, Sort.by(sorts));
        return this.questionRepository.findByCategoryId(id, pageable);
    }

    public Question getQuestion(Integer id) {
        Optional<Question> question = this.questionRepository.findById(id);
        if (question.isPresent()) {
            return question.get();
        } else {
            throw new DataNotFoundException("question not found");
        }
    }

    public void modify(Question question, String subject, String content, String postcode, String roadAddress, String jibunAddress, String detailAddress, String extraAddress,
                       Category category, MultipartFile file) throws Exception{
        String projectPath = AppConfig.getImageFileDirPath();;

        if (file.getOriginalFilename().equals("")){
            //새 파일이 없을 때
            question.setFilename(question.getFilename());
            question.setFilepath(question.getFilepath());

        } else if (file.getOriginalFilename() != null){
            //새 파일이 있을 때
            File f = new File(question.getFilepath());

            if (f.exists()) { // 파일이 존재하면
                f.delete(); // 파일 삭제
            }

            UUID uuid = UUID.randomUUID();
            String fileName = uuid + "_" + file.getOriginalFilename();
            File saveFile = new File(projectPath, fileName);
            file.transferTo(saveFile);

            question.setFilename(fileName);
            question.setFilepath(projectPath + fileName);
        }
        question.setSubject(subject);
        question.setContent(content);
        question.setPostcode(postcode);
        question.setRoadAddress(roadAddress);
        question.setJibunAddress(jibunAddress);
        question.setDetailAddress(detailAddress);
        question.setExtraAddress(extraAddress);
        question.setModifyDate(LocalDateTime.now());
        question.setCategory(category);
        this.questionRepository.save(question);
    }

    public void delete(Question question) {
        this.questionRepository.delete(question);
    }

    private Specification<Question> search(String kw) {
        return new Specification<>() {
            private static final long serialVersionUID = 1L;
            @Override
            public Predicate toPredicate(Root<Question> q, CriteriaQuery<?> query, CriteriaBuilder cb) {
                query.distinct(true);  // 중복을 제거
                return cb.like(q.get("subject"), "%" + kw + "%"); // 제목
            }
        };
    }
}
