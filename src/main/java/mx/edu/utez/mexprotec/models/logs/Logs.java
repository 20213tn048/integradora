package mx.edu.utez.mexprotec.models.logs;

import com.fasterxml.jackson.databind.JsonNode;
import io.hypersistence.utils.hibernate.type.json.JsonType;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Type;


@Entity
@Table(name = "logs")
@Getter
@Setter
@NoArgsConstructor
public class Logs {
    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(name = "uuid", strategy = "uuid2")
    private String id;

    @Column(columnDefinition = "json")
    @Type(JsonType.class)
    private JsonNode data;

    @Column(name = "method")
    private String method;

    @Column(name = "request_uri")
    private String requestUri;

    @Column(name = "ip")
    private String ip;

    @Column(name = "user_agent")
    private String userAgent;

    @Column(name = "status")
    private int status;

    @Column(name = "created_at", columnDefinition = "TIMESTAMP")
    private String createdAt;
}

