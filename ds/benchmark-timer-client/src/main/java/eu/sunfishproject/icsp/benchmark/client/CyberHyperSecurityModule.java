package eu.sunfishproject.icsp.benchmark.client;

import com.google.common.io.BaseEncoding;
import io.jsonwebtoken.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.bouncycastle.jce.provider.BouncyCastleProvider;

import java.security.KeyFactory;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.Security;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Date;
import java.util.Map.Entry;
import java.util.Set;

public class CyberHyperSecurityModule {

  private final Logger                          log = LogManager.getLogger(CyberHyperSecurityModule.class);

  private static CyberHyperSecurityModule instance;
  private final PublicKey                 pubKey;
  private final PrivateKey                privKey;

  public static CyberHyperSecurityModule getInstance() throws Exception {
    if (instance == null) {
      instance = new CyberHyperSecurityModule();
    }
    return instance;
  }

  private CyberHyperSecurityModule() throws Exception {
    Security.addProvider(new BouncyCastleProvider());
    final KeyFactory keyFactory = KeyFactory.getInstance("ECDSA", "BC");
    final X509EncodedKeySpec pubKeySpec = new X509EncodedKeySpec(BaseEncoding.base64().decode(
        "MIGbMBAGByqGSM49AgEGBSuBBAAjA4GGAAQBX4Zi6LsN0Y2LbJflD0LISklwOuC0LrPAfNEWRmO6utop8W+OXnJliO7GJ3ZFIIl+avveTfZo0+ncoxPROyRr6ccAjAOHdqWC7U2auq8LKAvfDh9R1p8H1rhu+GE4AMrpd2jp8V3Kib+Pbce1h/liHUEc1TDFBr3y42mkT70YA/hf8Io="));
    pubKey = keyFactory.generatePublic(pubKeySpec);

    final PKCS8EncodedKeySpec privKeySpec = new PKCS8EncodedKeySpec(BaseEncoding.base64().decode(
        "MIH3AgEAMBAGByqGSM49AgEGBSuBBAAjBIHfMIHcAgEBBEIBdeBO3UM2zX7mxL8Pdx9fT8KSVhSYipKpxunkw/DCBja5ABBnI5dmfRrR3BUtUq76IXnkUDFQl0RcCmIIBg8NF3WgBwYFK4EEACOhgYkDgYYABAFfhmLouw3RjYtsl+UPQshKSXA64LQus8B80RZGY7q62inxb45ecmWI7sYndkUgiX5q+95N9mjT6dyjE9E7JGvpxwCMA4d2pYLtTZq6rwsoC98OH1HWnwfWuG74YTgAyul3aOnxXcqJv49tx7WH+WIdQRzVMMUGvfLjaaRPvRgD+F/wig=="));
    privKey = keyFactory.generatePrivate(privKeySpec);

  }

  public String genJWT(final String issuer, final String audience, final Date exp) {
    final JwtBuilder bld = Jwts.builder();
    return bld.setSubject(issuer).setIssuer(issuer).setIssuedAt(new Date(System.currentTimeMillis()))
        .setAudience(audience).setExpiration(exp).signWith(SignatureAlgorithm.ES512, privKey).compact();
  }

  public boolean validateJWS(final String jws) throws Exception {
    try {
      final Claims claims = Jwts.parser().setSigningKey(pubKey).parseClaimsJws(jws).getBody();
      final Set<Entry<String, Object>> entrySet = claims.entrySet();
      for (final Entry<String, Object> e : entrySet) {
        log.debug(e.getKey() + ": " + e.getValue());
      }
      return true;

    } catch (final SignatureException e) {
      return false;
    }

  }

}
