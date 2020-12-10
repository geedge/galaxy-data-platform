package com.mesalab.hos.util;

import com.amazonaws.util.BinaryUtils;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import javax.servlet.http.HttpServletRequest;
import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SignatureException;
import java.util.Map;
import java.util.TreeMap;

/**
 * Created by wk1 on 2020/5/26.
 */
public class AwsUtil {
    private static final org.apache.log4j.Logger logger = org.apache.log4j.Logger.getLogger(AwsUtil.class);

    private final static char[] hexArray = "0123456789ABCDEF".toCharArray();
    private static final String HMAC_SHA256_ALGORITHM = "HmacSHA256";
    private static final String HMAC_SHA1_ALGORITHM = "HmacSHA1";

    public static String calculateSignatureV4(HttpServletRequest request, String signHeaders, String credential, String accessKey, String secretKey) throws Exception {
        String[] signArray = credential.split("/");
        StringBuilder canonicalURL = new StringBuilder("");
        /* Step 1.1 以HTTP方法(GET, PUT, POST, etc.)开头, 然后换行. */
        canonicalURL.append(request.getMethod()).append("\n");
        /* Step 1.2 添加URI参数，换行. */
        canonicalURL.append(request.getRequestURI()).append("\n");
        Map<String, String[]> paraMap = new TreeMap<String, String[]>(request.getParameterMap());
        if (paraMap.size() == 0) {
            canonicalURL.append("\n");
        } else {
            StringBuilder queryString = new StringBuilder("");
            for (Map.Entry<String, String[]> entry : paraMap.entrySet()) {
                queryString.append(entry.getKey()).append("=").append(entry.getValue()[0]).append("&");
            }
            String paramString = queryString.toString();
            paramString = paramString.substring(0, paramString.length() - 1);
            canonicalURL.append(paramString).append("\n");
        }
        String[] shs = signHeaders.split(";");

        for (String sh : shs) {
            request.getHeader(sh);
            canonicalURL.append(sh).append(":").append(request.getHeader(sh)).append("\n");
        }

        canonicalURL.append("\n");
        canonicalURL.append(signHeaders).append("\n");

        String hashcode = request.getHeader("x-amz-content-sha256");
        if (hashcode == null) {
            hashcode = "e3b0c44298fc1c149afbf4c8996fb92427ae41e4649b934ca495991b7852b855";
        }
        canonicalURL.append(hashcode);

        byte[] kSigning = ("AWS4" + secretKey).getBytes(StandardCharsets.UTF_8);

        StringBuilder sign = new StringBuilder("");
        for (int i = 1; i < signArray.length; i++) {
            kSigning = HmacSHA256(kSigning, signArray[i]);
            if (i == signArray.length - 1) {
                sign.append(signArray[i]);
            } else {
                sign.append(signArray[i]).append("/");
            }
        }
        String StringToSign = "AWS4-HMAC-SHA256" + "\n" + request.getHeader("x-amz-date") + "\n" + sign + "\n" + generateHex(canonicalURL.toString());
        return bytesToHex(HmacSHA256(kSigning, StringToSign));
    }


    public static String calculateSignatureV2(HttpServletRequest request, String SecretKey) throws Exception {
        StringBuilder stringToSign = new StringBuilder("");
        stringToSign.append(request.getMethod()).append("\n");
        String md5 = request.getHeader("Content-MD5");
        if (md5 == null) {
            stringToSign.append("\n");
        } else {
            stringToSign.append(md5).append("\n");
        }
        String ctype = request.getHeader("Content-Type");
        if (ctype == null) {
            stringToSign.append("\n");
        } else {
            stringToSign.append(ctype).append("\n");
        }
        String date = request.getHeader("Date");
        stringToSign.append(date).append("\n");
        stringToSign.append(request.getRequestURI());

        String queryString = request.getQueryString();

        if (queryString != null && !queryString.contains("=")) {
            stringToSign.append(queryString);
        }
        return calculateV2HMAC(stringToSign.toString(), "222");
    }

    public static byte[] HmacSHA256(byte[] key, String data) throws Exception {
        String algorithm = "HmacSHA256";
        Mac mac = Mac.getInstance(algorithm);
        mac.init(new SecretKeySpec(key, algorithm));
        return mac.doFinal(data.getBytes(StandardCharsets.UTF_8));
    }

    public static String bytesToHex(byte[] bytes) {
        char[] hexChars = new char[bytes.length * 2];
        for (int j = 0; j < bytes.length; j++) {
            int v = bytes[j] & 0xFF;
            hexChars[j * 2] = hexArray[v >>> 4];
            hexChars[j * 2 + 1] = hexArray[v & 0x0F];
        }
        return new String(hexChars).toLowerCase();
    }

    private static String generateHex(String data) throws NoSuchAlgorithmException, UnsupportedEncodingException {
        MessageDigest messageDigest;
        try {
            messageDigest = MessageDigest.getInstance("SHA-256");
            messageDigest.update(data.getBytes(StandardCharsets.UTF_8));
            byte[] digest = messageDigest.digest();
            return String.format("%064x", new java.math.BigInteger(1, digest));
        } catch (NoSuchAlgorithmException e) {
            logger.error(e.toString());
        }
        return null;
    }

    private static String calculateV2HMAC(String data, String key) throws java.security.SignatureException {
        String result;
        try {
            // Get an hmac_sha256 key from the raw key bytes.
            SecretKeySpec signingKey = new SecretKeySpec(key.getBytes(StandardCharsets.UTF_8), HMAC_SHA256_ALGORITHM);
            // Get an hmac_sha256 Mac instance and initialize with the signing key.
            Mac mac = Mac.getInstance(HMAC_SHA1_ALGORITHM);
            mac.init(signingKey);
            // Compute the hmac on input data bytes.
            byte[] rawHmac = mac.doFinal(data.getBytes(StandardCharsets.UTF_8));
            // Base64-encode the hmac by using the utility in the SDK
            result = BinaryUtils.toBase64(rawHmac);
        } catch (Exception e) {
            throw new SignatureException("Failed to generate HMAC : " + e.getMessage());
        }
        return result;
    }
}
