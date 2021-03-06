package com.example.assignment.view;

import androidx.appcompat.app.AlertDialog;
import androidx.core.content.ContextCompat;
import androidx.core.widget.NestedScrollView;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.viewpager.widget.ViewPager;

import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.assignment.R;
import com.example.assignment.adapters.MyFragmentAdapter;
import com.example.assignment.databinding.Tab2FragmentBinding;
import com.example.assignment.utils.SaveBitmap;
import com.example.assignment.model.User;
import com.example.assignment.view.activities.MainActivity;
import com.example.assignment.viewmodel.MyViewModel;

import java.util.Calendar;
import java.util.Date;

import static android.app.Activity.RESULT_CANCELED;

public class CreateEntryFragment extends Fragment {
    MyFragmentAdapter myFragmentAdapter;


    private MyViewModel mMyViewModel;
   // @BindView(R.id.image_view_user)
    ImageView userImage;

    Tab2FragmentBinding binding;


  //  @BindView(R.id.editTextDate)
    TextView userBirthDay;

  //  @BindView(R.id.editTextPersonName)
    EditText userName;
    //@BindView(R.id.enlarge_image)
    ImageView enlargeImage;
 //   @BindView(R.id.scroll_view)
    NestedScrollView scrollView;


   // @BindView(R.id.editTextPhone)
    EditText userPhoneNumber;
   // @BindView(R.id.editTextPhone1)
    EditText userPhoneNumber1;
   // @BindView(R.id.editTextPhone2)
    EditText userPhoneNumber2;
   // @BindView(R.id.phone_linear_layout1)
    LinearLayout phone_linear_layout1;
 //   @BindView(R.id.phone_linear_layout2)
    LinearLayout phone_linear_layout2;

 //   @BindView(R.id.pincode)
    EditText pincode;
  //  @BindView(R.id.pincode1)
    EditText pincode1;
 //   @BindView(R.id.pincode2)
    EditText pincode2;

    //@BindView(R.id.button)
    Button button;
   // @BindView(R.id.edit_details)
    Button editDetails;
    //@BindView(R.id.date_picker)
    TextView datePick;
   // @BindView(R.id.buttonAddProfilePic)
    Button addProfilePic;

  //  @BindView(R.id.edit_details_done)
    Button editDetailsDone;

    String ProfilePicPath;
    String ProfilePicUri;

    private final int REQUEST_CODE_CAMERA = 0;
    private final int REQUEST_CODE_GALLERY = 1;
    int id;

    SaveBitmap saveBitmap;

    public CreateEntryFragment() {
    }

    ;

    public static CreateEntryFragment Tab2Instance(int id) {
        CreateEntryFragment createEntryFragment = new CreateEntryFragment();
        Bundle bundle = new Bundle();

        bundle.putInt("ID", id);
        createEntryFragment.setArguments(bundle);
        return createEntryFragment;
    }

    public static CreateEntryFragment newInstance() {
        return new CreateEntryFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            int id = getArguments().getInt("ID");
            Log.d("Tag", String.valueOf(id));


        }
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        //View view = inflater.inflate(R.layout.tab2_fragment, container, false);
        Tab2FragmentBinding binding=DataBindingUtil.inflate(inflater,R.layout.tab2_fragment,container,false);
          View view=binding.getRoot();
       this.binding=binding;


       // ButterKnife.bind(this,view);



        phone_linear_layout1=view.findViewById(R.id.phone_linear_layout1);
        phone_linear_layout2=view.findViewById(R.id.phone_linear_layout2);
        pincode=view.findViewById(R.id.pincode);
        pincode1=view.findViewById(R.id.pincode1);
        pincode2=view.findViewById(R.id.pincode2);

        editDetails=view.findViewById(R.id.edit_details);

        userImage=view.findViewById(R.id.image_view_user);
        userBirthDay=view.findViewById(R.id.editTextDate);
        userName=view.findViewById(R.id.editTextPersonName);
        scrollView=view.findViewById(R.id.scroll_view);


        userPhoneNumber=view.findViewById(R.id.editTextPhone);
        userPhoneNumber1=view.findViewById(R.id.editTextPhone1);
        userPhoneNumber2=view.findViewById(R.id.editTextPhone2);
        button=view.findViewById(R.id.button);


