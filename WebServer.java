/* Primzen Rowena Ladia
* CMSC 137 CD-2L
* References:
* http://www.dailyfreecode.com/code/socket-http-server-1251.aspx
* https://www.codeproject.com/Articles/452052/Build-Your-Own-Web-Server
* http://www.java2s.com/Code/Java/Network-Protocol/ASimpleWebServer.htm
* http://javarevisited.blogspot.com/2015/06/how-to-create-http-server-in-java-serversocket-example.html
*/

import java.io.*;
import java.net.*;
import java.util.*;
import java.util.regex.Pattern;

public class WebServer extends Thread {
	static final String HTML_START = "<html>" + "<title>WebServer</title>" + "<body>";

	static final String HTML_END = "</body>" + "</html>";

	Socket clientconnected = null;
	BufferedReader fromClient = null;
	DataOutputStream toClient = null;

	public WebServer(Socket client){
		clientconnected = client;
	}

	public void run(){
		String currline = null;
		String postbound = null;
		String contentlength = null;
		String file = null;
		String headerline;
		StringTokenizer tokenize;
		String httpmethod;
		String httpquery;
		String params;
		Boolean param;
		Hashtable<String,String> postarg;



		PrintWriter fout = null;

		try{
			System.out.println("Connected to server: Client" + clientconnected.getInetAddress() + ";" + clientconnected.getPort());
		

			fromClient = new BufferedReader(new InputStreamReader(clientconnected.getInputStream()));
			toClient = new DataOutputStream(clientconnected.getOutputStream());

			currline = fromClient.readLine();
			
			headerline = currline;
			tokenize = new StringTokenizer(headerline);
			httpmethod = tokenize.nextToken();
			httpquery = tokenize.nextToken();
			params = new String();
			param = false;
			postarg = new Hashtable<String,String>();

			System.out.println(currline);

			if(httpmethod.equals("GET")){
				System.out.println("GET request");

				if(httpquery.equals("/")){
					file = "index.html";
				}

				else{
					file = httpquery.substring(1);

					if(file.indexOf("?") != -1 && (file.length() - 1) != file.indexOf("?")){
						param = true;
						params += "<table style='width:15%'>";

						for(String arg : file.split(Pattern.quote("?"), 2)[1].split("&")){
							params += "<tr><td>" + (arg.split("=", 2))[0] + "</td><td>" + (arg.split("=", 2))[1] + "</td></tr>";
						}

						params += "</table>";
					}

					else if (!file.endsWith(".htm") && !file.endsWith(".html")){
						try {
							sendResponse(200, file, true);
						}
						catch(FileNotFoundException a1){

						}
					}
				}

				File openfile = new File(file);
				
				if(param || (file.length()-1) == file.indexOf("?")){
					openfile = new File((file.split(Pattern.quote("?"), 2))[0]);
					String responses = HTML_START;

					try{
						Scanner scans = new Scanner(openfile);

						while(scans.hasNextLine()){
							responses = responses + scans.nextLine();
						}

						scans.close();

						if(param){
							responses = responses + params;
						}

						responses = responses + HTML_END;

						try{
							sendResponse(200, responses, false);
						}
						catch(SocketException s){

						}
					}

					catch(FileNotFoundException a){
						System.out.println(a.toString());

						if(a.toString().indexOf("Permission denied") != -1){
							responses = HTML_START + "403 Forbidden" + HTML_END;
							sendResponse(403, responses, false);
						}

						else if(a.toString().indexOf("No such file") != -1){
							responses = HTML_START + "404 Not Found" + HTML_END;
							sendResponse(404, responses, false);
						}
					}
				}

				else{
					String responses = "<table style='width:15%>";
					String prevlines = "";

					System.out.println("POST request");

					do{
						currline = fromClient.readLine();

						if(currline.indexOf("Content-type: multipart/form-data") != -1){
							String boundary = currline.split("boundary=")[1];

							//upload actual contents
							while(true){
								System.out.println("" + currline);

								if(currline.equals("--" + boundary + "--")){
									//store previous line's values
									responses = responses + "<td>" + prevlines + "</td></tr></table>";
									break;
								}

								else if(currline.equals("--" + boundary)){
									responses = responses + "<td>" + prevlines + "</td></tr>";
								}

								else if (currline.indexOf("name") != -1){
									responses = responses + "<tr><td>" + (currline.split("\""))[1] + "</td>";
								}

								prevlines = currline;
								currline = fromClient.readLine();
							}

							sendResponse(200, responses, false);
						}
					} while(fromClient.ready());
				}
			}
		}
		catch(Exception e){
			e.printStackTrace();
		}
	}

	public void sendResponse (int statcode, String responses, boolean isfile) throws Exception {
		String statline = null;
		String serverdets = "Server: Java HTTPServer";
		String contentlengthline = null;
		String file = null;
		String contenttype = "Content-type: text/html" + "\r\n";
		FileInputStream fin = null;

		if(statcode == 200){
			statline = "HTTP/1.1 200 OK" + "\r\n";
		}
		else{
			statline = "HTTP 1.1 404 Not Found" + "\r\n";
		}

		if(isfile){
			file = responses;
			fin = new FileInputStream(file);

			contentlengthline = "Content-length: " + Integer.toString(fin.available()) + "\r\n";

			if(!file.endsWith(".htm") && !file.endsWith(".html")){
				contenttype = "Content-type: " + "\r\n";
			}
		}

		else{
			responses = WebServer.HTML_START + responses + WebServer.HTML_END;
			contentlengthline = "Content-length: " + responses.length() + "\r\n";
		}

		toClient.writeBytes(statline);
		toClient.writeBytes(serverdets);
		toClient.writeBytes(contenttype);
		toClient.writeBytes(contentlengthline);
		toClient.writeBytes("Connection: close");
		toClient.writeBytes("\r\n");

		if(isfile){
			sendfile(fin, toClient);
		}
		else{
			toClient.writeBytes(responses);
		}

		toClient.close();
	}

	public void sendfile(FileInputStream fin, DataOutputStream out) throws Exception {
		byte[] buffer = new byte[1024];

		int readbytes;

		while((readbytes = fin.read(buffer)) != -1){
			out.write(buffer, 0, readbytes);
		}

		fin.close();
	}

	public static void main(String args[]) throws Exception{

		if(args.length != 1){
			System.out.println("Usage: java WebServer <port number>");
			System.exit(1);
		}

		ServerSocket server = new ServerSocket(Integer.parseInt(args[0]), 10, InetAddress.getByName("127.0.0.1"));
		System.out.println("Waiting for client on port " + Integer.parseInt(args[0]));

		while(true){
			Socket connected = server.accept();
			(new WebServer(connected)).start();
		}
	}
}