package com.nowcoder.community.util;

/**
 * @Author 杜俊宏
 * Date on 2021/2/10 20:49
 */
public class RedisKeyUtil {

    private static final String SPLIT = ":";

    private static final String PREFIX_ENTITY_LIKE = "like:entity";

    //某个实体的赞
    //like:entity:entityType:entityId->set(userId)
    public static String getEntityLikeKey(int entityType,int entityId) {
        return PREFIX_ENTITY_LIKE+SPLIT+entityType+SPLIT+entityId;
    }

}
