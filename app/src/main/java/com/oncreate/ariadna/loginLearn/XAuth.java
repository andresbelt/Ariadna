package com.oncreate.ariadna.loginLearn;

import android.net.Uri;
import android.util.Base64;
import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Random;
import java.util.TreeMap;
import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

public final class XAuth {
    private static final int API_LEVEL = 3;
    private static final Random random;
    private static final String unreservedChars = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789-_.~";
    private byte[] body;
    private String clientId;
    private String clientSecret;
    private String method;
    private String normalizedUri;
    private Map<String, Object> parameters;
    private Uri requestUri;

    static {
        random = new Random();
    }

    public Map<String, Object> getParameters() {
        return this.parameters;
    }

    public void setParameters(Map<String, Object> value) {
        this.parameters = value;
    }

    public Uri getRequestUri() {
        return this.requestUri;
    }

    public void setRequestUri(Uri value) {
        this.requestUri = value;
    }

    public String getNormalizedUri() {
        return this.normalizedUri;
    }

    public void setNormalizedUri(String value) {
        this.normalizedUri = value;
    }

    public String getMethod() {
        return this.method;
    }

    public void setMethod(String value) {
        String newMethod = value.toUpperCase();
        if (newMethod.equals("POST") || newMethod.equals("GET")) {
            this.method = newMethod;
            return;
        }
        throw new IllegalArgumentException("This method is not supported");
    }

    public String getClientId() {
        return this.clientId;
    }

    public void setClientId(String value) {
        this.clientId = value;
    }

    public String getClientSecret() {
        return this.clientSecret;
    }

    public void setClientSecret(String value) {
        this.clientSecret = value;
    }

    public XAuth(Uri requestUri, String clientId, String clientSecret) {
        this.parameters = new HashMap();
        this.method = "POST";
        this.body = null;
        setClientId(clientId);
        setClientSecret(clientSecret);
        this.requestUri = requestUri;
        this.normalizedUri = this.requestUri.getScheme() + "://" + this.requestUri.getHost();
        if (!(this.requestUri.getPort() == -1 || ((this.requestUri.getScheme().equals("http") && this.requestUri.getPort() == 80) || (this.requestUri.getScheme().equals("https") && this.requestUri.getPort() == 443)))) {
            this.normalizedUri += ":" + this.requestUri.getPort();
        }
        this.normalizedUri += this.requestUri.getPath();
    }

    private void initializeXAuth() {
        this.parameters.put("ClientID", this.clientId);
        this.parameters.put("ClientSecret", this.clientSecret);
        if (!this.parameters.containsKey("Nonce")) {
            this.parameters.put("Nonce", generateNonce());
        }
        if (!this.parameters.containsKey("Timestamp")) {
            this.parameters.put("Timestamp", generateTimeStamp());
        }
    }

    public String generateSignature() {
        initializeXAuth();
        TreeMap<String, Object> baseParams = new TreeMap(this.parameters);
        baseParams.remove("Signature");
        baseParams.remove("ClientSecret");
        StringBuilder sb = new StringBuilder();
        sb.append(this.method).append('&');
        sb.append(urlEncode(this.normalizedUri)).append('&');
        int paramCount = 0;
        for (Entry<String, Object> kv : baseParams.entrySet()) {
            if (paramCount > 0) {
                sb.append("%26");
            }
            sb.append((String) kv.getKey());
            sb.append("%3D").append(kv.getValue().toString());
            paramCount++;
        }
        try {
            byte[] signatureBaseBytes;
            if (this.body != null) {
                sb.append('&');
                byte[] sbBytes = sb.toString().getBytes("UTF-8");
                signatureBaseBytes = new byte[(sbBytes.length + this.body.length)];
                System.arraycopy(sbBytes, 0, signatureBaseBytes, 0, sbBytes.length);
                System.arraycopy(this.body, 0, signatureBaseBytes, sbBytes.length, this.body.length);
            } else {
                signatureBaseBytes = sb.toString().getBytes("UTF-8");
            }
            String signature = sha1(urlEncode(this.clientSecret).getBytes("UTF-8"), signatureBaseBytes);
            this.parameters.put("Signature", signature);
            return signature;
        } catch (UnsupportedEncodingException e) {
            return null;
        }
    }

    public static String sha1(byte[] keyBytes, byte[] bodyBytes) {
        SecretKeySpec key = new SecretKeySpec(keyBytes, "HmacSHA1");
        try {
            Mac mac = Mac.getInstance("HmacSHA1");
            mac.init(key);
            return Base64.encodeToString(mac.doFinal(bodyBytes), 0).trim();
        } catch (NoSuchAlgorithmException e) {
            return null;
        } catch (InvalidKeyException e2) {
            return null;
        }
    }

    public static String hashPassword(String password) {
        try {
            String hash = sha1("password".getBytes("UTF-8"), password.getBytes("UTF-8"));
            return hash.substring(0, hash.length() - 1);
        } catch (Exception e) {
            return null;
        }
    }

    public String generateAuthorizationQueryString() {
        String signature = generateSignature();
        StringBuilder sb = new StringBuilder();
        for (Entry<String, Object> entry : this.parameters.entrySet()) {
            String key = (String) entry.getKey();
            if (!(key.equals("ClientSecret") || key.equals("Signature"))) {
                sb.append(urlEncode(key)).append("=").append(urlEncode(entry.getValue().toString())).append("&");
            }
        }
        sb.append("Signature=").append(urlEncode(signature));
        sb.append("&Version=3");
        return sb.toString();
    }

    private static String urlEncode(String value) {
        StringBuilder result = new StringBuilder();
        for (char c : value.toCharArray()) {
            if (unreservedChars.contains(Character.toString(c))) {
                result.append(c);
            } else {
                String hex = Integer.toHexString(Character.codePointAt(Character.toString(c), 0)).toUpperCase();
                result.append('%');
                if (hex.length() == 1) {
                    result.append('0');
                }
                result.append(hex);
            }
        }
        return result.toString();
    }

    private static String generateTimeStamp() {
        return Long.toString(new Date().getTime());
    }

    private static String generateNonce() {
        return Integer.toString(123400 + random.nextInt(9000000));
    }

    public byte[] getBody() {
        return this.body;
    }

    public void setBody(byte[] value) {
        this.body = value;
    }
}
