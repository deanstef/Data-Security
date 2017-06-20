package eu.sunfishproject.icsp.proxy.certificate;

import eu.sunfishproject.icsp.proxy.certificate.provider.BCCertificateProvider;
import eu.sunfishproject.icsp.proxy.certificate.provider.IAIKCertificateProvider;
//import iaik.asn1.structures.Name;
//import iaik.utils.KeyAndCertificate;
//import iaik.x509.X509CertificateFactory;
//import iaik.x509.extensions.ExtendedKeyUsage;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
//import org.bouncycastle.util.encoders.Base64;

import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.security.auth.x500.X500Principal;
import java.io.*;
import java.math.BigInteger;
import java.net.URISyntaxException;
import java.net.URL;
import java.security.*;
import java.security.cert.Certificate;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;

/**
 * @author <a href="mailto:dominik.ziegler@a-sit.at">Dominik Ziegler</a>
 */
public class CertificateUtil {


//    private static final String KEYGEN_ALGORITHM = "RSA";
//    private static final String SECURE_RANDOM_ALGORITHM = "SHA1PRNG";
//    private static final String SIGNATURE_ALGORITHM = "SHA1WithRSAEncryption";
//    private static final int ROOT_KEYSIZE = 2048;
//    private static final int NEW_KEYSIZE = 1024;
//
//    private static final Logger log = LogManager.getLogger(CertificateUtil.class);
//
//
//
//    public static final SSLContext createSSLContext(Certificate remoteCertificate) {
//
//        try {
//
//            KeyPair serverKey = generateKeyPair(NEW_KEYSIZE);
//            X509Certificate serverCert = generateServerCert(remoteCertificate, serverKey);
//            SSLContext sslContext = generateSSLContext(serverCert, serverKey.getPrivate());
//
//            return sslContext;
//
//        } catch(Exception e) {
//            e.printStackTrace();
//        }
//
//        return null;
//
//    }
//
//
//    public static final SSLContext createSSLContext(String commonName) {
//
//        try {
//
//            KeyPair serverKey = generateKeyPair(NEW_KEYSIZE);
//
//            X509Certificate serverCert = generateServerCert(commonName, serverKey);
//            SSLContext sslContext = generateSSLContext(serverCert, serverKey.getPrivate());
//
//            return sslContext;
//
//        } catch(Exception e) {
//            e.printStackTrace();
//        }
//
//        return null;
//
//    }
//
//
//    public static final X509Certificate generateServerCert(Certificate remoteCertificate, KeyPair serverKey) {
//
//        try {
//
//            CertificateFactory cf = CertificateFactory.getInstance("X.509");
//            ByteArrayInputStream bais = new ByteArrayInputStream(remoteCertificate.getEncoded());
//            X509Certificate remoteX509 = (X509Certificate) cf.generateCertificate(bais);
//
//
//            KeyStore.PrivateKeyEntry rootPrivateKey = getRootPrivateKeyFromKeystore();
//            X509Certificate rootCert = getCertFromPrivateKey(rootPrivateKey);
//
//
//            BigInteger serialNumber = BigInteger.valueOf(new SecureRandom().nextLong());
////        BigInteger serialNumber = remoteCertificate.getSerialNumber();
//
//            Date notBefore = new Date(System.currentTimeMillis() - 86400000L * 365);
//            Date notAfter = new Date(System.currentTimeMillis() + 86400000L * 365);
//
//
//            X509Certificate serverCert = IAIKCertificateProvider.generateCertificate(remoteX509, serverKey, rootCert, rootPrivateKey, serialNumber, notBefore, notAfter);
//
//
//            return serverCert;
//
//        } catch(Exception e) {
//            e.printStackTrace();
//        }
//
//        return null;
//
//    }
//
//
//    public static final X509Certificate generateServerCert(String commonName, KeyPair serverKey) {
//
//        try {
//
//            KeyStore.PrivateKeyEntry rootPrivateKey = getRootPrivateKeyFromKeystore();
//            X509Certificate rootCert = getCertFromPrivateKey(rootPrivateKey);
//
//            Name issuerDN = new Name(rootCert.getSubjectX500Principal().getEncoded());
//            Name subjectDN = generateSubjectDN(commonName);
//            Collection<List<?>> subjectAlternativeNames = new ArrayList<>();
//            int basicConstraints = -1;
//            boolean[] keyUsage = null;
//            List<String> extendedKeyUsage = generateExtendedKeyUsage();
//            BigInteger serialNumber = BigInteger.valueOf(new SecureRandom().nextLong());
//            Date notBefore = new Date(System.currentTimeMillis() - 86400000L * 365);
//            Date notAfter = new Date(System.currentTimeMillis() + 86400000L * 365);
//
//
//            return IAIKCertificateProvider.generateCertificate(issuerDN, subjectDN, subjectAlternativeNames, basicConstraints, keyUsage, extendedKeyUsage, serverKey, rootCert, rootPrivateKey, serialNumber, notBefore, notAfter);
//
//
//        } catch(Exception e) {
//            e.printStackTrace();
//        }
//
//        return null;
//
//    }
//
//
//
//    private static KeyPair generateKeyPair(int keySize) throws NoSuchAlgorithmException, NoSuchProviderException {
//        KeyPairGenerator generator = KeyPairGenerator.getInstance(KEYGEN_ALGORITHM/* , PROVIDER_NAME */);
//        SecureRandom secureRandom = SecureRandom
//                .getInstance(SECURE_RANDOM_ALGORITHM/* , PROVIDER_NAME */);
//        generator.initialize(keySize, secureRandom);
//        return generator.generateKeyPair();
//    }
//
//
//
//    private static SSLContext generateSSLContext(X509Certificate certificate, PrivateKey privateKey) throws IOException, GeneralSecurityException {
//
//        java.security.cert.Certificate[] originalChain = new java.security.cert.Certificate[]{certificate};
//
//        KeyStore keyStore = KeyStore.getInstance("jks");
//        keyStore.load(null, "password".toCharArray());
//        keyStore.setKeyEntry("mitm", privateKey, "password".toCharArray(), originalChain);
//
//        KeyManagerFactory keyManagerFactory = KeyManagerFactory.getInstance(KeyManagerFactory.getDefaultAlgorithm());
//        keyManagerFactory.init(keyStore, "password".toCharArray());
//
//        TrustManager[] trustManagers = new TrustManager[]{new TrustEveryone()};
//
//        SSLContext sslContext = SSLContext.getInstance("TLS");
//        sslContext.init(keyManagerFactory.getKeyManagers(), trustManagers, null);
//
//        return sslContext;
//
//    }
//
//
//    private static KeyStore.PrivateKeyEntry getRootPrivateKeyFromKeystore() throws KeyStoreException, UnrecoverableEntryException, NoSuchAlgorithmException, URISyntaxException, IOException, CertificateException {
//
//        InputStream keyStoreStream = CertificateUtil.class.getResourceAsStream("/root[password].jks");
//
//        if(keyStoreStream == null) {
//            throw new IOException("Could not find keystore!");
//        }
//        final char[] keyStorePassword = "password".toCharArray();
////        final String keyStoreType = "PKCS12";
//        final String keyStoreType = "jks";
//
//        String keyAlias = "root";
//
//        final KeyStore keyStore = KeyStore.getInstance(keyStoreType);
//        keyStore.load(keyStoreStream, keyStorePassword);
//
//        KeyStore.PrivateKeyEntry rootPrivateKey = (KeyStore.PrivateKeyEntry) keyStore.getEntry(keyAlias, new KeyStore.PasswordProtection(keyStorePassword));
//
//
//        return rootPrivateKey;
//
//    }
//
//    private static X509Certificate getCertFromPrivateKey(KeyStore.PrivateKeyEntry privateKey) throws CertificateException {
//
//        byte data[] = privateKey.getCertificate().getEncoded();
//
//
//        CertificateFactory factory = CertificateFactory.getInstance("X.509");
//        InputStream in = new ByteArrayInputStream(data);
//        X509Certificate cert = (X509Certificate) factory.generateCertificate(in);
//
//        return cert;
//    }
//
//
//
//
//
//
//        private static class TrustEveryone implements javax.net.ssl.X509TrustManager {
//        @Override
//        public void checkClientTrusted(java.security.cert.X509Certificate[] chain, String authenticationType) {
//        }
//
//        @Override
//        public void checkServerTrusted(java.security.cert.X509Certificate[] chain, String authenticationType) {
//        }
//
//        public java.security.cert.X509Certificate[] getAcceptedIssuers() {
//            return null;
//        }
//    }
//
//
//    private static void printCertificate(X509Certificate certificate, X509Certificate signator)  {
//
//
//
//        try {
//
//            certificate.checkValidity(new Date());
//            certificate.verify(signator.getPublicKey());
//
//
//            System.out.println("-----BEGIN CERTIFICATE-----");
//            System.out.println(new String(Base64.encode(certificate.getEncoded())));
//            System.out.println("-----END CERTIFICATE-----");
//
//        } catch(Exception e) {
//
//            log.error(e);
//        }
//
//
//    }
//
//    private static Name generateSubjectDN(String commonName) {
//
//        Name subjectDN = new iaik.asn1.structures.Name();
//
//        subjectDN.addRDN(iaik.asn1.ObjectID.country,"EU");
//        subjectDN.addRDN(iaik.asn1.ObjectID.organization,"Sunfish");
//        subjectDN.addRDN(iaik.asn1.ObjectID.commonName,commonName);
//
//        return subjectDN;
//    }
//
//
//    private static List<String> generateExtendedKeyUsage() {
//        List<String> extendedKeyUsage = new ArrayList<>();
//
//        String serverAuth = ExtendedKeyUsage.serverAuth.getValue().toString();
//        String clientAuth = ExtendedKeyUsage.clientAuth.getValue().toString();
//
//        extendedKeyUsage.add(serverAuth);
//        extendedKeyUsage.add(clientAuth);
//
//        return extendedKeyUsage;
//
//    }


}
