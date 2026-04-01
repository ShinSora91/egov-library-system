package egovframework.example.book.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
@ToString
public class BookVO {
    private int bookNo;
    private String bookTitle;
    private String bookAuthor;
    private String bookPublisher;
    private String registDate;

    @Builder.Default
    private String deleteYn = "N";
    private String deleteeDate;
}
