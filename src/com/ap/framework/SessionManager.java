package com.ap.framework;

import javax.servlet.http.HttpSession;
import java.util.Map;
import java.util.HashMap;
import javax.servlet.ServletContext;

public class SessionManager {
    Map<String,HttpSession> sessionMap = new HashMap<>();

    // get a session from the cache
    // creates one if the sessionId is null or empty
    // TODO: what if the session id is not found? create or throw a "not found" exception?
    public HttpSession getSession(ServletContext ctx, String sessionId) {
        HttpSession result = null;

        if (sessionId != null) {
            result = sessionMap.get(sessionId);
        }

        if (result == null) {
            if (sessionId == null || sessionId.isEmpty()) {
                // create a new random session id
                sessionId = Long.toHexString(Double.doubleToLongBits(Math.random()));
            }
            result = new HttpSessionImpl(ctx, sessionId);
            sessionMap.put(sessionId, result);
        }

        return result;
    }

    // TODO: what about inactivity time-outs?
}
