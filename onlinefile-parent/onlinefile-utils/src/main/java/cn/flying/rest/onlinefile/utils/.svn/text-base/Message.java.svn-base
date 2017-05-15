package cn.flying.rest.onlinefile.utils;

import org.jivesoftware.smack.packet.Packet;
import org.jivesoftware.smack.packet.XMPPError;
import org.jivesoftware.smack.util.StringUtils;

public class Message extends Packet
{
  private Type type = Type.NORMAL;
  private String fullname = null;
  private String date = null;
  private String time = null;
  private String subject = null;
  private String body = null;
  private String thread = null;

  public Message()
  {
  }

  public Message(String to)
  {
    if (to == null) {
      throw new IllegalArgumentException("Parameter cannot be null");
    }
    setTo(to);
  }

  public Message(String to, Type type)
  {
    if ((to == null) || (type == null)) {
      throw new IllegalArgumentException("Parameters cannot be null.");
    }
    setTo(to);
    this.type = type;
  }

  public Type getType()
  {
    return this.type;
  }

  public void setType(Type type)
  {
    if (type == null) {
      throw new IllegalArgumentException("Type cannot be null.");
    }
    this.type = type;
  }
  
  public void setFullname(String fullname){
	  this.fullname = fullname ;
  }
  public void setDate(String date){
	  this.date = date ;
  }
  public void setTime(String time){
	  this.time = time ;
  }

  public String getSubject()
  {
    return this.subject;
  }

  public void setSubject(String subject)
  {
    this.subject = subject;
  }

  public String getBody()
  {
    return this.body;
  }

  public void setBody(String body)
  {
    this.body = body;
  }

  public String getThread()
  {
    return this.thread;
  }

  public void setThread(String thread)
  {
    this.thread = thread;
  }

  public String toXML() {
    StringBuffer buf = new StringBuffer();
    buf.append("<message");
    if (getPacketID() != null) {
      buf.append(" id=\"").append(getPacketID()).append("\"");
    }
    if (getTo() != null) {
      buf.append(" to=\"").append(StringUtils.escapeForXML(getTo())).append("\"");
    }
    if (getFrom() != null) {
      buf.append(" from=\"").append(StringUtils.escapeForXML(getFrom())).append("\"");
    }
    if (this.type != Type.NORMAL) {
      buf.append(" type=\"").append(this.type).append("\"");
    }
    if (this.fullname != null) {
    	buf.append(" fromCnName=\"").append(this.fullname).append("\"");
    }
    if (this.date != null) {
    	buf.append(" date=\"").append(this.date).append("\"");
    }
    if (this.time != null) {
    	buf.append(" time=\"").append(this.time).append("\"");
    }
    buf.append(">");
    if (this.subject != null) {
      buf.append("<subject>").append(StringUtils.escapeForXML(this.subject)).append("</subject>");
    }
    if (this.body != null) {
      buf.append("<body>").append(StringUtils.escapeForXML(this.body)).append("</body>");
    }
    if (this.thread != null) {
      buf.append("<thread>").append(this.thread).append("</thread>");
    }

    if (this.type == Type.ERROR) {
      XMPPError error = getError();
      if (error != null) {
        buf.append(error.toXML());
      }
    }

    buf.append(getExtensionsXML());
    buf.append("</message>");
    return buf.toString();
  }

  public static class Type
  {
    public static final Type NORMAL = new Type("normal");

    public static final Type CHAT = new Type("chat");

    public static final Type GROUP_CHAT = new Type("groupchat");

    public static final Type HEADLINE = new Type("headline");

    public static final Type ERROR = new Type("error");
    private String value;

    public static Type fromString(String type)
    {
      if (type == null) {
        return NORMAL;
      }
      type = type.toLowerCase();
      if (CHAT.toString().equals(type)) {
        return CHAT;
      }
      if (GROUP_CHAT.toString().equals(type)) {
        return GROUP_CHAT;
      }
      if (HEADLINE.toString().equals(type)) {
        return HEADLINE;
      }
      if (ERROR.toString().equals(type)) {
        return ERROR;
      }

      return NORMAL;
    }

    private Type(String value)
    {
      this.value = value;
    }

    public String toString() {
      return this.value;
    }
  }
}
