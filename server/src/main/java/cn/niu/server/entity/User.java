package cn.niu.server.entity;

import jdk.nashorn.internal.objects.annotations.Constructor;
import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * 用户POJO
 * @author Ben
 */
@Data
@AllArgsConstructor
public class User {
    private String username;
    private String password;
    private String nickname;
}
