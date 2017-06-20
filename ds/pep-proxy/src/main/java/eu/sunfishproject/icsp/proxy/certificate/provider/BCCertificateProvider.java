package eu.sunfishproject.icsp.proxy.certificate.provider;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
//import org.bouncycastle.asn1.ASN1ObjectIdentifier;
//import org.bouncycastle.asn1.ASN1Sequence;
//import org.bouncycastle.asn1.x500.X500Name;
//import org.bouncycastle.asn1.x509.*;
//import org.bouncycastle.cert.CertIOException;
//import org.bouncycastle.cert.X509v3CertificateBuilder;
//import org.bouncycastle.cert.jcajce.JcaX509CertificateConverter;
//import org.bouncycastle.cert.jcajce.JcaX509ExtensionUtils;
//import org.bouncycastle.cert.jcajce.JcaX509v3CertificateBuilder;
//import org.bouncycastle.operator.ContentSigner;
//import org.bouncycastle.operator.OperatorCreationException;
//import org.bouncycastle.operator.jcajce.JcaContentSignerBuilder;

import javax.security.auth.x500.X500Principal;
import java.math.BigInteger;
import java.security.*;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.*;

/**
 * @author <a href="mailto:dominik.ziegler@a-sit.at">Dominik Ziegler</a>
 */
public class BCCertificateProvider {

//
//    private static final Logger log = LogManager.getLogger(BCCertificateProvider.class);
//
//
//
//    public static X509Certificate generateCertificate(X509Certificate remoteCertificate, KeyPair serverKey,  X509Certificate rootCert, KeyStore.PrivateKeyEntry rootPrivateKey, BigInteger serialNumber, Date notBefore, Date notAfter) throws CertIOException, NoSuchAlgorithmException, CertificateException, OperatorCreationException {
//
//
//        X500Name issuer = X500Name.getInstance(rootCert.getSubjectX500Principal().getEncoded());
//        X500Name subject = X500Name.getInstance(remoteCertificate.getSubjectX500Principal().getEncoded());
//
//        PublicKey publicKey = serverKey.getPublic();
//
//
//        X509v3CertificateBuilder certificateBuilder = new JcaX509v3CertificateBuilder(issuer, serialNumber, notBefore,
//                notAfter, subject,  publicKey);
//
//        JcaX509ExtensionUtils extUtils = new JcaX509ExtensionUtils();
//        PublicKey rootPublicKey = rootCert.getPublicKey();
//        AuthorityKeyIdentifier authorityKeyIdentifier = extUtils.createAuthorityKeyIdentifier(rootPublicKey);
//        certificateBuilder.addExtension(Extension.authorityKeyIdentifier, false, authorityKeyIdentifier);
//
//        GeneralNames subjectAlternativeName =  getSubjectAlternativeNames(remoteCertificate.getSubjectAlternativeNames());
//        certificateBuilder.addExtension(Extension.subjectAlternativeName, false, subjectAlternativeName);
//
//
//        int basicContraint = remoteCertificate.getBasicConstraints();
//        BasicConstraints basicConstraints = basicContraint == -1 ? new BasicConstraints(false) : new BasicConstraints(basicContraint);
//        certificateBuilder.addExtension(Extension.basicConstraints, false, basicConstraints);
//
//        KeyUsage keyUsage =  new KeyUsage(getKeyUsage(remoteCertificate.getKeyUsage()));
//        certificateBuilder.addExtension(Extension.keyUsage, true, keyUsage);
//
//        ExtendedKeyUsage extendedKeyUsage = getExtendedKeyUsage(remoteCertificate.getExtendedKeyUsage());
//        certificateBuilder.addExtension(Extension.extendedKeyUsage, false, extendedKeyUsage);
//
//        X509Certificate serverCert = signCertificate(certificateBuilder, rootPrivateKey.getPrivateKey());
//
//        return serverCert;
//
//    }
//
//
//
//
//    private static X509Certificate signCertificate(X509v3CertificateBuilder certificateBuilder, PrivateKey privateKey) throws OperatorCreationException,
//            CertificateException {
//
//        try {
//            ContentSigner signer = new JcaContentSignerBuilder("SHA1withRSA").build(privateKey);
//
//            X509Certificate cert = new JcaX509CertificateConverter().getCertificate(certificateBuilder.build(signer));
//
//            return cert;
//        } catch (Exception e) {
//            e.printStackTrace();
//
//        }
//
//        return null;
//    }
//
//
//
//
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
//    private static GeneralNames getSubjectAlternativeNames(Collection<List<?>> subjectAlternativeNames) {
//
//
//
//        List<GeneralName> encodedSubjectAlternativeNames = new ArrayList<>(subjectAlternativeNames.size());
//
//        Iterator it = subjectAlternativeNames.iterator();
//        while (it.hasNext()) {
//            List list = (List) it.next();
//            int type = ((Integer) list.get(0)).intValue();
//            String subjectAlternativeName = (String) list.get(1);
//
//            GeneralName generalName = new GeneralName(type, subjectAlternativeName);
//            encodedSubjectAlternativeNames.add(generalName);
//
//        }
//
//        return new GeneralNames(encodedSubjectAlternativeNames.toArray(new GeneralName[encodedSubjectAlternativeNames.size()]));
//
//    }
//
//    private static ExtendedKeyUsage getExtendedKeyUsage(List<String> extendedKeyUsage) {
//
//        ArrayList<KeyPurposeId> keyPurposeIds = new ArrayList<>(extendedKeyUsage.size());
//
//        for(String keyPurpose : extendedKeyUsage) {
//
//            ASN1ObjectIdentifier objectIdentifier = new ASN1ObjectIdentifier(keyPurpose);
//
//            KeyPurposeId keyPurposeId =  KeyPurposeId.getInstance(objectIdentifier);
//            keyPurposeIds.add(keyPurposeId);
//        }
//
//
//        return new ExtendedKeyUsage(keyPurposeIds.toArray(new KeyPurposeId[keyPurposeIds.size()]));
//
//    }



}
