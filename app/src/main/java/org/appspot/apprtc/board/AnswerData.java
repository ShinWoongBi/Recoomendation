package org.appspot.apprtc.board;

/**
 * Created by kippe_000 on 2017-10-28.
 */

public class AnswerData {
    int id;
    String mail;
    String name;
    String time;
    String content;

    AnswerData(int id, String mail, String name, String time, String content){
        this.id = id;
        this.mail = mail;
        this.name = name;
        this.time = time;
        this.content = content;
    }
}
