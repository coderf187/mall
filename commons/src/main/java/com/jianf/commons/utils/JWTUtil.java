package com.jianf.commons.utils;

import com.auth0.jwt.Algorithm;
import com.auth0.jwt.JWTSigner;
import com.auth0.jwt.JWTVerifier;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;

/**
 * 加解密方法（征信报告）
 */
public class JWTUtil {
	private static final Logger logger = LoggerFactory.getLogger(JWTUtil.class);
    static final String APPLY_NO_FLAG = "203";// 标识
    static final long timeExp = 43200;//失效时间（秒）

    /**
     * 加密
     */
    public static String sign(String secret, Map<String, Object> content) {
        JWTSigner.Options opt = new JWTSigner.Options();
        opt.setAlgorithm(Algorithm.HS256);

        long st = System.currentTimeMillis();
        long iat = st / 1000l; // 生成时间，秒
        long exp = iat + timeExp; // 失效时间

        JWTSigner signer = new JWTSigner(secret);
        Map<String, Object> claims = new HashMap<>();
        claims.put("iss", "datapi");
        claims.put("exp", exp);
        claims.put("iat", iat);
        claims.put("secret", secret);
        claims.put("token", st);
        claims.put("accessToken", MD5Util.MD5(st+secret));


        if (content != null) {
            for (String key : content.keySet()) {
                claims.put(key, content.get(key));
            }
        }
        String jwt = signer.sign(claims, opt);
        return jwt;
    }

    /**
     * 解析
     */
    public static Map<String, Object> parser(String jwt, String secret) {
        Map<String, Object> response = new HashMap<>();
        try {
            JWTVerifier verifier = new JWTVerifier(secret);
            Map<String, Object> claims = verifier.verify(jwt);
            response.put("success", true);
            response.put("result", claims);
        } catch (Exception e) {
            response.put("success", false);
            response.put("errorInfo", e.getMessage());
            logger.error("解析异常",e);
        }
        return response;
    }

    /**
     * 生成大数据编号
     *
     * @param loanApplyInfoId
     * @return
     */
    public static Long getDataCenterId(Long loanApplyInfoId) {
        String ali = loanApplyInfoId.toString();
        //修改数据中心进件号
        if (ali.length() < 7) {
            int idLength = ali.length();
            for (int i = 0; i < 7 - idLength; i++) {
                ali = "0" + ali;
            }
        }
        return Long.parseLong(APPLY_NO_FLAG + DateUtils.getDatePatternConnect() + ali);
    }

    public static void main(String[] args) {
        String str = "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJzdWNjZXNzRGlyZWN0VXJsIjoiaHR0cHM6Ly93d3cuZGF0YUNlbnRlci5jb20vY3JlZGl0cmVwb3J0L2Nsb3Nld2VidmlldyIsImlzcyI6ImRhdGFwaSIsIm1vYmlsZSI6IjE3NjAwODEwMzYwIiwic2VjcmV0IjoiMEE4MUIyNEU1N0Q3RDE5RjhERDI4QkRGNjcyMTUxQ0JFRTdBQ0NCNjYxQjkwOTY0NTQ5RUU0N0MwNDc5MkYyRSIsImFjY2Vzc1Rva2VuIjoiOURENTdDQjQwRjY4MDM3QkYxNDE5ODYwODQ0MzZGNkEiLCJ1c2VyTmFtZSI6IuWPtueri-agkyIsInRva2VuIjoxNTE1MTQ2MTg3NzQzLCJyZWxhdGlvbmFsUGFyYW0iOiJ7XCJjdXN0SWRcIjpcIjEzNlwifSIsImNhcmRJZCI6IjEzMDgyMzE5OTMwNTE2NTc3WCIsImFwcGx5Tm8iOiIyMDMyMDE4MDEwNTAwMDAxMzYiLCJleHAiOjE1MTUxODkzODcsImlhdCI6MTUxNTE0NjE4N30.NxHZMFs4aIzQo35zxWGMTJnCd2oufya9UT40zdptBEo";
        System.out.println(parser(str, "0A81B24E57D7D19F8DD28BDF672151CBEE7ACCB661B90964549EE47C04792F2E"));
    }
}
