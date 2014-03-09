package blueprint.pisi_reader;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.ActivityNotFoundException;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends Activity {
	
	/* Constant */
	static final int MENU_CAMERA = Menu.FIRST;
	static final int REQUEST_TAKE_PHOTO = 1;
	static final int REQUEST_UPLOAD_PHOTO = 2;
	static final int REQUEST_PIC_CROP = 3;
	static final int REQUEST_INPUT_TEXT = 4;
	String mCurrentPhotoPath;
	public static String IMAGE_PATH = "image_path";
	public static String BOOK_LEVEL = "book_level";
	public static String BOOK_TEXT = "book_text";
	//captured picture uri
	private Uri picUri;
	Bitmap bitmap;
	
	/* UI */
	TextView mTextGuide;
	ImageView mImageView;
	TextView mTextInputed;
//	Button mBtnSend;
	

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		initiateVariables();
	}
	
	private void initiateVariables() {
		mTextGuide = (TextView) this.findViewById(R.id.txt_title);
		mImageView = (ImageView) this.findViewById(R.id.img_book);
		mTextInputed = (TextView) this.findViewById(R.id.txt_inputed);
//		mBtnSend = (Button) this.findViewById(R.id.btn_send);
//		mCurrentPhotoPath = 
		
//		mBtnSend.setOnClickListener(new View.OnClickListener() {
//			@Override
//			public void onClick(View v) {
//				uploadBookImage();
//			}
//		});
		
		if (mImageView.getDrawable() == null) {
			mTextGuide.setText(R.string.guide_take_picture);
		} else {
			//TODO
			/** image crop */
			mTextGuide.setText(R.string.guide_upload_book);
			mImageView.setOnClickListener(new View.OnClickListener() {
				
				@Override
				public void onClick(View v) {
					try {
						performCrop();
		        	}
		            catch(ActivityNotFoundException anfe){
		        		//display an error message
		        		String errorMessage = "Whoops - your device doesn't support capturing images!";
		        		Toast toast = Toast.makeText(MainActivity.this, errorMessage, Toast.LENGTH_SHORT);
		        		toast.show();
		        	}
				}
			});
		}
	}
	
	@SuppressLint("NewApi")
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		
		if (mImageView.getDrawable() != null) {
		    MenuItem item = menu.findItem(R.id.action_upload);
		    item.setVisible(true);
		}
		return true;
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		super.onOptionsItemSelected(item);
		switch(item.getItemId())
        {
        case R.id.action_camera:
//			dispatchTakePictureIntent();
        	popDialog();
			return true;
        case R.id.action_upload:
        	uploadBookImage();
        	return true;
//        default:
//            return super.onOptionsItemSelected(item);
        }
		return false;
	}
	
	private void dispatchTakePictureIntent() {
	    Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
	    // Ensure that there's a camera activity to handle the intent
	    if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
	        // Create the File where the photo should go
	        File photoFile = null;
	        try {
	            photoFile = createImageFile();
	        } catch (IOException ex) {
	            // Error occurred while creating the File
	            Log.e("io", ex.getMessage());
	        }
	        // Continue only if the File was successfully created
	        if (photoFile != null) {
	        	takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT,
				        Uri.fromFile(photoFile));
				mCurrentPhotoPath = photoFile.getAbsolutePath();
				setResult(Activity.RESULT_OK, takePictureIntent);
				takePictureIntent.putExtra("return-data", true);
	        }
	    }

		startActivityForResult(takePictureIntent, REQUEST_TAKE_PHOTO);
	}
	
	private void inputActivty() {
		Intent inputIntent = new Intent(this, TypeTextActivity.class);
		startActivityForResult(inputIntent, REQUEST_INPUT_TEXT);
	}
	
	@SuppressLint("NewApi")
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
	    if (requestCode == REQUEST_TAKE_PHOTO && resultCode == RESULT_OK) {
//	        Bundle extras = data.getExtras();
//	        Bitmap imageBitmap = (Bitmap) extras.get("data");
//	        mImageView.setImageBitmap(imageBitmap);
	    	
	    	/** Galaxy S3 bug, cannot get("data") straightly, 
	    	 * fixed as following: */
	    	if (mCurrentPhotoPath != null) {
		    	bitmap = rotateImage(mCurrentPhotoPath);
	    	}
	    	if (bitmap != null){
		    	Log.v("ANDROIDDEBUG", mCurrentPhotoPath);
		    	if(mImageView.getVisibility() == View.GONE) mImageView.setVisibility(View.VISIBLE);
		    	setImageLayout();
	    		mImageView.setImageBitmap(bitmap);
	    		mTextGuide.setText(R.string.guide_upload_book);
	    		mImageView.setOnClickListener(new View.OnClickListener() {
					
					@Override
					public void onClick(View v) {
						try {
							performCrop();
			        	}
			            catch(ActivityNotFoundException anfe){
			        		//display an error message
			        		String errorMessage = "Whoops - your device doesn't support capturing images!";
			        		Toast toast = Toast.makeText(MainActivity.this, errorMessage, Toast.LENGTH_SHORT);
			        		toast.show();
			        	}
					}
				});
		    
//	    		handleCameraPhoto();
//			    mBtnSend.setVisibility(View.VISIBLE);
	    		//TODO
	    		/** CROP PIC */
	    		File f = new File(mCurrentPhotoPath);
	    		picUri = Uri.fromFile(f);
//	    		performCrop();
	    	}
	    	
	    	mTextInputed.setText(null);
	    	mTextInputed.setVisibility(View.GONE);
	    }
	    else if (requestCode == REQUEST_UPLOAD_PHOTO) {
	    	//TODO
	    	if (resultCode == RESULT_OK) {
	    		String bookLevel = data.getStringExtra(MainActivity.BOOK_LEVEL);
	    		if (bookLevel != null) {
	    			mTextGuide.setText(R.string.guide_judge_book);
	    			mTextGuide.append(": " + bookLevel);
	    		}
	    		else {
	    			mTextGuide.setText(R.string.guide_no_level);
	    		}
	    	}
	    	else if (resultCode == RESULT_CANCELED) {
	    		mTextGuide.setText(R.string.guide_upload_failed);
	    	}
	    }
	    else if(requestCode == REQUEST_PIC_CROP){
	    	if (resultCode == RESULT_OK) {
		    	//TODO
				//get the returned data
				Bundle extras = data.getExtras();
				//get the cropped bitmap
				bitmap = (Bitmap) extras.get("data");
				//retrieve a reference to the ImageView
				writeBitmapToFile(bitmap, mCurrentPhotoPath);
				
				//display the returned cropped image
//				mImageView.setImageBitmap(thePic);
				bitmap = rotateImage(mCurrentPhotoPath);
				mImageView.setImageBitmap(bitmap);
	    	}
		}
	    else if (requestCode == REQUEST_INPUT_TEXT) {
	    	if (resultCode == RESULT_OK) {
	    		String bookLevel = data.getStringExtra(MainActivity.BOOK_LEVEL);
	    		String bookText = data.getStringExtra(MainActivity.BOOK_TEXT);
	    		if (bookLevel != null) {
	    			mTextGuide.setText(R.string.guide_judge_book);
	    			mTextGuide.append(": " + bookLevel);
	    			
	    			mTextInputed.setVisibility(View.VISIBLE);
	    			mTextInputed.setText(bookText);
	    			mImageView.setVisibility(View.GONE);
	    			mCurrentPhotoPath = null;
	    			mImageView.setImageDrawable(null);
	    		}
	    		else {
	    			mTextGuide.setText(R.string.guide_no_level);
	    		}
	    	}
	    }
	    
		// reload action bar menu icons
    	MainActivity.this.invalidateOptionsMenu();
	}
	
	private File createImageFile() throws IOException {
	    // Create an image file name
	    String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
	    String imageFileName = "BOOK_" + timeStamp + "_";
	    File storageDir = Environment.getExternalStorageDirectory();//.getExternalStoragePublicDirectory(
	            //Environment.DIRECTORY_PICTURES);
	    File image = File.createTempFile(
	        imageFileName,  /* prefix */
	        ".jpg",         /* suffix */
	        storageDir      /* directory */
	    );

	    // Save a file: path for use with ACTION_VIEW intents
	    mCurrentPhotoPath = "file:" + image.getAbsolutePath();
	    return image;
	}
	
	// rotate image automatically according to the property of bitmap
	private Bitmap rotateImage(String filePath) {
		try {
	        File f = new File(filePath);
	        ExifInterface exif = new ExifInterface(f.getPath());
	        int orientation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL);

	        int angle = 0;

	        if (orientation == ExifInterface.ORIENTATION_ROTATE_90) {
	            angle = 90;
	        } 
	        else if (orientation == ExifInterface.ORIENTATION_ROTATE_180) {
	            angle = 180;
	        } 
	        else if (orientation == ExifInterface.ORIENTATION_ROTATE_270) {
	            angle = 270;
	        }
	        
	        BitmapFactory.Options opts = new BitmapFactory.Options ();
	        opts.inSampleSize = 2;   // for 1/2 the image to be loaded
//	        bitmap = Bitmap.createScaledBitmap (BitmapFactory.decodeFile(filePath, opts), 100, 100, true);
	        bitmap = BitmapFactory.decodeStream(new FileInputStream(f), null, opts);
	        
	        if (angle != 0) {
		        Matrix mat = new Matrix();
		        mat.postRotate(angle);
		        bitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), mat, true); 
	        }
	        return bitmap;
	    }
	    catch (IOException e) {
	        Log.w("TAG", "-- Error in setting image");
	    }   
	    catch(OutOfMemoryError oom) {
	        Log.w("TAG", "-- OOM Error in setting image");
	    }
		return null;
	}
	
	private void handleCameraPhoto() {

		if (mCurrentPhotoPath != null) {
			setPic();
			galleryAddPic();
			mCurrentPhotoPath = null;
		}

	}
	
	private void galleryAddPic() {
	    Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
	    File f = new File(mCurrentPhotoPath);
	    Uri contentUri = Uri.fromFile(f);
	    mediaScanIntent.setData(contentUri);
	    this.sendBroadcast(mediaScanIntent);
	}
	
	private void setPic() {
	    // Get the dimensions of the View
	    int targetW = mImageView.getWidth();
	    int targetH = mImageView.getHeight();

	    // Get the dimensions of the bitmap
	    BitmapFactory.Options bmOptions = new BitmapFactory.Options();
	    bmOptions.inJustDecodeBounds = true;
	    BitmapFactory.decodeFile(mCurrentPhotoPath, bmOptions);
	    int photoW = bmOptions.outWidth;
	    int photoH = bmOptions.outHeight;

	    // Determine how much to scale down the image
	    int scaleFactor = Math.min(photoW/targetW, photoH/targetH);

	    // Decode the image file into a Bitmap sized to fill the View
	    bmOptions.inJustDecodeBounds = false;
	    bmOptions.inSampleSize = scaleFactor;
	    bmOptions.inPurgeable = true;

	    Bitmap bitmap = BitmapFactory.decodeFile(mCurrentPhotoPath, bmOptions);
	    mImageView.setImageBitmap(bitmap);
	}
	
	// Some lifecycle callbacks so that the image can survive orientation change
	@Override
	protected void onSaveInstanceState(Bundle outState) {
	    outState.putString(IMAGE_PATH, mCurrentPhotoPath);
	    
	    super.onSaveInstanceState(outState);
	}

	@Override
	protected void onRestoreInstanceState(Bundle savedInstanceState) {
	    super.onRestoreInstanceState(savedInstanceState);
	    
	    mCurrentPhotoPath = savedInstanceState.getString(IMAGE_PATH);
	}
	
	//TODO
	private void uploadBookImage() {
		if (mCurrentPhotoPath != null) {
			Intent sendToServerIntent = new Intent(MainActivity.this, UploadToServer.class);
			sendToServerIntent.putExtra(IMAGE_PATH, mCurrentPhotoPath);
			startActivityForResult(sendToServerIntent, REQUEST_UPLOAD_PHOTO);
		}
		else {
			Toast.makeText(MainActivity.this, "Please take a picture of your book!", Toast.LENGTH_LONG).show();
		}
	}

	/**
     * Helper method to carry out crop operation
     */
    private void performCrop(){
    	//take care of exceptions
    	try {
    		//call the standard crop action intent (the user device may not support it)
	    	Intent cropIntent = new Intent("com.android.camera.action.CROP"); 
	    	//indicate image type and Uri
	    	cropIntent.setDataAndType(picUri, "image/*");
	    	//set crop properties
//	    	cropIntent.putExtra("crop", "true");
	    	//indicate aspect of desired crop
	    	cropIntent.putExtra("aspectX", 1);
	    	cropIntent.putExtra("aspectY", 1);
	    	//indicate output X and Y
	    	cropIntent.putExtra("outputX", 100);
	    	cropIntent.putExtra("outputY", 100);
	    	//retrieve data on return
//	    	cropIntent.putExtra("scale", true);
	    	cropIntent.putExtra("return-data", true);
	    	//start the activity - we handle returning in onActivityResult
	        startActivityForResult(cropIntent, REQUEST_PIC_CROP);  
    	}
    	//respond to users whose devices do not support the crop action
    	catch(ActivityNotFoundException anfe){
    		//display an error message
    		String errorMessage = "Whoops - your device doesn't support the crop action!";
    		Toast toast = Toast.makeText(this, errorMessage, Toast.LENGTH_SHORT);
    		toast.show();
    	}
    }
    
    private void writeBitmapToFile(Bitmap bmp, String filename) {
    	FileOutputStream out = null;
    	try {
    	       out = new FileOutputStream(filename);
    	       bmp.compress(Bitmap.CompressFormat.JPEG, 100, out);
    	} catch (Exception e) {
    	    e.printStackTrace();
    	} finally {
    	       try{
    	           out.close();
    	       } catch(Throwable ignore) {}
    	}
    }
    
    /** Pop single choice dialog
     *  */
    private void popDialog() {
    	// 1. Instantiate an AlertDialog.Builder with its constructor
    	AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);

    	CharSequence[] items = {getResources().getString(R.string.take_picture), 
    							getResources().getString(R.string.input_text)};
		// 2. Chain together various setter methods to set the dialog characteristics
    	builder.setTitle(R.string.dialog_title)
    	       .setItems(items, new DialogInterface.OnClickListener() {
                   public void onClick(DialogInterface dialog, int which) {
                       switch(which) {
                       case 0:
                    	   dispatchTakePictureIntent();
                    	   break;
                       case 1:
                    	   inputActivty();
                    	   break;

                       }
                   }
            });

    	// 3. Get the AlertDialog from create()
    	AlertDialog dialog = builder.create();
    	dialog.show();
    }
    
    private void setImageLayout() {
    	final int oriHeight = bitmap.getHeight();
		final int oriWidth = bitmap.getWidth();

		/*if(mContext.getClass().getName().equals("com.bomoda.jasper.activity.HomeActivity") || mContext.getClass().getName().equals("com.bomoda.jasper.activity.WallDetailActivity")){*/
			int width = (int)(getResources().getDisplayMetrics().widthPixels - 40 * getResources().getDisplayMetrics().density);
			int height = (int) Math.ceil(width * (float)oriHeight/oriWidth);
			RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(width, height);
            mImageView.setLayoutParams(lp);
    }
    
}
