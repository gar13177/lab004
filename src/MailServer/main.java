package MailServer;

import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class main {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
		
		int maxthreads = 3;
		int panicthreads = 2;
		
		try{
			String config = new String(Files.readAllBytes(Paths.get("config.txt")), StandardCharsets.UTF_8).toUpperCase();
			
			Pattern p = Pattern.compile("(\\s|\\n)*(?:MAXTHREADS)(\\s|\\n)*=(\\s|\\n)*([0-9]+)");
			Matcher m = p.matcher(config);
			if (m.matches()){
				maxthreads = Integer.parseInt(m.group(4));
				System.out.println("Cantidad maxima de threads: "+maxthreads);
			}else{
				System.out.println("Cantidad maxima default: "+maxthreads);
			}
		} catch (Exception e){
			System.out.println("error: "+e.getMessage());
		}
		
		RequestQueue queue = new RequestQueue();
		ThreadPool pool = new ThreadPool(maxthreads,panicthreads,queue);
		queue.setThreadPool(pool);
		pool.init();
		new WebServer(queue);
		
	}

}
