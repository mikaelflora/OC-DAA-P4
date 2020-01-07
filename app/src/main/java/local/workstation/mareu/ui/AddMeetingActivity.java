package local.workstation.mareu.ui;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.format.DateFormat;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.DatePicker;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import java.text.ParseException;
import java.util.Calendar;
import java.util.List;
import java.util.Objects;

import local.workstation.mareu.R;

import static local.workstation.mareu.ui.meeting_list.ListMeetingActivity.sApiService;

/**
 * Add new meeting
 */
public class AddMeetingActivity extends AppCompatActivity {

    private boolean mError;

    private List<String> mRooms;
    private TextInputLayout mRoomNameTextInputLayout;
    private AutoCompleteTextView mRoomNameAutoCompleteTextView;

    private TextInputLayout mTopicTextInputLayout;

    private ChipGroup mEmailsChipGroup;
    private TextInputEditText mEmailsTextInputEditText;

    private TextInputLayout mDateTextInputLayout;
    private TextInputEditText mDateTextInputEditText;

    private TextInputLayout mStartTimeTextInputLayout, mEndTimeTextInputLayout;
    private TextInputEditText mStartTimeTextInputEditText, mEndTimeTextInputEditText;

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Global -->
        setContentView(R.layout.activity_add_meeting);
        mError = false;
        // Global <--

        // Meeting room -->
        mRooms = sApiService.getRooms();
        mRoomNameTextInputLayout = findViewById(R.id.room_name_layout);
        mRoomNameAutoCompleteTextView = findViewById(R.id.room_name);

        mRoomNameAutoCompleteTextView.setAdapter(new ArrayAdapter<>(
                this,
                R.layout.room_item,
                mRooms));

