package acc.hotsix.file_share.global.error;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum ExceptionCode {
    // Common

    // File 공통 오류
    INVALID_PASSWORD("File-001", "Access denied: invalid password", 400),
    FILE_DUPLICATE("File-002", "fileName already exists in the bucket.", 400),
    FILE_NOT_FOUND("File-003", "file does not exist.", 404),
    FILE_TYPE_MISMATCH("File-004", "The registered file does not match the extension type.", 400),

    // File 검색 오류
    MISSING_SEARCH_RESULT("Search-001", "No search results found.", 404),
    INVALID_QUERY_PARAM("Search-002", "Only 'asc' or 'desc' can be entered for the name and time parameters.", 400),
    MISSING_SEARCH_PARAM("Search-003", "At least one search condition must be entered.", 400),

    // File api 실행 오류
    UPLOAD_ERROR("API-001", "An error occurred during file upload.", 500),
    DOWNLOAD_ERROR("API-002", "An error occurred during file download.", 500),
    DETAIL_ERROR("API-003", "An error occurred during file read.", 500),
    SHARE_ERROR("API-004", "An error occurred during file share.", 500),
    DELETE_ERROR("API-005", "An error occurred while generating the file share link.", 500)
    ;

    private final String code;
    private final String message;
    private final int status;
}
