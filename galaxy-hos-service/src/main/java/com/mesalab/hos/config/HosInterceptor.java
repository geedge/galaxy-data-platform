package com.mesalab.hos.config;

import com.mesalab.hos.entity.ErrorResponse;
import com.mesalab.hos.util.ResponseMessageUtil;
import com.mesalab.hos.util.XmlUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.mesalab.hos.util.AwsUtil.calculateSignatureV2;
import static com.mesalab.hos.util.AwsUtil.calculateSignatureV4;
import static com.mesalab.hos.util.PublicUtil.getUUID;

/**
 * Created by wk1 on 2020/5/25.
 */
@Component
public class HosInterceptor implements HandlerInterceptor {

    private final org.apache.log4j.Logger logger = org.apache.log4j.Logger.getLogger(this.getClass());
    @Value("${auth.open}")
    Integer tag;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {


        String accessKey = "default";
        String secretKey = "default";
        String userToken = "c21f969b5f03d33d43e04f8f136e7682";

        String uuid = getUUID();
        String hostId = HosProperties.ip;
        boolean isPass = false;

        if (tag == 0) {
            try {
                String Token = request.getHeader("token");
                if (Token == null) {
                    String auth = request.getHeader("Authorization");// 从 http 请求头中取出 token
                    if (auth != null && auth.contains("AWS4-HMAC-SHA256")) {
                        Pattern pDateRelativePars3e = Pattern.compile("\\bCredential=(.*?),\\s*\\bSignedHeaders=(.*?),\\s*\\bSignature=(.*)", Pattern.CASE_INSENSITIVE);
                        Matcher matcherParse = pDateRelativePars3e.matcher(auth);
                        if (matcherParse.find()) {
                            String Credential = matcherParse.group(1);
                            String signHeaders = matcherParse.group(2);
                            String signKey = matcherParse.group(3);
                            String[] credentialMessage = Credential.split("/");
                            accessKey = credentialMessage[0];
                            String Signature = calculateSignatureV4(request, signHeaders, Credential, accessKey, secretKey);
                            if (Signature.equals(signKey)) {
                                isPass = true;
                            }
                        }
                    } else if (auth != null && auth.contains("AWS")) {
                        Pattern pDateRelativePars3e = Pattern.compile("\\b(.*?):(.*)", Pattern.CASE_INSENSITIVE);
                        Matcher matcherParse = pDateRelativePars3e.matcher(auth);
                        if (matcherParse.find()) {
                            accessKey = matcherParse.group(1);
                            String Signature = matcherParse.group(2);
                            String preSignature = calculateSignatureV2(request, secretKey);
                            if (Signature.equals(preSignature)) {
                                isPass = true;
                            }
                        }
                    }
                } else {

                    if (userToken.equals(Token)) {
                        isPass = true;
                    }
                }
            } catch (Exception e) {
                logger.error(e.toString());
            }
            if (isPass) {
                request.setAttribute("username", accessKey);
                request.setAttribute("requestId", uuid);
                request.setAttribute("hostId", hostId);
                return true;
            } else {
                ErrorResponse errorResponse = ResponseMessageUtil.getResponseMessage("SignatureDoesNotMatch", hostId, uuid, "", "", response);
                returnXML(response, XmlUtil.convertToXml(errorResponse));
                return false;
            }
        } else {
            request.setAttribute("username", accessKey);
            request.setAttribute("requestId", uuid);
            request.setAttribute("hostId", hostId);
            return true;

        }

    }


    private void returnXML(HttpServletResponse response, String message) {
        PrintWriter writer = null;
        response.setCharacterEncoding("UTF-8");
        response.setStatus(403);
        response.setContentType("application/xml;charset=UTF-8");
        try {
            writer = response.getWriter();
            writer.print(message);

        } catch (IOException e) {
            logger.error("response error", e);
        } finally {
            if (writer != null) {
                writer.close();
            }
        }
    }

    /**
     * 处理请求完成后视图渲染之前的处理操作
     *
     * @param request
     * @param response
     * @param handler
     * @param modelAndView
     * @throws Exception
     */
    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, @Nullable ModelAndView modelAndView) throws Exception {
    }

    /**
     * 视图渲染之后的操作
     *
     * @param request
     * @param response
     * @param handler
     * @param ex
     * @throws Exception
     */
    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, @Nullable Exception ex) throws Exception {
    }


}
