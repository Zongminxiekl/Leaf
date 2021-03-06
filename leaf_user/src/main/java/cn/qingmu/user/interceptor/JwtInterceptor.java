package cn.qingmu.user.interceptor;

import cn.qingmu.util.JwtUtil;
import io.jsonwebtoken.Claims;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author Zongmin
 * @date 2020-9-3 11:29:9
 */

@Component
public class JwtInterceptor implements HandlerInterceptor {

    @Autowired
    private JwtUtil jwtUtil;

    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        //无论如何都放行，能不能操作还是在具体的操作中去判断
        //拦截器只是把头请求中包含token的进行解析验证
        String header = request.getHeader("Authorization");
        if(header != null && !"".equals(header)) {
            //如果包含有Authorization头信息，就对其进行解析
            if(header.startsWith("Bearer ")) {
                //得到token
                String token = header.substring(7);
                //对令牌进行验证
                try{
                    Claims claims = jwtUtil.parseJWT(token);
                    String roles = (String) claims.get("roles");
                    if(roles != null && roles.equals("admin")) {
                       request.setAttribute("claims_admin",token);
                    }
                    if(roles != null && roles.equals("user")) {
                        request.setAttribute("claims_user",token);
                    }
                }catch(Exception e) {
                    throw new RuntimeException("令牌不正确!");
                }
            }
        }
        return true;
    }
}
