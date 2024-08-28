package lltw.scopyright.utils;


import   io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.Map;

/**
 * @author Sakura
 */
@Component
public class JJWT {

    private static final String SING_KEY = "Sakura";
    private static final long EXPIRE = 1000 * 60 * 60 * 24;
    /**
     * genJwt 生成JWT令牌
     */
    public String genJwt(Map<String, Object>claims){

        return Jwts.builder()
                .signWith(SignatureAlgorithm.HS256,SING_KEY)
                .setClaims(claims)
                .setExpiration(new Date(System.currentTimeMillis() + EXPIRE))
                // 生成jwt令牌
                .compact();
    }

    /**
     * parseJwt 解析JWT令牌
     */
    public static void parseJwt(String jwt){
        Claims claims = Jwts.parser()
                .setSigningKey(SING_KEY )
                .parseClaimsJws(jwt)
                .getBody();
    }
}
