package org.sunfish.icsp.common.xacml.exceptions;

/**
 * @author <a href="mailto:dominik.ziegler@a-sit.at">Dominik Ziegler</a>
 */
public class XACMLBuilderException extends Exception {


    public XACMLBuilderException(String message) {
        super(message);
    }

    public XACMLBuilderException(String message, Throwable throwable) {
        super(message, throwable);
    }


}