        addProfilePic=view.findViewById(R.id.buttonAddProfilePic);
        editDetailsDone=view.findViewById(R.id.edit_details_done);

        enlargeImage= view.findViewById(R.id.enlarge_image);
        datePick=view.findViewById(R.id.date_picker);
        mMyViewModel = ViewModelProviders.of(getActivity()).get(MyViewModel.class);

        if (getArguments() != null) {

            editDetails.setVisibility(View.VISIBLE);
             id = getArguments().getInt("ID");
            addProfilePic.setVisibility(View.GONE);
            button.setVisibility(View.GONE);
            pincode.setEnabled(false);
            pincode1.setEnabled(false);
            pincode2.setEnabled(false);
            userPhoneNumber.setEnabled(false);
            userPhoneNumber1.setEnabled(false);
            userPhoneNumber2.setEnabled(false);
            datePick.setVisibility(View.GONE);
            userName.setEnabled(false);
            mMyViewModel.fetchDetailsFromDatabase(id);
            mMyViewModel.getUser().observe(getViewLifecycleOwner(), user -> {
                binding.setUser(user);
              /*  userImage.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        enlargeImage.setVisibility(View.VISIBLE);
                        if (user.getImage() != null) {
                            Glide.with(CreateEntryFragment.this).load(Uri.parse(user.getImage()))
                                    .placeholder(R.drawable.ic_baseline_person_24)
                                    .into(enlargeImage);
                        } else {
                            enlargeImage.setImageResource(R.drawable.ic_baseline_person_24);
                        }

                    }
                });*/
                Log.d("image","image is" +user.getImage());
                if (user.getImage() != null) {
                    Glide.with(this).load(Uri.parse(user.getImage()))
                            .placeholder(R.drawable.ic_baseline_person_24)
                            .into(userImage);
                } else {
                    userImage.setImageResource(R.drawable.ic_baseline_person_24);
                }
               // userName.setText(user.getName());
              //  userPhoneNumber.setText(user.getPhoneNumber().substring(3));
                //pincode.setText(user.getPhoneNumber().substring(0,3));

                Log.d("TAG", user.getPhoneNumber());
                if (user.getPhoneNumber2() != null) {

                    phone_linear_layout1.setVisibility(View.VISIBLE);
                 //   userPhoneNumber1.setText(user.getPhoneNumber2().substring(3));
                 //   pincode1.setText(user.getPhoneNumber2().substring(0,3));
                }
                if (user.getPhoneNumber3() != null) {

                    phone_linear_layout2.setVisibility(View.VISIBLE);
                   // userPhoneNumber2.setText(user.getPhoneNumber3().substring(3));
                    Log.e("TAG1", user.getPhoneNumber3().substring(3) );
                  //  pincode2.setText(user.getPhoneNumber3().substring(0,3));
                }

              //  userBirthDay.setText(user.getBirthday());



            });
            editDetails.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    editDetails.setVisibility(View.GONE);
                    addProfilePic.setVisibility(View.VISIBLE);
                    editDetailsDone.setVisibility(View.VISIBLE);
                    pincode.setEnabled(true);
                    pincode1.setEnabled(true);
                    pincode2.setEnabled(true);
                    userPhoneNumber.setEnabled(true);
                    userPhoneNumber1.setEnabled(true);
                    userPhoneNumber2.setEnabled(true);
                    datePick.setVisibility(View.VISIBLE);
                    userName.setEnabled(true);


                }
            });



            editDetailsDone.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String name =userName.getText().toString();
                    String phoneNumber;
                    String phoneNumber1;
                    String phoneNumber2;
                    if (pincode.getText().toString().equals("")) {
                        phoneNumber = "+91" + userPhoneNumber.getText().toString();
                    } else {
                        phoneNumber = pincode.getText().toString() + userPhoneNumber.getText().toString();
                    }
                    if (pincode1.getText().toString().equals("")) {
                        phoneNumber1 = "+91" + userPhoneNumber1.getText().toString();
                    } else {
                        phoneNumber1 = pincode1.getText().toString() + userPhoneNumber1.getText().toString();
                    }
                    if (pincode2.getText().toString().equals("")) {
                        phoneNumber2 = "+91" + userPhoneNumber2.getText().toString();
                    } else {
                        phoneNumber2 = pincode2.getText().toString() + userPhoneNumber2.getText().toString();
                    }
                    String birthday =userBirthDay.getText().toString();
                    if (ProfilePicUri==null){
                        mMyViewModel.getUser().observe(getActivity(), user -> {
                            ProfilePicUri=user.getImage();
                        });
                    }


                    mMyViewModel.updateUser(name,birthday,phoneNumber,phoneNumber1,phoneNumber2,ProfilePicUri,id);
                    ((MainActivity) getActivity()).switchToTab1fragment();




                }
            });



        }

        mMyViewModel.fetchDetailsFromDatabase(id);

        mMyViewModel.getUser().observe(getViewLifecycleOwner(), user -> {
            userImage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    scrollView.setVisibility(View.GONE);
                   enlargeImage.setVisibility(View.VISIBLE);
                    if (user.getImage() != null) {
                        Glide.with(getActivity()).load(Uri.parse(user.getImage()))
                                .placeholder(R.drawable.ic_baseline_person_24)
                                .into(enlargeImage);
                    } else {
                        enlargeImage.setImageResource(R.drawable.ic_baseline_person_24);
                    }



                }
            });

        });

        enlargeImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                enlargeImage.setVisibility(View.GONE);
                scrollView.setVisibility(View.VISIBLE);
            }
        });






        datePick.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatePickerDialog datePickerDialog = new DatePickerDialog(getContext(), new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int year, int month, int dayOfMonth) {
                        month = month + 1;
                        String date = dayOfMonth + "/" + month + "/" + year;
                        userBirthDay.setText(date);
                    }
                },
                        Calendar.getInstance().get(Calendar.YEAR),
                        Calendar.getInstance().get(Calendar.MONTH),
                        Calendar.getInstance().get(Calendar.DAY_OF_MONTH));
                datePickerDialog.show();

            }
        });

        userPhoneNumber.setOnTouchListener(new View.OnTouchListener() {
            @SuppressLint("ClickableViewAccessibility")
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                phone_linear_layout1.setVisibility(View.VISIBLE);
                return false;
            }
        });
        userPhoneNumber1.setOnTouchListener(new View.OnTouchListener() {
            @SuppressLint("ClickableViewAccessibility")
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                phone_linear_layout2.setVisibility(View.VISIBLE);
                return false;
            }
        });

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String name = userName.getText().toString();
                String phoneNumber;
                String phoneNumber1;
                String phoneNumber2;

                if (pincode.getText().toString().equals("")) {
                    phoneNumber = "+91" + userPhoneNumber.getText().toString();
                } else {
                    phoneNumber = pincode.getText().toString() + userPhoneNumber.getText().toString();
                }
                if (pincode1.getText().toString().equals("")) {
                    phoneNumber1 = "+91" + userPhoneNumber1.getText().toString();
                } else {
                    phoneNumber1 = pincode1.getText().toString() + userPhoneNumber1.getText().toString();
                }
                if (pincode2.getText().toString().equals("")) {
                    phoneNumber2 = "+91" + userPhoneNumber2.getText().toString();
                } else {
                    phoneNumber2 = pincode2.getText().toString() + userPhoneNumber2.getText().toString();
                }


                String birthday = userBirthDay.getText().toString();
                if (name.equals("") || phoneNumber.equals("")) {
                    Toast.makeText(getContext(), "Enter Your Details", Toast.LENGTH_SHORT).show();
                } else {
                    Log.d("Time",String.valueOf(System.currentTimeMillis()));
                    User user = new User(name, phoneNumber, phoneNumber1, phoneNumber2, birthday, ProfilePicUri, new Date(),System.currentTimeMillis());

                    Log.d("Phone", phoneNumber1);
                    Log.d("Phone", phoneNumber2);
                    Log.d("DATE1", String.valueOf(new Date()));
                    mMyViewModel.saveToDatabase(user);
                    Toast.makeText(getContext(), "Successfully Registered", Toast.LENGTH_SHORT).show();
                    clearFields();

                    changeTab();
                }


            }
        });

        addProfilePic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectImage(getContext());

            }
        });

        return view;
    }

    private void selectImage(Context context) {
        final CharSequence[] options = {"Take Photo", "Choose from Gallery", "Cancel"};

        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Choose your profile picture");

        builder.setItems(options, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int item) {
                if (options[item].equals("Take Photo")) {

                    checkPermissionAndStartCamera();

                } else if (options[item].equals("Choose from Gallery")) {

                    checkPermissionAndOpenGallery();

                } else
                    dialogInterface.dismiss();
            }
        });
        builder.show();
    }

    private void checkPermissionAndOpenGallery() {
        if (ContextCompat.checkSelfPermission(getContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE) !=
                PackageManager.PERMISSION_GRANTED ||
                ContextCompat.checkSelfPermission(getContext(), Manifest.permission.READ_EXTERNAL_STORAGE) !=
                        PackageManager.PERMISSION_GRANTED)

            requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE,
                            Manifest.permission.READ_EXTERNAL_STORAGE},
                    REQUEST_CODE_GALLERY);
        else
            startOpenGalleryIntent();
    }

    private void startOpenGalleryIntent() {
        Intent intentPickPhoto = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI);
        startActivityForResult(intentPickPhoto, REQUEST_CODE_GALLERY);
    }

    private void startTakePictureIntent() {
        Intent intentTakePicture = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(intentTakePicture, REQUEST_CODE_CAMERA);
    }

    private void checkPermissionAndStartCamera() {
        if (ContextCompat.checkSelfPermission(getContext(), Manifest.permission.CAMERA) !=
                PackageManager.PERMISSION_GRANTED)
            requestPermissions(new String[]{Manifest.permission.CAMERA}, REQUEST_CODE_CAMERA);
        else
            startTakePictureIntent();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == REQUEST_CODE_CAMERA) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED)
                startTakePictureIntent();
            else
                Toast.makeText(getContext(), "Failed. Please grant camera permission", Toast.LENGTH_SHORT).show();
        } else if (requestCode == REQUEST_CODE_GALLERY) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED && grantResults[1] == PackageManager.PERMISSION_GRANTED)
                startOpenGalleryIntent();
            else
                Toast.makeText(getContext(), "Failed. Please grant gallery permission", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (resultCode != RESULT_CANCELED) {
            switch (requestCode) {
                case REQUEST_CODE_CAMERA:
                    Bitmap bitmapCameraImage = (Bitmap) data.getExtras().get("data");
                    Uri cameraImageUri;
                    try {
                        Log.d("TAG", "Inside try of onActivity result of Tab2Fragment");

                        cameraImageUri = SaveBitmap.saveBitmapReturnUri(bitmapCameraImage);
                        //   cameraImageUri = uriFromBitmap.getImageUri(getContext(),bitmapCameraImage);
                        Log.d("TAG", "cameraUri: " + cameraImageUri.toString());
                        //  Log.d("TAG", "cameraUri: " + cameraImageUri.getPath());
                        userImage.setImageURI(cameraImageUri);
                        //  ProfilePicPath = cameraImageUri.getPath();

                        ProfilePicUri = cameraImageUri.toString();

                    } catch (Exception e) {
                        Log.d("TAG", "Inside catch: " + e.getMessage());
                        e.printStackTrace();
                    }

                    break;

                case REQUEST_CODE_GALLERY:
                    Uri selectedImageUri = data.getData();
                    // Log.d("TAG", "URi: " + selectedImageUri.getPath());
                    //   ProfilePicPath = selectedImageUri.getPath();
                    ProfilePicUri = selectedImageUri.toString();
                    userImage.setImageURI(selectedImageUri);
                    break;
            }
        }
    }


    private void changeTab() {
        ViewPager viewPager = getActivity().findViewById(R.id.view_pager);
        viewPager.setCurrentItem(0, true);
    }

    private void clearFields() {
        userImage.setImageResource(R.drawable.ic_baseline_person_24);
        phone_linear_layout1.setVisibility(View.GONE);
        phone_linear_layout2.setVisibility(View.GONE);
        userName.setText("");
        userPhoneNumber.setText("");
        userPhoneNumber1.setText("");
        userPhoneNumber2.setText("");
        pincode1.setText("");
        pincode.setText("");
        pincode2.setText("");

        userBirthDay.setText("");
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mMyViewModel = new ViewModelProvider(getActivity()).get(MyViewModel.class);
    }
}