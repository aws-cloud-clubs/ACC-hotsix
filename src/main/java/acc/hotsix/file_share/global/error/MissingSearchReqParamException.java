package acc.hotsix.file_share.global.error;

public class MissingSearchReqParamException extends Exception{
    public MissingSearchReqParamException() {
        super("Search criteria does not exist.");
    }
}