        mRoomNameAutoCompleteTextView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if(event.getAction() == MotionEvent.ACTION_DOWN) {
                    mRoomNameAutoCompleteTextView.showDropDown();
                    return true;
                }
                return (event.getAction() == MotionEvent.ACTION_UP);
            }
        });
        // Meeting room <--

        // Meeting topic -->
        mTopicTextInputLayout = findViewById(R.id.topic);
        // Meeting topic <--

        // Meeting participants -->
        mEmailsChipGroup = findViewById(R.id.emails_group);
        mEmailsTextInputEditText = findViewById(R.id.emails);

        mEmailsTextInputEditText.addTextChangedListener(new TextWatcher() {
            private int mPreviousPosition = 0;
            private boolean mNewEmail = false;

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                mNewEmail = false;
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(count >=1) {
                    if(s.charAt(start) == ' ' || s.charAt(start) == ',' || s.charAt(start) == ';') {
                        mNewEmail = true;
                    }
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (mNewEmail) {
                    Chip email = new Chip(getApplicationContext());
                    email.setText(s.subSequence(mPreviousPosition, s.length()).toString());

                    mEmailsChipGroup.addView(email);
                    mPreviousPosition = s.length();
                }
//                // TODO
//                if (mNewEmail) {
//                    ChipDrawable email = ChipDrawable.createFromResource(AddMeetingActivity.this, R.xml.chip);
//
//                    email.setText(s.subSequence(mPreviousPosition, s.length()).toString());
//                    email.setBounds(0, 0, email.getIntrinsicWidth(), email.getIntrinsicHeight());
//
//                    ImageSpan span = new ImageSpan(email);
//                    s.setSpan(span, mPreviousPosition, s.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
//
//                    mPreviousPosition = s.length();
//                }
            }
        });
        // Meeting participants <--

        // Meeting date -->
        mDateTextInputLayout = findViewById(R.id.date_layout);
        mDateTextInputEditText = findViewById(R.id.date);

        mDateTextInputEditText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar calendar = Calendar.getInstance();
                DatePickerDialog mDatePickerDialog;

                mDatePickerDialog = new DatePickerDialog(v.getContext(),
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                                Calendar cal = Calendar.getInstance();
                                cal.set(year, month, dayOfMonth);
                                mDateTextInputEditText.setText(android.text.format.DateFormat.getDateFormat(getApplicationContext()).format(cal.getTime()));
                            }
                        },
                        calendar.get(Calendar.YEAR),
                        calendar.get(Calendar.MONTH),
                        calendar.get(Calendar.DAY_OF_MONTH));
                mDatePickerDialog.show();
            }
        });
        // Meeting date <--

        // Meeting time -->
        mStartTimeTextInputLayout = findViewById(R.id.from_layout);
        mStartTimeTextInputEditText = findViewById(R.id.from);
        mEndTimeTextInputLayout = findViewById(R.id.to_layout);
        mEndTimeTextInputEditText = findViewById(R.id.to);

        mStartTimeTextInputEditText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar time = Calendar.getInstance();

                int unroundedMinutes = time.get(Calendar.MINUTE);
                int mod = unroundedMinutes % 15;
                time.add(Calendar.MINUTE, mod > 0 ? (15 - mod) : 0);
                if (unroundedMinutes > 45)
                    time.add(Calendar.HOUR_OF_DAY, 1);

                TimePickerDialog mTimePickerDialog;

                mTimePickerDialog = new TimePickerDialog(AddMeetingActivity.this,
                        new TimePickerDialog.OnTimeSetListener() {
                            @Override
                            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                                Calendar tim = Calendar.getInstance();
                                tim.set(Calendar.HOUR_OF_DAY, hourOfDay);
                                tim.set(Calendar.MINUTE, minute);
                                mStartTimeTextInputEditText.setText(android.text.format.DateFormat.getTimeFormat(getApplicationContext()).format(tim.getTime()));
                            }
                        },
                        time.get(Calendar.HOUR_OF_DAY),
                        time.get(Calendar.MINUTE),
                        DateFormat.is24HourFormat(AddMeetingActivity.this));
                mTimePickerDialog.show();
            }
        });

        mEndTimeTextInputEditText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar time = Calendar.getInstance();

                int unroundedMinutes = time.get(Calendar.MINUTE);
                int mod = unroundedMinutes % 15;
                time.add(Calendar.MINUTE, mod > 0 ? (15 - mod) : 0);
                if (unroundedMinutes > 45)
                    time.add(Calendar.HOUR_OF_DAY, 1);

                TimePickerDialog mTimePickerDialog;

                mTimePickerDialog = new TimePickerDialog(AddMeetingActivity.this,
                        new TimePickerDialog.OnTimeSetListener() {
                            @Override
                            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                                Calendar tim = Calendar.getInstance();
                                tim.set(Calendar.HOUR_OF_DAY, hourOfDay);
                                tim.set(Calendar.MINUTE, minute);
                                mEndTimeTextInputEditText.setText(android.text.format.DateFormat.getTimeFormat(getApplicationContext()).format(tim.getTime()));
                            }
                        },
                        time.get(Calendar.HOUR_OF_DAY),
                        time.get(Calendar.MINUTE),
                        DateFormat.is24HourFormat(AddMeetingActivity.this));
                mTimePickerDialog.show();
            }
        });
        // Meeting time <--
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_add_meeting, menu);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.action_add_meeting:
                add_meeting();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void add_meeting() {
        validateTextInput(mTopicTextInputLayout);
        validateTextInput(mRoomNameTextInputLayout);
        validateDateInput(mDateTextInputLayout);
        validateTimeInput(mStartTimeTextInputLayout);
        validateTimeInput(mEndTimeTextInputLayout);

        if (mError) {
            Toast.makeText(this.getApplicationContext(), R.string.error_add_new_meeting, Toast.LENGTH_LONG).show();
            mError = false;
        } else {
            Toast.makeText(this.getApplicationContext(), R.string.add_new_meeting, Toast.LENGTH_LONG).show();
            // TODO
            // Upload with Fake Service API
            finish();
        }
    }

    private void validateTextInput(TextInputLayout inputValue) {
        String tmpValue = Objects.requireNonNull(inputValue.getEditText()).getText().toString().trim();

        if (tmpValue.isEmpty()) {
            inputValue.setError(getText(R.string.error_empty_field));
            mError = true;
        } else {
            inputValue.setError(null);
        }
    }

    private void validateDateInput(TextInputLayout inputValue) {
        String tmpValue = Objects.requireNonNull(inputValue.getEditText()).getText().toString().trim();

        if (tmpValue.isEmpty()) {
            inputValue.setError(getText(R.string.error_empty_field));
            mError = true;
        } else {
            // valid date format ?
            try {
                android.text.format.DateFormat.getDateFormat(getApplicationContext()).parse(tmpValue);
                inputValue.setError(null);
            } catch (ParseException e) {
                inputValue.setError(getText(R.string.error_invalid_date_format));
                mError = true;
            }
        }
    }

    private void validateTimeInput(TextInputLayout inputValue) {
        String tmpValue = Objects.requireNonNull(inputValue.getEditText()).getText().toString().trim();

        if (tmpValue.isEmpty()) {
            inputValue.setError(getText(R.string.error_empty_field));
            mError = true;
        } else {
            // valid time format ?
            try {
                android.text.format.DateFormat.getTimeFormat(getApplicationContext()).parse(tmpValue);
                inputValue.setError(null);
            } catch (ParseException e) {
                inputValue.setError(getText(R.string.error_invalid_time_format));
                mError = true;
            }
        }
    }

    private void validateEmailInput(TextInputLayout inputValue) {
        String tmpValue = Objects.requireNonNull(inputValue.getEditText()).getText().toString().trim();

        if (tmpValue.isEmpty()) {
            inputValue.setError(getText(R.string.error_empty_field));
            mError = true;
        } else if (!mRooms.contains(tmpValue)) {
            inputValue.setError(getText(R.string.error_invalid_field));
            mError = true;
        } else {
            inputValue.setError(null);
        }
    }
}
