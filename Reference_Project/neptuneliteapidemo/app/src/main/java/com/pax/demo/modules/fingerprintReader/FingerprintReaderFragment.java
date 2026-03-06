package com.pax.demo.modules.fingerprintReader;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.RequiresApi;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.pax.dal.IFingerprintReader;
import com.pax.dal.entity.FingerprintResult;
import com.pax.dal.exceptions.FingerprintDevException;
import com.pax.demo.R;
import com.pax.demo.base.BaseFragment;
import com.pax.demo.base.DemoApp;
import com.pax.neptunelite.api.NeptuneLiteUser;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * @author wenyongzhe
 * @date 17/04/2024
 */
public class FingerprintReaderFragment extends BaseFragment{
    private TextView tvResult,tv_imageSize_tips;
    private Button btnFingerCaptureFeature,btn_fingerCaptureImage,btn_fingerCaptureStop,
            bt_fingerOpen,btn_close,btn_setImageSize,btn_compress,
            bt_power_on,bt_power_off;
    private Button btn_getNfiqScore,btn_getNfiq2Score;
    private EditText et_width,et_height,et_compress;
    private ImageView imageView;

    private byte[] feature1 = null;
    private byte[] feature2 = null;
    private boolean featureFlag = true;
    private ScrollView scrollView;
    Context mContext;

    public static final byte FINGER_RETVAL_IS_OPEND = 4;
    public static final byte FINGER_RETVAL_IS_STARTED = 6;
    public static final byte FINGER_RETVAL_NOT_OPEND = 5;
    public static final byte FINGER_RETVAL_NOT_POWER_OFF = 2;
    public static final byte FINGER_RETVAL_NOT_POWER_ON = 3;
    public static final byte FINGER_RETVAL_NOT_STARTED = 7;
    public static final byte SERVICE_NOT_AVAILABLE = 1;
    public static final byte RET_EXEC_ERR = -2;

