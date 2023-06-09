package common.message;

/**
 * @author Dantence
 * @description:
 * @date 2023/2/27
 */
public interface MessageType {
    // 登录请求
    String BROADCAST_LOGIN_REQ = "1";
    // 登录返回
    String BROADCAST_LOGIN_RESP = "2";
    // 文件发送
    String FILE_SEND = "3";
    // 退出登录请求
    String BROADCAST_LOGOUT_REQ = "4";
    // 文件下载请求
    String FILE_DOWNLOAD_REQ = "5";
    // 文件下载响应
    String FILE_DOWNLOAD_RESP = "6";
    // 退出登录响应
    String BROADCAST_LOGOUT_RESP = "7";
    // 关闭程序请求
    String CLOSE_REQ = "8";
    // 关闭程序响应
    String CLOSE_RESP = "9";
    // 心跳检测请求
    String HEART_BEAT_REQ = "10";
    // 心跳检测响应
    String HEART_BEAT_RESP = "11";

}
