package com.dongyimai.oauth;

import org.junit.jupiter.api.Test;
import org.springframework.security.jwt.Jwt;
import org.springframework.security.jwt.JwtHelper;
import org.springframework.security.jwt.crypto.sign.RsaVerifier;

public class ParseJwtTest {

    /***
     * 校验令牌
     */
    @Test
    public void testParseToken(){
        //令牌
        String token = "eyJhbGciOiJSUzI1NiIsInR5cCI6IkpXVCJ9.eyJyb2xlcyI6ImFkbWluLHVzZXIiLCJuYW1lIjoidWppdXllIiwiaWQiOiIxIn0.L8vy45_7LR4tgX-p4NkxAOs6p5KPoqz3RVBqk3eeZONiCiB0tQEqcrgxVaaIrLgY5telhLWg0Qkw-3LYtZBECnO3C4gqfHREFOUuCtoFdc9F2JNDvhUUmgGTOJrkGP5m7-H51xtDuzv1YarqQOgVwTgKig2KW2bxxOZIiolubGixkzCwgicpT2N3g0a-vObIFsd7L6vXgvTdggva9Fiq_kgqnhVIgq9e2Bq2sStQvskO0QzYhZowk5PCD2_QOHxTuVhStGP8B9D5DB1lJzR3w7nYZbkIHDPn2p96GCXlG_bKSIfuaU2gVz3Tpyh69hkI65hLX90wDPfWP0JMKNMGBA";

        //公钥
        String publickey = "-----BEGIN PUBLIC KEY-----MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAtYRm5oE3U3G3r4mK57te6zolBz9my0bdS6tAc6H3v3GDaANrCDs3C/16y/FS+09VwgMz31KcUaWwBKGPpQ7slPFCEo0h047b0+WazPEO3d7FLO2uwLF5JoFd68ZB4xPaevlVmSNZ4Aa18D/07gKwHnP+VDsHgdIqLIZ1v8Nq250I8kSIpq+yq3JBV4vPp5R4UP9/if5jMvgZX+HA3/W5cBv1P0qONl1rRS/aRAqiiBJVNpgkPz81g6y+gfb2vB6T7cKk1kKsJbY6dS7i6GXgX1e37MsYmTndRLXodsh0oLsZ0YVB8m1z1ogABY5A/H0I7GYCWszHoltB14itf5vuFQIDAQAB-----END PUBLIC KEY-----";

        //校验Jwt
        Jwt jwt = JwtHelper.decodeAndVerify(token, new RsaVerifier(publickey));

        //获取Jwt原始内容
        String claims = jwt.getClaims();
        System.out.println(claims);
        //jwt令牌
        String encoded = jwt.getEncoded();
        System.out.println(encoded);
    }
}
