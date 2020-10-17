//package reflect;
//
//import androidx.annotation.RequiresApi;
//import androidx.appcompat.app.AppCompatActivity;
//import androidx.core.app.ActivityCompat;
//
//import android.Manifest;
//import android.accessibilityservice.FingerprintGestureController;
//import android.app.KeyguardManager;
//import android.content.Context;
//import android.content.Intent;
//import android.content.pm.PackageManager;
//import android.hardware.fingerprint.FingerprintManager;
//import android.os.Build;
//import android.os.Bundle;
//import android.provider.Settings;
//import android.widget.Toast;
//
//import com.example.reflect.manifests.R;
//
//@RequiresApi(api = Build.VERSION_CODES.M)
//public class LoginActivity extends AppCompatActivity {
//
//    FingerprintManager fingerprintManager;
//    KeyguardManager keyguardManager;
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_login);
//
//        fingerprintManager = (FingerprintManager)getSystemService(Context.FINGERPRINT_SERVICE);
//        keyguardManager = (KeyguardManager)getSystemService(Context.KEYGUARD_SERVICE);
//        startFingerprint();
//    }
//
//    private void startFingerprint() {
//        if(checkFingerprintSettings()) {
//            FingerprintAuthenticator authenticator = FingerprintAuthenticator.getInstance();
//        }
//    }
//    private boolean checkFingerprintSettings(){
//
//        if(ActivityCompat.checkSelfPermission(this, Manifest.permission.USE_FINGERPRINT) != PackageManager.PERMISSION_DENIED){
//            return false;
//        }
//        if(fingerprintManager.isHardwareDetected()) {
//            if(fingerprintManager.hasEnrolledFingerprints()) {
//                if(keyguardManager.isKeyguardSecure()){
//                    return true;
//                }
//            } else {
//                Toast.makeText(this, "Enroll Fingerprint", Toast.LENGTH_SHORT).show();
//                startActivity(new Intent(Settings.ACTION_SECURITY_SETTINGS));
//            }
//        }
//        return false;
//    }
//
//
//
//
//
//}