    @SuppressLint("MissingInflatedId")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_fingerprint_reader, container, false);
        mContext = getActivity();
        dialogInit();
        // 初始化TextToSpeech对象
        tvResult = (TextView) view.findViewById(R.id.tv_result);
        imageView = view.findViewById(R.id.imageView);
        et_width = view.findViewById(R.id.et_width);
        et_height = view.findViewById(R.id.et_height);
        scrollView = view.findViewById(R.id.scrollView);
        et_compress = view.findViewById(R.id.et_compress);
        tv_imageSize_tips = view.findViewById(R.id.tv_imageSize_tips);
        tv_imageSize_tips.setText("width:256-600 height:360-800");
        btn_compress = view.findViewById(R.id.btn_compress);
        bt_power_on = view.findViewById(R.id.bt_power_on);
        bt_power_off = view.findViewById(R.id.bt_power_off);

        bt_power_on.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
            @Override
            public void onClick(View view) {
                try {
                    NeptuneLiteUser.getInstance().getDal(mContext).getFingerprintReader().controlPower(true);
                    Toast.makeText(DemoApp.appContext,"succes",Toast.LENGTH_SHORT).show();
                } catch (Exception e) {
                    Toast.makeText(DemoApp.appContext,"error",Toast.LENGTH_SHORT).show();
                }
            }
        });
        bt_power_off.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
            @Override
            public void onClick(View view) {
                try {
                    NeptuneLiteUser.getInstance().getDal(mContext).getFingerprintReader().controlPower(false);
                    Toast.makeText(DemoApp.appContext,"succes",Toast.LENGTH_SHORT).show();
                } catch (Exception e) {
                    Toast.makeText(DemoApp.appContext,"error",Toast.LENGTH_SHORT).show();
                }
            }
        });
        btn_compress.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
            @Override
            public void onClick(View view) {
                fingerCaptureImage(3,0);
            }
        });

        btn_getNfiqScore = view.findViewById(R.id.btn_getNfiqScore);
        btn_getNfiqScore.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
            @Override
            public void onClick(View view) {
                fingerCaptureImage(1,1);
            }
        });

        btn_getNfiq2Score = view.findViewById(R.id.btn_getNfiq2Score);
        btn_getNfiq2Score.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
            @Override
            public void onClick(View view) {
                fingerCaptureImage(1,2);
            }
        });

        btn_setImageSize = view.findViewById(R.id.btn_setImageSize);
        btn_setImageSize.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                handler.sendEmptyMessage(2);
            }
        });

        bt_fingerOpen = view.findViewById(R.id.bt_fingerOpen);
        bt_fingerOpen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                fingerCaptureOpen();
            }
        });

        btnFingerCaptureFeature = view.findViewById(R.id.btn_fingerCaptureFeature);
        btnFingerCaptureFeature.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
            @Override
            public void onClick(View view) {
                fingerCaptureFeature();
            }
        });

        btn_fingerCaptureStop = view.findViewById(R.id.btn_fingerCaptureStop);
        btn_fingerCaptureStop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                fingerCaptureStop();
            }
        });

        btn_fingerCaptureImage = view.findViewById(R.id.btn_fingerCaptureImage);
        btn_fingerCaptureImage.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
            @Override
            public void onClick(View view) {
                fingerCaptureImage(2,0);
            }
        });

        btn_close = view.findViewById(R.id.btn_close);
        btn_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                close();
            }
        });
        showLog(getString(R.string.OPEN_TIPS));
        return view;
    }

    public void close(){
        try {
            NeptuneLiteUser.getInstance().getDal(mContext).getFingerprintReader().close();
            Thread.sleep(2000);
            showLog("close Success");
        } catch (FingerprintDevException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            showLog("close error "+getErrorCode(e.getErrCode()));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public void compareFeature() {
        if(feature1 == null || feature2 == null){
            Toast.makeText(mContext, R.string.tips, Toast.LENGTH_LONG).show();
        }
        try {
            int result = NeptuneLiteUser.getInstance().getDal(mContext).getFingerprintReader().compareFeature(4,feature1,feature2);
            showLog("compareFeature: " + result);
        } catch (FingerprintDevException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void fingerCaptureOpen() {
        showLog("Opening...");
        new Thread(){
            @Override
            public void run() {
                try {
                    NeptuneLiteUser.getInstance().getDal(mContext).getFingerprintReader().open();
                    Thread.sleep(2000);
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            showLog("Open success");
                            handler.sendEmptyMessage(2);
                        }
                    });
                    try {
                        NeptuneLiteUser.getInstance().getDal(mContext).getFingerprintReader().setTimeout(5000);
                        showLog("Set Timeout(5s) success");
                    } catch (Exception e) {
                        e.printStackTrace();
                        showLog("Set Timeout error");
                    }
                } catch (FingerprintDevException e) {
                    e.printStackTrace();
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            showLog("Open error "+e.getMessage());
                        }
                    });
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }
        }.start();
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public void fingerCaptureFeature() {
        try {
            showLog(getString(R.string.captureTips));
            NeptuneLiteUser.getInstance().getDal(mContext).getFingerprintReader().extractFeature(2, new IFingerprintReader.FingerprintListener() {
                @Override
                public void onError(int i) {
                    showLog("CaptureFeature onError: " + getErrorCode(i));
                }

                @Override
                public void onSuccess(FingerprintResult fingerprintResult) {
                    if(featureFlag){
                        feature1 = fingerprintResult.getFeatureCode();
                    }else {
                        feature2 = fingerprintResult.getFeatureCode();
                    }
                    featureFlag = !featureFlag;
                    String mFeatureCode = bytesToHexString(fingerprintResult.getFeatureCode());
                    showLog("CaptureFeature: " + mFeatureCode.subSequence(0,10) + "...");
                    if(feature1 != null && feature2 != null){
                        compareFeature();
                    }
                }
            });
        } catch (FingerprintDevException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            showLog("CaptureFeature onError: " + getErrorCode(e.getErrCode()));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void fingerCaptureStop() {
        try {
            NeptuneLiteUser.getInstance().getDal(mContext).getFingerprintReader().stop();
            showLog("CaptureStop Success");
        } catch (FingerprintDevException e) {
            e.printStackTrace();
            showLog("CaptureStop Error "+getErrorCode(e.getErrCode()));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    byte[] captureImage = null;
    int compress = 5;
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public void fingerCaptureImage(int flag,int nfiq2Score) {
        try {
            showLog(getString(R.string.captureTips));
            try {
                compress = Integer.parseInt(et_compress.getText().toString());
            }catch (Exception e){
            }
            NeptuneLiteUser.getInstance().getDal(mContext).getFingerprintReader().extractImage(flag, compress, new IFingerprintReader.FingerprintListener() {
                @Override
                public void onError(int i) {
                    showLog("CaptureImage onError: " + getErrorCode(i));
                }

                @Override
                public void onSuccess(FingerprintResult fingerprintResult) {
                    showLog("CaptureImage imageQuality: " + fingerprintResult.getImageQuality());
                    new Thread(){
                        @Override
                        public void run() {
                            Bitmap bitmap;
                            captureImage = fingerprintResult.getCaptureImage();

                            if(flag ==2 ){
                                String imageUrl = new File(mContext.getExternalFilesDir(Environment.DIRECTORY_PICTURES), "latest_fingerprint_picture.bmp").getAbsolutePath();
                                saveImage(captureImage, imageUrl);
                            }
                            bitmap = getPicFromBytes(captureImage);

                            getActivity().runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    imageView.setImageBitmap(bitmap);
                                }
                            });
                        }
                    }.start();

                }
            });
        } catch (FingerprintDevException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            showLog("CaptureImage onError: " + getErrorCode(e.getErrCode()));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

//    public static void convertRawToPng(byte[] rawData, int width, int height, String outputFilePath) throws IOException {
//        // 确保图像数据是有效的
//        if (rawData == null || width <= 0 || height <= 0) {
//            throw new IllegalArgumentException("Invalid image data or dimensions.");
//        }
//
//        // 假设RAW数据是RGB格式，每个像素占用3个字节
//        BufferedImage bufferedImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
//
//        // 将原始数据复制到BufferedImage中
//        for (int y = 0; y < height; y++) {
//            for (int x = 0; x < width; x++) {
//                int index = (y * width + x) * 3; // RGB数据的索引
//                int rgb = 0xFF << 24 | // Alpha
//                        (rawData[index] & 0xFF) << 16 | // Red
//                        (rawData[index + 1] & 0xFF) << 8 | // Green
//                        (rawData[index + 2] & 0xFF); // Blue
//                bufferedImage.setRGB(x, y, rgb);
//            }
//        }
//
//        // 将BufferedImage保存为PNG文件
//        File outputFile = new File(outputFilePath);
//        ImageIO.write(bufferedImage, "PNG", outputFile);
//        System.out.println("RAW image converted to PNG successfully: " + outputFile.getAbsolutePath());
//    }

    public void saveImage(byte[] imageData, String imageUrl) {
        try {
            // 指定保存图片的路径和文件名
            File outputFile = new File(imageUrl);
            // 使用FileOutputStream将字节数组写入文件
            FileOutputStream fos = new FileOutputStream(outputFile);
            fos.write(imageData);
            fos.flush(); // 刷新缓冲区
            fos.close(); // 关闭输出流
            showLog("The picture was successfully saved to：" + imageUrl);
        } catch (IOException e) {
            showLog("An error occurred while saving the picture：" + e.getMessage());
            e.printStackTrace();
        }
    }

    Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case 1:
                    tvResult.append(msg.obj.toString()+"\n");
                    Log.w("FingerprintReader", msg.obj.toString());
                    break;
                case 2:
                    try {
                        String width = et_width.getText().toString();
                        String height = et_height.getText().toString();
                        NeptuneLiteUser.getInstance().getDal(mContext).getFingerprintReader().setImageSize(Integer.parseInt(width),Integer.parseInt(height));
                        showLog("Set Image Size "+width +"x"+ height +" success");
                    } catch (Exception e) {
                        e.printStackTrace();
                        showLog("Set Image Size error"+getString(R.string.OPEN_TIPS));
                    }
                    break;

            }
            scrollView.post(new Runnable() {
                @Override
                public void run() {
                    scrollView.smoothScrollTo(0, tvResult.getBottom());
                }
            });
        }
    };

    private void showLog(final String log) {
        Message ms = new Message();
        ms.obj = log;
        ms.what = 1;
        handler.sendMessage(ms);
    }

    /**
     * 将字节数组转换为ImageView可调用的Bitmap对象
     * @param bytes
     * @return
     **/
    public static Bitmap getPicFromBytes(byte[] bytes) {
        BitmapFactory.Options opts = new BitmapFactory.Options();
        opts.inSampleSize = 2;
        if (bytes != null)
            if (opts != null)
                return BitmapFactory.decodeByteArray(bytes, 0, bytes.length, opts);
            else
                return BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
        return null;
    }

    public  String bytesToHexString(byte[] bArray) {
        StringBuilder sb = new StringBuilder(bArray.length);
        String sTemp;
        for (int i = 0; i < bArray.length; i++) {
            sTemp = Integer.toHexString(0xFF & bArray[i]);
            if (sTemp.length() < 2)
                sb.append(0);
            sb.append(sTemp.toUpperCase());
        }
        return sb.toString();
    }

    private String getErrorCode(int code){
        switch (code){
            case FINGER_RETVAL_IS_OPEND:
                return getString(R.string.FINGER_RETVAL_IS_OPEND);
            case FINGER_RETVAL_IS_STARTED:
                return getString(R.string.FINGER_RETVAL_IS_STARTED);
            case FINGER_RETVAL_NOT_OPEND:
                return getString(R.string.FINGER_RETVAL_NOT_OPEND);
            case FINGER_RETVAL_NOT_POWER_OFF:
                return getString(R.string.FINGER_RETVAL_NOT_POWER_OFF);
            case FINGER_RETVAL_NOT_POWER_ON:
                return getString(R.string.FINGER_RETVAL_NOT_POWER_ON);
            case FINGER_RETVAL_NOT_STARTED:
                return getString(R.string.FINGER_RETVAL_NOT_STARTED);
            case SERVICE_NOT_AVAILABLE:
                return getString(R.string.SERVICE_NOT_AVAILABLE);
            case RET_EXEC_ERR:
                return getString(R.string.RET_EXEC_ERR);
            default:
                return String.valueOf(code);

        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        close();
    }

    Handler dialogHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            if(mAlertDialog == null){
                dialogInit();
            }
            switch (msg.what){
                case 1:
                    mAlertDialog.show();
                    break;
                case 2:
                    mAlertDialog.dismiss();
                    break;
            }
        }
    };

    AlertDialog mAlertDialog;
    private void dialogInit(){
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setMessage("    Getting Nfiq2Score...");
        builder.setCancelable(false);
        mAlertDialog = builder.create();
    }

}
