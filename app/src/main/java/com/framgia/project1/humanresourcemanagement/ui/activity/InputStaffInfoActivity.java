package com.framgia.project1.humanresourcemanagement.ui.activity;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.framgia.project1.humanresourcemanagement.R;
import com.framgia.project1.humanresourcemanagement.data.model.Constant;
import com.framgia.project1.humanresourcemanagement.data.model.DBSchemaConstant;
import com.framgia.project1.humanresourcemanagement.data.model.Department;
import com.framgia.project1.humanresourcemanagement.data.model.Staff;
import com.framgia.project1.humanresourcemanagement.data.remote.DatabaseRemote;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class InputStaffInfoActivity extends AppCompatActivity implements DBSchemaConstant, TextWatcher {
    Button butChoose, butSubmit, butUnSelect;
    ImageView imageAvatar;
    EditText edtName, edtBirth, edtPlaceBirth, edtPhone;
    Spinner spnStatus, spnPosition, spnDepartment;

    //attribute of staff
    String name, birthDay, placeBirth, phone, avatar = Constant.NOAVARTAR;
    int position = -1, status = -1, idDepartment = -1, idStaff = -1;
    private static final int select_photo = 100;
    DatabaseRemote remote;
    List<String> listDepartment;
    ArrayAdapter<String> adapterDepartment;
    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_input_infor);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle(R.string.inputinfostaff);
        setSupportActionBar(toolbar);
        initElement();
    }

    private void initElement() {
        remote = new DatabaseRemote(getApplicationContext());
        try {
            remote.openDataBase();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        List<Department> listDepartmentList = new ArrayList<>();
        edtName = (EditText) findViewById(R.id.edtName);
        edtBirth = (EditText) findViewById(R.id.edtDateOfBirth);
        edtPhone = (EditText) findViewById(R.id.edtPhoneNumber);
        edtPlaceBirth = (EditText) findViewById(R.id.edtPlaceDateOfBirth);
        imageAvatar = (ImageView) findViewById(R.id.avatar);
        setSpinner();
        checkActivityModifi();
        butChoose = (Button) findViewById(R.id.btnChoose);
        butChoose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent photoPickIntent = new Intent(Intent.ACTION_PICK);
                photoPickIntent.setType("image/*");
                startActivityForResult(photoPickIntent, select_photo);
            }
        });
        butUnSelect = (Button) findViewById(R.id.btnBoChon);
        butUnSelect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                imageAvatar.setImageResource(R.drawable.origin_avatar);
                avatar = Constant.NOAVARTAR;
            }
        });
        butSubmit = (Button) findViewById(R.id.btnSubmit);
        butSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                name = edtName.getText().toString();
                birthDay = edtBirth.getText().toString();
                placeBirth = edtPlaceBirth.getText().toString();
                phone = edtPhone.getText().toString();
                Staff staff;
                if (name.length() * birthDay.length() * placeBirth.length() * phone.length() * avatar.length() != 0 && position > -1 && status > -1 && idDepartment > -1) {
                    staff = new Staff(1, idDepartment, status, position, name, birthDay, placeBirth, phone, avatar);
                    remote.insertStaff(staff);
                    Toast.makeText(InputStaffInfoActivity.this, R.string.ADD_SUCCESSFUL, Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(InputStaffInfoActivity.this, R.string.ADD_FAILED, Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    // check where intent is from, if it is from long click in list staff, we will show change information activity
    private void checkActivityModifi() {
        Intent intent = getIntent();
        Bundle bundle = intent.getBundleExtra(Constant.ID);
        if (bundle != null && (idStaff = bundle.getInt(Constant.ID_INTENT)) > -1) {
            Cursor cursor = remote.searchStaff(idStaff);
            if (cursor.moveToFirst()) {
                Staff staff = new Staff(cursor);
                edtName.setText(cursor.getString(cursor.getColumnIndex(COLUMN_NAME_STAFF)));
                edtBirth.setText(cursor.getString(cursor.getColumnIndex(COLUMN_BIRTHDAY)));
                edtPlaceBirth.setText(cursor.getString(cursor.getColumnIndex(COLUMN_PLACEOFBIRTH)));
                edtPhone.setText(cursor.getString(cursor.getColumnIndex(COLUMN_PHONENUMBER)));
                spnPosition.setSelection(1);
                spnStatus.setSelection(1);
                avatar = cursor.getString(cursor.getColumnIndex(COLUMN_AVATAR));
                if (!avatar.equals(Constant.NOAVARTAR)) {
                    Bitmap bitmap = BitmapFactory.decodeFile(avatar);
                    imageAvatar.setImageBitmap(bitmap);
                }
            }
        }
    }

    private void setSpinner() {
        //set department spinner
        spnDepartment = (Spinner) findViewById(R.id.spnDepartment);
        List<Department> departmentList = remote.getDepartmentList();
        listDepartment = new ArrayList<>();
        int depSize = departmentList.size();
        for (int i = 0; i < depSize; i++) {
            listDepartment.add(departmentList.get(i).getName());
        }
        adapterDepartment = new ArrayAdapter<String>(getApplicationContext(), R.layout.spinner_layout, listDepartment);
        adapterDepartment.setDropDownViewResource(R.layout.spinner_layout);
        spnDepartment.setAdapter(adapterDepartment);
        String check = "";
        int listDepartmentSize = listDepartment.size();
        for (int i = 0; i < listDepartmentSize; i++) {
            check += "," + remote.getIdDepartment(listDepartment.get(i));
        }
        spnDepartment.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                idDepartment = remote.getIdDepartment(spnDepartment.getItemAtPosition(i).toString());
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });

        //set position spinner
        spnPosition = (Spinner) findViewById(R.id.spnPosition);
        ArrayAdapter<String> adapterPosition = new ArrayAdapter<String>(getApplicationContext(), R.layout.spinner_layout, getResources().getStringArray(R.array.array_position));
        adapterPosition.setDropDownViewResource(R.layout.spinner_layout);
        spnPosition.setAdapter(adapterPosition);
        spnPosition.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                position = i;
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });

        //set status spinner
        spnStatus = (Spinner) findViewById(R.id.spnStatus);
        ArrayAdapter<String> adapterStatus = new ArrayAdapter<String>(getApplicationContext(), R.layout.spinner_layout, getResources().getStringArray(R.array.array_status));
        adapterStatus.setDropDownViewResource(R.layout.spinner_layout);
        spnStatus.setAdapter(adapterStatus);
        spnStatus.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                status = i;
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 100) {
            if (resultCode == RESULT_OK) {
                Uri uriImage = data.getData();
                String[] filePathColumn = {MediaStore.Images.Media.DATA};
                Cursor cursor = getContentResolver().query(uriImage, filePathColumn, null, null, null);
                cursor.moveToFirst();
                int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                avatar = cursor.getString(columnIndex);
                cursor.close();
                Bitmap bitmapImage = BitmapFactory.decodeFile(avatar);
                imageAvatar = (ImageView) findViewById(R.id.avatar);
                imageAvatar.setImageBitmap(bitmapImage);
            }
        } else {
            Toast.makeText(getBaseContext(), R.string.imagefail, Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

    }

    @Override
    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

    }

    @Override
    public void afterTextChanged(Editable editable) {

    }

    @Override
    protected void onResume() {
        super.onResume();
        setSpinner();
    }
}