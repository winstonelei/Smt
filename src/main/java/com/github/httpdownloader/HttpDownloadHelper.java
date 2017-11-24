package com.github.httpdownloader;

import net.sf.ehcache.search.aggregator.Count;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.net.URLConnection;
import java.util.concurrent.CountDownLatch;

/**
 * http  下载器
 */
public class HttpDownloadHelper {

	private boolean useTimestamp = false;
    private boolean skipExisting = false;

    public boolean download(URL source, File dest,  DownloadProgress progress, Long timeoutValue) throws Exception {
        if (dest.exists() && skipExisting) {
            return true;
        }
        //don't do any progress, unless asked
        if (progress == null) {
            progress = new NullProgress();
        }
        //set the timestamp to the file date.
        long timestamp = 0;
        boolean hasTimestamp = false;
        if (useTimestamp && dest.exists()) {
            timestamp = dest.lastModified();
            hasTimestamp = true;
        }

        //CountDownLatch countDownLatch = new CountDownLatch(1);
        GetThread getThread = new GetThread(source, dest, hasTimestamp, timestamp, progress);
        try {
            getThread.setDaemon(true);
            getThread.start();
            
            long startTime = System.currentTimeMillis();
            System.out.println("开始下载文件.."+startTime);
            //可以使用countDownLatch 代替join
            getThread.join(timeoutValue);
            // countDownLatch.wait(timeoutValue);

            if (getThread.isAlive()) {
                throw new Exception("The GET operation took longer than " + timeoutValue + ", stopping it.");
            }
            long endTime = System.currentTimeMillis();
            System.out.println("结束文件下载耗时="+(endTime-startTime));
        }
        catch (InterruptedException ie) {
            return false;
        } finally {
            getThread.closeStreams();
        }

        return getThread.wasSuccessful();
    }


    /**
     * Interface implemented for reporting
     * progress of downloading.
     */
    public interface DownloadProgress {
        /**
         * begin a download
         */
        void beginDownload();

        /**
         * tick handler
         */
        void onTick();

        /**
         * end a download
         */
        void endDownload();
    }

    /**
     * do nothing with progress info
     */
    public static class NullProgress implements DownloadProgress {

        /**
         * begin a download
         */
        public void beginDownload() {

        }

        /**
         * tick handler
         */
        public void onTick() {
        }

        /**
         * end a download
         */
        public void endDownload() {

        }
    }

    /**
     * verbose progress system prints to some output stream
     */
    public static class VerboseProgress implements DownloadProgress {
        private int dots = 0;
        // CheckStyle:VisibilityModifier OFF - bc
        PrintWriter writer;
        // CheckStyle:VisibilityModifier ON

        /**
         * Construct a verbose progress reporter.
         *
         * @param out the output stream.
         */
        public VerboseProgress(PrintStream out) {
            this.writer = new PrintWriter(out);
        }

        /**
         * Construct a verbose progress reporter.
         *
         * @param writer the output stream.
         */
        public VerboseProgress(PrintWriter writer) {
            this.writer = writer;
        }

        /**
         * begin a download
         */
        public void beginDownload() {
            writer.print("Downloading ");
            dots = 0;
        }

        /**
         * tick handler
         */
        public void onTick() {
            writer.print(".");
            if (dots++ > 50) {
                writer.flush();
                dots = 0;
            }
        }

        /**
         * end a download
         */
        public void endDownload() {
            writer.println("DONE");
            writer.flush();
        }
    }

    
    private class UploadThread extends Thread{
    	private String fileType;
    	private String filePath;
    	private String accessToken;
    	private String jsonObj ;
    	
