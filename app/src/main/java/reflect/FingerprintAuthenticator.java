//package reflect;
//
//import android.os.Build;
//import android.security.keystore.KeyGenParameterSpec;
//import android.security.keystore.KeyProperties;
//
//import androidx.annotation.RequiresApi;
//
//import java.io.IOException;
//import java.io.InvalidObjectException;
//import java.security.InvalidAlgorithmParameterException;
//import java.security.InvalidKeyException;
//import java.security.KeyStore;
//import java.security.KeyStoreException;
//import java.security.NoSuchAlgorithmException;
//import java.security.NoSuchProviderException;
//import java.security.UnrecoverableKeyException;
//import java.security.cert.CertificateEncodingException;
//import java.security.cert.CertificateException;
//
//import javax.crypto.Cipher;
//import javax.crypto.KeyGenerator;
//import javax.crypto.NoSuchPaddingException;
//import javax.crypto.SecretKey;
//
//public class FingerprintAuthenticator {
//
//    private FingerprintAuthenticator fingerprintAuthenticator;
//    private KeyStore keyStore;
//    private KeyGenerator keyGenerator;
//    private static final String KEY_NAME = "android.fingerprint";
//    private Cipher cipher;
//
//    @RequiresApi(api = Build.VERSION_CODES.M)
//    private FingerprintAuthenticator() {
//        initAuthentication();
//    }
//
//    @RequiresApi(api = Build.VERSION_CODES.M)
//    public void initAuthentication() {
//        try {
//            keyStore = KeyStore.getInstance("AndroidKeyStore");
//        } catch (KeyStoreException e){
//            e.printStackTrace();
//        }
//
//        try {
//            keyGenerator = KeyGenerator.getInstance(KeyProperties.KEY_ALGORITHM_AES, "AndroidKeyStore");
//        } catch (NoSuchAlgorithmException e) {
//            e.printStackTrace();
//        } catch (NoSuchProviderException e) {
//            e.printStackTrace();
//        }
//
//        try {
//            keyStore.load(null);
//
//            keyGenerator.init(new KeyGenParameterSpec.Builder(KEY_NAME,
//                    KeyProperties.PURPOSE_ENCRYPT | KeyProperties.PURPOSE_DECRYPT)
//                    .setBlockModes(KeyProperties.BLOCK_MODE_CBC)
//                    .setEncryptionPaddings(KeyProperties.ENCRYPTION_PADDING_PKCS7)
//                    .build());
//            keyGenerator.generateKey();
//        } catch (IOException e){
//             e.printStackTrace();
//        } catch (NoSuchAlgorithmException e) {
//            e.printStackTrace();
//        } catch (CertificateException e) {
//            e.printStackTrace();
//        } catch (InvalidAlgorithmParameterException e) {
//            e.printStackTrace();
//        }
//    }
//
//    public void cipherInit() {
//        try {
//            cipher = Cipher.getInstance(KeyProperties.KEY_ALGORITHM_AES + "/" + KeyProperties.BLOCK_MODE_CBC + "/" + KeyProperties.ENCRYPTION_PADDING_NONE);
//        } catch (NoSuchAlgorithmException e){
//            e.printStackTrace();
//        } catch (NoSuchPaddingException e) {
//            e.printStackTrace();
//        }
//
//        try {
//            keyStore.load(null);
//            SecretKey key = (SecretKey) keyStore.getKey(KEY_NAME, null);
//            cipher.init(Cipher.ENCRYPT_MODE, key);
//        } catch (IOException e){
//            e.printStackTrace();
//        } catch (NoSuchAlgorithmException e) {
//            e.printStackTrace();
//        } catch (CertificateException e) {
//            e.printStackTrace();
//        } catch (UnrecoverableKeyException e) {
//            e.printStackTrace();
//        } catch (KeyStoreException e) {
//            e.printStackTrace();
//        } catch (InvalidKeyException e) {
//            e.printStackTrace();
//        }
//    }
//
//    @RequiresApi(api = Build.VERSION_CODES.M)
//    FingerprintAuthenticator getInstance(){
//        if(fingerprintAuthenticator == null) {
//            fingerprintAuthenticator = new FingerprintAuthenticator();
//        }
//        return fingerprintAuthenticator;
//    }
//}
