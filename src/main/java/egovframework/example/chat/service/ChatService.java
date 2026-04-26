package egovframework.example.chat.service;

import java.util.List;
import java.util.Map;
import java.util.HashMap;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import egovframework.example.book.domain.BookVO;
import egovframework.example.book.service.BookService;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ChatService {

    @Value("${groq.api.key}")
    private String groqApiKey;

    private final BookService bookService;

    public String chat(String userMessage) {

        //DB에서 도서 목록 조회
        BookVO bookVO = new BookVO();
        bookVO.setFirstIndex(0);
        bookVO.setPageUnit(100);
        List<BookVO> bookList = bookService.getBookList(bookVO);

        //도서 목록을 텍스트로 변환
        StringBuilder bookInfo = new StringBuilder();
        for (BookVO book : bookList) {
            bookInfo.append(String.format(
                "도서번호: %d, 제목: %s, 저자: %s, 출판사: %s, 대출가능: %d권, 전체: %d권\n",
                book.getBookNo(),
                book.getBookTitle(),
                book.getBookAuthor(),
                book.getBookPublisher(),
                book.getAvailableStock(),
                book.getTotalStock()
            ));
        }

        //시스템 프롬프트
        String systemPrompt =
            "당신은 도서관 사서 챗봇입니다. " +
            "아래는 현재 도서관의 도서 목록과 대출 가능 현황입니다.\n\n" +
            bookInfo.toString() +
            "\n사용자의 질문에 위 정보를 바탕으로 친절하게 한국어로 답변해주세요. " +
            "도서 검색, 대출 가능 여부 안내를 도와주세요.";

        return callGroqAPI(systemPrompt, userMessage);
    }

    private String callGroqAPI(String systemPrompt, String userMessage) {
        RestTemplate restTemplate = new RestTemplate();

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(groqApiKey);

        Map<String, Object> requestBody = new HashMap<>();
        requestBody.put("model", "llama-3.3-70b-versatile");
        requestBody.put("max_tokens", 1024);
        requestBody.put("messages", List.of(
            Map.of("role", "system", "content", systemPrompt),
            Map.of("role", "user", "content", userMessage)
        ));

        HttpEntity<Map<String, Object>> request = new HttpEntity<>(requestBody, headers);

        try {
            ResponseEntity<Map> response = restTemplate.postForEntity(
                "https://api.groq.com/openai/v1/chat/completions",
                request,
                Map.class
            );

            Map<String, Object> body = response.getBody();
            List<Map<String, Object>> choices =
                (List<Map<String, Object>>) body.get("choices");
            Map<String, Object> message =
                (Map<String, Object>) choices.get(0).get("message");
            return (String) message.get("content");

        } catch (Exception e) {
            e.printStackTrace();
            return "죄송합니다. 오류: " + e.getMessage();
        }
    }
}