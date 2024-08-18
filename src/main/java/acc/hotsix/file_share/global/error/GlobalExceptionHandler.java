package acc.hotsix.file_share.global.error;

import acc.hotsix.file_share.global.error.exception.*;
import org.springframework.beans.TypeMismatchException;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.NoHandlerFoundException;

@RestControllerAdvice
public class GlobalExceptionHandler {
    /* 공통 예외 */
    // 유효성 검사 실패
    @ExceptionHandler(BindException.class)
    public ResponseEntity<ExceptionResponse> handle(BindException e) {
        return createErrorResponseEntity(ExceptionCode.INVALID_INPUT);
    }

    // 파라미터 타입이 잘못됨
    @ExceptionHandler(TypeMismatchException.class)
    public ResponseEntity<ExceptionResponse> handle(TypeMismatchException e) {
        return createErrorResponseEntity(ExceptionCode.TYPE_MISMATCH);
    }

    // 잘못된 HTTP 메소드 사용
    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public ResponseEntity<ExceptionResponse> handle(HttpRequestMethodNotSupportedException e) {
        return createErrorResponseEntity(ExceptionCode.INVALID_HTTP_METHOD);
    }

    // 존재하지 않는 URL 접근
    @ExceptionHandler(NoHandlerFoundException.class)
    public ResponseEntity<ExceptionResponse> handle(NoHandlerFoundException e) {
        return createErrorResponseEntity(ExceptionCode.URL_NOT_FOUND);
    }

    /* 파일 공통 예외 */
    // 비밀번호가 일치하지 않음
    @ExceptionHandler(InvalidPasswordException.class)
    public ResponseEntity<ExceptionResponse> handle(InvalidPasswordException e) {
        return createErrorResponseEntity(e.getExceptionCode());
    }

    // 파일을 찾을 수 없음
    @ExceptionHandler(FileNotFoundException.class)
    public ResponseEntity<ExceptionResponse> handle(FileNotFoundException e) {
        return createErrorResponseEntity(e.getExceptionCode());
    }

    // 중복된 파일
    @ExceptionHandler(FileDuplicateException.class)
    public ResponseEntity<ExceptionResponse> handle(FileDuplicateException e) {
        return createErrorResponseEntity(e.getExceptionCode());
    }

    /* 파일 수정 예외 */
    // 파일 확장자가 일치하지 않음
    @ExceptionHandler(FileTypeMismatchException.class)
    public ResponseEntity<ExceptionResponse> handle(FileTypeMismatchException e) {
        return createErrorResponseEntity(e.getExceptionCode());
    }

    /* 파일 검색 예외 */
    // 검색 결과가 존재하지 않음
    @ExceptionHandler(MissingSearchResultException.class)
    public ResponseEntity<ExceptionResponse> handle(MissingSearchResultException e) {
        return createErrorResponseEntity(e.getExceptionCode());
    }

    // 검색 쿼리 파라미터가 잘못됨
    @ExceptionHandler(InvalidQueryParamException.class)
    public ResponseEntity<ExceptionResponse> handle(InvalidQueryParamException e) {
        return createErrorResponseEntity(e.getExceptionCode());
    }

    // 검색 쿼리 파라미터가 없음
    @ExceptionHandler(MissingSearchParamException.class)
    public ResponseEntity<ExceptionResponse> handle(MissingSearchParamException e) {
        return createErrorResponseEntity(e.getExceptionCode());
    }

    /* 파일 API 예외 */
    // 업로드 예외
    @ExceptionHandler(UploadFileException.class)
    public ResponseEntity<ExceptionResponse> handle(UploadFileException e) {
        return createErrorResponseEntity(e.getExceptionCode());
    }

    // 다운로드 예외
    @ExceptionHandler(DownloadFileException.class)
    public ResponseEntity<ExceptionResponse> handle(DownloadFileException e) {
        return createErrorResponseEntity(e.getExceptionCode());
    }

    // 업데이트 예외
    @ExceptionHandler(UpdateFileException.class)
    public ResponseEntity<ExceptionResponse> handle(UpdateFileException e) {
        e.printStackTrace();
        return createErrorResponseEntity(e.getExceptionCode());
    }

    // 상세조회 예외
    @ExceptionHandler(DetailFileException.class)
    public ResponseEntity<ExceptionResponse> handle(DetailFileException e) {
        return createErrorResponseEntity(e.getExceptionCode());
    }

    // 공유링크 생성 예외
    @ExceptionHandler(ShareFileException.class)
    public ResponseEntity<ExceptionResponse> handle(ShareFileException e) {
        return createErrorResponseEntity(e.getExceptionCode());
    }

    // 삭제 예외
    @ExceptionHandler(DeleteFileException.class)
    public ResponseEntity<ExceptionResponse> handle(DeleteFileException e) {
        return createErrorResponseEntity(e.getExceptionCode());
    }

    // 전역 예외 처리
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ExceptionResponse> handle(Exception e) {
        return createErrorResponseEntity(ExceptionCode.EXCEPTION);
    }

    private ResponseEntity<ExceptionResponse> createErrorResponseEntity(ExceptionCode exceptionCode) {
        return new ResponseEntity<>(
                new ExceptionResponse(exceptionCode),
                HttpStatusCode.valueOf(exceptionCode.getStatus())
        );
    }
}
