package com.example.websocket;

import com.example.entity.dto.ClientDetail;
import com.example.entity.dto.ClientSsh;
import com.example.mapper.ClientDetailMapper;
import com.example.mapper.ClientSshMapper;
import com.jcraft.jsch.ChannelShell;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
import jakarta.annotation.Resource;
import jakarta.websocket.*;
import jakarta.websocket.server.PathParam;
import jakarta.websocket.server.ServerEndpoint;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Slf4j
@Component
@ServerEndpoint("/terminal/{clientId}")
public class TerminalWebSocket {

    private static ClientDetailMapper clientDetailMapper;

    @Resource
    public void setDetailMapper(ClientDetailMapper detailMapper) {
        TerminalWebSocket.clientDetailMapper = detailMapper;
    }

    private static ClientSshMapper clientSshMapper;

    @Resource
    public void setSshMapper(ClientSshMapper sshMapper) {
        TerminalWebSocket.clientSshMapper = sshMapper;
    }

    private static final Map<Session, Shell> sessionMap = new ConcurrentHashMap<>();
    private final ExecutorService service = Executors.newSingleThreadExecutor();

    @OnOpen
    public void onOpen(Session session,
                        @PathParam(value = "clientId") String clientId) throws Exception {
        ClientDetail detail = clientDetailMapper.selectById(clientId);
        ClientSsh ssh = clientSshMapper.selectById(clientId);
        if(detail == null || ssh == null) {
            session.close(new CloseReason(CloseReason.CloseCodes.CANNOT_ACCEPT, "无法识别此主机"));
            return;
        }
        if(this.createSshConnection(session, ssh, detail.getClientAddress())) {
            log.info("主机 {} 的SSH连接已创建", detail.getClientAddress());
        }
    }

    @OnMessage
    public void onMessage(Session session, String message) throws IOException {
        Shell shell = sessionMap.get(session);
        OutputStream output = shell.output;
        output.write(message.getBytes(StandardCharsets.UTF_8));
        output.flush();
    }

    @OnClose
    public void onClose(Session session) throws IOException {
        Shell shell = sessionMap.get(session);
        if(shell != null) {
            shell.close();
            sessionMap.remove(session);
            log.info("主机 {} 的SSH连接已断开", shell.js.getHost());
        }
    }

    @OnError
    public void onError(Session session, Throwable error) throws IOException {
        log.error("用户WebSocket连接出现错误", error);
        session.close();
    }

    private boolean createSshConnection(Session session, ClientSsh ssh, String clientAddress) throws IOException{
        try {
            JSch jSch = new JSch();
            com.jcraft.jsch.Session js = jSch.getSession(ssh.getUsername(), clientAddress, ssh.getPort());
            js.setPassword(ssh.getPassword());
            js.setConfig("StrictHostKeyChecking", "no");
            js.setTimeout(3000);
            js.connect();
            ChannelShell channel = (ChannelShell) js.openChannel("shell");
            channel.setPtyType("xterm");
            channel.connect(1000);
            sessionMap.put(session, new Shell(session, js, channel));
            return true;
        } catch (JSchException e) {
            String message = e.getMessage();
            if(message.equals("Auth fail")) {
                session.close(new CloseReason(CloseReason.CloseCodes.CANNOT_ACCEPT,
                        "登录SSH失败，用户名或密码错误"));
                log.error("连接SSH失败，用户名或密码错误，登录失败");
            } else if(message.contains("Connection refused")) {
                session.close(new CloseReason(CloseReason.CloseCodes.CANNOT_ACCEPT,
                        "连接被拒绝，可能是没有启动SSH服务或是放开端口"));
                log.error("连接SSH失败，连接被拒绝，可能是没有启动SSH服务或是放开端口");
            } else {
                session.close(new CloseReason(CloseReason.CloseCodes.CANNOT_ACCEPT, message));
                log.error("连接SSH时出现错误", e);
            }
        }
        return false;
    }

    private class Shell {
        private final Session session;
        private final com.jcraft.jsch.Session js;
        private final ChannelShell channel;
        private final InputStream input;
        private final OutputStream output;

        public Shell(Session session, com.jcraft.jsch.Session js, ChannelShell channel) throws IOException {
            this.js = js;
            this.session = session;
            this.channel = channel;
            this.input = channel.getInputStream();
            this.output = channel.getOutputStream();
            service.submit(this::read);
        }

        private void read() {
            try {
                byte[] buffer = new byte[1024 * 1024];
                int i;
                while ((i = input.read(buffer)) != -1) {
                    String text = new String(Arrays.copyOfRange(buffer, 0, i), StandardCharsets.UTF_8);
                    session.getBasicRemote().sendText(text);
                }
            } catch (Exception e) {
                log.error("读取SSH输入流时出现问题", e);
            }
        }

        public void close() throws IOException {
            input.close();
            output.close();
            channel.disconnect();
            js.disconnect();
            service.shutdown();
        }
    }
}
