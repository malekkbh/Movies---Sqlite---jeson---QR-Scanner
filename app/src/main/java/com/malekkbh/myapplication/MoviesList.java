package com.malekkbh.myapplication;


import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Build;
import android.os.Bundle;
import android.os.Vibrator;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.SparseArray;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.vision.CameraSource;
import com.google.android.gms.vision.Detector;
import com.google.android.gms.vision.barcode.Barcode;
import com.google.android.gms.vision.barcode.BarcodeDetector;

import java.io.IOException;


public class MoviesList extends Fragment {

    private static final int PERMISSION_REQUEST_CODE = 23;
    RecyclerView rv;
    SurfaceView sv;
    CameraSource cameraSource;
    TextView tv;
    FloatingActionButton fab ;

    BarcodeDetector barcodeDetector;

    SharedPreferences ref = MainActivity.appContext.getSharedPreferences("v" , 0) ;


    DbHelper dbHelper = new DbHelper(this.getContext() , ref.getInt("version" , 1)) ;

    SQLiteDatabase db = dbHelper.getReadableDatabase();

    public MoviesList() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_movies_list, container, false);




        rv = (RecyclerView) v.findViewById(R.id.rv);
        sv = (SurfaceView) v.findViewById(R.id.camraView);
        tv = (TextView) v.findViewById(R.id.cameraTv);
        fab = (FloatingActionButton) v.findViewById(R.id.fab) ;

        barcodeDetector = new BarcodeDetector.Builder(this.getContext()).setBarcodeFormats(Barcode.QR_CODE).build();

        cameraSource = new CameraSource.Builder(this.getContext(), barcodeDetector).setRequestedPreviewSize(640, 680).build();


        sv.setZOrderOnTop(true);

        sv.getHolder().addCallback(new SurfaceHolder.Callback() {
            @Override
            public void surfaceCreated(SurfaceHolder holder) {
                if (ActivityCompat.checkSelfPermission(MainActivity.appContext, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                    System.out.println("kkkkkkkkkkk no camraa");

                    requestPermission();

//                    return;
                }
                try {
                    cameraSource.start(holder);
                }
                catch (IOException e ){
                    e.printStackTrace();
                }
            }

            @Override
            public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

            }

            @Override
            public void surfaceDestroyed(SurfaceHolder holder) {
                cameraSource.stop();
            }
        });

        barcodeDetector.setProcessor(new Detector.Processor<Barcode>() {
            @Override
            public void release() {

            }

            @Override
            public void receiveDetections(Detector.Detections<Barcode> detections) {

                final SparseArray<Barcode> qrcodes = detections.getDetectedItems() ;

                if (qrcodes.size() != 0 ) {
                    tv.post(new Runnable() {
                        @Override
                        public void run() {
                            Vibrator vibrator = (Vibrator)MainActivity.appContext.getSystemService(Context.VIBRATOR_SERVICE) ;
                            vibrator.vibrate(400);
                            tv.setVisibility(View.VISIBLE);
                            tv.setText(qrcodes.valueAt(0).displayValue);
                            Cursor data = db.rawQuery("SELECT title,image,rating,releaseYear,genre  FROM 'Movies' WHERE title=?  " , new String[] {qrcodes.valueAt(0).displayValue} );
                            if (data.getCount()>0) {
                               Snackbar.make(MoviesList.this.getActivity().findViewById(R.id.container), "Current movie already exist in the Database" , Snackbar.LENGTH_LONG).show(); ;
                            }else {
                                dbHelper.addData(new Movie(qrcodes.valueAt(0).displayValue )) ;
                            }



                        }
                    });
                }



            }
        });


        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sv.setVisibility(View.VISIBLE);
            }
        });



        rv.setAdapter( new RvAdopter(this.getContext()));
        rv.setLayoutManager(new LinearLayoutManager(this.getContext()));

        v.setFocusableInTouchMode(true);
        v.requestFocus();
        v.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if( keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_UP) {
                    if (sv.getVisibility() == View.VISIBLE){
                        sv.setVisibility(View.INVISIBLE);
                        tv.setVisibility(View.INVISIBLE);

//
                        return true ;
                    }else
                        //Log.i(tag, "onKey Back listener is working!!!");
                        // getFragmentManager().popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
//                    getFragmentManager().beginTransaction().replace(R.id.container , new StudentSingUp()).commit() ;
                        return false;
                }
                return false;
            }

        });



        return v ;
    }//viewOnCreat


    private void requestPermission() {

        ActivityCompat.requestPermissions(getActivity(),
                new String[]{Manifest.permission.CAMERA},
                PERMISSION_REQUEST_CODE);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case PERMISSION_REQUEST_CODE:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(MainActivity.appContext, "Permission Granted", Toast.LENGTH_SHORT).show();

                    // main logic
                } else {
                    Toast.makeText(MainActivity.appContext, "Permission Denied", Toast.LENGTH_SHORT).show();
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        if (ContextCompat.checkSelfPermission(getContext(), Manifest.permission.CAMERA)
                                != PackageManager.PERMISSION_GRANTED) {
                            showMessageOKCancel("You need to allow access permissions",
                                    new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                                                requestPermission();
                                            }
                                        }
                                    });
                        }
                    }
                }
                break;
        }
    }

    private void showMessageOKCancel(String message, DialogInterface.OnClickListener okListener) {
        new AlertDialog.Builder(MainActivity.appContext)
                .setMessage(message)
                .setPositiveButton("OK", okListener)
                .setNegativeButton("Cancel", null)
                .create()
                .show();
    }




        }






