package acc.hotsix.file_share.global.error.exception;

public class FileTypeMismatchException extends Exception {
    public FileTypeMismatchException(String fileName, String expectedExtension, String actualExtension) {
        super("File '" + fileName + "' extension mismatch: expected extension '" + expectedExtension + "', actual extension '" + actualExtension + "'");
    }
}
