package eu.sunfishproject.icsp.proxy.certificate.provider;


//import iaik.asn1.CodingException;
//import iaik.asn1.ObjectID;
//import iaik.asn1.structures.AlgorithmID;
//import iaik.asn1.structures.GeneralName;
//import iaik.asn1.structures.GeneralNames;
//import iaik.asn1.structures.Name;
//import iaik.utils.RFC2253NameParserException;
//import iaik.x509.X509ExtensionException;
//import iaik.x509.extensions.*;

import java.math.BigInteger;
import java.security.*;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.*;

/**
 * @author <a href="mailto:dominik.ziegler@a-sit.at">Dominik Ziegler</a>
 */
public class IAIKCertificateProvider {

//
//    public static X509Certificate generateCertificate(X509Certificate remoteCertificate, KeyPair serverKey,  X509Certificate rootCert, KeyStore.PrivateKeyEntry rootPrivateKey, BigInteger serialNumber, Date notBefore, Date notAfter) throws InvalidKeyException, CertificateException, NoSuchAlgorithmException, RFC2253NameParserException, X509ExtensionException, CodingException {
//        Name issuerDN = new Name(rootCert.getSubjectX500Principal().getEncoded());
//        Name subjectDN = new Name(remoteCertificate.getSubjectX500Principal().getEncoded());
//        Collection<List<?>> subjectAlternativeNames = remoteCertificate.getSubjectAlternativeNames();
//        int basicConstraints = remoteCertificate.getBasicConstraints();
//        boolean[] keyUsage = remoteCertificate.getKeyUsage();
//        List<String> extendedKeyUsage = remoteCertificate.getExtendedKeyUsage();
//
//        return generateCertificate(issuerDN, subjectDN, subjectAlternativeNames, basicConstraints, keyUsage, extendedKeyUsage, serverKey, rootCert, rootPrivateKey, serialNumber, notBefore, notAfter);
//    }
//
//
//    public static X509Certificate generateCertificate(Name issuerDN, Name subjectDN, Collection<List<?>> subjectAlternativeNames, int basicConstraints, boolean[] keyUsage, List<String> extendedKeyUsage, KeyPair serverKey, X509Certificate rootCert, KeyStore.PrivateKeyEntry rootPrivateKey, BigInteger serialNumber, Date notBefore, Date notAfter) throws InvalidKeyException, CertificateException, NoSuchAlgorithmException, RFC2253NameParserException, X509ExtensionException, CodingException {
//
//        PublicKey publicKey = serverKey.getPublic();
//
//
//        iaik.x509.X509Certificate serverCert = new iaik.x509.X509Certificate();
//        serverCert.setIssuerDN(issuerDN);
//        serverCert.setSubjectDN(subjectDN);
//        serverCert.setSerialNumber(serialNumber);
//        serverCert.setValidNotBefore(notBefore);
//        serverCert.setValidNotAfter(notAfter);
//        serverCert.setPublicKey(publicKey);
//
//
//
//        byte[] aki = new SubjectKeyIdentifier(rootCert.getPublicKey()).get();
//        AuthorityKeyIdentifier authorityKeyIdentifier = new AuthorityKeyIdentifier(aki);
//        serverCert.addExtension(authorityKeyIdentifier);
//
//
//        SubjectAltName subjectAltName = new SubjectAltName();
//        subjectAltName.setGeneralNames(getSubjectAlternativeNames(subjectAlternativeNames));
//        serverCert.addExtension(subjectAltName);
//
//
//        BasicConstraints basicConstraintsExtension = new BasicConstraints(false, basicConstraints);
//        serverCert.addExtension(basicConstraintsExtension);
//
//        KeyUsage keyUsageExtension = new KeyUsage(getKeyUsage(keyUsage));
//        keyUsageExtension.setCritical(true);
//        serverCert.addExtension(keyUsageExtension);
//
//        ExtendedKeyUsage extendedKeyUsageExtension = getExtendedKeyUsage(extendedKeyUsage);
//        serverCert.addExtension(extendedKeyUsageExtension);
//
//        serverCert.sign(AlgorithmID.sha256WithRSAEncryption, rootPrivateKey.getPrivateKey(), "IAIK");
//
//        return serverCert;
//
//    }
//
//
//
//
//    private static int getKeyUsage(boolean[] array) {
//
//        int keyUsage = 0;
//
//        if(array == null || array.length != 9) {
//            return  KeyUsage.digitalSignature |
//                    KeyUsage.nonRepudiation |
//                    KeyUsage.keyEncipherment |
//                    KeyUsage.dataEncipherment;
//        }
//
//        if (array[0]) {
//            keyUsage = keyUsage | KeyUsage.digitalSignature;
//        }
//        if (array[1]) {
//            keyUsage = keyUsage | KeyUsage.nonRepudiation;
//        }
//        if (array[2]) {
//            keyUsage = keyUsage | KeyUsage.keyEncipherment;
//        }
//        if (array[3]) {
//            keyUsage = keyUsage | KeyUsage.dataEncipherment;
//        }
//        if (array[4]) {
//            keyUsage = keyUsage | KeyUsage.keyAgreement;
//        }
//        if (array[5]) {
//            keyUsage = keyUsage | KeyUsage.keyCertSign;
//        }
//        if (array[6]) {
//            keyUsage = keyUsage | KeyUsage.cRLSign;
//        }
//        if (array[7]) {
//            keyUsage = keyUsage | KeyUsage.encipherOnly;
//        }
//        if (array[8]) {
//            keyUsage = keyUsage | KeyUsage.decipherOnly;
//        }
//        return keyUsage;
//
//    }
//
//
//    private static ExtendedKeyUsage getExtendedKeyUsage(List<String> extendedKeyUsage) {
//
//        ArrayList<ObjectID> keyPurposeIds = new ArrayList<>(extendedKeyUsage.size());
//
//        for(String keyPurpose : extendedKeyUsage) {
//
//            ObjectID keyPurposeId = new ObjectID(keyPurpose);
//            keyPurposeIds.add(keyPurposeId);
//        }
//
//
//        return new ExtendedKeyUsage(keyPurposeIds.toArray(new ObjectID[keyPurposeIds.size()]));
//
//    }
//
//
//    private static GeneralNames getSubjectAlternativeNames(Collection<List<?>> subjectAlternativeNames) {
//
//        GeneralNames generalNames = new GeneralNames();
//
//        Iterator it = subjectAlternativeNames.iterator();
//        while (it.hasNext()) {
//            List list = (List) it.next();
//            int type = ((Integer) list.get(0)).intValue();
//            String subjectAlternativeName = (String) list.get(1);
//
//            GeneralName generalName = new GeneralName(type, subjectAlternativeName);
//            generalNames.addName(generalName);
//
//        }
//
//        return generalNames;
//    }

}
