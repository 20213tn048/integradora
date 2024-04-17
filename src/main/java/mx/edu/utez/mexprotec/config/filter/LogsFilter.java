package mx.edu.utez.mexprotec.config.filter;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import mx.edu.utez.mexprotec.models.logs.Logs;
import mx.edu.utez.mexprotec.models.logs.LogsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;

@Component
public class LogsFilter extends OncePerRequestFilter {

    private final LogsRepository repository;
    private final com.fasterxml.jackson.databind.ObjectMapper objectMapper;

    @Autowired
    private LogsFilter(LogsRepository repository, com.fasterxml.jackson.databind.ObjectMapper objectMapper) {
        this.repository = repository;
        this.objectMapper = objectMapper;
    }
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        MultiReadHttpServletRequest multiReadRequest = new MultiReadHttpServletRequest(request);
        MultiReadHttpServletResponse multiReadResponse = new MultiReadHttpServletResponse(response);
        filterChain.doFilter(multiReadRequest, multiReadResponse);
        String body = multiReadRequest.getReader().lines().reduce("", (accumulator, actual) -> accumulator + actual);
        String responseBody = new String(multiReadResponse.getContentAsByteArray(), response.getCharacterEncoding());
        ObjectNode jsonNode = objectMapper.createObjectNode();
        Logs logs = new Logs();
        String token = request.getHeader("Authorization");
        if (token != null) {
            token = token.replace("Bearer ", "");
        }
        logs.setMethod(request.getMethod());
        logs.setRequestUri(request.getRequestURI());
        logs.setIp(request.getRemoteAddr());
        logs.setUserAgent(request.getHeader("User-Agent"));
        logs.setStatus(response.getStatus());
        jsonNode.put("token", token);
        try {
            jsonNode.set("body", objectMapper.readTree(body));
        } catch (JsonProcessingException e) {
            jsonNode.put("body", "Invalid JSON");
        }
        try {
            jsonNode.set("response", objectMapper.readTree(responseBody));
        } catch (JsonProcessingException e) {
            jsonNode.put("response", "Invalid JSON");
        }
        JsonNode node = objectMapper.valueToTree(jsonNode);
        logs.setData(node);
        logs.setCreatedAt(new SimpleDateFormat("yyyy.MM.dd.HH.mm.ss").format(new Timestamp(System.currentTimeMillis())));
        multiReadResponse.copyBodyToResponse();
        try {
            if (request.getMethod().equals(HttpMethod.PUT.name()) || request.getMethod().equals(HttpMethod.POST.name()) || request.getMethod().equals(HttpMethod.DELETE.name())) {
                repository.save(logs);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
