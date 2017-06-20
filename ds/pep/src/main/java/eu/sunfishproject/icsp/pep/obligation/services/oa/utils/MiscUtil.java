/*
 * Copyright 2011 Federal Chancellery Austria and
 * Graz University of Technology
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package eu.sunfishproject.icsp.pep.obligation.services.oa.utils;



import eu.sunfishproject.icsp.pep.obligation.services.oa.exception.EgovUtilException;

import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;
import javax.xml.transform.*;
import javax.xml.transform.stream.StreamResult;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;



/**
 * Class providing several utility methods.
 *
 * @author <a href="mailto:Arne.Tauber@egiz.gv.at">Arne Tauber</a>
 *
 */
public class MiscUtil {

    public static final String DEFAULT_SLASH = "/";

    private static final int IO_BUFFER_SIZE = 4 * 1024;

    public static void copyStream(InputStream is, OutputStream os) throws IOException {
        byte[] b = new byte[IO_BUFFER_SIZE];
        int read;
        while ((read = is.read(b)) != -1) {
            os.write(b, 0, read);
        }
    }

    public static void assertNotNull(Object param, String name) {
        if (param == null) {
            throw new NullPointerException(name + " must not be null.");
        }
    }

    public static boolean areAllNull(Object... objects) {
        for (Object o : objects) {
            if (o != null) {
                return false;
            }
        }
        return true;
    }

    public static String extractContentType(String contentTypeString) {
        if (contentTypeString == null) {
            return "";
        }
        if (contentTypeString.indexOf(";") != -1) {
            return contentTypeString.substring(0, contentTypeString.indexOf(";"));
        }
        return contentTypeString;
    }

    public static XMLGregorianCalendar getXMLGregorianCalendar(Date date)
            throws DatatypeConfigurationException {
        GregorianCalendar cal = (GregorianCalendar) GregorianCalendar.getInstance();
        cal.setTime(date);
        return DatatypeFactory.newInstance().newXMLGregorianCalendar(cal);
    }

    public static XMLGregorianCalendar getXMLGregorianCalendar(String str)
            throws DatatypeConfigurationException {
        return DatatypeFactory.newInstance().newXMLGregorianCalendar(str);
    }

    public static X509Certificate readCertificate(InputStream certStream)
            throws CertificateException {
        CertificateFactory cf = CertificateFactory.getInstance("X.509");
        return (X509Certificate) cf.generateCertificate(certStream);
    }

    public static boolean isEmpty(String str) {
        return str == null || "".equals(str);
    }

    public static boolean isNotEmpty(String str) {
        return str != null && !"".equals(str);
    }

    public static byte[] sourceToByteArray(Source result)
            throws TransformerException {
        TransformerFactory factory = TransformerFactory.newInstance();
        Transformer transformer = factory.newTransformer();
        transformer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "yes");
        transformer.setOutputProperty(OutputKeys.METHOD, "xml");
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        StreamResult streamResult = new StreamResult();
        streamResult.setOutputStream(out);
        transformer.transform(result, streamResult);
        return out.toByteArray();
    }

//	public static Document parseDocument(InputStream inputStream)
//	    throws IOException {
//		try {
//			DocumentBuilderFactory docBuilderFactory = DocumentBuilderFactory
//			    .newInstance();
//			docBuilderFactory.setNamespaceAware(true);
//			DocumentBuilder docBuilder = docBuilderFactory.newDocumentBuilder();
//			return docBuilder.parse(inputStream);
//		} catch (ParserConfigurationException e) {
//			throw new IOException(e);
//		} catch (SAXException e) {
//			throw new IOException(e);
//		}
//	}

    public static String removePrecedingSlash(String path, String slash) {
        assertNotEmpty(slash, "Shash");
        if (!isEmpty(path)) {
            while (path.startsWith(slash)) {
                path = path.substring(slash.length(), path.length());
            }
        }
        return path;
    }

    public static String removePrecedingSlash(String path) {
        return removePrecedingSlash(path, DEFAULT_SLASH);
    }

    public static void assertNotEmpty(String param, String name) {
        if (param == null) {
            throw new NullPointerException(name + " must not be null.");
        }
        if (param.length() == 0) {
            throw new IllegalArgumentException(name + " must not be empty.");
        }
    }

    @SuppressWarnings("rawtypes")
    public static boolean isEmpty(Properties props) {
        if (props == null || props.isEmpty()) {
            return true;
        }
        Iterator it = props.values().iterator();
        while (it.hasNext()) {
            if (MiscUtil.isNotEmpty((String) it.next())) {
                return false;
            }
        }
        return true;
    }

    public static boolean isEmpty(Empty empty) {
        return empty == null || empty.isEmpty();
    }

    public static boolean isNotEmpty(Empty empty) {
        return !isEmpty(empty);
    }

    public static boolean isEmpty(byte[] data) {
        return data == null || data.length == 0;
    }

    public static boolean isNotEmpty(byte[] data) {
        return !isEmpty(data);
    }

    public static <T> boolean isEmpty(Collection<T> c) {
        return c == null || c.isEmpty();
    }

    public static <T> boolean isNotEmpty(Collection<T> c) {
        return !isEmpty(c);
    }

    public static boolean areAllEmpty(String... strings) {
        for (String s : strings) {
            if (s != null && s.trim().length() != 0) {
                return false;
            }
        }
        return true;
    }

    public static boolean areAllEmpty(Empty... empties) {
        if (empties != null) {
            for (Empty e : empties) {
                if (e != null && !e.isEmpty()) {
                    return false;
                }
            }
        }
        return true;
    }

    public static <T> void assertNotEmpty(T[] param, String name) {
        if (param == null) {
            throw new NullPointerException(name + " must not be null.");
        }
        if (param.length == 0) {
            throw new IllegalArgumentException(name + " must not be empty.");
        }
    }

    public static void assertNotEmpty(Empty empty, String name) {
        if (empty == null) {
            throw new NullPointerException(name + " must not be null.");
        }
        if (empty.isEmpty()) {
            throw new IllegalArgumentException(name + " must not be empty.");
        }
    }

    public static void assertNotEmpty(byte[] param, String name) {
        if (param == null) {
            throw new NullPointerException(name + " must not be null.");
        }
        if (param.length == 0) {
            throw new IllegalArgumentException(name + " must not be empty.");
        }
    }

    public static Date parseXMLDate(String xmlDate) throws EgovUtilException {
        if (xmlDate == null) {
            return null;
        }
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        try {
            return sdf.parse(xmlDate);
        } catch (ParseException e) {
            throw new EgovUtilException(e);
        }
    }

    public static <T> boolean isEmpty(T[] array) {
        return array == null || array.length == 0;
    }

    public static <T> boolean isNotEmpty(T[] array) {
        return !isEmpty(array);
    }

    public static String convertDateFromStandardToXML(String dateString) {
        MiscUtil.assertNotNull(dateString, "dateString");
        Date date = parseDate(dateString);
        return formatDate(date, "yyyy-MM-dd");
    }

    public static Date parseDate(String dateString) {
        return parseDate(dateString, "dd.MM.yyyy");
    }

    public static Date parseDate(String dateString, String pattern) {
        MiscUtil.assertNotNull(dateString, "dateString");
        MiscUtil.assertNotNull(pattern, "pattern");
        SimpleDateFormat sdf = new SimpleDateFormat(pattern);
        try {
            return sdf.parse(dateString);
        } catch (ParseException e) {
            return null;
        }
    }

    public static String formatDate(Date date, String format) {
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        return sdf.format(date);
    }

}
