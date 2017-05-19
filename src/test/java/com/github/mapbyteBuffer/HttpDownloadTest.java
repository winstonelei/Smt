package com.github.mapbyteBuffer;

import com.github.httpdownloader.HttpDownloadHelper;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by winstone on 2017/5/19.
 */
public class HttpDownloadTest {

    public static void main(String[] args){
        try {
			/*URL source= new URL("http://robot.campusphere.cn/robot/materialData/audioMsg/910ae467874b4acaa85308911ff1835e");
			DownloadProgress progress = new NullProgress();
			File dest = new File("F:\\Temp\\hello11\\ABC.mp3");
			new HttpDownloadHelper().download(source, dest, progress, 3000L);*/

            URL source= new URL("http://gs.njust.edu.cn/_upload/article/files/e1/c6/d03ba67b47cf8d5e69ba9d9bccf8/769be492-b17c-48be-b160-64c316b72f5b.pdf");
            HttpDownloadHelper.DownloadProgress progress = new HttpDownloadHelper.NullProgress();
            File dest = new File("F:\\tmp\\webmagic\\abc.pdf");
            new HttpDownloadHelper().download(source, dest, progress, 3000L);
            //
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
