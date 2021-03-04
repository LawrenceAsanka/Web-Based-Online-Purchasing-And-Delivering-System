package lk.bit.web.util.message;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;

public class TextMsgSender {

    public void sendTextMsg(String receiver, String text) throws IOException {
        URL message = new URL("http://textit.biz/sendmsg/index.php?id=94774924237&pw=7727&to="+receiver+"&text="+text+"&from=VG+DISTRIBUTORS");
        BufferedReader in = new BufferedReader(
                new InputStreamReader(message.openStream()));

        String inputLine;
        while ((inputLine = in.readLine()) != null)
            System.out.println(inputLine);
        in.close();
    }
}
