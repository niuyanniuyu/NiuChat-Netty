package cn.niu.server.entity;

import lombok.Data;

/**
 * 聊天组POJO
 *
 * @author Ben
 */
@Data
public class ChatGroup {
    /**
     * 聊天组id
     */
    private Long id;

    /**
     * 群组名
     */
    private String groupName;
}
