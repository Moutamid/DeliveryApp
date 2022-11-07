package nuleo.autopart.grube;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

public class RegisterActivity extends AppCompatActivity {


    EditText username, fullname, email, password, sexo, edad;
    Button register;
    TextView txt_login;

    String[] items= {"Masculino", "Femenino", "Prefiero no decirlo"};
    AutoCompleteTextView autoCompleteTextView;
    ArrayAdapter<String> adapterItems;

    String[] items2= {"13", "14","15","16","17","18","19","20","21","22","23","24","25","26","27","28","29","30","31","32","33","34","35","36","37","38","39","40","41","42","43","44","45","46","47","48","49","50","51","52","53","54","55","56","57","58","59","60","61","62","63","64","65","66","67","68","69","70","71","72","73","74","75","76","77","78","79","80","81","82","83","84","85","86","87","88","89","90","91","92"};
    AutoCompleteTextView autoCompleteTextView2;
    ArrayAdapter<String> adapterItems2;

    FirebaseAuth auth;
    DatabaseReference reference;
    ProgressDialog pd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        autoCompleteTextView = findViewById(R.id.sexo);
        adapterItems = new ArrayAdapter<String>(this,R.layout.listt_item,items);
        autoCompleteTextView.setAdapter(adapterItems);

        autoCompleteTextView2 = findViewById(R.id.edad);
        adapterItems2 = new ArrayAdapter<String>(this,R.layout.listtt_item,items2);
        autoCompleteTextView2.setAdapter(adapterItems2);

        username = findViewById(R.id.username);
        fullname = findViewById(R.id.fullname);
        email = findViewById(R.id.email);
        password = findViewById(R.id.password);
        register = findViewById(R.id.register);
        txt_login = findViewById(R.id.txt_login);
        sexo = findViewById(R.id.sexo);
        edad = findViewById(R.id.edad);

        auth = FirebaseAuth.getInstance();

        autoCompleteTextView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String item = parent.getItemAtPosition(position).toString();

            }
        });

        autoCompleteTextView2.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String item = parent.getItemAtPosition(position).toString();

            }
        });

        findViewById(R.id.alerta).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                AlertDialog.Builder builder=new AlertDialog.Builder(RegisterActivity.this);
                builder.setTitle("GRUBE");
                builder.setMessage("Los usuarios que deseen  publicar autos, deben de colocar la palabra grube en minuscula, antes de escribir el nombre del taller. Ejemplo:grube Autopartes.");

                builder.setCancelable(false);

                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        dialog.dismiss();
                    }
                });
                builder.create().show();
            }
        });

        txt_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(RegisterActivity.this, LoginActivity.class));
            }
        });

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pd = new ProgressDialog(RegisterActivity.this);
                pd.setMessage("Ingresando a GRUBE.");
                pd.show();

                String atr_username = username.getText().toString();
                String atr_fullname = fullname.getText().toString();
                String atr_email = email.getText().toString();
                String atr_password = password.getText().toString();
                String atr_sexo = sexo.getText().toString();
                String atr_edad = edad.getText().toString();

                if (TextUtils.isEmpty(atr_username) || TextUtils.isEmpty(atr_fullname  ) || TextUtils.isEmpty(atr_edad) || TextUtils.isEmpty(atr_sexo)
                        || TextUtils.isEmpty(atr_email) || TextUtils.isEmpty(atr_password)){
                    Toast.makeText(RegisterActivity.this, "Es requerido llenar todas las casillas", Toast.LENGTH_LONG).show();
                } else  if (atr_password.length() < 6) {
                    Toast.makeText(RegisterActivity.this, "La contraseña debe de llevar mas de 6 caracteres", Toast.LENGTH_LONG).show();
                } else {
                    register(atr_username, atr_fullname, atr_edad, atr_sexo, atr_email, atr_password);

                }
            }
        });

    }

    private void register (final String username,final String fullname,final String edad,final String sexo, String email, String password){
        auth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(RegisterActivity.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            FirebaseUser firebaseUser = auth.getCurrentUser();
                            String userid = firebaseUser.getUid();

                            reference = FirebaseDatabase.getInstance().getReference().child("Users").child(userid);

                            HashMap<String, Object> hashMap = new HashMap<>();
                            hashMap.put("id", userid);
                            hashMap.put("username", username.toLowerCase());
                            hashMap.put("fullname", fullname);
                            hashMap.put("sexo", sexo);
                            hashMap.put("edad", edad);
                            hashMap.put("bio", "");
                            hashMap.put("imageurl", "https://firebasestorage.googleapis.com/v0/b/grubenl.appspot.com/o/usuario.jpg?alt=media&token=7451291e-8704-427e-8032-b8dbfa71a5d9");




                            reference.setValue(hashMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()) {
                                        pd.dismiss();
                                        Intent intent = new Intent(RegisterActivity.this, MainActivity.class);
                                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                                        startActivity(intent);

                                    }
                                }
                            });
                        }else{
                            pd.dismiss();
                            Toast.makeText(RegisterActivity.this, "No puedes registrarte con este correo electronico o contraseña.", Toast.LENGTH_SHORT).show();



                        }


                    }
                });
    }
}