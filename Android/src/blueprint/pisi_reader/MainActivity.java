package blueprint.pisi_reader;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import blueprint.pisi_reader.R.id;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends Activity {
	
	/* Constant */
	static final int MENU_CAMERA = Menu.FIRST;
	static final int REQUEST_TAKE_PHOTO = 1;
	static final int REQUEST_UPLOAD_PHOTO = REQUEST_TAKE_PHOTO + 1;
	String mCurrentPhotoPath;
	public static String IMAGE_PATH = "image_path";
	public static String BOOK_LEVEL = "book_level";
	
	/* UI */
	TextView mTextGuide;
	ImageView mImageView;
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
			mTextGuide.setText(R.string.guide_upload_book);
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
			dispatchTakePictureIntent();
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
	
	@SuppressLint("NewApi")
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
	    if (requestCode == REQUEST_TAKE_PHOTO && resultCode == RESULT_OK) {
//	        Bundle extras = data.getExtras();
//	        Bitmap imageBitmap = (Bitmap) extras.get("data");
//	        mImageView.setImageBitmap(imageBitmap);
	    	
	    	/** Galaxy S3 bug, cannot get("data") straightly, 
	    	 * fixed as following: */
	    	
	    	Bitmap bitmap = rotateImage(mCurrentPhotoPath);
	    	if (bitmap != null){
		    	Log.v("ANDROIDDEBUG", mCurrentPhotoPath);
	    		mImageView.setImageBitmap(bitmap);
	    		mTextGuide.setText(R.string.guide_upload_book);
		    
//	    		handleCameraPhoto();
//			    mBtnSend.setVisibility(View.VISIBLE);
			    
			    // reload action bar menu icons
		    	MainActivity.this.invalidateOptionsMenu();
	    	}
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
	}
	
	private File createImageFile() throws IOException {
	    // Create an image file name
	    String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
	    String imageFileName = "BOOK_" + timeStamp + "_";
	    File storageDir = Environment.getExternalStoragePublicDirectory(
	            Environment.DIRECTORY_PICTURES);
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

	        Matrix mat = new Matrix();
	        mat.postRotate(angle);

	        Bitmap bmp = BitmapFactory.decodeStream(new FileInputStream(f), null, null);
	        Bitmap correctBmp = Bitmap.createBitmap(bmp, 0, 0, bmp.getWidth(), bmp.getHeight(), mat, true); 
	        return correctBmp;
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

}
