package acc.hotsix.file_share.global.error;

public class FileNotFoundException extends Exception {
    public FileNotFoundException(String fileName) {
        super("The file '" + fileName + "' does not exist.");
    }
}
