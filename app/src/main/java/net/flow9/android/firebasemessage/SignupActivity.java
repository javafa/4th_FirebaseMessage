package net.flow9.android.firebasemessage;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import net.flow9.android.firebasemessage.model.User;
import net.flow9.android.firebasemessage.util.DialogUtil;
import net.flow9.android.firebasemessage.util.VerificationUtil;

public class SignupActivity extends AppCompatActivity {

    FirebaseAuth auth;
    FirebaseDatabase database;
    DatabaseReference userRef;
    //DatabaseReference emailRef;

    private EditText editEmail;
    private EditText editPassword;
    private EditText editPasswordRe;
    private EditText editName;
    private EditText editPhone;
    private EditText editBirthday;
    private Button btnSignup;
    private RadioGroup gender;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        // 파이어베이스 모듈 사용하기
        auth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();
        // 데이터베이스 user 레퍼런스 생성
        userRef = database.getReference("user");
        //emailRef = database.getReference("email");

        initView();
    }
    // 등록처리
    public void signup(View view){
        final String email = editEmail.getText().toString();
        final String password = editPassword.getText().toString();
        final String name = editName.getText().toString();
        final String phone = editPhone.getText().toString();
        final String birthday = editBirthday.getText().toString();
        int selectedId = gender.getCheckedRadioButtonId();
        final String gender = selectedId == R.id.radioMale ? "Male" : "Female";

        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){

                    // 1. 정상등록시 사용자 정보 등록
                    FirebaseUser fUser = auth.getCurrentUser();
                    UserProfileChangeRequest.Builder profile = new UserProfileChangeRequest.Builder();
                    profile.setDisplayName(name);
                    //userBuilder.setPhotoUri("http://사진url");
                    fUser.updateProfile(profile.build()); // 사용자 정보 등록 listener 사용가능

                    // 2. 정상등록시 안내 메일 발송
                    fUser.sendEmailVerification()
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                DialogUtil.showDialog("이메일을 발송하였습니다. 확인해주세요!",SignupActivity.this, true);
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                DialogUtil.showDialog("오류발생:"+e.getMessage(),SignupActivity.this, false);
                            }
                    });
                    // 3. 사용자 등록
                    String tempKey = email.replace(".","_");
                    User user = new User(tempKey , name, email, "", birthday, gender, phone);
                    userRef.child(tempKey).setValue(user);

                    //String tempKey = email.replace(".","_");
                    //emailRef.child(tempKey).child("uid").setValue(fUser.getUid());
                }
                }
            })
            .addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    DialogUtil.showDialog("오류발생:"+e.getMessage(),SignupActivity.this, false);
                }
            });
    }



    boolean checkEmail = false;
    boolean checkPassword = false;
    boolean checkRepeat = false;
    boolean checkName = false;
    private void enableSignupButton(){
        if(checkEmail && checkPassword && checkRepeat && checkName){
            btnSignup.setEnabled(true);
        }else{
            btnSignup.setEnabled(false);
        }
    }

    private void initView() {
        editEmail = (EditText) findViewById(R.id.editEmail);
        editEmail.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                checkEmail = VerificationUtil.isValidEmail(charSequence.toString());
                enableSignupButton();
            }
            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        editPassword = (EditText) findViewById(R.id.editPassword);
        editPassword.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                checkPassword = VerificationUtil.isValidPassword(charSequence.toString());
                enableSignupButton();
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        editPasswordRe = (EditText) findViewById(R.id.editPasswordRe);
        editPasswordRe.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                String password = editPassword.getText().toString();
                checkRepeat = password.equals(charSequence.toString());
                enableSignupButton();
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        editName = (EditText) findViewById(R.id.editName);
        editName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                checkName = VerificationUtil.isValidName(charSequence.toString());
                enableSignupButton();
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        editPhone = findViewById(R.id.editPhone);
        editBirthday = findViewById(R.id.editBirthday);
        gender = findViewById(R.id.gender);
        btnSignup = findViewById(R.id.btnSignup);
    }
}
