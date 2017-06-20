package eu.sunfishproject.icsp.pep.obligation.services.masking;

/**
 * @author <a href="mailto:dominik.ziegler@a-sit.at">Dominik Ziegler</a>
 */
public enum MaskingDataType {

    TYPE_JSON("json"),
    TYPE_XML("xml"),
    TYPE_TEXT("text")
    ;

    private final String text;


    private MaskingDataType(final String text) {
        this.text = text;
    }


    @Override
    public String toString() {
        return text;
    }

}
