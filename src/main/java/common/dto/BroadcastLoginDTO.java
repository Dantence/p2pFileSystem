package common.dto;/**
 * @author Dantence
 * @description:
 * @date 2023/3/23
 */

import java.io.Serializable;
import java.util.List;

/**
 * @projectName: p2pFileSystem
 * @package: common.dto
 * @className: BroadcastLoginDTO
 * @author: Dantence
 * @description: TODO
 * @date: 2023/3/23 13:25
 * @version: 1.0
 */
public class BroadcastLoginDTO implements Serializable {
    String ip;
    List<String> resources;

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public List<String> getResources() {
        return resources;
    }

    public void setResources(List<String> resources) {
        this.resources = resources;
    }
}
