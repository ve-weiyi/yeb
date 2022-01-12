package com.hust.server.config.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * @Description jwtToken工具栏
 * 用于出token中取出用户登录的信息
 * @Author weiyi
 * @Date 2021/12/27
 */
@Component
public class JwtTokenUtil {

    private static final String issuer="iLocker";
    private static final String CLAIM_KEY_CONTENT= "content";
    private static final String CLAIM_KEY_PUBLIC_KEY= "publicKey";
    private static final String CLAIM_KEY_PRIVATE_KEY = "privateKey";


    private static final String content="床前明月光，疑是地上霜。";
    private static final String publicKey="举头望明月";
    private static final String privateKey ="低头思故乡" ;
    /**
     *  token中的字段
     *     iss：Issuer，发行者
     *     sub：Subject，主题
     *     aud：Audience，观众
     *     exp：Expiration time，过期时间
     *     nbf：Not before
     *     iat：Issued at Time，发行时间
     *     jti：JWT ID
     *
     */

    @Value("${jwt.tokenHeader}")
    private String tokenHeader;
    @Value("${jwt.tokenHead}")
    private String tokenHead;

    @Value("${jwt.secret}")//jwt 密钥
    private String secret;
    @Value("${jwt.expiration}")//失效时间
    private Long expiration;

    /**
     * 根据用户信息生成token
     * @param userDetails
     * @return
     */
    //用户信息通过Security中的 UserDetails 拿取
    public String generateToken(UserDetails userDetails){
        Map<String,Object> jwtToken = new HashMap<>();
        jwtToken.put(Claims.ISSUER,issuer);
        jwtToken.put(Claims.SUBJECT,userDetails.getUsername());
        jwtToken.put(Claims.ISSUED_AT,new Date());
        jwtToken.put(CLAIM_KEY_CONTENT,content);
        jwtToken.put(CLAIM_KEY_PUBLIC_KEY,publicKey);
        jwtToken.put(CLAIM_KEY_PRIVATE_KEY, privateKey);
        //根据荷载生成jwt
        return generateToken(jwtToken);
    }

    /**
     * 根据荷载生成 JWT TOKEN
     * @param claims
     * @return
     */
    private String generateToken(Map<String,Object> claims){
        return Jwts.builder()
                //设置负载
                .setClaims(claims)
                //失效时间
                .setExpiration(generateExpirationDate())
                //签名 设置加密算法，通常为HMAC SHA256
                .signWith(SignatureAlgorithm.HS512, secret)
                .compact();
    }

    /**
     * 生成token失效时间
     * @return
     */
    private Date generateExpirationDate() {
        return new Date(System.currentTimeMillis() + expiration * 1000);
    }

    /**
     * 从token中获取登录用户名
     * @param token
     * @return
     */
    public String getUserNameFromToken(String token){
        String username;
        try {
            Claims claims = getClaimsFromToken(token);
            //通过荷载 claims 就可以拿到用户名 sub字段
            username = claims.getSubject();
        } catch (Exception e) {
            username = null;
        }
        return username;
    }

    /**
     * 判断token是否有效,即token中用户名是否等于userDetails中的用户名
     * @param token
     * @param userDetails
     * @return
     */
    public boolean validateToken(String token, UserDetails userDetails) {
        String username = getUserNameFromToken(token);
        //token是否过期并且荷载中的用户名和userDetails中是否一致
        return username.equals(userDetails.getUsername())  &&  !isTokenExpired(token);
    }

    /**
     * 判断token是否可以被刷新
     * 如果过期了就可以被刷新，如果没过期就不能被刷新
     * @param token
     * @return
     */
    public boolean canRefresh(String token){
        return !isTokenExpired(token);
    }

    /**
     * 刷新token
     * @param token
     * @return
     */
    public String refreshToken(String token){
        Claims claims = getClaimsFromToken(token);
        //将创建时间改成当前时间，就相当于去刷新了
        claims.put(Claims.ISSUED_AT,new Date());
        return generateToken(claims);
    }

    /**
     * 判断token是否失效
     * @param token
     * @return
     */
    private boolean isTokenExpired(String token) {
        Date expireDate = getExpiredDateFromToken(token);
        //判断token时间是否是当前时间的前面 .before
        return expireDate.before(new Date());
    }

    /**
     * 从token中获取过期时间
     * @param token
     * @return
     */
    private Date getExpiredDateFromToken(String token) {
        //从token里面获取荷载
        //因为token的过期时间有对应的数据,设置过的,荷载里面就有设置过的数据
        Claims claims = getClaimsFromToken(token);
        return claims.getExpiration();
    }


    /**
     * 从token中获取荷载
     * @param token
     * @return
     */
    private Claims getClaimsFromToken(String token) {
        //拿到荷载
        Claims claims = null ;
        try {
            claims = Jwts.parser()
                    //签名
                    .setSigningKey(secret)
                    //密钥
                    .parseClaimsJws(token)
                    .getBody();
        } catch (ExpiredJwtException e) {
            e.printStackTrace();
        }
        return claims;
    }


    public String getUserNameFromToken(HttpServletRequest httpServletRequest) {
        String userName=null;
        //通过 request 获取请求头
        String authHeader = httpServletRequest.getHeader(tokenHeader);
        //验证头部，不存在，或者不是以tokenHead：Bearer开头的
        if (authHeader != null && authHeader.startsWith(tokenHead)) {
            //存在，就做一个字符串的截取，其实就是获取了登录的token
            String authToken = authHeader.substring(tokenHead.length());
            //jwt根据token获取用户名
            //token存在用户名但是未登录
            userName = getUserNameFromToken(authToken);
        }
        return userName;
    }

    public Claims getTokenClaimInfo(HttpServletRequest httpServletRequest) {
        //  Map tokenInfoMap = new HashMap();
        String userName=null;
        //通过 request 获取请求头
        String authHeader = httpServletRequest.getHeader(tokenHeader);
        //验证头部，不存在，或者不是以tokenHead：Bearer开头的
        if (authHeader != null && authHeader.startsWith(tokenHead)) {
            //存在，就做一个字符串的截取，其实就是获取了登录的token
            String authToken = authHeader.substring(tokenHead.length());
            //jwt根据token获取用户名
            //token存在用户名但是未登录
            userName = getUserNameFromToken(authToken);
            return getClaimsFromToken(authToken);
        }
        return null;
    }
}
