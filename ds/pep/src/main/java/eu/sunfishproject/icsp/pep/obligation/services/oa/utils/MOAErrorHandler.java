/*
 * Copyright 2003 Federal Chancellery Austria
 * MOA-ID has been developed in a cooperation between BRZ, the Federal
 * Chancellery Austria - ICT staff unit, and Graz University of Technology.
 *
 * Licensed under the EUPL, Version 1.1 or - as soon they will be approved by
 * the European Commission - subsequent versions of the EUPL (the "Licence");
 * You may not use this work except in compliance with the Licence.
 * You may obtain a copy of the Licence at:
 * http://www.osor.eu/eupl/
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the Licence is distributed on an "AS IS" basis,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the Licence for the specific language governing permissions and
 * limitations under the Licence.
 *
 * This product combines work with different licenses. See the "NOTICE" text
 * file for details on the various modules and licenses.
 * The "NOTICE" text file is part of the distribution. Any derivative works
 * that you distribute must include a readable copy of the "NOTICE" text file.
 */


package eu.sunfishproject.icsp.pep.obligation.services.oa.utils;

import org.apache.xml.utils.DefaultErrorHandler;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;

/**
 * An <code>ErrorHandler</code> that logs a message and throws a
 * <code>SAXException</code> upon <code>error</code> and <code>fatal</code>
 * parsing errors.
 * 
 * @author Patrick Peck
 * @author Sven Aigner
 */
public class MOAErrorHandler extends DefaultErrorHandler {

	
  /**
   * Logs a warning message.
   * 
   * @see org.xml.sax.ErrorHandler#warning(SAXParseException)
   */
  public void warning(SAXParseException exception) throws SAXException {
    warn("parser.00", messageParams(exception), null);
  }

  /**
   * Logs a warning and rethrows the <code>exception</code>.
   * 
   * @see org.xml.sax.ErrorHandler#error(SAXParseException)
   */
  public void error(SAXParseException exception) throws SAXException {
	  warn("parser.01", messageParams(exception), null);
	  
	  // if Target attribute is missing in QualifyingProperties - don't throw exception (bug fix for old MOCCA signatures)
	  if (exception.getMessage().startsWith("cvc-complex-type.4: Attribute 'Target' must appear on element"))
		  warn("parser.04", new Object[] {"Attribute 'Target' must appear on element 'QualifyingProperties' - ignored for compatibility reasons."}, null);
	  else
		  throw exception;    
  }

  /**
   * Logs a warning and rethrows the <code>exception</code>.
   * 
   * @see org.xml.sax.ErrorHandler#fatalError(SAXParseException)
   */
  public void fatalError(SAXParseException exception) throws SAXException {
    warn("parser.02", messageParams(exception), null);
    throw exception;
  }

  /**
   * Log a warning message.
   * 
   * @param messageId The message ID to log.
   * @param parameters Additional message parameters.
   * @param t The <code>Throwable</code> to log; usually the cause of this
   * warning.
   */
  private static void warn(
    String messageId,
    Object[] parameters,
    Throwable t) {

//    MessageProvider msg = MessageProvider.getInstance();
//    Logger.warn(new LogMsg(msg.getMessage(messageId, parameters)), t);
  }

  /**
   * Put the system id, line and column number information from the exception
   * into an <code>Object</code> array, to provide it as a
   * <code>MessageFormat</code> parameter.
   * 
   * @param e The <code>SAXParseException</code> containing the
   * source system id and line/column numbers.
   * @return An array containing the system id (a <code>String</code>) as well
   * as line/column numbers (2 <code>Integer</code> objects) from the
   * <code>SAXParseException</code>.
   */
  private static Object[] messageParams(SAXParseException e) {
    return new Object[] {
      e.getMessage(),
      e.getSystemId(),
      new Integer(e.getLineNumber()),
      new Integer(e.getColumnNumber())};
  }

}