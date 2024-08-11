package xyz.sorridi.arc.utils;

import org.apache.commons.text.RandomStringGenerator;
import org.bouncycastle.asn1.x500.X500Name;
import org.bouncycastle.cert.jcajce.JcaX509CertificateConverter;
import org.bouncycastle.cert.jcajce.JcaX509v3CertificateBuilder;
import org.bouncycastle.operator.jcajce.JcaContentSignerBuilder;

import java.math.BigInteger;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.cert.X509Certificate;
import java.util.Date;

public class SecureLayer
{
    private static final RandomStringGenerator generator;
    private static final long ONE_YEAR = 365L * 24 * 60 * 60 * 1000;

    static
    {
        generator = new RandomStringGenerator.Builder()
                .withinRange('0', 'z')
                .filteredBy(Character::isLetterOrDigit)
                .build();
    }

    public static X509Certificate generateCertificateAndKey(KeyPair keyPair) throws Exception
    {
        return generateCert(keyPair, "SHA512withRSA");
    }

    public static KeyPair generateKeyPair() throws NoSuchAlgorithmException
    {
        KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("RSA");
        keyPairGenerator.initialize(2048);
        return keyPairGenerator.generateKeyPair();
    }

    public static X509Certificate generateCert(KeyPair keyPair, String signatureAlgorithm) throws Exception
    {
        X500Name subject = new X500Name("CN=Self-Signed Certificate");

        long time = System.currentTimeMillis();

        Date startDate = new Date(time);
        Date endDate = new Date(time + ONE_YEAR);

        BigInteger serialNumber = BigInteger.valueOf(time);

        var certificateBuilder = new JcaX509v3CertificateBuilder(subject,
                                                                 serialNumber,
                                                                 startDate,
                                                                 endDate,
                                                                 subject,
                                                                 keyPair.getPublic());

        var signer = new JcaContentSignerBuilder(signatureAlgorithm).build(keyPair.getPrivate());

        var certificateHolder = certificateBuilder.build(signer);

        return new JcaX509CertificateConverter().getCertificate(certificateHolder);
    }

    public static String generateRandomPassword(int length)
    {
        return generator.generate(length);
    }
}