    	UploadThread(String fileType, String filePath,String token){
    		this.fileType = fileType;
    		this.filePath = filePath;
    		this.accessToken = token;
    	}
    	public void run(){
		    try {
		    	System.out.println("tokne="+accessToken);
		    	System.out.println("fileType="+fileType);
				String action = "https://qyapi.weixin.qq.com/cgi-bin/media/upload?access_token="
				        + accessToken + "&type=" + fileType;
				URL url = new URL(action);
				String result = null;
				File file = new File(filePath);
				if (!file.exists() || !file.isFile()) {
				    throw new IOException("上传的文件不存在");
				}
				HttpURLConnection con = (HttpURLConnection) url.openConnection();
				con.setRequestMethod("POST"); // 以Post方式提交表单，默认get方式
				con.setDoInput(true);
				con.setDoOutput(true);
				con.setUseCaches(false); // post方式不能使用缓存
				// 设置请求头信息
				con.setRequestProperty("Connection", "Keep-Alive");
				con.setRequestProperty("Charset", "UTF-8");
				// 设置边界
				String BOUNDARY = "----------" + System.currentTimeMillis();
				con.setRequestProperty("Content-Type", "multipart/form-data; boundary=" + BOUNDARY);
				// 请求正文信息
				// 第一部分：
				StringBuilder sb = new StringBuilder();
				sb.append("--"); // 必须多两道线
				sb.append(BOUNDARY);
				sb.append("\r\n");
				sb.append("Content-Disposition: form-data;name=\"file\";filename=\""+ file.getName() + "\"\r\n");
				sb.append("Content-Type:application/octet-stream\r\n\r\n");
				byte[] head = sb.toString().getBytes("utf-8");
				// 获得输出流
				OutputStream out = new DataOutputStream(con.getOutputStream());
				// 输出表头
				out.write(head);
				// 文件正文部分
				// 把文件已流文件的方式 推入到url中
				DataInputStream in = new DataInputStream(new FileInputStream(file));
				int bytes = 0;
				byte[] bufferOut = new byte[1024];
				while ((bytes = in.read(bufferOut)) != -1) {
				    out.write(bufferOut, 0, bytes);
				}
				in.close();
				// 结尾部分
				byte[] foot = ("\r\n--" + BOUNDARY + "--\r\n").getBytes("utf-8");// 定义最后数据分隔线
				out.write(foot);
				out.flush();
				out.close();
				StringBuffer buffer = new StringBuffer();
				BufferedReader reader = null;
				try {
				    // 定义BufferedReader输入流来读取URL的响应
				    reader = new BufferedReader(new InputStreamReader(con.getInputStream()));
				    String line = null;
				    while ((line = reader.readLine()) != null) {
				        buffer.append(line);
				    }
				    if (result == null) {
				        result = buffer.toString();
				    }
				} catch (IOException e) {
				    System.out.println("发送POST请求出现异常！" + e);
				    e.printStackTrace();
				    throw new IOException("数据读取异常");
				} finally {
				    if (reader != null) {
				        reader.close();
				    }
				}
				jsonObj = result;
				System.out.println("得到的上传文件="+jsonObj);
			} catch (MalformedURLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (ProtocolException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (UnsupportedEncodingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	}
    }
    
    private class GetThread extends Thread {

        private final URL source;
        private final File dest;
        private final boolean hasTimestamp;
        private final long timestamp;
        private final DownloadProgress progress;

        private boolean success = false;
        private IOException ioexception = null;
        private InputStream is = null;
        private OutputStream os = null;
        private URLConnection connection;
        private int redirections = 0;
        //private CountDownLatch countDownLatch;


        GetThread(URL source, File dest, boolean h, long t, DownloadProgress p) {
            this.source = source;
            this.dest = dest;
            hasTimestamp = h;
            timestamp = t;
            progress = p;
        }

        public void run() {
            try {
                success = get();
            } catch (IOException ioex) {
                ioexception = ioex;
            }finally {
              //  countDownLatch.countDown();
            }
        }

        private boolean get() throws IOException {

            connection = openConnection(source);

            if (connection == null) {
                return false;
            }

            boolean downloadSucceeded = downloadFile();

            //if (and only if) the use file time option is set, then
            //the saved file now has its timestamp set to that of the
            //downloaded file
            if (downloadSucceeded && useTimestamp) {
                updateTimeStamp();
            }

            return downloadSucceeded;
        }


        private boolean redirectionAllowed(URL aSource, URL aDest) throws IOException {
            // Argh, github does this...
//            if (!(aSource.getProtocol().equals(aDest.getProtocol()) || ("http"
//                    .equals(aSource.getProtocol()) && "https".equals(aDest
//                    .getProtocol())))) {
//                String message = "Redirection detected from "
//                        + aSource.getProtocol() + " to " + aDest.getProtocol()
//                        + ". Protocol switch unsafe, not allowed.";
//                throw new IOException(message);
//            }
            redirections++;
            if (redirections > 5) {
                String message = "More than " + 5 + " times redirected, giving up";
                throw new IOException(message);
            }


            return true;
        }

        private URLConnection openConnection(URL aSource) throws IOException {

            // set up the URL connection
            URLConnection connection = aSource.openConnection();
            // modify the headers
            // NB: things like user authentication could go in here too.
            if (hasTimestamp) {
                connection.setIfModifiedSince(timestamp);
            }

            if (connection instanceof HttpURLConnection) {
                ((HttpURLConnection) connection).setInstanceFollowRedirects(false);
                ((HttpURLConnection) connection).setUseCaches(true);
                ((HttpURLConnection) connection).setConnectTimeout(5000);
            }
            // connect to the remote site (may take some time)
            connection.connect();

            // First check on a 301 / 302 (moved) response (HTTP only)
            if (connection instanceof HttpURLConnection) {
                HttpURLConnection httpConnection = (HttpURLConnection) connection;
                int responseCode = httpConnection.getResponseCode();
                if (responseCode == HttpURLConnection.HTTP_MOVED_PERM ||
                        responseCode == HttpURLConnection.HTTP_MOVED_TEMP ||
                        responseCode == HttpURLConnection.HTTP_SEE_OTHER) {
                    String newLocation = httpConnection.getHeaderField("Location");
                    String message = aSource
                            + (responseCode == HttpURLConnection.HTTP_MOVED_PERM ? " permanently"
                            : "") + " moved to " + newLocation;
                    URL newURL = new URL(newLocation);
                    if (!redirectionAllowed(aSource, newURL)) {
                        return null;
                    }
                    return openConnection(newURL);
                }
                // next test for a 304 result (HTTP only)
                long lastModified = httpConnection.getLastModified();
                if (responseCode == HttpURLConnection.HTTP_NOT_MODIFIED
                        || (lastModified != 0 && hasTimestamp && timestamp >= lastModified)) {
                    // not modified so no file download. just return
                    // instead and trace out something so the user
                    // doesn't think that the download happened when it
                    // didn't
                    return null;
                }
                // test for 401 result (HTTP only)
                if (responseCode == HttpURLConnection.HTTP_UNAUTHORIZED) {
                    String message = "HTTP Authorization failure";
                    throw new IOException(message);
                }
            }

            //REVISIT: at this point even non HTTP connections may
            //support the if-modified-since behaviour -we just check
            //the date of the content and skip the write if it is not
            //newer. Some protocols (FTP) don't include dates, of
            //course.
            return connection;
        }

        private boolean downloadFile() throws FileNotFoundException, IOException {
            IOException lastEx = null;
            for (int i = 0; i < 3; i++) {
                // this three attempt trick is to get round quirks in different
                // Java implementations. Some of them take a few goes to bind
                // property; we ignore the first couple of such failures.
                try {
                    is = connection.getInputStream();
                    break;
                } catch (IOException ex) {
                    lastEx = ex;
                }
            }
            if (is == null) {
                throw new IOException("Can't get " + source + " to " + dest, lastEx);
            }

            os = new FileOutputStream(dest);
            progress.beginDownload();
            boolean finished = false;
            try {
                byte[] buffer = new byte[1024 * 100];
                int length;
                while (!isInterrupted() && (length = is.read(buffer)) >= 0) {
                    os.write(buffer, 0, length);
                    progress.onTick();
                }
                finished = !isInterrupted();
            } finally {
                if (!finished) {
                    // we have started to (over)write dest, but failed.
                    // Try to delete the garbage we'd otherwise leave
                    // behind.
                    //IOUtils.closeWhileHandlingException(os, is);
                    dest.delete();
                } else {
                    //IOUtils.close(os, is);
                }
            }
            progress.endDownload();
            return true;
        }

        private void updateTimeStamp() {
            long remoteTimestamp = connection.getLastModified();
            if (remoteTimestamp != 0) {
                dest.setLastModified(remoteTimestamp);
            }
        }

        /**
         * Has the download completed successfully?
         * <p/>
         * <p>Re-throws any exception caught during executaion.</p>
         */
        boolean wasSuccessful() throws IOException {
            if (ioexception != null) {
                throw ioexception;
            }
            return success;
        }

        /**
         * Closes streams, interrupts the download, may delete the
         * output file.
         */
        void closeStreams() throws IOException {
            interrupt();
            if (success) {
               // IOUtils.close(is, os);
            } else {
                // IOUtils.closeWhileHandlingException(is, os);
                if (dest != null && dest.exists()) {
                    dest.delete();
                }
            }
        }
    }
}
