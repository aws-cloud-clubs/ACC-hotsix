package acc.hotsix.file_share.global.error;

public class UploadFileException extends Exception {
    public UploadFileException() {
        super("An error occurred during file upload.");
    }
}
