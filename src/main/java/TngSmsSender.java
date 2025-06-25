/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

import com.twilio.Twilio;
import com.twilio.rest.api.v2010.account.Message;

public class TngSmsSender {
    static Colors color = new Colors();

    public static final String ACCOUNT_SID = "AC695e6b6034b7479e447190d16d4c2160";
    public static final String AUTH_TOKEN = "e745ac4376f1634f808b0ca4ae749898";
    private static final String FROM_PHONE = "+16198151437";
    static {
        Twilio.init(ACCOUNT_SID, AUTH_TOKEN);
    }
    public static String sendVerificationCode(String phoneNumber) {
        String code = String.format("%06d", (int)(Math.random() * 1000000));
        Message message = Message.creator(
                new com.twilio.type.PhoneNumber("+6" + phoneNumber),
                new com.twilio.type.PhoneNumber(FROM_PHONE),
                "Your TNG verification code is: " + code
        ).create();
        System.out.println(color.GREEN + "Sent message SID: " + message.getSid() + color.RESET);
        return code;
    }
}


