package mx.edu.utez.mexprotec.segurity.controller;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Setter
@Getter
public class ResponseChangeDto {
    private String message;
    private boolean error;

    public ResponseChangeDto(String message, boolean error) {
        this.message = message;
        this.error = error;
    }
}
