package common.message;/**
 * @author Dantence
 * @description:
 * @date 2023/3/23
 */

import common.dto.BroadcastLoginDTO;

import java.io.Serializable;

/**
 * @projectName: p2pFileSystem
 * @package: common
 * @className: Message
 * @author: Dantence
 * @description: TODO
 * @date: 2023/3/23 12:15
 * @version: 1.0
 */
public class Message implements Serializable {
    String messageType;
    BroadcastLoginDTO broadcastLoginDTO;
    byte[] fileBytes;
    String src;
    String dest;
    String sender;
    String receiver;

    public String getSrc() {
        return src;
    }

    public void setSrc(String src) {
        this.src = src;
    }

    public String getDest() {
        return dest;
    }

    public void setDest(String dest) {
        this.dest = dest;
    }

    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public String getReceiver() {
        return receiver;
    }

    public void setReceiver(String receiver) {
        this.receiver = receiver;
    }

    public byte[] getFileBytes() {
        return fileBytes;
    }

    public void setFileBytes(byte[] fileBytes) {
        this.fileBytes = fileBytes;
    }

    public String getMessageType() {
        return messageType;
    }

    public void setMessageType(String messageType) {
        this.messageType = messageType;
    }

    public BroadcastLoginDTO getBroadcastLoginDTO() {
        return broadcastLoginDTO;
    }

    public void setBroadcastLoginDTO(BroadcastLoginDTO broadcastLoginDTO) {
        this.broadcastLoginDTO = broadcastLoginDTO;
    }


}
