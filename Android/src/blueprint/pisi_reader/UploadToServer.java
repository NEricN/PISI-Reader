package blueprint.pisi_reader;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import android.os.Bundle;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

public class UploadToServer extends Activity {
    
    TextView messageText;
//    Button uploadButton;
    int serverResponseCode = 0;
    ProgressDialog dialog = null;
    String bookLevel = null;
    String upLoadServerUri = null;
    
    /* Result Status */
    final static int RESULT_SUCCESS = 0;
    final static int RESULT_FAILED = 1;
     
    /**********  File Path *************/
//    final String uploadFilePath = "/mnt/sdcard/";
//    final String uploadFileName = "service_lifecycle.png";
    String picturePath = "";
     
    @Override
    public void onCreate(Bundle savedInstanceState) {
         
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload_to_server);
        
        picturePath = getIntent().getStringExtra(MainActivity.IMAGE_PATH);
          
//        uploadButton = (Button)findViewById(R.id.uploadButton);
        messageText  = (TextView)findViewById(R.id.messageText);
         
        messageText.setText("Uploading file path :- " + picturePath); //'/mnt/sdcard/"+uploadFileName+"'");
         
        /************* Ruby Rails upload path ****************/
        //TODO
        upLoadServerUri = "http://192.168.6.135:8080/upload"; //"http://www.androidexample.com/media/UploadToServer.php";
         
