package com.framgia.project1.humanresourcemanagement.ui.activity;

import android.app.DatePickerDialog;
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
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.framgia.project1.humanresourcemanagement.R;
import com.framgia.project1.humanresourcemanagement.data.model.Constant;
import com.framgia.project1.humanresourcemanagement.data.model.DBSchemaConstant;
import com.framgia.project1.humanresourcemanagement.data.model.Department;
import com.framgia.project1.humanresourcemanagement.data.model.Staff;
import com.framgia.project1.humanresourcemanagement.data.remote.DatabaseRemote;

import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class InputStaffInfoActivity extends AppCompatActivity implements DBSchemaConstant, TextWatcher, DatePickerDialog.OnDateSetListener {
    private Button mBtnSubmit;
    private ImageView mImageAvatar;
    private ImageButton mImageButton;
    private EditText mEdtName;
    private EditText mEdtPlaceBirth;
    private EditText mEdtPhone;
    private TextView mTxtDateOfBirth;
    private Spinner mSpnStatus;
    private Spinner mSpnPosition;
    private Spinner mSpnDepartment;
    //attribute of staff
    boolean changeFlag = false;
    String mName, mBirthDay, mPlaceBirth, mPhone, mAvatar = Constant.NOAVARTAR;
    int mPosition = -1, mStatus = -1, mIdDepartment = -1, mIdStaff = -1;
    private static final int select_photo = 100;
    DatabaseRemote mRemote;
    private List<String> mListDepartment;
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
        mRemote = new DatabaseRemote(getApplicationContext());
        List<Department> listDepartmentList = new ArrayList<>();
        mEdtName = (EditText) findViewById(R.id.edtName);
        mTxtDateOfBirth = (TextView) findViewById(R.id.txtDateOfBirth);
        mTxtDateOfBirth.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDatepickerDialog();
            }
        });
        mEdtPhone = (EditText) findViewById(R.id.edtPhoneNumber);
        mEdtPlaceBirth = (EditText) findViewById(R.id.edtPlaceDateOfBirth);
        mImageAvatar = (ImageView) findViewById(R.id.avatar);
        setSpinner();
        checkActivityModifi();
        mImageButton = (ImageButton) findViewById(R.id.avatar);
        mImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent photoPickIntent = new Intent(Intent.ACTION_PICK);
                photoPickIntent.setType("image/*");
                startActivityForResult(photoPickIntent, select_photo);
            }
        });
        mImageButton.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                mImageAvatar.setImageResource(R.drawable.origin_avatar);
                mAvatar = Constant.NOAVARTAR;
                return false;
            }
        });
        mBtnSubmit = (Button) findViewById(R.id.btnSubmit);
        mBtnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mName = mEdtName.getText().toString();
                mBirthDay = mTxtDateOfBirth.getText().toString();
                mPlaceBirth = mEdtPlaceBirth.getText().toString();
                mPhone = mEdtPhone.getText().toString();
                Staff staff;
                if (mName.length() * mBirthDay.length() * mPlaceBirth.length() * mPhone.length() * mAvatar.length() != 0
                        && mPosition > -1 && mIdDepartment > -1 && mIdDepartment > -1) {
                    staff = new Staff(1, mIdDepartment, mStatus, mPosition, mName, mBirthDay, mPlaceBirth, mPhone, mAvatar);
                    if (changeFlag == false) {
                        try {
                            mRemote.openDataBase();
                            mRemote.insertStaff(staff);
                        } catch (SQLException e) {
                            e.printStackTrace();
                        }
                        Toast.makeText(InputStaffInfoActivity.this, R.string.ADD_SUCCESSFUL, Toast.LENGTH_LONG).show();
                    } else {
                        int i = mRemote.updateStaff(mIdStaff, staff);
                        if (i > 0) {
                            Toast.makeText(getApplicationContext(), R.string.UPDATE_SUCCESSFUL, Toast.LENGTH_LONG).show();
                        }
                    }
                } else {
                    Toast.makeText(InputStaffInfoActivity.this, R.string.ADD_FAILED, Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    private void showDatepickerDialog() {
        Calendar calendar = Calendar.getInstance();
        int day, month, year;
        Date mDate;
        String time = mTxtDateOfBirth.getText().toString();
        if (time.length() < Constant.LENGTH_OF_DATE_FORMAT) {
            day = calendar.get(Calendar.DAY_OF_MONTH);
            month = calendar.get(Calendar.MONTH);
            year = calendar.get(Calendar.YEAR);
        } else {
            mDate = formatDateFromString(time);
            if (mTxtDateOfBirth != null)
                calendar.setTime(mDate);
            day = calendar.get(Calendar.DAY_OF_MONTH);
            month = calendar.get(Calendar.MONTH);
            year = calendar.get(Calendar.YEAR);
        }
        DatePickerDialog dialog = new DatePickerDialog(this, this, year, month, day);
        dialog.show();
    }

    private Date formatDateFromString(String time) {
        SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy", Locale.US);
        Date date = null;
        try {
            date = format.parse(time);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return date;
    }

    // check where intent is from, if it is from long click in list staff, we will show change information activity
    private void checkActivityModifi() {
        Intent intent = getIntent();
        Bundle bundle = intent.getBundleExtra(Constant.ID);
        if (bundle != null && (mIdStaff = bundle.getInt(Constant.ID_INTENT)) > -1) {
            changeFlag = true;
            try {
                mRemote.openDataBase();
            } catch (SQLException e) {
                e.printStackTrace();
            }
            Cursor cursor = mRemote.searchStaff(mIdStaff);
            if (cursor.moveToFirst()) {
                mEdtName.setText(cursor.getString(cursor.getColumnIndex(COLUMN_NAME_STAFF)));
                mTxtDateOfBirth.setText(cursor.getString(cursor.getColumnIndex(COLUMN_BIRTHDAY)));
                mEdtPlaceBirth.setText(cursor.getString(cursor.getColumnIndex(COLUMN_PLACEOFBIRTH)));
                mEdtPhone.setText(cursor.getString(cursor.getColumnIndex(COLUMN_PHONENUMBER)));
                mSpnDepartment.setSelection(cursor.getInt(cursor.getColumnIndex(COLUMN_ID_DEPARTMENT)) - 1);
                mSpnPosition.setSelection(cursor.getInt(cursor.getColumnIndex(COLUMN_POSITION)));
                mSpnStatus.setSelection(cursor.getInt(cursor.getColumnIndex(COLUMN_STATUS)));
                mAvatar = cursor.getString(cursor.getColumnIndex(COLUMN_AVATAR));
                if (!mAvatar.equals(Constant.NOAVARTAR)) {
                    Bitmap bitmap = BitmapFactory.decodeFile(mAvatar);
                    mImageAvatar.setImageBitmap(bitmap);
                }
            }
        }
    }

    private void setSpinner() {
        try {
            mRemote.openDataBase();
        //set department spinner
        mSpnDepartment = (Spinner) findViewById(R.id.spnDepartment);
        List<Department> departmentList = mRemote.getDepartmentList();
        mListDepartment = new ArrayList<>();
        int depSize = departmentList.size();
        for (int i = 0; i < depSize; i++) {
            mListDepartment.add(departmentList.get(i).getName());
        }
        ArrayAdapter<String> mAdapterDepartment;
        mAdapterDepartment = new ArrayAdapter<String>(getApplicationContext(), R.layout.spinner_layout, mListDepartment);
        mAdapterDepartment.setDropDownViewResource(R.layout.spinner_layout);
        mSpnDepartment.setAdapter(mAdapterDepartment);
        String check = "";
        int listDepartmentSize = mListDepartment.size();
        for (int i = 0; i < listDepartmentSize; i++) {
            check += "," + mRemote.getIdDepartment(mListDepartment.get(i));
        }
        mSpnDepartment.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                mIdDepartment = mRemote.getIdDepartment(mSpnDepartment.getItemAtPosition(i).toString());
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });

        //set position spinner
        mSpnPosition = (Spinner) findViewById(R.id.spnPosition);
        ArrayAdapter<String> adapterPosition = new ArrayAdapter<String>(getApplicationContext(), R.layout.spinner_layout, getResources().getStringArray(R.array.array_position));
        adapterPosition.setDropDownViewResource(R.layout.spinner_layout);
        mSpnPosition.setAdapter(adapterPosition);
        mSpnPosition.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                mPosition = i;
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });

        //set status spinner
        mSpnStatus = (Spinner) findViewById(R.id.spnStatus);
        ArrayAdapter<String> adapterStatus = new ArrayAdapter<String>(getApplicationContext(), R.layout.spinner_layout, getResources().getStringArray(R.array.array_status));
        adapterStatus.setDropDownViewResource(R.layout.spinner_layout);
        mSpnStatus.setAdapter(adapterStatus);
        mSpnStatus.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                mStatus = i;
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });
        } catch (SQLException e) {
            e.printStackTrace();
        }
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
                mAvatar = cursor.getString(columnIndex);
                cursor.close();
                Bitmap bitmapImage = BitmapFactory.decodeFile(mAvatar);
                mImageAvatar = (ImageView) findViewById(R.id.avatar);
                mImageAvatar.setImageBitmap(bitmapImage);
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
    }

    @Override
    public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {
        mBirthDay = i2 + "/" + i1 + "/" + i;
        mTxtDateOfBirth.setText(mBirthDay);
    }
}