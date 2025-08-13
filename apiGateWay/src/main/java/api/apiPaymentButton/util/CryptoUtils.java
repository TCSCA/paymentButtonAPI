package api.apiPaymentButton.util;

import jakarta.annotation.PostConstruct;
import org.bouncycastle.crypto.CryptoException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

import javax.crypto.*;
import javax.crypto.spec.SecretKeySpec;
import java.io.*;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.NoSuchAlgorithmException;


@PropertySource(value = "classpath:routes.properties")
@Component
public class CryptoUtils {

    private static final String ALGORITHM = "AES";
    private static final String TRANSFORMATION = "AES";

    @Value("${route.int.file.directory}")
    private String inputFileRoute;

    @Value("${file.encrypt.key}")
    private String encryptedKey;

    @Value("${route.int.file.directory.output}")
    private String outputFileRoute;

    private static String input;

    private static String output;

    private static String encryptedKeyValue;

    @PostConstruct
    public void init() {
        input = inputFileRoute;
        output = outputFileRoute;
        encryptedKeyValue = encryptedKey;
    }


    public static void encrypt()
            throws CryptoException {
        doCrypto(Cipher.ENCRYPT_MODE, input, output);
    }

    private static void doCrypto(int cipherMode, String inputFileRoute, String outputFileRoute) throws CryptoException {
        try {

            Key secretKey = new SecretKeySpec(encryptedKeyValue.getBytes(), ALGORITHM);
            Cipher cipher = Cipher.getInstance(TRANSFORMATION);
            cipher.init(cipherMode, secretKey);

            File inputFile = new File(inputFileRoute);
            FileInputStream inputStream = new FileInputStream(inputFile);
            byte[] inputBytes = new byte[(int) inputFile.length()];
            inputStream.read(inputBytes);

            byte[] outputBytes = cipher.doFinal(inputBytes);

            FileOutputStream outputStream = new FileOutputStream(outputFileRoute);
            outputStream.write(outputBytes);

            inputStream.close();
            outputStream.close();

        } catch (NoSuchPaddingException | NoSuchAlgorithmException
                 | InvalidKeyException | BadPaddingException
                 | IllegalBlockSizeException | IOException ex) {
            throw new CryptoException("Error encrypting/decrypting file", ex);
        }
    }

    public static String decrypt(String fileName) throws FileNotFoundException {
        String content;

        File inputFile = new File(fileName);
        try (FileInputStream fileIn = new FileInputStream(inputFile)) {
            Cipher cipher = Cipher.getInstance(TRANSFORMATION);
            Key secretKey = new SecretKeySpec(encryptedKeyValue.getBytes(), ALGORITHM);
            byte[] fileIv = new byte[16];
            fileIn.read(fileIv);
            cipher.init(Cipher.DECRYPT_MODE, secretKey);

            try (
                    CipherInputStream cipherIn = new CipherInputStream(fileIn, cipher);
                    InputStreamReader inputReader = new InputStreamReader(cipherIn);
                    BufferedReader reader = new BufferedReader(inputReader)
            ) {

                StringBuilder sb = new StringBuilder();
                String line;
                while ((line = reader.readLine()) != null) {
                    sb.append(line);
                }
                content = sb.toString();
            }

        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (NoSuchPaddingException e) {
            throw new RuntimeException(e);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        } catch (InvalidKeyException e) {
            throw new RuntimeException(e);
        }
        return content.substring(3);
    }
}
