package sample.singletons;

import sample.models.DecodedKeys;
import sample.models.EncodedKeys;
import sample.models.Log;

import javax.crypto.Cipher;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.security.*;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;


public class SecureProvider {
    private static SecureProvider ourInstance = new SecureProvider();

    public static SecureProvider getInstance() {
        return ourInstance;
    }

    private SecureProvider() {
    }


    public void saveMyKeysIntoFile(EncodedKeys myKeys) throws Exception {

//        write in public key
        File pubFile = new File("public.txt");
        FileWriter pubFileWriter = new FileWriter(pubFile);
        pubFileWriter.write(myKeys.getPublicKey());
        pubFileWriter.flush();
        pubFileWriter.close();


        File prFile = new File("private.txt");
        FileWriter prFileWriter = new FileWriter(prFile);
        prFileWriter.write(myKeys.getPrivateKey());
        prFileWriter.flush();
        prFileWriter.close();

        Log.debug("saved successfully");

//        private key

    }


    public EncodedKeys keyGeneration() throws Exception {
        KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("RSA");
        keyPairGenerator.initialize(4096);
        KeyPair keyPair = keyPairGenerator.generateKeyPair();
        PublicKey publicKey = keyPair.getPublic();
        PrivateKey privateKey = keyPair.getPrivate();
        String publicKeyAsBase64 = Base64.getEncoder().encodeToString(publicKey.getEncoded());
        String privateKeyKeyAsBase64 = Base64.getEncoder().encodeToString(privateKey.getEncoded());
        Log.debug("key generation done successfully");

        return new EncodedKeys(publicKeyAsBase64, privateKeyKeyAsBase64);
    }

    public DecodedKeys decodedKeys(EncodedKeys myKeys) throws Exception {
        byte[] pKeyAsByte = Base64.getDecoder().decode(myKeys.getPublicKey());
        KeyFactory kf = KeyFactory.getInstance("RSA");
        byte[] prKeyAsByte = Base64.getDecoder().decode(myKeys.getPrivateKey());
        PrivateKey prK = kf.generatePrivate(new PKCS8EncodedKeySpec(prKeyAsByte));
//        generate your public  key from string
        PublicKey pk = kf.generatePublic(new X509EncodedKeySpec(pKeyAsByte));

        return new DecodedKeys(pk, prK);
    }

    public EncodedKeys encodedKeys() throws Exception {
        PublicKey publicKey = getPublicKeyFromFile();
        PrivateKey privateKey = getPrivateKeyFromFile();
        String publicKeyAsBase64 = Base64.getEncoder().encodeToString(publicKey.getEncoded());
        String privateKeyKeyAsBase64 = Base64.getEncoder().encodeToString(privateKey.getEncoded());
        return new EncodedKeys(publicKeyAsBase64, privateKeyKeyAsBase64);
    }

    private PublicKey getPublicKeyFromFile() throws Exception {
        File file = new File("public.txt");
        FileReader fileReader = new FileReader(file);
        StringBuffer stringBuffer = new StringBuffer();
        int numCharsRead;
        char[] charArray = new char[1024];
        while ((numCharsRead = fileReader.read(charArray)) > 0) {
            stringBuffer.append(charArray, 0, numCharsRead);
        }
        fileReader.close();

//
        KeyFactory kf = KeyFactory.getInstance("RSA");
        byte[] pKeyAsByte = Base64.getDecoder().decode(stringBuffer.toString());
        PublicKey pk = kf.generatePublic(new X509EncodedKeySpec(pKeyAsByte));
        return pk;
    }




    public  byte[] encrypt(String plainText, Key publicKey) throws Exception {
        //Get Cipher Instance RSA With ECB Mode and OAEPWITHSHA-512ANDMGF1PADDING Padding
        Cipher cipher = Cipher.getInstance("RSA/ECB/OAEPWITHSHA-512ANDMGF1PADDING");

        //Initialize Cipher for ENCRYPT_MODE
        cipher.init(Cipher.ENCRYPT_MODE, publicKey);

        //Perform Encryption
        byte[] cipherText = cipher.doFinal(plainText.getBytes());

        return cipherText;
    }

    public  String decrypt(byte[] cipherTextArray, Key privateKey) throws Exception {
        //Get Cipher Instance RSA With ECB Mode and OAEPWITHSHA-512ANDMGF1PADDING Padding
        Cipher cipher = Cipher.getInstance("RSA/ECB/OAEPWITHSHA-512ANDMGF1PADDING");

        //Initialize Cipher for DECRYPT_MODE
        cipher.init(Cipher.DECRYPT_MODE, privateKey);

        //Perform Decryption
        byte[] decryptedTextArray = cipher.doFinal(cipherTextArray);

        return new String(decryptedTextArray);
    }



    private PrivateKey getPrivateKeyFromFile() throws Exception {
        File file = new File("private.txt");
        FileReader fileReader = new FileReader(file);
        StringBuffer stringBuffer = new StringBuffer();
        int numCharsRead;
        char[] charArray = new char[1024];
        while ((numCharsRead = fileReader.read(charArray)) > 0) {
            stringBuffer.append(charArray, 0, numCharsRead);
        }
        fileReader.close();


        KeyFactory kf = KeyFactory.getInstance("RSA");
        byte[] prKeyAsByte = Base64.getDecoder().decode(stringBuffer.toString());
        return kf.generatePrivate(new PKCS8EncodedKeySpec(prKeyAsByte));
    }


    public PrivateKey getPrivateKeyFromBase64(String prkAsBase64) throws Exception {
        KeyFactory kf = KeyFactory.getInstance("RSA");
        byte[] prKeyAsByte = Base64.getDecoder().decode(prkAsBase64);
        return kf.generatePrivate(new PKCS8EncodedKeySpec(prKeyAsByte));
    }

    public PublicKey getPublicKeyFromBase64(String pkAsBase64) throws Exception {
        KeyFactory kf = KeyFactory.getInstance("RSA");
        byte[] pKeyAsByte = Base64.getDecoder().decode(pkAsBase64);
        PublicKey pk = kf.generatePublic(new X509EncodedKeySpec(pKeyAsByte));
        return pk;
    }
}



