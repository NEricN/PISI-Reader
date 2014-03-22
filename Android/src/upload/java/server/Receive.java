package upload.java.server;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import javax.imageio.ImageIO;
import javax.imageio.stream.ImageInputStream;
import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;

/**
 * 
 * @author Sandip
 */
@WebServlet("/Receive")
@MultipartConfig
public class Receive extends HttpServlet {

	/**
	 * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
	 * methods.
	 * 
	 * @param request
	 *            servlet request
	 * @param response
	 *            servlet response
	 * @throws ServletException
	 *             if a servlet-specific error occurs
	 * @throws IOException
	 *             if an I/O error occurs
	 */
	protected void processRequest(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		response.setContentType("text/html;charset=UTF-8");
		PrintWriter out = response.getWriter();
		try {
			Part part = request.getPart("uploaded_file");
			InputStream in = part.getInputStream();

			BufferedImage img = null;
			out.println("1");
//			InputStream in = request.getInputStream();
			
//			//TODO
//			byte[] buffer = new byte[1024*1024*1];
//			for (int length = 0; (length = in.read(buffer)) > 0;) {
//            }
//			
//			ImageInputStream iis = (ImageInputStream) ImageIO.read(new ByteArrayInputStream(buffer));

			ImageInputStream iis = ImageIO.createImageInputStream(in);
			// File f = new
			// File("http://localhost:8080/M-Admin_1.0_Web_Module/logo.png");
			// URL url = new
			// URL("http://localhost:8080/M-Admin_1.0_Web_Module/logo.png");

			out.println("2");
			// Image image = ImageIO.read(in);
			out.println("3");
			try {
				img = ImageIO.read(iis);
			} catch (Exception e) {
				out.println(e.getMessage());
			}
			out.println("4");
			File file = new File("E:\\tmp\\image.jpg");
			out.println("5");
			try {
				ImageIO.write(img, "jpg", file);
			} catch (Exception e) {
				out.println(e.getLocalizedMessage());
			}
			out.println("success");
		} finally {
			out.close();
		}
	}

	// <editor-fold defaultstate="collapsed"
	// desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
	/**
	 * Handles the HTTP <code>GET</code> method.
	 * 
	 * @param request
	 *            servlet request
	 * @param response
	 *            servlet response
	 * @throws ServletException
	 *             if a servlet-specific error occurs
	 * @throws IOException
	 *             if an I/O error occurs
	 */
	@Override
	protected void doGet(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		System.out.println("do get");
//		processRequest(request, response);
		HttpURLConnection conn = null;
		BufferedReader br = null;
		DataOutputStream dos = null;
		DataInputStream inStream = null;

		InputStream is = null;
		OutputStream os = null;
		boolean ret = false;
		String StrMessage = "";
		String exsistingFileName = "E:\\tmp\\cover.jpg";  

		String lineEnd = "\r\n";
		String twoHyphens = "--";
		String boundary = "*****";

		int bytesRead, bytesAvailable, bufferSize;

		byte[] buffer;

		int maxBufferSize = 1 * 1024 * 1024;

		String responseFromServer = "";

		String urlString = "http://192.168.1.12:8080/UploadImage/Receive";

		try {
			// ------------------ CLIENT REQUEST

			FileInputStream fileInputStream = new FileInputStream(new File(
					exsistingFileName));

			// open a URL connection to the Servlet

			URL url = new URL(urlString);

			// Open a HTTP connection to the URL

			conn = (HttpURLConnection) url.openConnection();

			// Allow Inputs
			conn.setDoInput(true);

			// Allow Outputs
			conn.setDoOutput(true);

			// Don't use a cached copy.
			conn.setUseCaches(false);

			// Use a post method.
			conn.setRequestMethod("POST");

			conn.setRequestProperty("Connection", "Keep-Alive");

			conn.setRequestProperty("Content-Type",
					"multipart/form-data;boundary=" + boundary);

			dos = new DataOutputStream(conn.getOutputStream());

			dos.writeBytes(twoHyphens + boundary + lineEnd);
			dos.writeBytes("Content-Disposition: form-data; name=\"uploaded_file\";"
					+ " filename=\"" + exsistingFileName + "\"" + lineEnd);
			dos.writeBytes(lineEnd);

			// create a buffer of maximum size

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

			// close streams

			fileInputStream.close();
			dos.flush();
			dos.close();

		} catch (MalformedURLException ex) {
			System.out.println("From ServletCom CLIENT REQUEST:" + ex);
		}

		catch (IOException ioe) {
			System.out.println("From ServletCom CLIENT REQUEST:" + ioe);
		}

		// ------------------ read the SERVER RESPONSE

		try {
			inStream = new DataInputStream(conn.getInputStream());
			String str;
			while ((str = inStream.readLine()) != null) {
				System.out.println("Server response is: " + str);
				System.out.println("");
			}
			inStream.close();

		} catch (IOException ioex) {
			System.out.println("From (ServerResponse): " + ioex);

		}
	}

	/**
	 * Handles the HTTP <code>POST</code> method.
	 * 
	 * @param request
	 *            servlet request
	 * @param response
	 *            servlet response
	 * @throws ServletException
	 *             if a servlet-specific error occurs
	 * @throws IOException
	 *             if an I/O error occurs
	 */
	@Override
	protected void doPost(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		processRequest(request, response);
	}

	/**
	 * Returns a short description of the servlet.
	 * 
	 * @return a String containing servlet description
	 */
	@Override
	public String getServletInfo() {
		return "Short description";
	}// </editor-fold>
};
