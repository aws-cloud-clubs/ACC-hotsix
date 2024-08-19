package acc.hotsix.file_share.global.error;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum ExceptionCode {
    // Common
    // DTO 값 유효성 검사 실패
    INVALID_INPUT("Common-001", "No value was entered or an invalid format was used.", 400),
    // 파라미터 타입이 잘못됨
    TYPE_MISMATCH("Common-002", "Invalid parameter data type.", 400),
    // 잘못된 HTTP 메소드
    INVALID_HTTP_METHOD("Common-003", "Invalid HTTP method type.", 400),
    // 존재하지 않는 URL 접근
    URL_NOT_FOUND("Common-004", "Page not found.", 404),

    // File 공통 오류
    INVALID_PASSWORD("File-001", "Access denied: invalid password", 400),
    FILE_NOT_FOUND("File-002", "file does not exist.", 404),
    FILE_DUPLICATE("File-003", "fileName already exists in the bucket. Please rename your file and try again.", 400),

    // File 수정 오류
    FILE_TYPE_MISMATCH("Update-001", "The registered file does not match the extension type.", 400),

    // File 검색 오류
    MISSING_SEARCH_RESULT("Search-001", "No search results found.", 404),
    INVALID_QUERY_PARAM("Search-002", "Only 'asc' or 'desc' can be entered for the name and time parameters.", 400),
    MISSING_SEARCH_PARAM("Search-003", "At least one search condition must be entered.", 400),

    // File api 실행 오류
    UPLOAD_ERROR("API-001", "An error occurred during file upload.", 500),
    DOWNLOAD_ERROR("API-002", "An error occurred during file download.", 500),
    UPDATE_ERROR("API-002", "An error occurred during file update.", 500),
    DETAIL_ERROR("API-003", "An error occurred during file read.", 500),
    SHARE_ERROR("API-004", "An error occurred while generating the file share link.", 500),
    DELETE_ERROR("API-005", "An error occurred during file delete.", 500),

    // 전역 예외
    EXCEPTION("exception", "An unexpected error occurred", 500)
    ;

    private final String code;
    private final String message;
    private final int status;
}
