package eu.sunfishproject.icsp.proxy.certificate;

//import org.bouncycastle.asn1.ASN1ObjectIdentifier;
//import org.bouncycastle.asn1.x500.X500Name;
//import org.bouncycastle.asn1.x509.BasicConstraints;
//import org.bouncycastle.cert.X509v3CertificateBuilder;
//import org.bouncycastle.cert.jcajce.JcaX509CertificateConverter;
//import org.bouncycastle.cert.jcajce.JcaX509v3CertificateBuilder;
//import org.bouncycastle.jce.X509KeyUsage;
//import org.bouncycastle.operator.ContentSigner;
//import org.bouncycastle.operator.jcajce.JcaContentSignerBuilder;

import javax.net.ssl.KeyManagerFactory;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.math.BigInteger;
import java.security.*;
import java.security.cert.*;
import java.util.Date;

/**
 * @author <a href="mailto:dominik.ziegler@a-sit.at">Dominik Ziegler</a>
 */
public class CACreator {

//    private static final Date NOT_BEFORE = new Date(System.currentTimeMillis() - 86400000L * 365);
//    private static final Date NOT_AFTER = new Date(System.currentTimeMillis() + 86400000L * 365);
//
//
//    private static final int KEY_SIZE = 2048;
//
//
//    private static final String DESKTOP_FOLDER = System.getProperty("user.home") + File.separator + "Desktop";
//
//
//    public static void main(String[] args) {
//
//
//        try {
//            KeyPairGenerator rsa = KeyPairGenerator.getInstance("RSA");
//            rsa.initialize(KEY_SIZE);
//            KeyPair keyPair = rsa.generateKeyPair();
//
//
//            byte[] pk = keyPair.getPublic().getEncoded();
//
//
//            X500Name subject = new X500Name("CN = SUNFISH PROXY CA");
//            BigInteger serialNumber = BigInteger.valueOf(new SecureRandom().nextLong());
//
//
//
//            X509v3CertificateBuilder certificateBuilder =
//                    new JcaX509v3CertificateBuilder(
//                            subject,
//                            serialNumber,
//                            NOT_BEFORE,
//                            NOT_AFTER,
//                            subject,
//                            keyPair.getPublic())
//                            .addExtension(
//                                    new ASN1ObjectIdentifier("2.5.29.19"),
//                                    false,
//                                    new BasicConstraints(true))
//                            .addExtension(
//                                    new ASN1ObjectIdentifier("2.5.29.15"),
//                                    true,
//                                    new X509KeyUsage(
//                                            X509KeyUsage.digitalSignature |
//                                                    X509KeyUsage.nonRepudiation   |
//                                                    X509KeyUsage.keyEncipherment  |
//                                                    X509KeyUsage.dataEncipherment |
//                                                    X509KeyUsage.cRLSign |
//                                                    X509KeyUsage.keyCertSign));
//
//
//
//            ContentSigner signer = new JcaContentSignerBuilder("SHA1withRSA").build(keyPair.getPrivate());
//
//            X509Certificate cert = new JcaX509CertificateConverter().getCertificate(certificateBuilder.build(signer));
//
//
//            writeCertificate(cert);
//            generateKeyStore(cert, keyPair.getPrivate());
//
//
//
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//
//        System.exit(0);
//
//
//    }
//
//
//
//
//    private static void writeCertificate(X509Certificate certificate) throws IOException {
//        PemFile pemFile = new PemFile(certificate, "CERTIFICATE");
//        writePemFile(pemFile, "ca-cert.pem");
//
//    }
//
//    private static void writePemFile(PemFile pemFile, String fileName) throws IOException {
//        File directory = new File(DESKTOP_FOLDER);
//
//        if (directory.exists() || directory.mkdirs()) {
//            // Path either exists or was created
//            File file = new File(DESKTOP_FOLDER + File.separator + fileName);
//            pemFile.write(file);
//
//        } else {
//            // The path could not be created for some reason
//        }
//
//    }
//
//    private static void generateKeyStore(X509Certificate certificate, PrivateKey privateKey) throws IOException, GeneralSecurityException {
//
//        java.security.cert.Certificate[] originalChain = new java.security.cert.Certificate[]{certificate};
//
//        KeyStore keyStore = KeyStore.getInstance("jks");
//        keyStore.load(null, "password".toCharArray());
//        keyStore.setKeyEntry("root", privateKey, "password".toCharArray(), originalChain);
//
//        KeyManagerFactory keyManagerFactory = KeyManagerFactory.getInstance(KeyManagerFactory.getDefaultAlgorithm());
//        keyManagerFactory.init(keyStore, "password".toCharArray());
//
//        File file = new File(DESKTOP_FOLDER + File.separator + "root[password].jks");
//        FileOutputStream fileOutputStream = new FileOutputStream(file);
//
//        keyStore.store(fileOutputStream, "password".toCharArray());
//
//    }



}