        doUpload();
        
//        uploadButton.setOnClickListener(new OnClickListener() {            
//            @Override
//            public void onClick(View v) {
//            		doUpload(); 
//            }
//        });
    }
    
    private void doUpload() {
    	dialog = ProgressDialog.show(UploadToServer.this, "", "Uploading book...", true);
        
        new Thread(new Runnable() {
            public void run() {
			 runOnUiThread(new Runnable() {
		        public void run() {
		            messageText.setText("uploading started.....");
		            }
		        });                      
		   
		     //TODO
			 uploadFile(picturePath);
                                          
            }
        }).start();        
    }
      
    public int uploadFile(String sourceFileUri) {
           
           
          String fileName = sourceFileUri;
  
          HttpURLConnection conn = null;
          DataOutputStream dos = null;  
          String lineEnd = "\r\n";
          String twoHyphens = "--";
          String boundary = "*****";
          int bytesRead, bytesAvailable, bufferSize;
          byte[] buffer;
          int maxBufferSize = 1 * 1024 * 1024; 
          File sourceFile = new File(sourceFileUri); 
           
          if (!sourceFile.isFile()) {
               
               dialog.dismiss(); 
                
               Log.e("uploadFile", "Source File not exist :"
                                   + picturePath);
                
               runOnUiThread(new Runnable() {
                   public void run() {
                       messageText.setText("Source File not exist :"
                               + picturePath);
                   }
               }); 
                
               return 0;
            
          }
          else
          {
               try { 
                    
                     // open a URL connection to the Servlet
                   FileInputStream fileInputStream = new FileInputStream(sourceFile);
                   URL url = new URL(upLoadServerUri);
                    
                   // Open a HTTP  connection to  the URL
                   conn = (HttpURLConnection) url.openConnection(); 
                   conn.setDoInput(true); // Allow Inputs
                   conn.setDoOutput(true); // Allow Outputs
                   conn.setUseCaches(false); // Don't use a Cached Copy
                   conn.setRequestMethod("POST");
                   conn.setRequestProperty("Connection", "Keep-Alive");
                   conn.setRequestProperty("ENCTYPE", "multipart/form-data");
                   conn.setRequestProperty("Content-Type", "multipart/form-data;boundary=" + boundary);
                   conn.setRequestProperty("uploaded_file", fileName); 
                    
                   dos = new DataOutputStream(conn.getOutputStream());
          
                   dos.writeBytes(twoHyphens + boundary + lineEnd); 
                   dos.writeBytes("Content-Disposition: form-data; name=\"uploaded_file\";filename=\""
                                             + fileName + "\"" + lineEnd);
                    
                   dos.writeBytes(lineEnd);
          
                   // create a buffer of  maximum size
                   bytesAvailable = fileInputStream.available(); 
          
                   bufferSize = Math.min(bytesAvailable, maxBufferSize);
                   buffer = new byte[bufferSize];
          
                   // read file and write it into form...
                   bytesRead = fileInputStream.read(buffer, 0, bufferSize);  
                      
                   while (bytesRead > 0) {
                        
                     dos.write(buffer, 0, bufferSize);
                     bytesAvailable = fileInputStream.available();
                     bufferSize = Math.min(bytesAvailable, maxBufferSize);
                     bytesRead = fileInputStream.read(buffer, 0, bufferSize);   
                      
                    }
          
                   // send multipart form data necesssary after file data...
                   dos.writeBytes(lineEnd);
                   dos.writeBytes(twoHyphens + boundary + twoHyphens + lineEnd);
          
                   // Responses from the server (code and message)
                   serverResponseCode = conn.getResponseCode();
                   //TODO
                   final String serverResponseMessage = conn.getResponseMessage();
                     
                   Log.i("uploadFile", "HTTP Response is : "
                           + serverResponseMessage + ": " + serverResponseCode);
                    
                   if(serverResponseCode == 200){
                	   //TODO
                	   BufferedReader in = new BufferedReader(new InputStreamReader(
                               conn.getInputStream()));
                	   
                	   String inputLine = null;
                       while ((inputLine = in.readLine()) != null)  {
                           bookLevel = inputLine;
                       }
                       in.close();
                       
                       runOnUiThread(new Runnable() {
                            public void run() {
                                 //TODO
                            	String msg = "This book has a level of: " + bookLevel;
//                                String msg = "File Upload Completed.\n\n See uploaded file here : \n\n"
//                                              +" http://www.androidexample.com/media/uploads/"
//                                              + picturePath.substring(picturePath.lastIndexOf(".") - 6, picturePath.lastIndexOf("."));
                                 
                                messageText.setText(msg);
                                Toast.makeText(UploadToServer.this, "File Upload Complete.", 
                                             Toast.LENGTH_SHORT).show();
                            }
                        });                
                   }    
                    
                   //close the streams //
                   fileInputStream.close();
                   dos.flush();
                   dos.close();
                     
              } catch (MalformedURLException ex) {
                   
                  dialog.dismiss();  
                  ex.printStackTrace();
                   
                  runOnUiThread(new Runnable() {
                      public void run() {
                          messageText.setText("MalformedURLException Exception : check script url.");
                          Toast.makeText(UploadToServer.this, "MalformedURLException", 
                                                              Toast.LENGTH_SHORT).show();
                      }
                  });
                   
                  Log.e("Upload file to server", "error: " + ex.getMessage(), ex);  
                  
                  responseToMain(RESULT_FAILED, null);
              } catch (Exception e) {
                   
                  dialog.dismiss();  
                  e.printStackTrace();
                   
                  runOnUiThread(new Runnable() {
                      public void run() {
                          messageText.setText("Got Exception : see logcat ");
                          Toast.makeText(UploadToServer.this, "Got Exception : see logcat ", 
                                  Toast.LENGTH_SHORT).show();
                      }
                  });
                  Log.e("Upload file to server Exception", "Exception : "
                                                   + e.getMessage(), e);  
                  responseToMain(RESULT_FAILED, null);
              }
              dialog.dismiss();
              
              //TODO
              responseToMain(RESULT_SUCCESS, bookLevel);
              
              return serverResponseCode; 
               
           } // End else block 
         } 
    
    private void responseToMain(int status, String level) {
    	Intent intent = new Intent();
    	if (status == RESULT_SUCCESS) {
	    	intent.putExtra(MainActivity.BOOK_LEVEL, level);
		    setResult(Activity.RESULT_OK, intent);
    	} else {
    		setResult(Activity.RESULT_CANCELED);
    	}
    		
	    UploadToServer.this.finish();
    }
}
