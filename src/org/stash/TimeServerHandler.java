package org.stash;

import java.util.Date;
import org.apache.mina.core.service.IoHandlerAdapter;
import org.apache.mina.core.session.IoSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TimeServerHandler extends IoHandlerAdapter {

    private final static Logger logger =
        LoggerFactory.getLogger(TimeServerHandler.class);

    @Override
    public void exceptionCaught(IoSession session, Throwable cause) {
        logger.error(cause.toString());
        session.close(true);
    }

    @Override
    public void messageReceived(IoSession session, Object message)
            throws Exception {
        String string = message.toString();

        if (string.trim().equalsIgnoreCase("quit")) {
            session.close(true);
            return;
        }

        Date date = new Date();
        session.write(date.toString());
        System.out.println("Message written...");
    }

    @Override
    public void sessionCreated(IoSession session) {
        logger.info("Session Created");
    }

    @Override
    public void sessionClosed(IoSession session) {
        logger.info("Session Closed");
    }

    @Override
    public void sessionOpened(IoSession session) {
        logger.info("Session Opened");
    }
}
