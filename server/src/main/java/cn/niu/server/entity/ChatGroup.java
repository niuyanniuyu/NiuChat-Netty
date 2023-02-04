package cn.niu.server.entity;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.Collections;
import java.util.Set;

/**
 * 聊天组POJO
 *
 * @author Ben
 */
@Data
@AllArgsConstructor
public class ChatGroup {

    /**
     * 群组名
     */
    private String groupName;

    /**
     * 聊天室成员
     */
    private Set<String> members;

    public static final ChatGroup EMPTY_GROUP = new ChatGroup("empty", Collections.emptySet());

}
