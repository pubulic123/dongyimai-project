package com.offcn.test;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.junit.Test;

import java.util.Date;

public class JwtTest {

	/****
	 *创建Jwt令牌
	 */
	@Test
	public void testCreateJwt2(){
		JwtBuilder builder= Jwts.builder()
				.setId("888")//设置唯一编号
				.setSubject("小白")//设置主题可以是JSON数据
				.setIssuedAt(new Date())//设置签发日期
				.setExpiration(new Date(System.currentTimeMillis()+2000)) //设置token
				.signWith(SignatureAlgorithm.HS256,"ujiuye");//设置签名使用HS256算法，并设置SecretKey(字符串)
		//构建并返回一个字符串
		System.out.println( builder.compact());
	}

/****
*创建Jwt令牌
*/
    @Test
    public void testCreateJwt(){
       	JwtBuilder builder= Jwts.builder()
      	.setId("888")//设置唯一编号
      	.setSubject("小白")//设置主题可以是JSON数据
      	.setIssuedAt(new Date())//设置签发日期
      	.signWith(SignatureAlgorithm.HS256,"ujiuye");//设置签名使用HS256算法，并设置SecretKey(字符串)
      	//构建并返回一个字符串
       	System.out.println( builder.compact());
    }

	/***
	 *解析Jwt令牌数据
	 */
	@Test
	public void testParseJwt(){
		String compactJwt="eyJhbGciOiJIUzI1NiJ9.eyJqdGkiOiI4ODgiLCJzdWIiOiLlsI_nmb0iLCJpYXQiOjE2NDc5NTcyMzR9.JvNjR6w8_3Isw0pAqZ_W72RcH86G-A0fRMhyhY2caOM";
		Claims claims = Jwts.parser().
				setSigningKey("ujiuye").
				parseClaimsJws(compactJwt).
				getBody();
		System.out.println(claims);
	}
}