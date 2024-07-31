package acc.hotsix.file_share.application;

import acc.hotsix.file_share.domain.File;
import acc.hotsix.file_share.global.error.FileNotFoundException;
import lombok.RequiredArgsConstructor;
import org.joda.time.LocalDate;
import org.springframework.stereotype.Service;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.Timestamp;
import java.time.LocalDateTime;
import java.util.Formatter;
import java.util.HashMap;
import java.util.List;

@Service
@RequiredArgsConstructor
public class FileShareServiceImpl implements FileShareService {
    private final FileService fileService;

    // URL의 길이
    private final int urlLength = 10;
    // 짧은 URL 생성
    public String generateShareLink(Long fileId) throws FileNotFoundException, NoSuchAlgorithmException {
        File fileMetaData = fileService.getFileById(fileId);

        HashMap<String, String> urlMap = fileService.getAllUrlMap();

        // 해시 알고리즘 설정
        final String hashAlgo = "SHA-256";
        MessageDigest md = MessageDigest.getInstance(hashAlgo);

        String shortHash = null;

        // URL 해시가 기존 URL 맵과 충돌하지 않을 때까지 반복
        do {
            LocalDateTime localDateTime = LocalDateTime.now();
            String hashInput = fileMetaData.getResource() + localDateTime.toString();
            byte[] hashBytes = md.digest(hashInput.getBytes());
            shortHash = byteArrayToHexString(hashBytes).substring(0, urlLength);
        } while (urlMap.containsValue(shortHash));

        fileMetaData.setLink(shortHash);
        fileService.saveMetaData(fileMetaData);

        return shortHash;
    }

    // 바이트 배열을 16진수 문자열로 변환
    private static String byteArrayToHexString(byte[] bytes) {
        Formatter formatter = new Formatter();
        for (byte b : bytes) {
            formatter.format("%02x", b);
        }
        String result = formatter.toString();
        formatter.close();
        return result;
    }
}
