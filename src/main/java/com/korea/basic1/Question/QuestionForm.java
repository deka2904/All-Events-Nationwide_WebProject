package com.korea.basic1.Question;

import com.korea.basic1.Category.Category;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

@Getter
@Setter
public class QuestionForm {
    @NotEmpty(message = "제목은 필수항목입니다.")
    @Size(max = 200)
    private String subject;

    @NotEmpty(message = "내용은 필수항목입니다.")
    private String content;

    @NotNull(message = "카테고리 선택은 필수항목입니다.")
    private Category category;

    @NotNull(message = "지역은 필수항목입니다.")
    private String address;

    private MultipartFile file; // 파일 업로드를 위한 필드
    private String filename; // 파일 이름을 저장할 필드

}